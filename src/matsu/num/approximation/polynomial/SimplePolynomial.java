/*
 * Copyright (c) 2024 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */
/*
 * 2024.6.23
 */
package matsu.num.approximation.polynomial;

import java.util.Arrays;

/**
 * <p>
 * 係数を直接与えることによる多項式関数を扱う.
 * </p>
 * 
 * <p>
 * <i>n</i>次の多項式は,
 * (<i>n</i> + 1) 個の係数:
 * <i>a</i><sub>0</sub>, <i>a</i><sub>1</sub>, ... ,
 * <i>a</i><sub><i>n</i></sub>
 * を与えることで構築される. <br>
 * <i>p</i>(<i>x</i>) =
 * <i>a</i><sub>0</sub> +
 * <i>a</i><sub>1</sub><i>x</i> + &middot;&middot;&middot; +
 * <i>a</i><sub><i>n</i></sub><i>x</i><sup><i>n</i></sup>
 * </p>
 * 
 * @author Matsuura Y.
 * @version 18.0
 * @deprecated このクラスは使われていない. おそらく今後も必要ないだろう.
 */
@Deprecated(forRemoval = true)
final class SimplePolynomial implements DoublePolynomial {

    //多項式の係数
    private final double[] coeff;

    private SimplePolynomial(double[] coeff) {
        super();
        if (coeff.length == 0) {
            throw new IllegalArgumentException("サイズ0");
        }
        if (!Arrays.stream(coeff).allMatch(Double::isFinite) ||
                !Arrays.stream(coeff).allMatch(Double::isFinite)) {
            throw new IllegalArgumentException("不正な値を含む");
        }
        this.coeff = coeff;
    }

    @Override
    public int degree() {
        return this.coeff.length - 1;
    }

    @Override
    public double value(double x) {
        double value = 0d;
        for (int i = coeff.length - 1; i >= 0; i--) {
            value *= x;
            value += coeff[i];
        }
        return value;
    }

    @Override
    public double[] coefficient() {
        return this.coeff.clone();
    }

    /**
     * 与えられた係数を持つ多項式関数を返す.
     * 
     * @param coeff 多項式係数
     *            { <i>a</i><sub>0</sub>, <i>a</i><sub>1</sub>, ... ,
     *            <i>a</i><sub><i>n</i></sub> }
     * @return 多項式関数
     * @throws IllegalArgumentException サイズが0の場合,
     *             係数に不正な値を含む場合
     * @throws NullPointerException nullが含まれる場合
     */
    public static SimplePolynomial of(double[] coeff) {
        return new SimplePolynomial(coeff.clone());
    }
}
