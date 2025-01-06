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

import java.util.Objects;
import java.util.function.DoubleUnaryOperator;

/**
 * Function value and scale supplier.
 *
 * @author Matsuura, Y.
 * @version 17.0
 * @deprecated 一時的に非推奨に
 */
@Deprecated
public final class FunctionScaleSupplier {

    /**
     * Minimum value of scaling value. <br>
     * The provided scale value is modified to this value when it is too small.
     */
    public static final double MINIMUM_SCALE_VALUE = 1E-200;

    private final DoubleUnaryOperator function;
    private final DoubleUnaryOperator scaleFactor;

    private FunctionScaleSupplier(DoubleUnaryOperator function, DoubleUnaryOperator scaleFactor) {
        if (Objects.isNull(function) || Objects.isNull(scaleFactor)) {
            throw new NullPointerException();
        }
        this.function = function;
        this.scaleFactor = (double operand) -> {
            double out = scaleFactor.applyAsDouble(operand);
            out = Math.abs(out);
            return out > MINIMUM_SCALE_VALUE ? out : MINIMUM_SCALE_VALUE;
        };
    }

    /**
     * Calculate function value at the given <i>x</i>.
     *
     * @param x x
     * @return function value
     */
    public double value(double x) {
        return function.applyAsDouble(x);
    }

    /**
     * Calculate scale at the given <i>x</i>.
     *
     * @param x x
     * @return function scale
     */
    public double scale(double x) {
        return scaleFactor.applyAsDouble(x);
    }

    /**
     * Create a function supplier based on scale = 1.
     *
     * @param function function
     * @return new {@link FunctionScaleSupplier}
     */
    public static FunctionScaleSupplier create(DoubleUnaryOperator function) {
        return new FunctionScaleSupplier(function, (double operand) -> 1);
    }

    /**
     * Create a set of function and scale supplier.
     *
     * @param function function
     * @param scaleFactor scaleFactor
     * @return new {@link FunctionScaleSupplier}
     */
    public static FunctionScaleSupplier createScalable(DoubleUnaryOperator function, DoubleUnaryOperator scaleFactor) {
        return new FunctionScaleSupplier(function, scaleFactor);
    }

}
