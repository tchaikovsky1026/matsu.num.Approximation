/*
 * Copyright (c) 2024 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */
/*
 * 2024.6.20
 */
package matsu.num.approximation.polynomial;

/**
 * Polynomial function interface.
 *
 * @author Matsuura Y.
 * @version 17.0
 */
public interface PolynomialFunction {

    /**
     * Calculate the value of the polynomial at the given point.
     *
     * @param x point
     * @return value of the polynomial
     */
    public double value(double x);

    /**
     * Return degree of the polynomial.
     *
     * @return degree
     */
    public int degree();

    /**
     * Return polynomial coefficients <i>a</i><sub><i>i</i></sub> such as <br>
     * <i>p</i>(<i>x</i>) = <i>a</i><sub>0</sub> +
     * <i>a</i><sub>1</sub><i>x</i> + &hellip; +
     * <i>a</i><sub><i>n</i>-1</sub><i>x</i><sup><i>n</i>-1</sup>.
     *
     * @return polynomial coefficients
     */
    public double[] getPolynomialCoefficient();
}
