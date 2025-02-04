/*
 * Copyright © 2024 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */
package matsu.num.approximation.rational;

import java.util.function.DoubleUnaryOperator;

import org.junit.Ignore;

import matsu.num.approximation.DoubleFiniteClosedInterval;

/**
 *
 */
@SuppressWarnings("deprecation")
@Ignore
final class RationalMinimaxApproximatorTest {

    public static void main(String[] args) {
        new RationalMinimaxApproximatorTest();
    }

    public RationalMinimaxApproximatorTest() {
        DoubleUnaryOperator function;
        DoubleUnaryOperator scaleFactor;
        //        function = (double t) -> MinimaxPolynomializerTest.sinSqrtX_perSqrtX(t);
        //        scaleFactor = (double t) -> MinimaxPolynomializerTest.sinSqrtX_perSqrtX(t);
        function = (double t) -> 1 + Math.log1p(t);
        scaleFactor = (double t) -> 1;

        FunctionScaleSupplier fss = FunctionScaleSupplier.createScalable(function, scaleFactor);

        int numeratorDegree = 2;
        int denominatorDegree = 3;

        double minX = -0.5;
        double maxX = 1;
        DoubleFiniteClosedInterval fr = DoubleFiniteClosedInterval.from(minX, maxX);
        double deltaX = 0.015625 * (maxX - minX);
        int repeat = 10000;

        RationalMinimaxApproximator bdmp = RationalMinimaxApproximator.create(
                fss, fr, numeratorDegree,
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

    @SuppressWarnings("unused")
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
