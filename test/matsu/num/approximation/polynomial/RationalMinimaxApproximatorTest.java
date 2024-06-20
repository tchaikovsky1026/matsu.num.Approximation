/*
 * 2021.6.16
 */
package matsu.num.approximation.polynomial;

import java.util.function.DoubleUnaryOperator;

import matsu.num.approximation.FiniteRange;
import matsu.num.commons.Exponentiation;

/**
 *
 * @author Matsuura Y.
 */
public class RationalMinimaxApproximatorTest {

    public static void main(String[] args) {
        new RationalMinimaxApproximatorTest();
    }

    public RationalMinimaxApproximatorTest() {
        DoubleUnaryOperator function;
        DoubleUnaryOperator scaleFactor;
        //        function = (double t) -> MinimaxPolynomializerTest.sinSqrtX_perSqrtX(t);
        //        scaleFactor = (double t) -> MinimaxPolynomializerTest.sinSqrtX_perSqrtX(t);
        function = (double t) -> 1 + Exponentiation.log1p(t);
        scaleFactor = (double t) -> 1;

        FunctionScaleSupplier fss = FunctionScaleSupplier.createScalable(function, scaleFactor);

        int numeratorDegree = 2;
        int denominatorDegree = 3;

        double minX = -0.5;
        double maxX = 1;
        FiniteRange fr = FiniteRange.create(minX, maxX);
        double deltaX = 0.015625 * (maxX - minX);
        int repeat = 10000;

        RationalMinimaxApproximator bdmp = RationalMinimaxApproximator.create(fss, fr, numeratorDegree,
                denominatorDegree, repeat);
        RationalFunction rational = bdmp.getRational();

        double[] numeCoeff = rational.getNumeratorCoefficient();
        double[] denomiCoeff = rational.getDenominatorCoefficient();

        System.out.println("numeCoeff");
        for (double numeCoeff1 : numeCoeff) {
            System.out.println(numeCoeff1);
        }
        System.out.println("");

        System.out.println("denomiCoeff");
        for (double denomiCoeff1 : denomiCoeff) {
            System.out.println(denomiCoeff1);
        }
        System.out.println("");

        System.out.println("x\tf\tr\te");
        for (double x = minX; x < maxX + 0.5 * deltaX; x += deltaX) {
            System.out.println(
                    x + "\t" + function.applyAsDouble(x) + "\t" + bdmp.getRational().value(x) + "\t" + bdmp.error(x));
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
