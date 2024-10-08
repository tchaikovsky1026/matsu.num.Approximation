/*
 * Copyright (c) 2024 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */
/*
 * 2024.10.9
 */
package matsu.num.approximation.generalfield;

/**
 * <p>
 * 実数に類似した体 (四則演算が定義された代数系) の元を表現する. <br>
 * 実質的にイミュータブルである. <br>
 * 値に基づく equality, comparability を提供する. <br>
 * comparability は数の自然順序と同等であり,
 * equality は compatibility と整合するように実装される.
 * </p>
 * 
 * <p>
 * このクラスは無限大, NaNを除いた {@code double} の拡張になっており,
 * 有限である任意の {@code double} に対し, それに相当するインスタンスが存在する. <br>
 * ただし, 正の0と負の0を区別せず, ともに0として扱う. <br>
 * 逆に, 無限大, NaNを表現するインスタンスは存在してはならず,
 * 演算結果がそのようになる場合は例外がスローされる.
 * </p>
 * 
 * <p>
 * {@code double} からこのクラスのインスタンスへのマッピングについて規定する. <br>
 * {@code 0d} と {@code -0d} は等価であり,
 * それ以外の異なる {@code double}
 * 値は異なる値にマッピングされるように実装される. <br>
 * すなわち, {@code d1}, {@code d2} を共に有限の {@code double} 値とし,
 * 少なくとも片方は0でないとしたとき,
 * 次の規約を満たす. <br>
 * 規約: {@code double} 値からこのクラスのインスタンスへのマッピングを {@code f} とすると,
 * 次が成立する:
 * </p>
 * <blockquote>
 * 
 * <pre>
 * Double.compare(d1, d2) == f(d1).compareTo(f(d2))
 * f(0d).compareTo(f(-0d)) == 0
 * (ただし, 「==」は Integer.signum の文脈である. すなわち符号だけに注目する.)
 * </pre>
 * 
 * </blockquote>
 * 
 * 
 * <hr>
 * 
 * <h2>IEEE 754型の浮動小数点数の利用</h2>
 * 
 * <p>
 * IEEE 754型の浮動小数点数をこのクラスにラップするためには, 次の注意が必要である.
 * </p>
 * 
 * <ul>
 * <li>無限大, NaNを排除する.</li>
 * <li>正の0と負の0を同一視する.</li>
 * </ul>
 * 
 * <p>
 * これらを実現する実装例について, 以下で説明する.
 * </p>
 * 
 * <p>
 * {@link PseudoRealNumber} 型を {@code T}, ラップされる要素の型を {@code E} とする. <br>
 * ただし, ここからの文脈では, {@code T}, {@code E} は仮型パラメータでなく実型パラメータである. <br>
 * また, {@code E} は comparability が定義されており,
 * equality と整合するとする. <br>
 * {@code E} には四則演算等の基本演算に加え,
 * 次のメソッド, フィールドがあらかじめ準備されているとする. <br>
 * </p>
 * 
 * <ul>
 * <li>
 * {@code E} が0であるかを判定するインスタンスメソッド,
 * {@code isZero(): boolean}
 * </li>
 * <li>
 * {@code E} が有限であるかを判定するインスタンスメソッド,
 * {@code isFinite(): boolean}
 * </li>
 * <li>
 * 正の0を表現する {@code E} 型の {@code static final} 定数,
 * <u>{@code POSITIVE_0}</u>{@code : E}
 * </li>
 * </ul>
 * 
 * <h3>フィールド, コンストラクタ</h3>
 * 
 * <p>
 * {@code T} は {@code final} フィールド {@code e:E} を持ち,
 * {@code e} は {@code T} のコンストラクタで初期化する. <br>
 * このとき, コンストラクタの引数で与えられる {@code e} が有限であるか, 0であるかを判定し,
 * 必要ならば例外スロー, 値の置き換えを行う. <br>
 * 例えば, コンストラクタは次のコードになる.
 * </p>
 * 
 * <blockquote>
 * 
 * <pre>
 * T(E e) {
 *     if (!e.isFinite()) {
 *         throw new IllegalArgumentException("有限でない");
 *     }
 *     if (e.isZero()) {
 *         e = E.POSITIVE_0;
 *     }
 *     this.e = e;
 * }
 * </pre>
 * 
 * </blockquote>
 * 
 * <p>
 * 上記コンストラクタにより, {@code T} のインスタンスのフィールド {@code e}
 * は有限かつ負の0でない状態が実現する. <br>
 * この状態を「正規化されている」と呼ぶ.
 * </p>
 * 
 * <h3>comparability, equaltity, 単項演算</h3>
 * 
 * <p>
 * 存在し得る存在し得る {@code T} 型インスタンスは正規化されているので, <br>
 * {@code T} 型の comparability は {@code E} 型の comparability に整合し, <br>
 * {@code equals} メソッド, {@code hashCode} メソッド, {@code compareTo} メソッドは
 * {@code E} 型のそれらを用いて実現できる.
 * </p>
 * 
 * <p>
 * 単項演算である {@code abs} メソッド, {@code negated} メソッドは,
 * {@code E} 型のそれらを用いて実行すればよい. <br>
 * IEEE 754型である場合, 有限の {@code e: E} について {@code -e (厳密な表記でない)} は有限であるので,
 * {@code T} のフィールドとして適切になる. <br>
 * {@code -e} により負の0が生成されるが, {@code T} のコンストラクタで正の0に置き換えられる.
 * </p>
 * 
 * <h3>四則演算</h3>
 * 
 * <p>
 * 四則演算の実装は, 内部的には {@code E} 型で行い,
 * 結果が有限でない場合は {@link ArithmeticException} をスローする. <br>
 * 例えば, 次のコードになる.
 * </p>
 * 
 * <blockquote>
 * 
 * <pre>
 * T plus(T augend) {
 *     E result = this.e.plus(augend.e);
 *     if(!result.isFinite(){
 *         throw new ArithmeticException("演算結果が有限でない");
 *     }
 *     return new T(result);
 * }
 * </pre>
 * 
 * </blockquote>
 * 
 * <p>
 * もしくは, 前述のコンストラクタを採用している場合, 次の例外翻訳でもよい.
 * </p>
 * 
 * <blockquote>
 * 
 * <pre>
 * T plus(T augend) {
 *     try {
 *         return new T(this.e.plus(augend.e));
 *     } catch (IllegalArgumentException iae) {
 *         throw new ArithmeticException("演算結果が有限でない");
 *     }
 * }
 * </pre>
 * 
 * </blockquote>
 * 
 * @author Matsuura Y.
 * @version 19.2
 * @param <T> このクラスと二項演算が可能な体構造の元を表す型.
 *            体の定義より, 自身に一致する.
 */
