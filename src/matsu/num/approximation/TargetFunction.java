/*
 * Copyright (c) 2024 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */
/*
 * 2024.6.20
 */
package matsu.num.approximation;

/**
 * <p>
 * 近似されるターゲット関数を扱うインターフェース. <br>
 * 有限閉区間で定義された1変数関数 <i>f</i>:
 * [<i>a</i>, <i>b</i>] &rarr; &#x211D;
 * を表す.
 * </p>
 * 
 * <p>
 * 近似において評価される誤差のスケール因子を <i>s</i><sub><i>f</i></sub> とする. <br>
 * <i>s</i><sub><i>f</i></sub>:
 * [<i>a</i>, <i>b</i>] &rarr; &#x211D;<sub>&gt;0</sub> <br>
 * <i>s</i><sub><i>f</i></sub> の値が小さいところでは,
 * <i>f</i>との差が小さくなるように近似される.
 * </p>
 * 
 * <p>
 * 実数は {@code double} 値として表現される. <br>
 * 区間内において, <i>f</i>は有限の {@code double} 値を返し,
 * <i>s</i><sub><i>f</i></sub>は有限かつ正の {@code double} 値を返すことが保証されている.
 * </p>
 * 
 * <p>
 * このインターフェースのサブタイプはイミュータブルであり,
 * かつすべてのメソッドはスレッドセーフであることが保証されている.
 * </p>
 * 
 * @author Matsuura Y.
 * @version 18.0
 */
public interface TargetFunction {

    /**
     * 与えられた <i>x</i> に対し, <i>f</i>(<i>x</i>) の値 (有限値) を返す. <br>
     * <i>x</i> が区間外の場合は {@link Double#NaN} を返す.
     * 
     * @param x <i>x</i>, 引数
     * @return <i>f</i>(<i>x</i>)
     */
    public abstract double value(double x);

    /**
     * 与えられた <i>x</i> に対し,
     * <i>s</i><sub><i>f</i></sub>(<i>x</i>) の値 (有限の正の値) を返す. <br>
     * <i>x</i> が区間外の場合は {@link Double#NaN} を返す.
     * 
     * @param x <i>x</i>, 引数
     * @return <i>s</i><sub><i>f</i></sub>(<i>x</i>)
     */
    public abstract double scale(double x);

    /**
     * <p>
     * 引数が <i>f</i> に受け入れられるかどうかを判定する.
     * </p>
     * 
     * <p>
     * <i>
     * 実装規約: <br>
     * {@code this.accepts(x) == this.interval().accepts(x)} <br>
     * を要請する. <br>
     * デフォルト実装はこれを満たしている.
     * </i>
     * </p>
     * 
     * @param x 引数
     * @return 引数が受け入れられる場合はtrue
     */
    public default boolean accepts(double x) {
        return this.interval().accepts(x);
    }

    /**
     * 自身が定義された閉区間 [<i>a</i>, <i>b</i>] を返す.
     * 
     * @return 区間
     */
    public abstract DoubleFiniteClosedInterval interval();

}
