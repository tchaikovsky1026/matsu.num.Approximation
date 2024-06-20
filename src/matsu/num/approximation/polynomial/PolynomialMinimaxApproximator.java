/**
 * 2022.5.5
 * ver6.0: Major update, 設計の見直し.
 */
package matsu.num.approximation.polynomial;

import java.util.Arrays;
import java.util.Random;
import matsu.num.approximation.FiniteRange;
import matsu.num.commons.Trigonometry;

/**
 * Minimax-approximator by polynomial function. <br>
 * This implements Remez algorithm. <br>
 * Minimize max<sub><i>x</i></sub>
 * |<i>f</i>(<i>x</i>)-<i>p</i>(<i>x</i>)|/<i>g</i>(<i>x</i>). <br>
 * <i>f</i>(<i>x</i>): function, <i>p</i>(<i>x</i>): polynomial fuction,
 * <i>g</i>(<i>x</i>): scaling value.
 *
 * @author Matsuura, Y.
 * @version 6.0.1
 */
public final class PolynomialMinimaxApproximator {

    private final int polynominalDegree;
    private FunctionScaleSupplier supplier;

    private FixedSizeNewtonPolynomial polynomial;

    /**
     * Create new polynomial approximator.
     *
     * @param supplier value and scale supplier
     * @param range approximating range
     * @param polynominalDegree degree of polynomial function
     * @param numberOfIteration number of iteration
     * @throws IllegalArgumentException degree of polynomial function
     */
    public PolynomialMinimaxApproximator(FunctionScaleSupplier supplier, FiniteRange range,
            int polynominalDegree, int numberOfIteration) {
        if (polynominalDegree < 0) {
            throw new IllegalArgumentException("polynomial degree < 0");
        }
        this.supplier = supplier;
        this.polynominalDegree = polynominalDegree;
        approximate(range, numberOfIteration);
    }

    /**
     * Minimax-approximate function by polynomial function.
     *
     * @param range approximating range
     * @param repeat repeat
     */
    private void approximate(FiniteRange range, int repeat) {
        double minX = range.lower();
        double maxX = range.upper();
        int numNode = polynominalDegree + 2;
        /* Quasi-Chebyshevノード */
        double[] node = new double[numNode];
        double scaleX = (maxX - minX) / 2;
        double shiftX = minX + scaleX;

        for (int i = 0, len = numNode; i < len; i++) {
            double c = Trigonometry.cospi((double) i / (numNode - 1));
            node[i] = c * scaleX + shiftX;
        }
        Arrays.sort(node);
        /* Remez法による多項式を用いたMinimax近似 */
        ApproximatorSet as = new ApproximatorSet(node, supplier);
        for (int rep = 0; rep < repeat; rep++) {
            as.update();
        }
        polynomial = as.getPolynomial();
    }

    /**
     * Calculate scaled signed-error at the given value: <br>
     * (<i>p</i>(<i>x</i>)-<i>f</i>(<i>x</i>))/<i>g</i>(<i>x</i>).
     *
     * @param x x
     * @return scaled error
     */
    public double error(double x) {
        return (polynomial.value(x) - supplier.value(x)) / supplier.scale(x);
    }

    /**
     * Get the previous polynomial function approximating the function.
     *
     * @return polynomial function
     */
    public PolynomialFunction getPolynomial() {
        return polynomial;
    }

    private static class ApproximatorSet {

        final int nodeSize;
        double[] node;
        FunctionScaleSupplier supplier;

        final int polynomialSize;
        FixedSizeNewtonPolynomial h1;
        FixedSizeNewtonPolynomial h2;
        double e;

        double[] tempPolynomialNode;
        double[] tempValueH1;
        double[] tempValueH2;

        Random random = new Random();

        public ApproximatorSet(double[] node, FunctionScaleSupplier supplier) {
            nodeSize = node.length;
            polynomialSize = nodeSize - 1;
            this.node = node;
            this.supplier = supplier;
            h1 = new FixedSizeNewtonPolynomial(polynomialSize);
            h2 = new FixedSizeNewtonPolynomial(polynomialSize);
            tempPolynomialNode = new double[polynomialSize];
            tempValueH1 = new double[polynomialSize];
            tempValueH2 = new double[polynomialSize];
        }

