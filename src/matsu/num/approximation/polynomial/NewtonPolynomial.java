/**
 * 2022.5.3
 * ver6.0: 実装.
 *
 * unfinished
 */
package matsu.num.approximation.polynomial;

/**
 * Newton polynomial interpolation interface. <br>
 * The formula of Newton polynomial is <br>
 * <i>p</i>(<i>x</i>) = <i>b</i><sub>0</sub> +
 * <i>b</i><sub>1</sub>(<i>x</i> - <i>c</i><sub>0</sub>) +
 * <i>b</i><sub>2</sub>(<i>x</i> - <i>c</i><sub>0</sub>)(<i>x</i> -
 * <i>c</i><sub>1</sub>) + &hellip; +
 * <i>b</i><sub><i>n</i> - 1</sub>(<i>x</i> - <i>c</i><sub>0</sub>)(<i>x</i> -
 * <i>c</i><sub>1</sub>)...(<i>x</i> - <i>c</i><sub><i>n</i> - 2</sub>).
 * <br>
 * <i>c</i><sub><i>i</i></sub>: node. <br>
 * <i>b</i><sub><i>i</i></sub>: Newton coefficient.
 *
 * @author Matsuura, Y.
 * @version 6.0.0
 */
public interface NewtonPolynomial extends PolynomialFunction {

    /**
     * Return the node <i>c</i><sub><i>i</i></sub>.
     *
     * @return node
     */
    public double[] getNode();

    /**
     * Return the Newton coefficients <i>b</i><sub><i>i</i></sub>.
     *
     * @return Newton coefficients
     */
    public double[] getNewtonCoefficient();

}
