/*
 * Copyright (c) 2024 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */
/*
 * 2024.6.20
 */
package matsu.num.approximation.component;

/**
 * <p>
 * {@code double} 値に関する有限閉区間を扱う.
 * </p>
 * 
 * <p>
 * サポートされているのは, 次を満たす閉区間 [<i>a</i>, <i>b</i>] である. <br>
 * |<i>a</i>| &lt; &infin;, |<i>b</i>| &lt; &infin;,
 * |<i>b</i> - <i>a</i>| &ge; max(<i>e</i><sub>a</sub>,
 * <i>e</i><sub>r</sub>|<i>a</i>|,
 * <i>e</i><sub>r</sub>|<i>b</i>|) <br>
 * ただし,
 * <i>e</i><sub>a</sub>, <i>e</i><sub>r</sub>
 * は定数である.
 * </p>
 *
 * @author Matsuura, Y.
 * @version 17.0
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

    private DoubleFiniteClosedInterval(double min, double max) {
        this.min = min;
        this.max = max;
    }

    /**
     * 下側境界値を返す.
     * 
     * @return 下側境界値
     */
    public double lower() {
        return this.min;
    }

    /**
     * 上側境界値を返す.
     * 
     * @return 上側境界値
     */
    public double upper() {
        return this.max;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof DoubleFiniteClosedInterval target)) {
            return false;
        }
        return Double.compare(this.min, target.min) == 0 &&
                Double.compare(this.max, target.max) == 0;
    }

    @Override
    public int hashCode() {
        int result = Double.hashCode(this.min);
        result = 31 * result + Double.hashCode(this.max);
        return result;
    }

    /**
     * <p>
     * 指定した値が閉区間の境界に適合するかを判定する. <br>
     * <i>x</i><sub>1</sub>, <i>x</i><sub>2</sub> &rarr;
     * [<i>x</i><sub>1</sub>, <i>x</i><sub>2</sub>] または
     * [<i>x</i><sub>2</sub>, <i>x</i><sub>1</sub>]
     * </p>
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
     * <p>
     * 指定した値を境界に持つ有限閉区間を返す. <br>
     * <i>x</i><sub>1</sub>, <i>x</i><sub>2</sub> &rarr;
     * [<i>x</i><sub>1</sub>, <i>x</i><sub>2</sub>] または
     * [<i>x</i><sub>2</sub>, <i>x</i><sub>1</sub>]
     * </p>
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
                    String.format("値が不適である: x1 = %s, x2 = %s", x1, x2));
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
