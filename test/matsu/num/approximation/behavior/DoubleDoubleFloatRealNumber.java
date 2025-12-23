/*
 * Copyright © 2024 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */
package matsu.num.approximation.behavior;

import org.junit.Ignore;

import matsu.num.approximation.DoubleLike;
import matsu.num.approximation.PseudoRealNumber;
import matsu.num.mathtype.DoubleDoubleFloat;

/**
 * double-double 型を扱う.
 * 
 * @author Matsuura Y.
 */
@Ignore
public final class DoubleDoubleFloatRealNumber
        extends PseudoRealNumber<DoubleDoubleFloatRealNumber> {

    public static final DoubleDoubleFloatRealNumber ZERO =
            new DoubleDoubleFloatRealNumber(DoubleDoubleFloat.POSITIVE_0);

    public static final DoubleDoubleFloatRealNumber ONE =
            new DoubleDoubleFloatRealNumber(DoubleDoubleFloat.POSITIVE_1);

    private static final PseudoRealNumber.TypeProvider<
            DoubleDoubleFloatRealNumber> PROVIDER = new Provider();

    private final DoubleDoubleFloat value;

    /**
     * {@code DoubleDoubleFloat} から生成するコンストラクタ.
     * 
     * @throws IllegalArgumentException 有限でない
     * @throws NullPointerException null
     */
    private DoubleDoubleFloatRealNumber(DoubleDoubleFloat value) {
        if (!value.isFinite()) {
            throw new IllegalArgumentException(
                    String.format("扱えない値: value = %s", value));
        }

        //-0を排除
        if (value.equals(DoubleDoubleFloat.NEGATIVE_0)) {
            value = DoubleDoubleFloat.POSITIVE_0;
        }

        this.value = value;
    }

    /**
     * {@code double} から生成するコンストラクタ.
     * 
     * @throws IllegalArgumentException 有限でない
     */
    private DoubleDoubleFloatRealNumber(double value) {
        this(DoubleDoubleFloat.valueOf(value));
    }

    @Override
    protected PseudoRealNumber.Provider<DoubleDoubleFloatRealNumber> provider() {
        return PROVIDER;
    }

    @Override
    public DoubleDoubleFloatRealNumber plus(DoubleDoubleFloatRealNumber augend) {
        return createOrThrowArithmeticException(this.value.plus(augend.value));
    }

    @Override
    public DoubleDoubleFloatRealNumber minus(DoubleDoubleFloatRealNumber subtrahend) {
        return createOrThrowArithmeticException(this.value.minus(subtrahend.value));
    }

    @Override
    public DoubleDoubleFloatRealNumber times(DoubleDoubleFloatRealNumber multiplicand) {
        return createOrThrowArithmeticException(this.value.times(multiplicand.value));
    }

    @Override
    public DoubleDoubleFloatRealNumber dividedBy(DoubleDoubleFloatRealNumber divisor) {
        return createOrThrowArithmeticException(this.value.dividedBy(divisor.value));
    }

    /**
     * 値を生成するか, 不正値 (無限大, NaN) の場合はArithmeticExcptionをスローする.
     * 
     * @throws ArithmeticException 不正値の場合
     */
    private static DoubleDoubleFloatRealNumber createOrThrowArithmeticException(DoubleDoubleFloat value) {
        try {
            return new DoubleDoubleFloatRealNumber(value);
        } catch (IllegalArgumentException iae) {
            throw new ArithmeticException(String.format("扱えない値: value = %s", value));
        }
    }

    @Override
    public DoubleDoubleFloatRealNumber negated() {
        return new DoubleDoubleFloatRealNumber(this.value.negated());
    }

    @Override
    public DoubleDoubleFloatRealNumber abs() {
        return new DoubleDoubleFloatRealNumber(this.value.abs());
    }

    @Override
    public double asDouble() {
        return this.value.doubleValue();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (!(obj instanceof DoubleDoubleFloatRealNumber target)) {
            return false;
        }

        return this.value.equals(target.value);
    }

    @Override
    public int hashCode() {
        return this.value.hashCode();
    }

    @Override
    public int compareTo(DoubleDoubleFloatRealNumber o) {
        return this.value.compareTo(o.value);
    }

    @Override
    public String toString() {
        return this.value.toString();
    }

    /**
     * {@link DoubleLike} の元のプロバイダを返す.
     * 
     * @return プロバイダ
     */
    public static PseudoRealNumber.TypeProvider<DoubleDoubleFloatRealNumber> elementTypeProvider() {
        return PROVIDER;
    }

    private static final class Provider
            extends PseudoRealNumber.TypeProvider<DoubleDoubleFloatRealNumber> {

        /**
         * 唯一のコンストラクタ.
         */
        Provider() {
            super(DoubleDoubleFloatRealNumber.class);
        }

        @Override
        public DoubleDoubleFloatRealNumber zero() {
            return ZERO;
        }

        @Override
        public DoubleDoubleFloatRealNumber one() {
            return ONE;
        }

        @Override
        public DoubleDoubleFloatRealNumber fromDoubleValue(double value) {
            return new DoubleDoubleFloatRealNumber(value);
        }
    }
}
