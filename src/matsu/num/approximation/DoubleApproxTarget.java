/*
 * Copyright (c) 2024 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */
/*
 * 2024.12.12
 */
package matsu.num.approximation;

/**
 * {@code double} 型で表現された, 近似されるターゲット関数を扱う. <br>
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
 * 実数は {@code double} 値として表現される. <br>
 * 区間内において, <i>f</i>は有限の値を返し,
 * <i>s</i><sub><i>f</i></sub>は有限かつ正の値を返す. <br>
 * 値が返せない場合は NaN を返しても良いが, 近似には失敗するだろう.
 * </p>
 * 
 * <p>
 * このインターフェースのサブタイプはイミュータブルであり,
 * かつすべてのメソッドはスレッドセーフであることが保証されている.
 * </p>
 * 
 * @author Matsuura Y.
 * @version 21.0
 */
public abstract class DoubleApproxTarget {

    /**
     * 唯一のコンストラクタ.
     */
    protected DoubleApproxTarget() {
        super();
    }

    /**
     * 与えられた <i>x</i> に対し, <i>f</i>(<i>x</i>) の値 (有限値) を返す. <br>
     * オーバーフローなどで値が計算できなかった場合, {@link Double#NaN} を返す. <br>
     * <i>x</i> が区間外の場合は {@link IllegalArgumentException} がスローされる.
     * 
     * @param x <i>x</i>, 引数
     * @return <i>f</i>(<i>x</i>)
     * @throws IllegalArgumentException 引数が区間外の場合
     */
    public final double value(double x) {
        if (!this.accepts(x)) {
            throw new IllegalArgumentException("範囲外");
        }

        double out = this.calcValue(x);
        return Double.isFinite(out) ? out : Double.NaN;
    }

    /**
     * {@link #value(double)} で返す値の計算を行うための抽象メソッド.
     * 
     * <p>
     * このメソッドは {@link #value(double)} の内部で呼ばれるために用意されており,
     * 引数 <i>x</i> は必ず区間内である. <br>
     * 公開は禁止され, サブクラスからもコールしてはならない. <br>
     * 戻り値は,
     * {@link Double#POSITIVE_INFINITY}, {@link Double#NEGATIVE_INFINITY},
     * {@link Double#NaN}
     * を返しても良い. <br>
     * (呼び出し元 ({@link #value(double)}) で {@link Double#NaN} に修正される.)
     * </p>
     * 
     * 
     * @implSpec アクセス修飾子を {@code public} にしてはいけない.
     * 
     * @param x <i>x</i>, 引数
     * @return <i>f</i>(<i>x</i>) の候補値
     */
    protected abstract double calcValue(double x);

    /**
     * 与えられた <i>x</i> に対し,
     * <i>s</i><sub><i>f</i></sub>(<i>x</i>) の値 (有限の正の値) を返す. <br>
     * 値が正でなかった場合 (一種のバグ) やオーバーフローなどで値が計算できなかった場合,
     * {@link Double#NaN} を返す. <br>
     * <i>x</i> が区間外の場合は {@link IllegalArgumentException} がスローされる.
     * 
     * @param x <i>x</i>, 引数
     * @return <i>s</i><sub><i>f</i></sub>(<i>x</i>)
     * @throws IllegalArgumentException 引数が区間外の場合
     */
    public final double scale(double x) {
        if (!this.accepts(x)) {
            throw new IllegalArgumentException("範囲外");
        }

        double out = this.calcScale(x);
        return Double.isFinite(out) && out > 0d ? out : Double.NaN;
    }

    /**
     * {@link #scale(double)} で返す値の計算を行うための抽象メソッド.
     * 
     * <p>
     * このメソッドは {@link #scale(double)} の内部で呼ばれるために用意されており,
     * 引数 <i>x</i> は必ず区間内である. <br>
     * 公開は禁止され, サブクラスからもコールしてはならない. <br>
     * 戻り値は, 0以下の数や,
     * {@link Double#POSITIVE_INFINITY}, {@link Double#NaN}
     * を返しても良い. <br>
     * (呼び出し元 ({@link #scale(double)}) で {@link Double#NaN} に修正される.)
     * </p>
     * 
     * @implSpec アクセス修飾子を {@code public} にしてはいけない.
     * 
     * @param x <i>x</i>, 引数
     * @return <i>s</i><sub><i>f</i></sub>(<i>x</i>) の候補値
     */
    protected abstract double calcScale(double x);

    /**
     * 引数が <i>f</i> に受け入れられるかどうかを判定する.
     * 
     * @param x 引数
     * @return 引数が受け入れられる場合はtrue
     */
    public final boolean accepts(double x) {
        return this.interval().accepts(x);
    }

    /**
     * 自身が定義された閉区間 [<i>a</i>, <i>b</i>] を返す.
     * 
     * @return 区間
     */
    public abstract DoubleFiniteClosedInterval interval();

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
