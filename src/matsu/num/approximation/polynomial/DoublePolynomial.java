/*
 * Copyright © 2024 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */
/*
 * 2025.12.23
 */
package matsu.num.approximation.polynomial;

/**
 * <p>
 * {@code double} 型で表現された実数体に関する多項式関数を表現するインターフェース. <br>
 * <i>p</i>: &#x211D; &rarr; &#x211D; <br>
 * <i>p</i>(<i>x</i>) =
 * <i>a</i><sub>0</sub> +
 * <i>a</i><sub>1</sub><i>x</i> + &sdot;&sdot;&sdot; +
 * <i>a</i><sub><i>n</i></sub><i>x</i><sup><i>n</i></sup> <br>
 * <i>n</i> は多項式の次数.
 * </p>
 * 
 * <p>
 * 実数は {@code double} 値として表現される.
 * </p>
 * 
 * <p>
 * このインターフェースのサブタイプはイミュータブルであり,
 * かつすべてのメソッドはスレッドセーフであることが保証されている.
 * </p>
 * 
 * @implSpec
 *               このインターフェースはモジュール内で実装されるために用意されており,
 *               モジュール外では実装してはいけない. <br>
 *               モジュール内で実装する場合でも, イミュータブルで関数的でなければならない.
 *
 * @author Matsuura Y.
 */
public interface DoublePolynomial {

    /**
     * 多項式の次数 <i>n</i> を返す.
     *
     * @return 次数 <i>n</i>
     */
    public abstract int degree();

    /**
     * 与えられた <i>x</i> に対し, <i>p</i>(<i>x</i>) の値を返す. <br>
     * {@link Double#POSITIVE_INFINITY},
     * {@link Double#NEGATIVE_INFINITY} あるいは
     * {@link Double#NaN} が返る場合もあり得る.
     * 
     * @param x <i>x</i>, 引数
     * @return <i>p</i>(<i>x</i>)
     */
    public abstract double value(double x);

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
    public abstract double[] coefficient();
}