        public ApproximatorSet update() {
            return this.updatePolynominalHSet().updateNode();
        }

        public FixedSizeNewtonPolynomial getPolynomial() {
            FixedSizeNewtonPolynomial h = new FixedSizeNewtonPolynomial(polynomialSize);
            System.arraycopy(h1.getNode(), 0, h.getNode(), 0, polynomialSize);

            double[] newton_h;
            newton_h = h.getNewtonCoefficient();
            double[] newton_h1 = h1.getNewtonCoefficient();
            double[] newton_h2 = h2.getNewtonCoefficient();
            double thisE = e;
            for (int i = 0, l = polynomialSize; i < l; i++) {
                newton_h[i] = newton_h1[i] - thisE * newton_h2[i];
            }

            return h;
        }

        /**
         * nodeX, function, scaleFactor をもとに, hSet を更新する.
         *
         * @param as
         * @return
         */
        private ApproximatorSet updatePolynominalHSet() {
            //ノードyiの作成
            System.arraycopy(node, 0, tempPolynomialNode, 0, polynomialSize);
            //値の作成
            for (int i = 0, len = polynomialSize; i < len; i++) {
                double x = tempPolynomialNode[i];
                tempValueH1[i] = supplier.value(x);
                tempValueH2[i] = supplier.scale(x);
                if (i % 2 == 1) {
                    tempValueH2[i] = -tempValueH2[i];
                }
            }
            //多項式h1,h2の計算と、Eの計算
            h1.setCrossPoints(tempPolynomialNode, tempValueH1);
            h2.setCrossPoints(tempPolynomialNode, tempValueH2);
            double xLast = node[nodeSize - 1];
            int sig_l = nodeSize % 2 == 0 ? 1 : -1;
            e = (h1.value(xLast) - supplier.value(xLast)) / (h2.value(xLast) + sig_l * supplier.scale(xLast));
            return this;
        }

        private ApproximatorSet updateNode() {
            for (int i = 1, len = nodeSize - 1; i < len; i++) {
                double x_m = node[i - 1];
                double x_0 = node[i];
                double x_M = node[i + 1];

                //1なら極大、-1なら極小を探す
                boolean individualLocalMax = !((i % 2 == 0) ^ (e > 0));
                //initial, 0
                double x_1 = x_0;
                double f_1 = (supplier.value(x_1) - h1.value(x_1) + e * h2.value(x_1)) / supplier.scale(x_1);
                //initial, -1/3
                double x_temp = x_0 + (x_m - x_0) * 0.33 * random.nextDouble();
                double f_temp = (supplier.value(x_temp) - h1.value(x_temp) + e * h2.value(x_temp)) / supplier.scale(x_temp);
                if (individualLocalMax ^ f_temp < f_1) {
                    x_1 = x_temp;
                    f_1 = f_temp;
                }
                //initial, -1/100
                x_temp = x_0 + (x_m - x_0) * 0.01 * random.nextDouble();
                f_temp = (supplier.value(x_temp) - h1.value(x_temp) + e * h2.value(x_temp)) / supplier.scale(x_temp);
                if (individualLocalMax ^ f_temp < f_1) {
                    x_1 = x_temp;
                    f_1 = f_temp;
                }
                //initial, 1/3
                x_temp = x_0 + (x_M - x_0) * 0.33 * random.nextDouble();
                f_temp = (supplier.value(x_temp) - h1.value(x_temp) + e * h2.value(x_temp)) / supplier.scale(x_temp);
                if (individualLocalMax ^ f_temp < f_1) {
                    x_1 = x_temp;
                    f_1 = f_temp;
                }
                //initial, 1/100
                x_temp = x_0 + (x_M - x_0) * 0.01 * random.nextDouble();
                f_temp = (supplier.value(x_temp) - h1.value(x_temp) + e * h2.value(x_temp)) / supplier.scale(x_temp);
                if (individualLocalMax ^ f_temp < f_1) {
                    x_1 = x_temp;
                }
                node[i] = x_1;
            }
            return this;
        }
    }

}
