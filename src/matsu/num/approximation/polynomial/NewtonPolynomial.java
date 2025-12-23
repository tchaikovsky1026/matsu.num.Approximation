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

import java.util.function.UnaryOperator;

import matsu.num.approximation.PseudoRealNumber;
import matsu.num.approximation.PseudoRealNumber.Provider;

/**
 * Newton 補間による多項式関数を扱う.
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
 * Newton 補間多項式は, ノードが極端に接近していたり値が極端で有ったりしなければ,
 * 生成することができる. <br>
 * しかし, たとえ生成できたとしても, 与える引数 <i>x</i> によっては
 * <i>p</i>(<i>x</i>) を計算できない場合がある. <br>
 * したがって, 多項式生成と値の計算の両方の場合で例外スローの可能性がある.
 * </p>
 * 
 * <p>
 * Newton 補間は逐次的にノードを追加するのに向いた方法であるが,
 * このクラスはそのような柔軟な利用は想定されておらず,
 * 内部的に利用するための, 最低限の機能を持つイミュータブルなオブジェクトとして実装されている. <br>
 * そのため, パッケージプライベートメソッドについては, 例外のスローでなくアサーションにより対応している. <br>
 * 言うまでもなく, 外部に公開されるべきではない.
 * </p>
 *
 * @author Matsuura, Y.
 */
final class NewtonPolynomial<T extends PseudoRealNumber<T>> implements Polynomial<T> {

    private final T[] node;
    private final T[] newtonCoeff;

    private final PseudoRealNumber.TypeProvider<T> elementTypeProvider;

    //多項式の係数
    private final T[] coeff;

    /**
     * 内部でバリデーションされていない.
     */
    private NewtonPolynomial(T[] node, T[] newtonCoeff,
            PseudoRealNumber.TypeProvider<T> elementTypeProvider) {
        this.node = node;
        this.newtonCoeff = newtonCoeff;
        this.elementTypeProvider = elementTypeProvider;

        //ここでArithmeticExceptionを発生させる可能性がある
        this.coeff = this.calcCoeff();
    }

    @Override
    public int degree() {
        return this.node.length - 1;
    }

    @Override
    public T value(T x) {
        T value = elementTypeProvider.zero();

        for (int i = this.node.length - 1; i >= 0; i--) {
            /*
             * この計算は, i = len - 1 = n のときは0乗算である.
             * Newton補間の場合, ノードc_nの値は計算時には使わないが,
             * コードの見やすさのため0乗算の形で残してある.
             */
            value = value.times(x.minus(node[i]));

            value = value.plus(newtonCoeff[i]);
        }
        return value;
    }

    @Override
    public T[] coefficient() {
        return this.coeff.clone();
    }

    @Override
    @Deprecated(forRemoval = true)
    public Provider<T> elementProvider() {
        return this.elementTypeProvider;
    }

    /**
     * @throws ArithmeticException 四則演算の結果, 係数が表現できなくなった場合
     */
    private T[] calcCoeff() {
        final int size = this.node.length;
        T[] polyCoeff = elementTypeProvider.createArray(0);
        for (int i = 0; i < size; i++) {
            T cp_smim1 = this.node[size - 1 - i];
            T[] nextCoeff = elementTypeProvider.createArray(i + 1);

            nextCoeff[0] = this.newtonCoeff[size - 1 - i];
            System.arraycopy(polyCoeff, 0, nextCoeff, 1, i);

            for (int j = 0; j < i; j++) {
                nextCoeff[j] = nextCoeff[j].minus(
                        cp_smim1.times(polyCoeff[j]));
            }
            polyCoeff = nextCoeff;
        }
        return polyCoeff;
    }

