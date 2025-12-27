/*
 * Copyright © 2024 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */
/*
 * 2025.12.27
 */
package matsu.num.approximation;

import java.math.BigDecimal;
import java.math.MathContext;

/**
 * {@link MathContext#DECIMAL128} ルールに基づく {@link BigDecimal} と同等の実数体.
 * 
 * @author Matsuura Y.
 */
public final class Decimal128 extends PseudoRealNumber<Decimal128> {

    private static final PseudoRealNumber.TypeProvider<Decimal128> TYPE_PROVIDER =
            new Decimal128.TypeProvider();

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
            throw new IllegalArgumentException("NOT accepted: value = " + value);
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
    protected PseudoRealNumber.TypeProvider<Decimal128> typeProvider() {
        return TYPE_PROVIDER;
    }

    /**
     * {@inheritDoc }
     * 
     * <p>
     * {@link Decimal128} では {@link ArithmeticException} はスローされない. <br>
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
     * {@link Decimal128} では {@link ArithmeticException} はスローされない. <br>
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
     * {@link Decimal128} では {@link ArithmeticException} はスローされない. <br>
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
            // 計算が破綻する可能性がある: ArithmeticException
            BigDecimal result = this.value.divide(divisor.value, MathContext.DECIMAL128);

            return new Decimal128(result);
        } catch (ArithmeticException ae) {
            throw new ArithmeticException("illegal operation");
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

        /*
         * BigDecimalはequalityとcomparabilityが整合しないので,
         * BigDecimal.equalsは使えない.
         */
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
         * BigDecimalはequalityとcomparabilityが整合しないので, BigDecimal.hachCodeは使えない.
         * そこで, doubleのハッシュコードを用いる.
         * BigDecimal内で0dと-0dは既に同一値になっているので, このハッシュコードはequalsに整合する.
         * 
         * doubleの表現力のため, 異なる値でも同一のハッシュコード値になる場合がある.
         */
        int result = 1;
        result = 31 * result + Double.hashCode(this.value.doubleValue());
        return result;
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
     * <p>
     * このメソッドは将来非推奨になり, 削除される可能性がある. <br>
     * {@link #elementTypeProvider()} が代替となる.
     * </p>
     * 
     * @return プロバイダ
     */
    @Deprecated(since = "24.4.0", forRemoval = true)
    public static PseudoRealNumber.Provider<Decimal128> elementProvider() {
        return TYPE_PROVIDER;
    }

    /**
     * {@link Decimal128} の元のプロバイダを返す.
     * 
     * @return プロバイダ
     */
    public static PseudoRealNumber.TypeProvider<Decimal128> elementTypeProvider() {
        return TYPE_PROVIDER;
    }

    private static final class TypeProvider
            extends PseudoRealNumber.TypeProvider<Decimal128> {

        /**
         * 唯一のコンストラクタ.
         */
        TypeProvider() {
            super(Decimal128.class);
        }

        @Override
        public Decimal128 fromDoubleValue(double value) {
            // ここで例外をスローする可能性がある
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
    }
}
