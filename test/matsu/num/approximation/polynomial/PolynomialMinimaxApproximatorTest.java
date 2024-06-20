/*
 * 2021.6.16
 */
package matsu.num.approximation.polynomial;

import java.util.function.DoubleUnaryOperator;

import matsu.num.approximation.FiniteRange;

/**
 *
 * @author Matsuura Y.
 */
public class PolynomialMinimaxApproximatorTest {

    public static void main(String[] args) {
        new PolynomialMinimaxApproximatorTest();
    }

    public PolynomialMinimaxApproximatorTest() {
        DoubleUnaryOperator function;
        DoubleUnaryOperator scaleFactor;
        function = (double t) -> PolynomialMinimaxApproximatorTest.sinSqrtX_perSqrtX(t);
        scaleFactor = (double t) -> 1;

        FunctionScaleSupplier fss = FunctionScaleSupplier.createScalable(function, function);

        int degree = 5;

        double minX = 0;
        double maxX = 0.4;
        FiniteRange fr = FiniteRange.create(minX, maxX);
        double deltaX = 0.015625 * (maxX - minX);
        int repeat = 1000;

        PolynomialMinimaxApproximator bdmp = new PolynomialMinimaxApproximator(fss, fr, degree, repeat);

        System.out.println("coeff");
        for (double polyCoeff : bdmp.getPolynomial().getPolynomialCoefficient()) {
            System.out.println(polyCoeff);
        }
        System.out.println("");

        System.out.println("node\tv\te");
        for (double node : ((NewtonPolynomial) bdmp.getPolynomial()).getNode()) {
            System.out.println(node + "\t" + function.applyAsDouble(node) + "\t" + bdmp.error(node));
        }
        System.out.println("");

        System.out.println("x\tv\te");
        for (double x = minX; x < maxX + 0.5 * deltaX; x += deltaX) {
            System.out.println(x + "\t" + function.applyAsDouble(x) + "\t" + bdmp.error(x));
        }
        System.out.println("");
    }

    private static double sinSqrtX_perSqrtX(double x) {

        double value = 0;
        int rep = 20;
        for (int i = rep; i >= 1; i--) {
            value *= -1;
            int i2 = 2 * i;
            int a = i2 * (i2 + 1);
            value /= a;
            value *= x;
            value += 1.0;
        }

        return value;
    }

}
