/*
 * Copyright (c) 2024 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */
/*
 * 2024.12.12
 */
package matsu.num.approximation.generalfield;

/**
 * {@code double} と同等の実数体. <br>
 * (ただし, {@link PseudoRealNumber} の契約に則り,
 * {@code 0d} と {@code -0d} は等価である.)
 * 
 * @author Matsuura Y.
 * @version 21.0
 */
public final class DoubleLike extends PseudoRealNumber<DoubleLike> {

    /**
     * {@link DoubleLike} の元のプロバイダ.
     */
    private static final PseudoRealNumber.Provider<DoubleLike> PROVIDER =
            new DoubleLike.Provider();

    /**
     * 0を表す.
     */
    private static final DoubleLike ZERO = new DoubleLike(0d);

    /**
     * 1を表す.
     */
    private static final DoubleLike ONE = new DoubleLike(1d);

    private final double value;

    /**
     * 唯一のコンストラクタ
     * 
     * @param value value
     * @throws IllegalArgumentException 引数が扱えない場合
     */
    private DoubleLike(double value) {
        super();

        if (!Double.isFinite(value)) {
            throw new IllegalArgumentException(
                    String.format("扱えない値: value = %s", value));
        }
        //-0dを回避する
        if (value == 0d) {
            value = 0d;
        }
        this.value = value;
    }

    /**
     * 外部からの呼び出し不可.
     * 
     * @return -
     */
    @Override
    protected PseudoRealNumber.Provider<DoubleLike> provider() {
        return PROVIDER;
    }

    /**
     * {@inheritDoc}
     * 
     * <p>
     * スローされる例外はスーパータイプに準じる.
     * </p>
     */
    @Override
    public DoubleLike plus(DoubleLike augend) {
        return createOrThrowArithmeticException(this.value + augend.value);
    }

    /**
     * {@inheritDoc}
     * 
     * <p>
     * スローされる例外はスーパータイプに準じる.
     * </p>
     */
    @Override
    public DoubleLike minus(DoubleLike subtrahend) {
        return createOrThrowArithmeticException(this.value - subtrahend.value);
    }

    /**
     * {@inheritDoc}
     * 
     * <p>
     * スローされる例外はスーパータイプに準じる.
     * </p>
     */
    @Override
    public DoubleLike times(DoubleLike multiplicand) {
        return createOrThrowArithmeticException(this.value * multiplicand.value);
    }

    /**
     * {@inheritDoc}
     * 
     * <p>
     * スローされる例外はスーパータイプに準じる.
     * </p>
     */
    @Override
    public DoubleLike dividedBy(DoubleLike divisor) {
        return createOrThrowArithmeticException(this.value / divisor.value);
    }

    /**
     * 値を生成するか, 不正値 (無限大, NaN) の場合はArithmeticExcptionをスローする.
     * 
     * @throws ArithmeticException 不正値
     */
    private static DoubleLike createOrThrowArithmeticException(double value) {
        try {
            return new DoubleLike(value);
        } catch (IllegalArgumentException iae) {
            throw new ArithmeticException(String.format("扱えない値: value = %s", value));
        }
    }

    @Override
    public DoubleLike negated() {
        return new DoubleLike(-this.value);
    }

    @Override
    public DoubleLike abs() {
        return new DoubleLike(Math.abs(this.value));
    }

    /**
     * {@inheritDoc }
     * 
     * <p>
     * {@link DoubleLike} では無限大は返らない.
     * </p>
     */
    @Override
    public double asDouble() {
        return this.value;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (!(obj instanceof DoubleLike target)) {
            return false;
        }

        //-0d,inf,NaNを回避しているので, ==で問題ない
        return this.value == target.value;
    }

    @Override
    public int hashCode() {
        return Double.hashCode(this.value);
    }

    @Override
    public String toString() {
        return Double.toString(this.value);
    }

    /**
     * {@inheritDoc}
     * 
     * <p>
     * スローされる例外はスーパータイプに準じる.
     * </p>
     */
    @Override
    public int compareTo(DoubleLike o) {
        return Double.compare(this.value, o.value);
    }

    /**
     * {@link DoubleLike} の元のプロバイダを返す.
     * 
     * @return プロバイダ
     */
    public static PseudoRealNumber.Provider<DoubleLike> elementProvider() {
        return PROVIDER;
    }

    private static final class Provider
            implements PseudoRealNumber.Provider<DoubleLike> {

        /**
         * 唯一のコンストラクタ.
         */
        Provider() {
            super();
        }

        @Override
        public DoubleLike zero() {
            return ZERO;
        }

        @Override
        public DoubleLike one() {
            return ONE;
        }

        @Override
        public DoubleLike fromDoubleValue(double value) {
            return new DoubleLike(value);
        }

        @Override
        public DoubleLike[] createArray(int length) {
            return new DoubleLike[length];
        }
    }
}
