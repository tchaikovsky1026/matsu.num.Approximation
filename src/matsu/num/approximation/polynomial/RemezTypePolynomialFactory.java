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

import java.util.Arrays;

import matsu.num.approximation.ApproxTarget;
import matsu.num.approximation.PseudoRealNumber;

/**
 * Remez アルゴリズムで使われる, 与えたノードで誤差の最大値をとることを期待する多項式を扱う.
 * 
 * <p>
 * <i>n</i> 次の Remez 多項式は, <br>
 * (<i>n</i> + 2)個のノード
 * </p>
 * 
 * @author Matsuura Y.
 * @param <T> 体を表す型パラメータ
 */
final class RemezTypePolynomialFactory<T extends PseudoRealNumber<T>> {

    private final ApproxTarget<T> target;
    private final PseudoRealNumber.TypeProvider<T> typeProvider;

    RemezTypePolynomialFactory(ApproxTarget<T> target) {
        this.target = target;
        this.typeProvider = target.elementTypeProvider();
    }

    /**
     * Remez 多項式を構成する. <br>
     * ノード数は2個以上で, 区間内でなければならない.
     * 
     * @param node ノード
     * @return Remez多項式
     * @throws ArithmeticException 計算が破綻して多項式の生成に失敗する場合 (ノードが接近しすぎる場合を含む)
     */
    Polynomial<T> create(T[] node) {
        assert node.length >= 2 : "ノード数が2未満";

        //ノードを検証し,ソートする
        node = node.clone();
        Arrays.sort(node);

        assert Arrays.stream(node).allMatch(this.target::accepts) : "ノードが定義域内でない";

        T[] thinnedNode = Arrays.copyOf(node, node.length - 1);

        /*
         * p1は, p(x_i) = f(x_i)を満たすような多項式.
         * i = 0, ... , n
         */
        T[] f = typeProvider.createArray(thinnedNode.length);
        for (int i = 0; i < f.length; i++) {
            //ArithmeticExが発生する可能性
            f[i] = this.target.value(thinnedNode[i]);
        }
        Polynomial<T> p1 = NewtonPolynomial.from(thinnedNode, f, typeProvider);

        /*
         * p2は, p(x_i) = (-1)^i * scale(x_i)を満たすような多項式.
         * i = 0, ... , n
         */
        T[] alternateError = typeProvider.createArray(thinnedNode.length);
        for (int i = 0; i < alternateError.length; i++) {
            //ArithmeticExが発生する可能性
            T scale = this.target.scale(thinnedNode[i]);
            alternateError[i] = (i & 1) == 1 ? scale.negated() : scale;
        }
        Polynomial<T> p2 = NewtonPolynomial.from(thinnedNode, alternateError, typeProvider);

        //x_{n+1}からEを求める
        T x_last = node[node.length - 1];
        T sign_scale_x_last = (node.length - 1 & 1) == 1
                ? this.target.scale(x_last).negated()
                : this.target.scale(x_last);
        //最悪の場合, ArithmeticEx
        T e = p1.value(x_last).minus(this.target.value(x_last))
                .dividedBy(p2.value(x_last).minus(sign_scale_x_last));

        for (int i = 0; i < f.length; i++) {
            f[i] = f[i].minus(alternateError[i].times(e));
        }
        return NewtonPolynomial.from(thinnedNode, f, typeProvider);
    }
}
