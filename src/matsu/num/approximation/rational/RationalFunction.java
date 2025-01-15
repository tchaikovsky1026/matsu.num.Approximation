/*
 * Copyright © 2024 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */
/*
 * 2024.6.20
 */
package matsu.num.approximation.rational;

/**
 * Rational function interface.
 *
 * @author Matsuura Y.
 * @deprecated 一時的に非推奨に
 */
@Deprecated
public interface RationalFunction {

    /**
     * Calculate the value of the rational at the given point.
     *
     * @param x point
     * @return value of the rational
     */
    public double value(double x);

    /**
     * Get degree of the numerator.
     *
     * @return degree
     */
    public int numeratorDegree();

    /**
     * Get degree of the denominator.
     *
     * @return degree
     */
    public int denominatorDegree();

    /**
     * Get numerator coefficients <i>a</i><sub><i>i</i></sub>. <br>
     * Rational <i>R</i>(<i>x</i>) = <i>P</i>(<i>x</i>)/<i>Q</i>(<i>x</i>) and
     * <br>
     * <i>P</i>(<i>x</i>) = <i>a</i><sub>0</sub> +
     * <i>a</i><sub>1</sub><i>x</i> + &hellip; +
     * <i>a</i><sub><i>n</i>-1</sub><i>x</i><sup><i>n</i>-1</sup>.
     *
     * @return numerator coefficients
     */
    public double[] getNumeratorCoefficient();

    /**
     * Get denominator coefficients <i>b</i><sub><i>i</i></sub>. <br>
     * Rational <i>R</i>(<i>x</i>) = <i>P</i>(<i>x</i>)/<i>Q</i>(<i>x</i>) and
     * <br>
     * <i>Q</i>(<i>x</i>) = <i>b</i><sub>0</sub> +
     * <i>b</i><sub>1</sub><i>x</i> + &hellip; +
     * <i>b</i><sub><i>n</i>-1</sub><i>x</i><sup><i>n</i>-1</sup>.
     *
     * @return denominator coefficients
     */
    public double[] getDenominatorCoefficient();

    public static double[] getNumeratorCombinedPolynomial(double[] polynomialCoefficient, RationalFunction rational) {
        double[] coeffNumerator = rational.getNumeratorCoefficient();
        double[] coeffDenominator = rational.getDenominatorCoefficient();
        int numOfNewNumerator =
                Math.max(coeffNumerator.length, polynomialCoefficient.length + coeffDenominator.length - 1);
        double[] out = new double[numOfNewNumerator];
        System.arraycopy(coeffNumerator, 0, out, 0, coeffNumerator.length);
        for (int i = 0; i < coeffDenominator.length; i++) {
            for (int j = 0; j < polynomialCoefficient.length; j++) {
                out[i + j] += coeffDenominator[i] * polynomialCoefficient[j];
            }
        }
        return out;
    }

}
