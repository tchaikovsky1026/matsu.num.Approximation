/*
 * Copyright (c) 2024 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */
/*
 * 2024.9.5
 */
package matsu.num.approximation.generalfield;

/**
 * <p>
 * {@link PseudoRealNumber} のサブタイプ {@code T} に関する有限閉区間を扱う. <br>
 * 区間の境界値に基づくequalityを提供する.
 * </p>
 * 
 * <p>
 * サポートされているのは, 次を満たす閉区間 [<i>a</i>, <i>b</i>] である. <br>
 * (<i>b</i> - <i>a</i>) &ge; max(<i>e</i><sub>a</sub>,
 * <i>e</i><sub>r</sub>|<i>a</i>|,
 * <i>e</i><sub>r</sub>|<i>b</i>|) <br>
 * ただし,
 * <i>e</i><sub>a</sub>, <i>e</i><sub>r</sub>
 * は定数である.
 * </p>
 *
 * @author Matsuura, Y.
 * @version 18.2
 * @param <T> 体の元を表現する型パラメータ
 */
public final class FiniteClosedInterval<T extends PseudoRealNumber<T>> {

    /**
     * 区間の絶対最小幅を表す定数 (<i>e</i><sub>a</sub>).
     */
    public static final double LOWER_LIMIT_OF_ABSOLUTE_WIDTH = 1E-200;

    /**
     * 区間の相対最小幅を表す定数 (<i>e</i><sub>r</sub>).
     */
    public static final double LOWER_LIMIT_OF_RELATIVE_WIDTH = 1E-10;

    private final T min;
    private final T max;

    private final T gap;

    private final int immutableHashCode;

    /**
     * 唯一の公開しないコンストラクタ.
     * {@literal min <= max} を要求する.
     */
    private FiniteClosedInterval(T min, T max) {
        assert min.compareTo(max) <= 0;

        this.min = min;
        this.max = max;

        this.gap = this.max.minus(this.min);

        this.immutableHashCode = this.calcHashCode();
    }

    /**
     * 下側境界値 (<i>a</i>) を返す.
     * 
     * @return 下側境界値
     */
    public T lower() {
        return this.min;
    }

    /**
     * 上側境界値 (<i>b</i>) を返す.
     * 
     * @return 上側境界値
     */
    public T upper() {
        return this.max;
    }

    /**
     * この区間の幅 (upper - lower) を返す.
     * 
     * @return 区間幅
     */
    public T gap() {
        return this.gap;
    }

    /**
     * <i>x</i> が閉区間に含まれる
     * (<i>x</i> &in; [<i>a</i>, <i>b</i>])
     * かどうかを判定する.
     * 
     * @param x <i>x</i>, 引数
     * @return 閉区間に含まれる場合はtrue
     * @throws NullPointerException 引数がnullの場合
     */
    public boolean accepts(T x) {
        return x.compareTo(this.lower()) >= 0 &&
                x.compareTo(this.upper()) <= 0;
    }

    /**
     * 与えられたインスタンスと自身との等価性を判定する.
     * 
     * @param obj 比較対象
     * @return 等価の場合はtrue
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof FiniteClosedInterval<?> target)) {
            return false;
        }
        return this.min.equals(target.min) &&
                this.max.equals(target.max);
    }

    /**
     * 自身のハッシュコードを返す.
     * 
     * @return ハッシュコード
     */
    @Override
    public int hashCode() {
        return this.immutableHashCode;
    }

    /**
     * ハッシュコードを計算する.
     * 不変クラスであるので, ライフサイクル全体でハッシュコードは不変である.
     * 
     * @return ハッシュコード
     */
    private int calcHashCode() {
        int result = this.min.hashCode();
        result = 31 * result + this.max.hashCode();
        return result;
    }

    /**
     * <p>
     * 自身の文字列表現を返す.
     * </p>
     * 
     * <p>
     * 文字列表現はバージョン間の互換性は担保されない. <br>
     * おそらく次のような形式だろう. <br>
     * {@code ClosedInterval[lower, upper]}
     * </p>
     * 
     * @return 文字列表現
     */
    @Override
    public String toString() {
        return String.format(
                "ClosedInterval[%s, %s]",
                this.min, this.max);
    }

    /**
     * <p>
     * 指定した値が閉区間の境界に適合するかを判定する. <br>
     * <i>x</i><sub>1</sub>, <i>x</i><sub>2</sub> &rarr;
     * [<i>x</i><sub>1</sub>, <i>x</i><sub>2</sub>] または
     * [<i>x</i><sub>2</sub>, <i>x</i><sub>1</sub>]
     * </p>
     * 
     * @param <T> 体の元を表現する型パラメータ
     * @param x1 <i>x</i><sub>1</sub>, 引数
     * @param x2 <i>x</i><sub>2</sub>, 引数
     * @return 適合する場合はtrue
     */
    public static <T extends PseudoRealNumber<T>> boolean acceptsBoundaryValues(T x1, T x2) {
        T width = x1.minus(x2).abs();
        return width.asDouble() >= LOWER_LIMIT_OF_ABSOLUTE_WIDTH &&
                width.compareTo(x1.abs().times(LOWER_LIMIT_OF_RELATIVE_WIDTH)) >= 0 &&
                width.compareTo(x2.abs().times(LOWER_LIMIT_OF_RELATIVE_WIDTH)) >= 0;
    }

    /**
     * <p>
     * 指定した値を境界に持つ有限閉区間を返す. <br>
     * <i>x</i><sub>1</sub>, <i>x</i><sub>2</sub> &rarr;
     * [<i>x</i><sub>1</sub>, <i>x</i><sub>2</sub>] または
     * [<i>x</i><sub>2</sub>, <i>x</i><sub>1</sub>]
     * </p>
     * 
     * <p>
     * 値の正当性の判定には
     * {@link #acceptsBoundaryValues(PseudoRealNumber, PseudoRealNumber)} が用いられ,
     * 不適の場合は例外がスローされる.
     * </p>
     *
     * @param <T> 体の元を表現する型パラメータ
     * @param x1 <i>x</i><sub>1</sub>, 引数
     * @param x2 <i>x</i><sub>2</sub>, 引数
     * @return 有限閉区間
     * @throws IllegalArgumentException 値が適合しない場合
     */
    public static <T extends PseudoRealNumber<T>> FiniteClosedInterval<T> from(T x1, T x2) {
        if (!acceptsBoundaryValues(x1, x2)) {
            throw new IllegalArgumentException(
                    String.format("値が不適である: x1 = %s, x2 = %s", x1, x2));
        }

        final T min;
        final T max;
        if (x1.compareTo(x2) > 0) {
            min = x2;
            max = x1;
        } else {
            min = x1;
            max = x2;
        }
        return new FiniteClosedInterval<>(min, max);
    }
}
