/*
 * Copyright © 2024 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */
/*
 * 2025.12.26
 */
package matsu.num.approximation.polynomial;

import java.util.Arrays;

import matsu.num.approximation.component.ApproximationFailedException;

/**
 * <p>
 * Newton 補間による多項式関数を扱う.
 * </p>
 * 
 * <p>
 * <i>n</i>次の Newton 補間多項式は,
 * (<i>n</i> + 1) 個のノード:
 * <i>c</i><sub>0</sub>, <i>c</i><sub>1</sub>, ... ,
 * <i>c</i><sub><i>n</i></sub>
 * と, そのときの値:
 * <i>p</i>(<i>c</i><sub>0</sub>),
 * <i>p</i>(<i>c</i><sub>1</sub>), ... ,
 * <i>p</i>(<i>c</i><sub><i>n</i></sub>)
 * を与えることで構築される.
 * </p>
 * 
 * <p>
 * Newton 補間は逐次的にノードを追加するのに向いた方法であるが,
 * このクラスはそのような柔軟な利用は想定されておらず,
 * 内部的に利用するための, 最低限の機能を持つイミュータブルなオブジェクトとして実装されている. <br>
 * そのため, パッケージプライベートメソッドの引数については, 例外のスローでなくアサーションにより対応している. <br>
 * 言うまでもなく, 外部に公開されるべきではない.
 * </p>
 *
 * @author Matsuura, Y.
 */
final class DoubleNewtonPolynomial implements DoublePolynomial {

    private final double[] node;
    private final double[] newtonCoeff;

    //多項式の係数
    private final double[] coeff;

    /**
     * @throws ApproximationFailedException 多項式の係数に不正値が混入した場合
     */
    private DoubleNewtonPolynomial(double[] node, double[] newtonCoeff) throws ApproximationFailedException {
        this.node = node;
        this.newtonCoeff = newtonCoeff;

        //引数に不正値がある場合, ここで例外がスローれる
        this.coeff = this.calcCoeff();
    }

    @Override
    public int degree() {
        return this.node.length - 1;
    }

    @Override
    public double value(double x) {
        double value = 0;
        for (int i = this.node.length - 1; i >= 0; i--) {
            value *= x - this.node[i];
            value += this.newtonCoeff[i];
        }
        return value;
    }

    @Override
    public double[] coefficient() {
        return this.coeff.clone();
    }

    private double[] calcCoeff() throws ApproximationFailedException {
        final int size = this.node.length;
        double[] polyCoeff = new double[0];
        for (int i = 0; i < size; i++) {
            double cp_smim1 = this.node[size - 1 - i];
            double[] nextCoeff = new double[i + 1];

            nextCoeff[0] = this.newtonCoeff[size - 1 - i];
            System.arraycopy(polyCoeff, 0, nextCoeff, 1, i);

            for (int j = 0; j < i; j++) {
                nextCoeff[j] -= cp_smim1 * polyCoeff[j];
            }
            polyCoeff = nextCoeff;
        }

        if (!Arrays.stream(polyCoeff).allMatch(Double::isFinite)) {
            throw new ApproximationFailedException("invalid coefficients");
        }

        return polyCoeff;
    }

    /**
     * 与えられたノードと値を実現するような, Newton 補間多項式を返す. <br>
     * ノード重複したり, 接近しすぎたりする場合, 値が極端な場合は,
     * 係数に不正値が生じて例外がスローされることがある.
     * 
     * @param node ノード
     * @param value ノードに対応する値
     * @return Newton 補間多項式
     * @throws ApproximationFailedException 多項式の係数に不正値が混入した場合
     * @throws NullPointerException null
     */
    static DoubleNewtonPolynomial from(double[] node, double[] value) throws ApproximationFailedException {
        node = node.clone();
        value = value.clone();

        assert node.length == value.length : "mismatch size";
        assert node.length > 0 : "size 0";
        assert Arrays.stream(node).allMatch(Double::isFinite)
                && Arrays.stream(value).allMatch(Double::isFinite) : "including invalid values";

        return new DoubleNewtonPolynomial(node, new NewtonCoefficientCalc(node, value).calcAndGet());
    }

    private static final class NewtonCoefficientCalc {

        private final double[] node;
        private final double[] value;

        NewtonCoefficientCalc(double[] node, double[] value) {
            super();
            this.node = node;
            this.value = value;
        }

        /**
         * この戻り値は有限でない可能性がある
         */
        double[] calcAndGet() {
            double[] newtonCoeff = new double[this.node.length];
            for (int i = 0, size = this.node.length; i < size; i++) {
                double value_i = this.value[i];
                double x_i = this.node[i];
                for (int k = 0; k < i; k++) {
                    value_i -= newtonCoeff[k];
                    double den = x_i - this.node[k];
                    value_i /= den;
                }
                newtonCoeff[i] = value_i;
            }
            return newtonCoeff;
        }
    }
}
