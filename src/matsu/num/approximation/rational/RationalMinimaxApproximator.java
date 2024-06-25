/*
 * Copyright (c) 2024 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */
/*
 * 2024.6.25
 */
package matsu.num.approximation.rational;

import java.util.Arrays;

import matsu.num.approximation.DoubleFiniteClosedInterval;

/**
 * Minimax-approximator by rational function. <br>
 * Minimize max<sub><i>x</i></sub>
 * |<i>f</i>(<i>x</i>)-<i>R</i>(<i>x</i>)|/<i>g</i>(<i>x</i>). <br>
 * <i>f</i>(<i>x</i>): function, <i>R</i>(<i>x</i>): rational function,
 * <i>g</i>(<i>x</i>): scaling value. <br>
 * Require that <i>g</i>(<i>x</i>) is strictly positive.
 *
 * @author Matsuura Y.
 * @version 17.0
 * @deprecated 依存先の機能が不完全である
 */
@Deprecated
public final class RationalMinimaxApproximator {

    private final FunctionScaleSupplier supplier;
    private final int numeratorDegree;
    private final int denominatorDegree;

    private RationalFunction rational;

    /**
     * g(x)をスケール因子として、f(x)を有理式で近似する
     *
     * @param function func
     * @param scaleFactor scale
     * @param numeratorDegree
     * @param denominatorDegree
     */
    private RationalMinimaxApproximator(FunctionScaleSupplier supplier, DoubleFiniteClosedInterval range,
            int numeratorDegree, int denominatorDegree, int numberOfIterate) {
        this.supplier = supplier;
        this.numeratorDegree = numeratorDegree;
        this.denominatorDegree = denominatorDegree;

        //ここで例外がスローされるかもしれない.
        approximate(range, numberOfIterate);
    }

    /**
     * Minimax-approximate function by rational function.
     *
     * @param range approximating range
     * @param repeat repeat
     */
    private void approximate(DoubleFiniteClosedInterval range, int repeat) {
        double minX = range.lower();
        double maxX = range.upper();

        int numZeroNode = numeratorDegree + denominatorDegree + 1;
        int numExtremeNode = numZeroNode + 1;

        /* Quasi-Chebyshevノード */
        ApproximatorSet as;
        {
            double[] nodeZero = new double[numZeroNode];
            double[] nodeExtreme = new double[numExtremeNode];
            double scaleX = (maxX - minX) / 2;
            double shiftX = minX + scaleX;
            for (int i = 0, len = numZeroNode; i < len; i++) {
                double c = Math.cos(Math.PI * (2 * i + 1) / (2 * numZeroNode));
                nodeZero[i] = c * scaleX + shiftX;
            }
            for (int i = 0, len = numExtremeNode; i < len; i++) {
                double c = Math.cos(Math.PI * i / (numExtremeNode - 1));
                nodeExtreme[i] = c * scaleX + shiftX;
            }
            Arrays.sort(nodeZero);
            Arrays.sort(nodeExtreme);
            NodeSet nodeSet = new NodeSet(nodeZero, nodeExtreme);
            as = new ApproximatorSet(numeratorDegree, denominatorDegree, nodeSet, supplier);
        }

        /* 有理式を用いたMinimax近似 */
        for (int rep = 0; rep < repeat; rep++) {
            as.update();
        }

        rational = as.rational;
    }

    /**
     * Calculate scaled signed-error at the given value: <br>
     * (<i>R</i>(<i>x</i>)-<i>f</i>(<i>x</i>))/<i>g</i>(<i>x</i>).
     *
     * @param x x
     * @return scaled error}
     */
    public double error(double x) {
        return error_kernel(x, rational, supplier);
    }

    /**
     * Get the previous rational function approximating the function.
     *
     * @return rational function
     */
    public RationalFunction getRational() {
        return rational;
    }

    //=======================================================================
    //static
    /**
     * Create new rational approximator.
     *
     * @param supplier value and scale supplier
     * @param range approximating range
     * @param numeratorDegree degree of numerator polynomial function
     * @param denominatorDegree degree of denominator polynomial function
     * @param numberOfIteration number of iteration
     * @return new approximator
     * @throws IllegalArgumentException degree of numerator/denominator, &ensp;
     * @throws IllegalArgumentException too large degree of denominator
     */
    public static RationalMinimaxApproximator create(FunctionScaleSupplier supplier, DoubleFiniteClosedInterval range,
            int numeratorDegree, int denominatorDegree, int numberOfIteration) {
        if (numeratorDegree < 0 || denominatorDegree < 0) {
            throw new IllegalArgumentException("degree < 0");
        }
        try {
            return new RationalMinimaxApproximator(
                    supplier, range, numeratorDegree, denominatorDegree,
                    numberOfIteration);
        } catch (IllegalArgumentException iae) {
            throw new IllegalArgumentException(iae.getMessage());
        }
    }

    //===========================================================================
    private static class NodeSet {

        double[] nodeZero;
        double[] nodeExtreme;

        public NodeSet(double[] nodeZero, double[] nodeExtreme) {
            this.nodeZero = nodeZero;
            this.nodeExtreme = nodeExtreme;
        }
    }

    //kernel
    private static class ApproximatorSet {

        RationalImpl rational;
        NodeSet nodeSet;
        FunctionScaleSupplier supplier;

        public ApproximatorSet(int numeratorDegree, int denominatorDegree, NodeSet nodeSet,
                FunctionScaleSupplier supplier) {
            int degreeOfFreedom = numeratorDegree + denominatorDegree + 1;
            if (nodeSet.nodeZero.length != degreeOfFreedom || nodeSet.nodeExtreme.length != degreeOfFreedom + 1) {
                throw new IllegalArgumentException("Bug: dimension does not match");
            }

            this.rational = new RationalImpl(numeratorDegree, denominatorDegree);
            this.nodeSet = nodeSet;
            this.supplier = supplier;
        }

