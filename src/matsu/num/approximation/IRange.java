/**
 * 2022.5.3
 * ver6.0: バージョン変更
 */
package matsu.num.approximation;

/**
 * Range interface of one variable. 
 *
 * @author Matsuura, Y.
 * @version 6.0.0
 */
public interface IRange {
    
    /**
     * Minimum of absolute width.
     */
    public static final double MINIMUM_ABSOLUTE_WIDTH = 1E-200;
    /**
     * Minimum of relative width.
     */
    public static final double MINIMUM_RELATIVE_WIDTH = 1E-10;

    /**
     * Lower bound.
     * 
     * @return lower bound
     */
    public double lower();

    /**
     * Upper bound.
     * 
     * @return upper bound
     */
    public double upper();

}
