/*
 * Copyright (c) 2024 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */
/*
 * 2024.9.19
 */
package matsu.num.approximation.generalfield;

import java.math.BigDecimal;
import java.math.MathContext;

/**
 * {@link MathContext#DECIMAL128} ルールに基づく {@link BigDecimal} と同等の実数体.
 * 
 * 
 * @author Matsuura Y.
 * @version 19.1
 */
public final class Decimal128 extends PseudoRealNumber<Decimal128> {

    private static final PseudoRealNumber.Provider<Decimal128> PROVIDER =
            new Decimal128.Provider();
    private static final Decimal128 ZERO = Decimal128.of(0d);
    private static final Decimal128 ONE = Decimal128.of(1d);

    private final BigDecimal value;

    /**
     * このクラスのハッシュコード. イミュータブル.
     */
    private final int immutableHashCode;

    /**
     * 唯一のコンストラクタ.
     * 
     * @throws NullPointerException null
     */
    private Decimal128(BigDecimal value) {
        this.value = value;

        this.immutableHashCode = this.calcHashCode();
    }

    /**
     * 外部からの呼び出し不可.
     * 
     * @return -
     */
    @Override
    protected PseudoRealNumber.Provider<Decimal128> provider() {
        return PROVIDER;
    }

    /**
     * {@inheritDoc }
     * 
     * <p>
     * {@link Decimal128} では{@link ArithmeticException} はスローされない. <br>
     * その他のスローされる例外はスーパータイプに準じる.
     * </p>
     */
    @Override
    public Decimal128 plus(Decimal128 augend) {
        BigDecimal result = this.value.add(augend.value, MathContext.DECIMAL128);
        return new Decimal128(result);
    }

    /**
     * {@inheritDoc }
     * 
     * <p>
     * {@link Decimal128} では{@link ArithmeticException} はスローされない. <br>
     * その他のスローされる例外はスーパータイプに準じる.
     * </p>
     */
    @Override
    public Decimal128 minus(Decimal128 subtrahend) {
        BigDecimal result = this.value.subtract(subtrahend.value, MathContext.DECIMAL128);
        return new Decimal128(result);
    }

    /**
     * {@inheritDoc }
     * 
     * <p>
     * {@link Decimal128} では{@link ArithmeticException} はスローされない. <br>
     * その他のスローされる例外はスーパータイプに準じる.
     * </p>
     */
    @Override
    public Decimal128 times(Decimal128 multiplicand) {
        BigDecimal result = this.value.multiply(multiplicand.value, MathContext.DECIMAL128);
        return new Decimal128(result);
    }

    /**
     * {@inheritDoc }
     * 
     * <p>
     * スローされる例外はスーパータイプに準じる.
     * </p>
     */
    @Override
    public Decimal128 dividedBy(Decimal128 divisor) {
        try {
            BigDecimal result = this.value.divide(divisor.value, MathContext.DECIMAL128);
            return new Decimal128(result);
        } catch (ArithmeticException ae) {
            throw new ArithmeticException("計算結果が扱えない値");
        }
    }

    @Override
    public Decimal128 negated() {
        return new Decimal128(this.value.negate());
    }

    @Override
    public Decimal128 abs() {
        return new Decimal128(this.value.abs());
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

        if (!(obj instanceof Decimal128 target)) {
            return false;
        }

        return this.compareTo(target) == 0;
    }

    @Override
    public int hashCode() {
        return this.immutableHashCode;
    }

    /**
     * ハッシュコードを計算する.
     * 
     * @return ハッシュコード
     */
    private int calcHashCode() {

        //BigDecimal内では0dと-0dは既に同一値になっているので, このハッシュコードはequalsに整合する.
        return Double.hashCode(this.value.doubleValue());
    }

    /**
     * {@inheritDoc}
     * 
     * <p>
     * スローされる例外はスーパータイプに準じる.
     * </p>
     */
    @Override
    public int compareTo(Decimal128 o) {
        return this.value.compareTo(o.value);
    }

    @Override
    public String toString() {
        return this.value.toString();
    }

    /**
     * {@link Decimal128} の元のプロバイダを返す.
     * 
     * @return プロバイダ
     */
    public static PseudoRealNumber.Provider<Decimal128> elementProvider() {
        return PROVIDER;
    }

    /**
     * <p>
     * このメソッドは非公開である. <br>
     * 外部からの {@link Decimal128} インスタンスの生成は, プロバイダ経由で行う.
     * </p>
     * 
     * <p>
     * 与えた {@code double} 値に対応する {@link Decimal128} を返す. <br>
     * {@link PseudoRealNumber.Provider}{@code <}{@link Decimal128}{@code >}
     * から呼ばれることを想定されている.
     * </p>
     * 
     * @param value 値
     * @return value に相当するインスタンス
     * @throws IllegalArgumentException 引数が扱えない値の場合
     */
    private static Decimal128 of(double value) {
        if (!Double.isFinite(value)) {
            throw new IllegalArgumentException(
                    String.format("扱えない値: value = %s", value));
        }

        return new Decimal128(new BigDecimal(value, MathContext.DECIMAL128));
    }

    private static final class Provider
            implements PseudoRealNumber.Provider<Decimal128> {

        /**
         * 唯一のコンストラクタ.
         */
        Provider() {
            super();
        }

        @Override
        public Decimal128 fromDoubleValue(double value) {
            return Decimal128.of(value);
        }

        @Override
        public Decimal128 zero() {
            return ZERO;
        }

        @Override
        public Decimal128 one() {
            return ONE;
        }

        @Override
        public Decimal128[] createArray(int length) {
            if (length < 0) {
                throw new IllegalArgumentException("サイズが負");
            }
            return new Decimal128[length];
        }

    }

}
