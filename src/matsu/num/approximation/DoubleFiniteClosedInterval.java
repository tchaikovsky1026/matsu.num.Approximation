/*
 * Copyright © 2024 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */
/*
 * 2025.12.25
 */
package matsu.num.approximation;

/**
 * {@code double} 値に関する有限閉区間を扱う. <br>
 * 区間の境界値に基づく equality を提供する.
 * 
 * <p>
 * サポートされているのは, 次を満たす閉区間 [<i>a</i>, <i>b</i>] である. <br>
 * |<i>a</i>| &lt; &infin;, |<i>b</i>| &lt; &infin;,
 * (<i>b</i> - <i>a</i>) &ge; max(<i>e</i><sub>a</sub>,
 * <i>e</i><sub>r</sub>|<i>a</i>|,
 * <i>e</i><sub>r</sub>|<i>b</i>|) <br>
 * ただし,
 * <i>e</i><sub>a</sub>, <i>e</i><sub>r</sub>
 * は定数である.
 * </p>
 * 
 * <p>
 * このクラスのインスタンスは,
 * {@link #from(double, double)}
 * メソッドにより取得する. <br>
 * 引数が適切かどうかは,
 * {@link #acceptsBoundaryValues(double, double)}
 * により事前検証すべきである.
 * </p>
 *
 * @author Matsuura Y.
 */
public final class DoubleFiniteClosedInterval {

    /**
     * 区間の絶対最小幅を表す定数 (<i>e</i><sub>a</sub>).
     */
    public static final double LOWER_LIMIT_OF_ABSOLUTE_WIDTH = 1E-200;

    /**
     * 区間の相対最小幅を表す定数 (<i>e</i><sub>r</sub>).
     */
    public static final double LOWER_LIMIT_OF_RELATIVE_WIDTH = 1E-10;

    private final double min;
    private final double max;

    private final double gap;

    private final int immutableHashCode;

    private DoubleFiniteClosedInterval(double min, double max) {
        this.min = min;
        this.max = max;

        this.gap = this.max - this.min;

        this.immutableHashCode = this.calcHashCode();
    }

    /**
     * 下側境界値 (<i>a</i>) を返す.
     * 
     * @return 下側境界値
     */
    public double lower() {
        return this.min;
    }

    /**
     * 上側境界値 (<i>b</i>) を返す.
     * 
     * @return 上側境界値
     */
    public double upper() {
        return this.max;
    }

    /**
     * この区間の幅 (upper - lower) を返す.
     * 
     * @return 区間幅
     */
    public double gap() {
        return this.gap;
    }

    /**
     * <i>x</i> が閉区間に含まれるか:
     * <i>x</i> &in; [<i>a</i>, <i>b</i>]
     * を判定する.
     * 
     * @param x <i>x</i>, 引数
     * @return 閉区間に含まれる場合はtrue
     */
    public boolean accepts(double x) {
        return x >= this.lower()
                && x <= this.upper();
    }

    /**
     * 与えられたインスタンスとの等価性を判定する.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof DoubleFiniteClosedInterval target)) {
            return false;
        }
        return Double.compare(this.min, target.min) == 0
                && Double.compare(this.max, target.max) == 0;
    }

    /**
     * このインスタンスのハッシュコードを返す.
     */
    @Override
    public int hashCode() {
        return this.immutableHashCode;
    }

    /**
     * ハッシュコードを計算する.
     */
    private int calcHashCode() {
        int result = 1;
        result = 31 * result + Double.hashCode(this.min);
        result = 31 * result + Double.hashCode(this.max);
        return result;
    }

    /**
     * 自身の文字列表現を返す.
     * 
     * <p>
     * 文字列表現はバージョン間の互換性は担保されない. <br>
     * おそらく次のような形式だろう. <br>
     * {@code [%lower, %upper]}
     * </p>
     */
    @Override
    public String toString() {
        return String.format(
                "[%s, %s]",
                this.min, this.max);
    }

    /**
     * 指定した値が閉区間の境界に適合するかを判定する. <br>
     * <i>x</i><sub>1</sub>, <i>x</i><sub>2</sub> &rarr;
     * [<i>x</i><sub>1</sub>, <i>x</i><sub>2</sub>] または
     * [<i>x</i><sub>2</sub>, <i>x</i><sub>1</sub>]
     * 
     * @param x1 <i>x</i><sub>1</sub>, 引数
     * @param x2 <i>x</i><sub>2</sub>, 引数
     * @return 適合する場合はtrue
     */
    public static boolean acceptsBoundaryValues(double x1, double x2) {
        if (!Double.isFinite(x1) || !Double.isFinite(x2)) {
            return false;
        }
        double width = Math.abs(x1 - x2);
        return width >= LOWER_LIMIT_OF_ABSOLUTE_WIDTH &&
                width >= Math.abs(x1) * LOWER_LIMIT_OF_RELATIVE_WIDTH &&
                width >= Math.abs(x2) * LOWER_LIMIT_OF_RELATIVE_WIDTH;
    }

    /**
     * 指定した値を境界に持つ有限閉区間を返す. <br>
     * <i>x</i><sub>1</sub>, <i>x</i><sub>2</sub> &rarr;
     * [<i>x</i><sub>1</sub>, <i>x</i><sub>2</sub>] または
     * [<i>x</i><sub>2</sub>, <i>x</i><sub>1</sub>]
     * 
     * <p>
     * 値の正当性の判定には {@link #acceptsBoundaryValues(double, double)} が用いられ,
     * 不適の場合は例外がスローされる.
     * </p>
     *
     * @param x1 <i>x</i><sub>1</sub>, 引数
     * @param x2 <i>x</i><sub>2</sub>, 引数
     * @return 有限閉区間
     * @throws IllegalArgumentException 値が適合しない場合
     */
    public static DoubleFiniteClosedInterval from(double x1, double x2) {
        if (!acceptsBoundaryValues(x1, x2)) {
            throw new IllegalArgumentException(
                    String.format("not accepted: x1 = %s, x2 = %s", x1, x2));
        }

        double min;
        double max;
        if (x1 > x2) {
            min = x2;
            max = x1;
        } else {
            min = x1;
            max = x2;
        }
        return new DoubleFiniteClosedInterval(min, max);
    }
}
