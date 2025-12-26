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

import java.lang.reflect.Array;
import java.util.Objects;

/**
 * 実数に類似した体 (四則演算が定義された代数系) の元を表現する. <br>
 * (実質的に) イミュータブルな値クラスである. <br>
 * 値に基づく equality, comparability を提供する. <br>
 * comparability は数の自然順序と同等であり,
 * equality は compatibility と整合するように実装される.
 * 
 * <p>
 * このクラスは無限大, NaNを除いた {@code double} の拡張になっており,
 * 有限である任意の {@code double} に対し, それに相当するインスタンスが存在する. <br>
 * ただし, 正の0と負の0の区別はなく, ともに0として扱う. <br>
 * 逆に, 無限大, NaNを表現するインスタンスは存在せず,
 * 演算結果がそのようになる場合は例外がスローされる.
 * </p>
 * 
 * 
 * <hr>
 * 
 * <h2>実装要件詳細</h2>
 * 
 * <p>
 * (実装要件タグも合わせて参照すること.)
 * </p>
 * 
 * <h3>扱える値の範囲</h3>
 * 
 * <p>
 * このクラスの (サブタイプの) インスタンス {@code e} について,
 * 常に加法逆元 {@code -e} のインスタンスが生成できなければならない. <br>
 * すなわち, {@code int} のような正負で非対称な範囲を扱ってはならない.
 * </p>
 * 
 * 
 * <h3>演算による equality の伝播</h3>
 * 
 * <p>
 * {@code a1}, {@code a2}, {@code b1}, {@code b2}
 * をこのクラスの (サブタイプの) インスタンスとする. <br>
 * {@code a1.equals(a2)}, {@code b1.equals(b2)}
 * がともに {@code true} ならば, <br>
 * {@code c1 = a1.operate(b1)}, {@code c2 = a2.operate(b2)}
 * について, 次が成立しなければならない. <br>
 * ({@code operate} は任意の二項演算を表す.)
 * </p>
 * 
 * <ul>
 * <li>{@code a1.operate(b1)} が例外をスローするならば,
 * {@code a2.operate(b2)} も同一の例外をスローする.</li>
 * <li>{@code c1}, {@code c2} が存在するならば
 * {@code c1.equals(c2)} は必ず {@code true} になる.</li>
 * </ul>
 * 
 * <p>
 * この規約により, 等価と判定される2個のインスタンスは内部表現も等価になるように実装されるべきである.
 * </p>
 * 
 * 
 * <h3>{@code double} 値からこのクラスのインスタンスへのマッピング</h3>
 * 
 * <p>
 * クラス説明の通り,
 * {@code 0d} と {@code -0d} は等価であり,
 * それ以外の異なる {@code double}
 * 値は異なる値にマッピングされるように実装される. <br>
 * すなわち, {@code d1}, {@code d2} を共に有限の {@code double} 値とし,
 * 少なくとも片方は &pm;0 でないとしたとき,
 * {@code double} 値からこのクラスのインスタンスへのマッピングを {@code f} とすると,
 * 次が {@code true} にならなければならない.
 * </p>
 * <blockquote>
 * {@code sgn(Double.compare(d1, d2)) == sgn(f(d1).compareTo(f(d2)))} <br>
 * {@code f(0d).compareTo(f(-0d)) == 0 } <br>
 * (ただし, {@code sgn} は符号の意味である. {@code Integer.signum} と読み替えても良い.)
 * </blockquote>
 * 
 * 
 * <hr>
 * 
 * <h2>IEEE 754型の浮動小数点数の利用</h2>
 * 
 * <p>
 * ({@code double} を含む) IEEE 754型の浮動小数点数をこのクラスにラップするためには, 次の注意が必要である.
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
 * <pre>
 * T(E e) {
 *     if (!e.isFinite()) {
 *         throw new IllegalArgumentException("not finite");
 *     }
 *     if (e.isZero()) {
 *         e = E.POSITIVE_0;
 *     }
 *     this.e = e;
 * }
 * </pre>
 * 
 * <p>
 * 上記コンストラクタにより, {@code T} のインスタンスのフィールド {@code e}
 * は有限であり, かつ負の0でない状態が実現する. <br>
 * この状態を「正規化されている」と呼ぶ.
 * </p>
 * 
 * <h3>comparability, equaltity, abs, negated</h3>
 * 
 * <p>
 * 存在し得る {@code T} 型インスタンスは正規化されているので, <br>
 * {@code T} 型に要求される comparability は {@code E} 型の comparability に整合し, <br>
 * {@code equals} メソッド, {@code hashCode} メソッド, {@code compareTo} メソッドは
 * {@code E} 型のそれらを用いて実現できる. <br>
 * また, 演算による equality の伝播に関する規約は, 正規化によって自動的に満たされる.
 * </p>
 * 
 * <p>
 * 単項演算である {@code abs} メソッド, {@code negated} メソッドは,
 * {@code E} 型のそれらを用いて実行すればよい. <br>
 * IEEE 754型である場合, 有限の {@code E} 型についてその加法逆元は有限であるので,
 * {@code T} のフィールドとして適切になる. <br>
 * +0 を表す {@code E} 型の加法逆元は -0 となるが,
 * {@code T} のコンストラクタで +0 に置き換えられる.
 * </p>
 * 
 * <h3>四則演算</h3>
 * 
 * <p>
 * 四則演算の実装は, 内部的には {@code E} 型で行い,
 * 結果が有限でない場合は {@link ArithmeticException} をスローする. <br>
 * 例えば, 加算の場合は次のコードになる.
 * </p>
 * 
 * <pre>
 * T plus(T augend) {
 *     E result = this.e.plus(augend.e);
 *     if(!result.isFinite(){
 *         throw new ArithmeticException("not finite");
 *     }
 *     return new T(result);
 * }
 * </pre>
 * 
 * <p>
 * もしくは, 前述のコンストラクタを採用している場合, 次の例外翻訳でもよい.
 * </p>
 * 
 * <pre>
 * T plus(T augend) {
 *     try {
 *         return new T(this.e.plus(augend.e));
 *     } catch (IllegalArgumentException iae) {
 *         throw new ArithmeticException("not finite");
 *     }
 * }
 * </pre>
 * 
 * @implSpec
 *               型パラメータ {@code T} に自身型をバインドした場合,
 *               クラスを {@code final} にしなければならない.
 * 
 *               <p>
 *               {@link #typeProvider()} をオーバーライドしなければならない. <br>
 *               ({@link #typeProvider()} または {@link #provider()}
 *               のどちらかをオーバーライドすれば一応動くが,
 *               {@link #typeProvider()} をオーバーライドすることを強く推奨する.)
 *               </p>
 * 
 *               <p>
 *               その他は詳しくは「実装要件詳細」を参照すること.
 *               </p>
 * @author Matsuura Y.
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
     * この抽象メソッドは内部で利用されるために用意されているので,
     * 公開は禁止であり, サブクラスからもコールしてはならない.
     * </p>
     * 
     * <p>
     * このメソッドは将来非推奨になり, 削除される可能性がある. <br>
     * {@link #typeProvider()} が代替となる.
     * </p>
     * 
     * @implSpec
     *               アクセス修飾子を {@code public} にしてはいけない.
     * 
     *               <p>
     *               このプロバイダにより返されるインスタンスは複数回の呼び出しで同一でなければならない. <br>
     *               すなわち, {@code this.provider() == this.provider()}
     *               が必ず {@code true} でなければならない.
     *               </p>
     * 
     *               <p>
     *               {@link #typeProvider()} をオーバーライドできる場合,
     *               このメソッドをオーバーライドしてはいけない.
     *               </p>
     * 
     * @return プロバイダ
     */
    protected Provider<T> provider() {
        return null;
    }

    /**
     * このクラスが表現する元の生成を受け持つプロバイダを返す.
     * 
     * <p>
     * この抽象メソッドは内部で利用されるために用意されているので,
     * 公開は禁止であり, サブクラスからもコールしてはならない.
     * </p>
     * 
     * @implSpec
     *               アクセス修飾子を {@code public} にしてはいけない.
     * 
     *               <p>
     *               このプロバイダにより返されるインスタンスは複数回の呼び出しで同一でなければならない. <br>
     *               すなわち,
     *               {@code this.typeProvider() == this.typeProvider()}
     *               が必ず {@code true} でなければならない.
     *               </p>
     * 
     *               <p>
     *               このメソッドをオーバーライドできる場合,
     *               {@link #provider()} をオーバーライドしてはいけない.
     *               </p>
     * 
     * @return プロバイダ
     */
    /*
     * 将来的に provider() が削除された場合,
     * このメソッドは抽象メソッドに変更する.
     */
    protected TypeProvider<T> typeProvider() {
        return null;
    }

    /**
     * {@code double} 値を T 型に変換する.
     */
    private T fromDoubleValue(double value) {
        TypeProvider<T> typeProvider = this.typeProvider();
        Provider<T> provider = Objects.nonNull(typeProvider)
                ? typeProvider
                : this.provider();

        if (Objects.isNull(provider)) {
            throw new AssertionError(
                    "require overriding elementTypeProvider() or elementProvider(), "
                            + "and returning non-null");
        }

        return provider.fromDoubleValue(value);
    }

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
        return this.plus(fromDoubleValue(augend));
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
        return this.minus(fromDoubleValue(subtrahend));
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
        return this.times(fromDoubleValue(multiplicand));
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
        return this.dividedBy(fromDoubleValue(divisor));
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
     * @implSpec
     *               comparability と整合するので,
     *               型パラメータ {@code T} とのみ等価比較できることになる.
     */
    @Override
    public abstract boolean equals(Object obj);

    /**
     * このインスタンスのハッシュコードを返す.
     */
    @Override
    public abstract int hashCode();

    /**
     * 与えられたインスタンスとを比較する. <br>
     * equality と整合する.
     * 
     * @throws NullPointerException 引数がnullの場合
     */
    @Override
    public abstract int compareTo(T o);

    /**
     * 自身の文字列表現を返す.
     * 
     * <p>
     * 文字列表現はバージョン間の互換性は担保されない. <br>
     * おそらく次のような形式だろう. <br>
     * {@code %value}
     * </p>
     */
    @Override
    public abstract String toString();

    /**
     * clone不可, 常に例外をスローする.
     * 
     * @throws CloneNotSupportedException 常に
     */
    @Override
    protected final Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException();
    }

    /**
     * 体に対する値のプロバイダ.
     * 
     * <p>
     * {@link Provider} は {@link TypeProvider}
     * のスーパータイプである. <br>
     * 将来的に {@link Provider} が廃止され,
     * {@link TypeProvider} のみを提供する可能性があるため,
     * {@link Provider} 型に依存するコードは推奨されない.
     * </p>
     * 
     * @param <T> 体の元を表す型
     * @deprecated 将来の削除に向けて, 非推奨とする.
     */
    @Deprecated
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
         * @implSpec
         *               戻り値は複数回の呼び出しで同一でなければならない. <br>
         *               すなわち,
         *               {@code this.zero() == this.zero()}
         *               が必ず {@code true} でなければならない.
         * 
         * @return 加法単位元
         */
        public abstract T zero();

        /**
         * 乗法単位元 (1) を返す.
         * 
         * @implSpec
         *               戻り値は複数回の呼び出しで同一でなければならない. <br>
         *               すなわち, {@code this.one() == this.one()}
         *               が必ず {@code true} でなければならない.
         * 
         * @return 乗法単位元
         */
        public abstract T one();

        /**
         * 指定した長さの {@code T} 型の配列を返す. <br>
         * 配列は {@code null} 埋めされている. <br>
         * 長さは0以上でなければならない.
         * 
         * <p>
         * 配列は共変であるが, このメソッドは厳密に {@code T} 型の配列を返す.
         * </p>
         * 
         * @implSpec
         *               バインドされた型 {@code T} に厳密に一致する要素型を持つ配列を返さなければならない.
         * 
         *               <p>
         *               おそらく, 次のような実装であろう
         *               (ただし, {@code T} は具象型とする).
         *               </p>
         * 
         *               <pre>
         * public T[] createArray(int length){
         *     return new T[length];
         * }
         * </pre>
         * 
         * @param length 配列の長さ
         * @return 長さ {@code length} の {@code T} 型の配列
         * @throws NegativeArraySizeException lengthが0以上でないの場合
         */
        public abstract T[] createArray(int length);
    }

    /**
     * {@link Provider} よりも型安全な,
     * 体に対する値のプロバイダ.
     * 
     * <p>
     * {@link TypeProvider} は {@link Provider} の骨格実装としての役割以外に,
     * 型として扱うことの役割をになっている. <br>
     * {@link TypeProvider} のサブタイプは, {@link Provider} 型としてでなく,
     * {@link TypeProvider} 型として扱うことが推奨される.
     * </p>
     * 
     * @param <T> 体の元を表す型
     */
    public static abstract class TypeProvider<T extends PseudoRealNumber<T>>
            implements Provider<T> {

        private final Class<T> elementType;
        private final Class<T[]> arrayType;

        /**
         * 唯一のコンストラクタ. <br>
         * 型パラメータ {@code T} の型トークンを渡し,
         * 厳密な型安全性を実現する.
         * 
         * @param elementType 型パラメータ {@code T} の型トークン
         * @throws NullPointerException 引数が null の場合
         */
        protected TypeProvider(Class<T> elementType) {
            this.elementType = Objects.requireNonNull(elementType);

            // このキャストは必ず成功する
            @SuppressWarnings("unchecked")
            Class<T[]> arrayType = (Class<T[]>) elementType.arrayType();
            this.arrayType = arrayType;
        }

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
        @Override
        public abstract T fromDoubleValue(double value);

        /**
         * 加法単位元 (0) を返す.
         * 
         * @implSpec
         *               戻り値は複数回の呼び出しで同一でなければならない. <br>
         *               すなわち,
         *               {@code this.zero() == this.zero()}
         *               が必ず {@code true} でなければならない.
         * 
         * @return 加法単位元
         */
        @Override
        public abstract T zero();

        /**
         * 乗法単位元 (1) を返す.
         * 
         * @implSpec
         *               戻り値は複数回の呼び出しで同一でなければならない. <br>
         *               すなわち, {@code this.one() == this.one()}
         *               が必ず {@code true} でなければならない.
         * 
         * @return 乗法単位元
         */
        @Override
        public abstract T one();

        /**
         * 指定した長さの {@code T} 型の配列を返す. <br>
         * 配列は {@code null} 埋めされている. <br>
         * 長さは0以上でなければならない.
         * 
         * <p>
         * 配列は共変であるが, このメソッドは厳密に {@code T} 型の配列を返す.
         * </p>
         * 
         * @param length 配列の長さ
         * @return 長さ {@code length} の {@code T} 型の配列
         * @throws NegativeArraySizeException lengthが0以上でないの場合
         */
        @Override
        public final T[] createArray(int length) {
            // このキャストは必ず成功する
            return arrayType.cast(Array.newInstance(elementType, length));
        }

        /**
         * {@link Provider} から {@link TypeProvider} を生成するためのアダプター.
         * 
         * <p>
         * このアダプターで実行される型チェックは厳密でない. <br>
         * したがって, ここで例外がスローしなかった場合に, 後で
         * {@link ArrayStoreException} が生じる可能性がある. <br>
         * 可能な限り, {@link TypeProvider} を直接継承することが強く推奨される.
         * </p>
         * 
         * @param <T> 体の元を表す型
         * @param provider プロバイダ
         * @return {@link TypeProvider} に変換されたプロバイダ
         * @throws ClassCastException
         *             {@link Provider#createArray(int)} によって返される配列が,
         *             {@code T} の狭義サブタイプである場合 (ただし, 必ずスローするとは限らない)
         * @throws NullPointerException 引数が nullの場合
         */
        public static <T extends PseudoRealNumber<T>>
                TypeProvider<T> from(Provider<T> provider) {

            // サイズ0配列を生成し, クラスオブジェクトを取得する.
            @SuppressWarnings("unchecked")
            Class<T> elementType = (Class<T>) provider.createArray(0).getClass().componentType();

            // 正しい型かどうか, インスタンスをキャストすることで確かめる.
            elementType.cast(provider.zero());
            elementType.cast(provider.one());

            return new TypeProvider<>(elementType) {

                @Override
                public T fromDoubleValue(double value) {
                    return provider.fromDoubleValue(value);
                }

                @Override
                public T zero() {
                    return provider.zero();
                }

                @Override
                public T one() {
                    return provider.one();
                }
            };
        }
    }
}
