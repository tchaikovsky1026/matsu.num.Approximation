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
 * 独自クラスによる実数体で表現された, 近似されるターゲット関数を扱う. <br>
 * 有限閉区間で定義された1変数関数 <i>f</i>:
 * [<i>a</i>, <i>b</i>] &rarr; &#x211D;
 * を表す.
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
 * <i>s</i><sub><i>f</i></sub>は有限かつ正の値を返す. <br>
 * 値が返せない場合は {@link ArithmeticException} をスローことになるが, 近似には失敗するだろう.
 * </p>
 * 
 * <p>
 * このインターフェースのサブタイプはイミュータブルであり,
 * かつすべてのメソッドはスレッドセーフであることが保証されている.
 * </p>
 * 
 * @author Matsuura Y.
 * @version 21.0
 * @param <T> 体の元を表現する型パラメータ
 */
public abstract class ApproxTarget<T extends PseudoRealNumber<T>> {

    /**
     * 唯一のコンストラクタ.
     */
    protected ApproxTarget() {
        super();
    }

    /**
     * 与えられた <i>x</i> に対し, <i>f</i>(<i>x</i>) の値 (有限値) を返す. <br>
     * <i>x</i> が区間外の場合は {@link IllegalArgumentException} がスローされる. <br>
     * オーバーフローなどで値が計算できなかった場合, {@link ArithmeticException} がスローされる.
     * 
     * @param x <i>x</i>, 引数
     * @return <i>f</i>(<i>x</i>)
     * @throws IllegalArgumentException 引数が区間外の場合
     * @throws ArithmeticException 値が計算できなかった場合
     * @throws NullPointerException 引数がnullの場合
     */
    public final T value(T x) {
        if (!this.accepts(x)) {
            throw new IllegalArgumentException(
                    String.format("範囲外: %s", x));
        }

        return this.calcValue(x);
    }

    /**
     * {@link #value(PseudoRealNumber)} で返す値の計算を行うための抽象メソッド.
     * 
     * <p>
     * このメソッドは {@link #value(PseudoRealNumber)} の内部で呼ばれるために用意されており,
     * 引数 <i>x</i> は必ず区間内である
     * (したがって {@code null} でない). <br>
     * 公開は禁止され, サブクラスからもコールしてはならない. <br>
     * 戻り値が計算できない場合, {@link ArithmeticException} をスローすること.
     * </p>
     * 
     * @implSpec アクセス修飾子を {@code public} にしてはいけない.
     * 
     * @param x <i>x</i>, 引数
     * @return <i>f</i>(<i>x</i>) の候補値
     * @throws ArithmeticException 値が計算できなかった場合
     */
    protected abstract T calcValue(T x);

    /**
     * 与えられた <i>x</i> に対し,
     * <i>s</i><sub><i>f</i></sub>(<i>x</i>) の値 (有限の正の値) を返す. <br>
     * <i>x</i> が区間外の場合は {@link IllegalArgumentException} がスローされる. <br>
     * 値が正でなかった場合 (一種のバグ) やオーバーフローなどで値が計算できなかった場合, {@link ArithmeticException}
     * がスローされる.
     * 
     * @param x <i>x</i>, 引数
     * @return <i>s</i><sub><i>f</i></sub>(<i>x</i>)
     * @throws IllegalArgumentException 引数が区間外の場合
     * @throws ArithmeticException 値が計算できなかった場合, スケールの値が正でない場合
     * @throws NullPointerException 引数がnullの場合
     */
    public final T scale(T x) {
        if (!this.accepts(x)) {
            throw new IllegalArgumentException("範囲外");
        }

        T out = this.calcScale(x);
        if (out.compareTo(this.elementProvider().zero()) <= 0) {
            throw new ArithmeticException("スケールの値が正でない");
        }
        return out;
    }

    /**
     * {@link #scale(PseudoRealNumber)} で返す値の計算を行うための抽象メソッド.
     * 
     * <p>
     * このメソッドは {@link #scale(PseudoRealNumber)} の内部で呼ばれるために用意されており,
     * 引数 <i>x</i> は必ず区間内である
     * (したがって {@code null} でない). <br>
     * 公開は禁止され, サブクラスからもコールしてはならない. <br>
     * 戻り値が計算できない場合, {@link ArithmeticException} をスローすること. <br>
     * 0以下の数を返しても良い
     * (呼び出し元で例外スローに変換される).
     * </p>
     * 
     * @implSpec アクセス修飾子を {@code public} にしてはいけない.
     * 
     * @param x <i>x</i>, 引数
     * @return <i>s</i><sub><i>f</i></sub>(<i>x</i>) の候補値
     * @throws ArithmeticException 値が計算できなかった場合
     */
    protected abstract T calcScale(T x);

    /**
     * 引数が <i>f</i> に受け入れられるか:
     * 引数が区間内かどうかを判定する. <br>
     * 受け入れられる場合は {@link #value(PseudoRealNumber)}
     * , {@link #scale(PseudoRealNumber)}
     * において {@link IllegalArgumentException} はスローされない.
     * 
     * @param x 引数
     * @return 引数が受け入れられる場合はtrue
     * @throws NullPointerException 引数がnullの場合
     */
    public final boolean accepts(T x) {
        return this.interval().accepts(x);
    }

    /**
     * 自身が定義された閉区間 [<i>a</i>, <i>b</i>] を返す.
     * 
     * @implSpec
     *               このプロバイダにより返されるインスタンスは複数回の呼び出しで同一でなければならない. <br>
     *               すなわち, 次が必ず {@code true} でなければならない.
     *               <blockquote>
     *               {@code this.interval() == this.interval()}
     *               </blockquote>
     * 
     * @return 区間
     */
    public abstract FiniteClosedInterval<T> interval();

    /**
     * このターゲットが扱う体の元に関するプロバイダを返す.
     * 
     * @implSpec
     *               このプロバイダにより返されるインスタンスは複数回の呼び出しで同一でなければならない. <br>
     *               すなわち, 次が必ず {@code true} でなければならない.
     *               <blockquote>
     *               {@code this.elementProvider() == this.elementProvider()}
     *               </blockquote>
     * 
     * @return 体の元に関するプロバイダ
     */
    public abstract PseudoRealNumber.Provider<T> elementProvider();

    /**
     * このインスタンスの文字列表現を返す.
     * 
     * @return 文字列表現
     */
    @Override
    public String toString() {
        return this.getClass().getSimpleName() + ": anonymous";
    }

    /**
     * -
     * 
     * @return -
     * @throws CloneNotSupportedException 常に
     * @deprecated Clone不可
     */
    @Deprecated
    @Override
    protected final Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException();
    }

    /**
     * オーバーライド不可.
     */
    @Override
    @Deprecated
    protected final void finalize() throws Throwable {
        super.finalize();
    }
}