public abstract class PseudoRealNumber<T extends PseudoRealNumber<T>> implements Comparable<T> {

    /**
     * 唯一のコンストラクタ.
     */
    protected PseudoRealNumber() {
        super();
    }

    /**
     * このクラスが表現する元の生成を受け持つプロバイダを返す.
     * 
     * <p>
     * <i>
     * 実装規約: <br>
     * この抽象メソッドは内部で利用されるために用意されており,
     * 外部から呼ぶことは許されず,
     * 継承先でアクセス修飾子を緩めてはいけない. <br>
     * このメソッドの戻り値はキャッシュされたインスタンスを返さなければならない.
     * (呼び出しの度に新しいインスタンスを生成してはならない.)
     * </i>
     * </p>
     * 
     * @return プロバイダ
     */
    protected abstract Provider<T> provider();

    /**
     * 和を返す.
     * 
     * @param augend augend
     * @return 和
     * @throws ArithmeticException 演算結果がインスタンスとして表現できない場合
     * @throws NullPointerException 引数がnullの場合
     */
    public abstract T plus(T augend);

    /**
     * 和を返す.
     * 
     * @param augend augend
     * @return 和
     * @throws IllegalArgumentException 引数が無限大またはNaNの場合
     * @throws ArithmeticException 演算結果がインスタンスとして表現できない場合
     */
    public final T plus(double augend) {
        return this.plus(this.provider().fromDoubleValue(augend));
    }

    /**
     * 差を返す.
     * 
     * @param subtrahend subtrahend
     * @return 差
     * @throws ArithmeticException 演算結果がインスタンスとして表現できない場合
     * @throws NullPointerException 引数がnullの場合
     */
    public abstract T minus(T subtrahend);

