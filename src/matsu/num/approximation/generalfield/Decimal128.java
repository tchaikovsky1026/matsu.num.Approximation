/*
 * Copyright (c) 2024 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */
/*
 * 2024.10.8
 */
package matsu.num.approximation.generalfield;

import java.math.BigDecimal;
import java.math.MathContext;

/**
 * {@link MathContext#DECIMAL128} ルールに基づく {@link BigDecimal} と同等の実数体.
 * 
 * 
 * @author Matsuura Y.
 * @version 19.2
 */
public final class Decimal128 extends PseudoRealNumber<Decimal128> {

    private static final PseudoRealNumber.Provider<Decimal128> PROVIDER =
            new Decimal128.Provider();
    private static final Decimal128 ZERO = new Decimal128(0d);
    private static final Decimal128 ONE = new Decimal128(1d);

    private final BigDecimal value;

    /**
     * このクラスのハッシュコード. イミュータブル.
     */
    private final int immutableHashCode;

    /**
     * {@link BigDecimal} から生成するコンストラクタ.
     * 
     * @throws NullPointerException 引数がnullの場合
     */
    private Decimal128(BigDecimal value) {
        this.value = value;

        //ここでNullPointerExceptionが発生する可能性がある
        this.immutableHashCode = this.calcHashCode();
    }

    /**
     * {@code double} から生成するコンストラクタ.
     * 
     * @throws IllegalArgumentException 引数が有限でない場合
     */
    private Decimal128(double value) {
        this(toBigDecimal128(value));
    }

    /**
     * {@code double} を {@link BigDecimal} に変換する.
     * 
     * @throws IllegalArgumentException 引数が有限でない場合
     */
    private static BigDecimal toBigDecimal128(double value) {
        if (!Double.isFinite(value)) {
            throw new IllegalArgumentException(
                    String.format("扱えない値: value = %s", value));
        }

        //BigDecimalは正の0と負の0を区別しないので, これでよい.
        return new BigDecimal(value, MathContext.DECIMAL128);
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
     * {@link Decimal128} では0割りの場合のみ {@link ArithmeticException} がスローされる. <br>
     * その他のスローされる例外はスーパータイプに準じる.
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

        /*
         * BigDecimalはequalityとcomparabilityが整合しないので, BigDecimal.equalsは使えない.
         * そこで, doubleに直すことでcomparabilityに整合させる.
         * 
         * BigDecimal内では0dと-0dは既に同一値になっているので, このハッシュコードはequalsに整合する.
         * doubleに直すことで, 元は異なる値でも同一のハッシュコード値になる場合がある.
         */
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
            return new Decimal128(value);
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
