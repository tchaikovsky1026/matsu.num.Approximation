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
 * 実数は {@link PseudoRealNumber} のサブタイプ {@code T} として表現される. <br>
 * 区間内において, <i>f</i>は有限の値を返し,
 * <i>s</i><sub><i>f</i></sub>は有限かつ正の値を返すことが保証されている.
 * </p>
 * 
 * <p>
 * このインターフェースのサブタイプはイミュータブルであり,
 * かつすべてのメソッドはスレッドセーフであることが保証されている.
 * </p>
 * 
 * @author Matsuura Y.
 * @version 18.2
 * @param <T> 体の元を表現する型パラメータ
 */
public interface ApproxTarget<T extends PseudoRealNumber<T>> {

    /**
     * 与えられた <i>x</i> に対し, <i>f</i>(<i>x</i>) の値 (有限値) を返す. <br>
     * <i>x</i> が区間外の場合は {@link IllegalArgumentException} がスローされる. <br>
     * 
     * <p>
     * <i>
     * 実装規約: <br>
     * <i>x</i> が区間内であれば, 必ず値を返さなければならない. <br>
     * <i>x</i> が区間内であるにもかかわらず値を返せないような {@link ApproxTarget} インスタンスは存在してはならない.
     * </i>
     * </p>
     * 
     * @param x <i>x</i>, 引数
     * @return <i>f</i>(<i>x</i>)
     * @throws IllegalArgumentException 引数が区間外の場合
     * @throws NullPointerException 引数がnullの場合
     */
    public abstract T value(T x);

    /**
     * 与えられた <i>x</i> に対し,
     * <i>s</i><sub><i>f</i></sub>(<i>x</i>) の値 (有限の正の値) を返す. <br>
     * <i>x</i> が区間外の場合は {@link IllegalArgumentException} がスローされる. <br>
     * 
     * <p>
     * <i>
     * 実装規約: <br>
     * <i>x</i> が区間内であれば, 必ず値を返さなければならない. <br>
     * <i>x</i> が区間内であるにもかかわらず値を返せないような {@link ApproxTarget} インスタンスは存在してはならない.
     * </i>
     * </p>
     * 
     * @param x <i>x</i>, 引数
     * @return <i>s</i><sub><i>f</i></sub>(<i>x</i>)
     * @throws IllegalArgumentException 引数が区間外の場合
     * @throws NullPointerException 引数がnullの場合
     */
    public abstract T scale(T x);

    /**
     * <p>
     * 引数が <i>f</i> に受け入れられるか:
     * 引数が区間内かどうかを判定する. <br>
     * 受け入れられる場合は {@link #value(PseudoRealNumber)}
     * , {@link #scale(PseudoRealNumber)}
     * において {@link IllegalArgumentException} はスローされない.
     * </p>
     * 
     * <p>
     * <i>
     * 実装規約: <br>
     * {@code this.accepts(x) == this.interval().accepts(x)} <br>
     * を要請する
     * (例外をスローするかどうかも一致する). <br>
     * デフォルト実装はこれを満たしている.
     * </i>
     * </p>
     * 
     * @param x 引数
     * @return 引数が受け入れられる場合はtrue
     * @throws NullPointerException 引数がnullの場合
     */
    public default boolean accepts(T x) {
        return this.interval().accepts(x);
    }

    /**
     * 自身が定義された閉区間 [<i>a</i>, <i>b</i>] を返す.
     * 
     * @return 区間
     */
    public abstract FiniteClosedInterval<T> interval();

    /**
     * このターゲットが扱う体の元に関するプロバイダを返す.
     * 
     * @return 体の元に関するプロバイダ
     */
    public abstract PseudoRealNumber.Provider<T> elementProvider();
}
