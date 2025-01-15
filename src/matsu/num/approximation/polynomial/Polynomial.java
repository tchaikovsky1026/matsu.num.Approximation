/*
 * Copyright © 2024 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */
/*
 * 2024.22.0
 */
package matsu.num.approximation.polynomial;

import matsu.num.approximation.PseudoRealNumber;

/**
 * 独自クラスによる実数体に関する多項式関数を表現するインターフェース. <br>
 * <i>p</i>: &#x211D; &rarr; &#x211D; <br>
 * <i>p</i>(<i>x</i>) =
 * <i>a</i><sub>0</sub> +
 * <i>a</i><sub>1</sub><i>x</i> + &sdot;&sdot;&sdot; +
 * <i>a</i><sub><i>n</i></sub><i>x</i><sup><i>n</i></sup> <br>
 * <i>n</i> は多項式の次数.
 * 
 * <p>
 * 実数は {@link PseudoRealNumber} のサブタイプ {@code T} として表現される. <br>
 * <i>p</i>は有限の値を返すか, あるいは {@code T} で表現できない場合
 * (例えばオーバーフローした場合) は例外をスローする.
 * </p>
 * 
 * <p>
 * このインターフェースのサブタイプはイミュータブルであり,
 * かつすべてのメソッドはスレッドセーフであることが保証されている.
 * </p>
 * 
 * <p>
 * <i>
 * <u>
 * このインターフェースは実装を隠ぺいして型を公開するためのものである. <br>
 * 外部で実装することは不可.
 * </u>
 * </i>
 * </p>
 *
 * @author Matsuura Y.
 * @param <T> 体の元を表現する型パラメータ
 */
public sealed interface Polynomial<T extends PseudoRealNumber<T>> permits PolynomialSealed {

    /**
     * 多項式の次数 <i>n</i> を返す.
     *
     * @return 次数 <i>n</i>
     */
    public abstract int degree();

    /**
     * 与えられた <i>x</i> に対し, <i>p</i>(<i>x</i>) の値を返す. <br>
     * <i>p</i>(<i>x</i>) が {@code T} で表現できない場合は例外をスローする.
     * 
     * @param x <i>x</i>, 引数
     * @return <i>p</i>(<i>x</i>)
     * @throws ArithmeticException 計算不能で値が返せない場合
     * @throws NullPointerException 引数がnullの場合
     */
    public abstract T value(T x);

    /**
     * 多項式の係数
     * {
     * <i>a</i><sub>0</sub>,
     * <i>a</i><sub>1</sub>, ... ,
     * <i>a</i><sub><i>n</i></sub>
     * }
     * を返す.
     * 
     * @return 多項式の係数
     */
    public abstract T[] coefficient();

    /**
     * このターゲットが扱う体の元に関するプロバイダを返す.
     * 
     * @return 体の元に関するプロバイダ
     */
    public abstract PseudoRealNumber.Provider<T> elementProvider();
}
