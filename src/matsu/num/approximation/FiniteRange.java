/**
 * 2022.5.3
 * ver6.0: Major update.
 */
package matsu.num.approximation;

import java.io.Serializable;

/**
 * Finite range of one variable.
 *
 * @author Matsuura, Y.
 * @version 6.0.0
 */
public final class FiniteRange implements IRange, Cloneable, Serializable {

    private static final long serialVersionUID = 6_00_00L;

    private final double min;
    private final double max;

    private FiniteRange(double min, double max) {
        this.min = min;
        this.max = max;
    }

    @Override
    public double lower() {
        return min;
    }

    @Override
    public double upper() {
        return max;
    }

    /**
     * Create finite range from <i>x</i><sub>1</sub> and
     * <i>x</i><sub>2</sub>.<br>
     * <i>x</i><sub>1</sub>, <i>x</i><sub>2</sub> &rarr;
     * [<i>x</i><sub>1</sub>,<i>x</i><sub>2</sub>] or
     * [<i>x</i><sub>2</sub>,<i>x</i><sub>1</sub>].
     *
     * @param x1 min or max
     * @param x2 min or max
     * @return range
     */
    public static FiniteRange create(double x1, double x2) {
        if (!Double.isFinite(x1) || !Double.isFinite(x2)) {
            throw new IllegalArgumentException("irregular values.");
        }
        double min = x1 < x2 ? x1 : x2;
        double max = x1 > x2 ? x1 : x2;
        double absMin = Math.abs(min);
        double absMax = Math.abs(max);
        double abs = absMin > absMax ? absMin : absMax;
        double width = max - min;
        if (width < MINIMUM_ABSOLUTE_WIDTH + abs * MINIMUM_RELATIVE_WIDTH) {
            throw new IllegalArgumentException("x1 and x2 are approximately equal.");
        }

        return new FiniteRange(min, max);
    }

    @Override
    public FiniteRange clone() {
        FiniteRange out;
        try {
            out = (FiniteRange) super.clone();
        } catch (CloneNotSupportedException | ClassCastException e) {
            throw new IllegalStateException("failed: clone");
        }
        return out;
    }
}