    /**
     * 差を返す.
     * 
     * @param subtrahend subtrahend
     * @return 差
     * @throws IllegalArgumentException 引数が無限大またはNaNの場合
     * @throws ArithmeticException 演算結果がインスタンスとして表現できない場合
     */
    public final T minus(double subtrahend) {
        return this.minus(this.provider().fromDoubleValue(subtrahend));
    }

    /**
     * 積を返す.
     * 
     * @param multiplicand multiplicand
     * @return 積
     * @throws ArithmeticException 演算結果がインスタンスとして表現できない場合
     * @throws NullPointerException 引数がnullの場合
     */
    public abstract T times(T multiplicand);

    /**
     * 積を返す.
     * 
     * @param multiplicand multiplicand
     * @return 積
     * @throws IllegalArgumentException 引数が無限大またはNaNの場合
     * @throws ArithmeticException 演算結果がインスタンスとして表現できない場合
     */
    public final T times(double multiplicand) {
        return this.times(this.provider().fromDoubleValue(multiplicand));
    }

    /**
     * 商を返す.
     * 
     * @param divisor divisor
     * @return 商
     * @throws ArithmeticException 演算結果がインスタンスとして表現できない場合 (0割りが発生した場合を含む)
     * @throws NullPointerException 引数がnullの場合
     */
    public abstract T dividedBy(T divisor);

    /**
     * 商を返す.
     * 
     * @param divisor divisor
     * @return 商
     * @throws IllegalArgumentException 引数が無限大またはNaNの場合
     * @throws ArithmeticException 演算結果がインスタンスとして表現できない場合 (0割りが発生した場合を含む)
     */
    public final T dividedBy(double divisor) {
        return this.dividedBy(this.provider().fromDoubleValue(divisor));
    }

    /**
     * 自身の加法逆元 (-1倍) を返す.
     * 
     * @return 加法逆元
     */
    public abstract T negated();

    /**
     * 自身の絶対値を返す.
     * 
     * @return 絶対値
     */
    public abstract T abs();

    /**
     * このインスタンスの {@code double} 表現された値を返す. <br>
     * 無限大が返る場合があるが, NaNは返らない.
     * 
     * @return double表現した値
     */
    public abstract double asDouble();

    /**
     * 与えられたインスタンスとの等価性を判定する. <br>
     * comparability と整合する.
     * 
     * @param obj 比較相手
     * @return 等価の場合はtrue
     */
    @Override
    public abstract boolean equals(Object obj);

    /**
     * このインスタンスのハッシュコードを返す.
     * 
     * @return ハッシュコード
     */
    @Override
    public abstract int hashCode();

    /**
     * 与えられたインスタンスとを比較する. <br>
     * equality と整合する.
     * 
     * @param o 比較相手
     * @return 比較結果
     * @throws NullPointerException 引数がnullの場合
     */
    @Override
    public abstract int compareTo(T o);

    /**
     * このインスタンスの文字列表現を返す.
     * 
     * @return 文字列表現
     */
    @Override
    public abstract String toString();

    /**
     * 体に対する値のプロバイダ.
     * 
     * @author Matsuura Y.
     * @version 19.2
     * @param <T> 体の元を表す型
     */
    public static interface Provider<T extends PseudoRealNumber<T>> {

        /**
         * 与えた {@code double} 値と同等の体の元を返す.
         * 
         * <p>
         * 無限大, NaNが与えられた場合, かつその場合のみ例外がスローされる.
         * </p>
         * 
         * @param value 値
         * @return 体の元
         * @throws IllegalArgumentException valueが無限大またはNaNの場合
         */
        public abstract T fromDoubleValue(double value);

        /**
         * 加法単位元 (0) を返す.
         * 
         * @return 加法単位元
         */
        public abstract T zero();

        /**
         * 乗法単位元 (1) を返す.
         * 
         * @return 乗法単位元
         */
        public abstract T one();

        /**
         * 指定した長さの {@code T} 型の配列を返す. <br>
         * 配列は {@code null} 埋めされている. <br>
         * 長さは0以上でなければならない.
         * 
         * @param length 配列の長さ
         * @return 長さ {@code length} の {@code T} 型の配列
         * @throws IllegalArgumentException lengthが0以上でないの場合
         */
        public abstract T[] createArray(int length);
    }
}
