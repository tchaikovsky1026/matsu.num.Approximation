/*
 * Copyright (c) 2024 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */
/*
 * 2024.6.21
 */
package matsu.num.approximation.component;

import java.util.Arrays;
import java.util.Objects;
import java.util.function.DoubleUnaryOperator;

import matsu.num.approximation.PolynomialFunction;

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
 * このクラスにおいては最低限の機能を持つイミュータブルなオブジェクトとして実装されている. <br>
 * Newton 補間は逐次的にノードを追加するのに向いた方法であるが,
 * このクラスはそのような柔軟な利用は想定されていない. <br>
 * したがって, 外部に公開されるべきではない.
 * </p>
 *
 * @author Matsuura, Y.
 * @version 18.0
 */
public final class NewtonPolynomial implements PolynomialFunction {

    private final double[] node;
    private final double[] newtonCoeff;

    //多項式の係数
    private final double[] coeff;

    private NewtonPolynomial(double[] node, double[] newtonCoeff) {
        this.node = node;
        this.newtonCoeff = newtonCoeff;

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

    private double[] calcCoeff() {
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
        return polyCoeff;
    }

    /**
     * 与えられたノードと値を実現するような, Newton 補間多項式を返す. <br>
     * ノード重複したり, 接近しすぎたりする場合,
     * 例外はスローされないが補間多項式に不具合が生じる場合がある.
     * 
     * @param node ノード
     * @param value ノードに対応する値
     * @return Newton 補間多項式
     * @throws IllegalArgumentException サイズが整合しない場合, サイズが0の場合,
     *             ノードやvalueに不正な値を含む場合
     * @throws NullPointerException nullが含まれる場合
     */
    public static NewtonPolynomial from(double[] node, double[] value) {
        return construct(node.clone(), value.clone());
    }

    /**
     * 与えられた関数をノードの位置で評価し, Newton 補間多項式を返す. <br>
     * ノード重複したり, 接近しすぎたりする場合,
     * 例外はスローされないが補間多項式に不具合が生じる場合がある.
     * 
     * @param node ノード
     * @param function 関数
     * @return Newton 補間多項式
     * @throws IllegalArgumentException サイズが0の場合, ノードや関数値に不正な値を含む場合
     * @throws NullPointerException nullが含まれる場合
     */
    public static NewtonPolynomial from(double[] node, DoubleUnaryOperator function) {
        double[] nodeClone = node.clone();
        double[] value = Arrays.stream(nodeClone).map(Objects.requireNonNull(function)).toArray();
        return construct(nodeClone, value);
    }

    /**
     * Newton 補間多項式を構築する内部実装.
     * 引数にはcloneを渡すこと.
     * 
     * @throws IllegalArgumentException サイズが整合しない場合,
     *             サイズが0の場合, 不正な値を含む場合
     * @throws NullPointerException null
     */
    private static NewtonPolynomial construct(double[] node, double[] value) {
        //ここで例外発生の可能性がある
        NewtonCoefficientCalc calc = new NewtonCoefficientCalc(node, value);

        return new NewtonPolynomial(node, calc.calcAndGet());
    }

    private static final class NewtonCoefficientCalc {

        private final double[] node;
        private final double[] value;

        /**
         * @throws IllegalArgumentException サイズが整合しない場合,
         *             サイズが0の場合
         * @throws NullPointerException null
         */
        NewtonCoefficientCalc(double[] node, double[] value) {
            super();
            this.node = node;
            this.value = value;

            if (node.length != value.length) {
                throw new IllegalArgumentException("サイズが整合しない");
            }
            if (node.length == 0) {
                throw new IllegalArgumentException("サイズ0");
            }
            if (!Arrays.stream(node).allMatch(Double::isFinite) ||
                    !Arrays.stream(value).allMatch(Double::isFinite)) {
                throw new IllegalArgumentException("不正な値を含む");
            }
        }

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
