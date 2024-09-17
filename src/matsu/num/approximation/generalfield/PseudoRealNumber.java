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
 * 実数に類似した体 (四則演算が定義された代数系) の元を表現する. <br>
 * 実質的にイミュータブルである. <br>
 * 値に基づく equality, comparability を提供する. <br>
 * equality と compatibility は整合しなければならない.
 * </p>
 * 
 * <p>
 * このクラスは無限大, NaNを除いた {@code double} の拡張になっており,
 * 有限である任意の {@code double} に対し, それに相当するインスタンスが存在する. <br>
 * 逆に, 無限大, NaNを表現するインスタンスは存在してはならず,
 * 演算結果がそのようになる場合は例外がスローされる. <br>
 * equality と comparability が整合するという制約より,
 * {@code 0d} と {@code -0d} は等価でなければならない.
 * </p>
 * 
 * @author Matsuura Y.
 * @version 18.2
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
     * この抽象メソッドは内部で利用されることを想定して用意されており,
     * 継承先でアクセス修飾子を緩めるべきではない. <br>
     * このメソッドの呼び出しの度に新しいインスタンスを生成してはならない
     * (キャッシュされていなければならない).
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
     * 加法逆元を返す.
     * 
     * @return 加法逆元
     */
    public abstract T negated();

    /**
     * 絶対値を返す.
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
     * @return 比較結果, {@code double} に準拠する
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
     * @version 18.2
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
         * 指定した長さのT型の配列を返す. <br>
         * 配列は {@code null} 埋めされている. <br>
         * 長さは0以上でなければならない.
         * 
         * @param length 配列の長さ
         * @return 長さ {@code length} のT型の配列
         * @throws IllegalArgumentException lengthが0以上でないの場合
         */
        public abstract T[] createArray(int length);
    }
}