    /**
     * 与えられたノードと値を実現するような, Newton 補間多項式を返す. <br>
     * ノードが接近しすぎる, 値が極端である, といった場合は多項式が適切に生成できないため,
     * {@link ArithmeticException} がスローされる. <br>
     * ノードと値のサイズは, 1以上かつ同じでなければならない.
     * 
     * 
     * @param <T> 体の元を表す型パラメータ
     * @param node ノード
     * @param value ノードに対応する値
     * @param elementTypeProvider 体の元に関するプロバイダ
     * @return Newton 補間多項式
     * @throws ArithmeticException 多項式が適切に生成できない場合
     * @throws NullPointerException null
     */
    static <T extends PseudoRealNumber<T>> NewtonPolynomial<T> from(
            T[] node, T[] value, PseudoRealNumber.TypeProvider<T> elementTypeProvider) {

        return construct(
                node.clone(), value.clone(),
                PseudoRealNumber.TypeProvider.from(elementTypeProvider));
    }

    /**
     * 与えられた関数をノードの位置で評価し, Newton 補間多項式を返す. <br>
     * ノードが接近しすぎる, 値が極端である, といった場合は多項式が適切に生成できないため,
     * {@link ArithmeticException} がスローされる. <br>
     * ノードと値のサイズは, 1以上かつ同じでなければならない.
     * 
     * <p>
     * 関数 (function) は, {@code null} でない {@code T}
     * が与えられた場合は値を返すかもしくは{@link ArithmeticException} をスローしなければならない.
     * </p>
     * 
     * @param <T> 体の元を表す型パラメータ
     * @param node ノード
     * @param function 関数
     * @param elementTypeProvider 体の元に関するプロバイダ
     * @return Newton 補間多項式
     * @throws ArithmeticException 多項式が適切に生成できない場合
     * @throws NullPointerException nullが含まれる場合, 関数がnullを返した場合
     */
    static <T extends PseudoRealNumber<T>> NewtonPolynomial<T> from(
            T[] node, UnaryOperator<T> function,
            PseudoRealNumber.TypeProvider<T> elementTypeProvider) {

        T[] nodeClone = node.clone();
        T[] value = elementTypeProvider.createArray(nodeClone.length);
        for (int i = 0; i < nodeClone.length; i++) {
            value[i] = function.apply(nodeClone[i]);
        }

        return construct(
                nodeClone, value,
                PseudoRealNumber.TypeProvider.from(elementTypeProvider));
    }

    /**
     * Newton 補間多項式を構築する内部実装.
     * 引数にはcloneを渡すこと.
     * 
     * @throws ArithmeticException 多項式が適切に生成できない場合
     * @throws NullPointerException null
     */
    private static <T extends PseudoRealNumber<T>> NewtonPolynomial<T> construct(
            T[] node, T[] value, PseudoRealNumber.TypeProvider<T> elementTypeProvider) {

        assert node.length == value.length : "サイズが整合しない";
        assert node.length > 0 : "サイズ0";

        NewtonCoefficientCalc<T> calc =
                new NewtonCoefficientCalc<>(node, value, elementTypeProvider);
        return new NewtonPolynomial<>(node, calc.calcAndGet(), elementTypeProvider);
    }

    private static final class NewtonCoefficientCalc<T extends PseudoRealNumber<T>> {

        private final T[] node;
        private final T[] value;
        private final PseudoRealNumber.TypeProvider<T> elementTypeProvider;

        NewtonCoefficientCalc(T[] node, T[] value, PseudoRealNumber.TypeProvider<T> elementTypeProvider) {
            super();
            this.node = node;
            this.value = value;
            this.elementTypeProvider = elementTypeProvider;
        }

        /**
         * @throws ArithmeticException Tが生成できない場合
         * @throws NullPointerException null
         */
        T[] calcAndGet() {
            T[] newtonCoeff = elementTypeProvider.createArray(this.node.length);
            for (int i = 0, size = this.node.length; i < size; i++) {
                T value_i = this.value[i];
                T x_i = this.node[i];
                for (int k = 0; k < i; k++) {
                    value_i = value_i.minus(newtonCoeff[k]);
                    T den = x_i.minus(this.node[k]);
                    value_i = value_i.dividedBy(den);
                }
                newtonCoeff[i] = value_i;
            }
            return newtonCoeff;
        }
    }
}