        public final ApproximatorSet update() {
            return this.updateCoefficient().searchExtremeNode().updateNodeZero();
        }

        /**
         * nodeZeroで誤差0になるように係数を定める
         *
         * @return this
         */
        private ApproximatorSet updateCoefficient() {
            double[] nodeZero = nodeSet.nodeZero;
            int numNodeZero = nodeZero.length;
            double[] coeffNumerator = rational.coeffNumerator;
            double[] coeffDenominator = rational.coeffDenominator;
            double[] fi = new double[numNodeZero];

            //行列の生成
            double[][] mxCoeffEntry = new double[numNodeZero][numNodeZero];
            for (int i = 0; i < numNodeZero; i++) {
                double y = nodeZero[i];
                double f = supplier.value(y);
                fi[i] = f;
                mxCoeffEntry[i][0] = 1.0;
                for (int j = 1, len = coeffNumerator.length; j < len; j++) {
                    mxCoeffEntry[i][j] = y * mxCoeffEntry[i][j - 1];
                }
                if (coeffDenominator.length > 1) {
                    mxCoeffEntry[i][coeffNumerator.length] = -y * f;
                    for (int j = 2, len = coeffDenominator.length; j < len; j++) {

                        mxCoeffEntry[i][j + coeffNumerator.length - 1] = y
                                * mxCoeffEntry[i][j + coeffNumerator.length - 2];
                    }
                }
            }

            double[] root = LinearEquationSolver.execute(mxCoeffEntry, fi);
            System.arraycopy(root, 0, coeffNumerator, 0, coeffNumerator.length);
            coeffDenominator[0] = 1;
            System.arraycopy(root, coeffNumerator.length, coeffDenominator, 1, coeffDenominator.length - 1);
            return this;
        }

        /**
         * value(nodeExtreme[])を見て,nodeZero[]を調整する
         *
         * @return this
         */
        private ApproximatorSet updateNodeZero() {
            double[] nodeZero = nodeSet.nodeZero;
            double[] nodeExtreme = nodeSet.nodeExtreme;
            double minGap = Math.abs((nodeExtreme[nodeExtreme.length - 1] - nodeExtreme[0]) / nodeExtreme.length);
            double resol = 0.01;
            minGap *= resol;
            for (int i = 0, len = nodeZero.length; i < len; i++) {
                double ym = nodeExtreme[i];
                double yp = nodeExtreme[i + 1];
                double x0 = nodeZero[i];
                double width1 = Math.abs(x0 - ym);
                double width2 = Math.abs(yp - x0);
                double width = Math.min(width1, width2);
                if (width < minGap) {
                    continue;
                }
                double gapX = resol * width;

                double fm = Math.abs(error_kernel(ym, rational, supplier));
                double fp = Math.abs(error_kernel(yp, rational, supplier));
                nodeZero[i] += fp > fm ? gapX : -gapX;
            }
            return this;
        }

        /**
         * value(nodeExtreme[])を見て,nodeExtreme[]を極値の方へ動かす
         *
         * @return this
         */
        private ApproximatorSet searchExtremeNode() {

            double[] nodeZero = nodeSet.nodeZero;
            double[] nodeExtreme = nodeSet.nodeExtreme;
            double minGap = Math.abs((nodeExtreme[nodeExtreme.length - 1] - nodeExtreme[0]) / nodeExtreme.length);
            double resol = 0.01;
            minGap *= resol;

            for (int i = 1, len = nodeExtreme.length - 1; i < len; i++) {
                double xm = nodeZero[i - 1];
                double xp = nodeZero[i];
                double y0 = nodeExtreme[i];
                double width1 = Math.abs(y0 - xm);
                double width2 = Math.abs(xp - y0);
                double width = Math.min(width1, width2);
                if (width < minGap) {
                    continue;
                }
                double gapY = resol * width;
                double fm = Math.abs(error_kernel(y0 - gapY, rational, supplier));
                double fp = Math.abs(error_kernel(y0 + gapY, rational, supplier));
                y0 += fp > fm ? gapY : -gapY;
                nodeExtreme[i] = y0;
            }

            return this;
        }

    }

    private static class RationalImpl implements RationalFunction {

        private double[] coeffNumerator;
        private double[] coeffDenominator;

        RationalImpl(int numeratorDegree, int denominatorDegree) {
            this.coeffNumerator = new double[numeratorDegree + 1];
            this.coeffDenominator = new double[denominatorDegree + 1];
        }

        @Override
        public final double value(double x) {
            double valueNume = 0;
            for (int j = coeffNumerator.length - 1; j >= 0; j--) {
                valueNume *= x;
                valueNume += coeffNumerator[j];
            }
            double valueDenomi = 0;
            for (int j = coeffDenominator.length - 1; j >= 0; j--) {
                valueDenomi *= x;
                valueDenomi += coeffDenominator[j];
            }
            return valueNume / valueDenomi;
        }

        @Override
        public int numeratorDegree() {
            return coeffNumerator.length - 1;
        }

        @Override
        public int denominatorDegree() {
            return coeffDenominator.length - 1;
        }

        @Override
        public double[] getNumeratorCoefficient() {
            return coeffNumerator.clone();
        }

        @Override
        public double[] getDenominatorCoefficient() {
            return coeffDenominator.clone();
        }

    }

    //=========================================
    //kernel
    private static double error_kernel(double x, RationalFunction rational, FunctionScaleSupplier supplier) {
        return (rational.value(x) - supplier.value(x)) / supplier.scale(x);
    }

}
