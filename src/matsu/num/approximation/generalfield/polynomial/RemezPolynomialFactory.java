/*
 * Copyright (c) 2024 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */
/*
 * 2024.9.15
 */
package matsu.num.approximation.generalfield.polynomial;

import java.util.Arrays;

import matsu.num.approximation.generalfield.ApproxTarget;
import matsu.num.approximation.generalfield.PseudoRealNumber;

/**
 * <p>
 * Remez アルゴリズムで使われる, 与えたノードで誤差の最大値をとることを期待する多項式を扱う.
 * </p>
 * 
 * <p>
 * <i>n</i> 次の Remez 多項式は, <br>
 * (<i>n</i> + 2)個のノード
 * </p>
 * 
 * @author Matsuura Y.
 * @version 18.2
 * @param <T> 体を表す型パラメータ
 */
final class RemezPolynomialFactory<T extends PseudoRealNumber<T>> {

    private final ApproxTarget<T> target;
    private final PseudoRealNumber.Provider<T> provider;

    RemezPolynomialFactory(ApproxTarget<T> target) {
        this.target = target;
        this.provider = target.elementProvider();
    }

    /**
     * Remez 多項式を構成する.
     * 
     * @param node ノード
     * @return Remez多項式
     * @throws IllegalArgumentException ノード数が2未満の場合, ノードが不正の場合
     * @throws ArithmeticException 計算が破綻する場合
     * @throws NullPointerException null
     */
    public Polynomial<T> create(T[] node) {
        if (node.length < 2) {
            throw new IllegalArgumentException("ノード数が2未満");
        }

        //ノードを検証し,ソートする
        node = node.clone();
        Arrays.sort(node);
        if (!Arrays.stream(node).allMatch(this.target::accepts)) {
            throw new IllegalArgumentException("ノードが定義域内でない");
        }

        T[] thinnedNode = Arrays.copyOf(node, node.length - 1);

        /*
         * p1は, p(x_i) = f(x_i)を満たすような多項式.
         * i = 0, ... , n
         */
        T[] f = provider.createArray(thinnedNode.length);
        for (int i = 0; i < f.length; i++) {
            //Targetの規約上, 例外は発生しない
            f[i] = this.target.value(thinnedNode[i]);
        }
        Polynomial<T> p1 = NewtonPolynomial.from(thinnedNode, f, provider);

        /*
         * p2は, p(x_i) = (-1)^i * scale(x_i)を満たすような多項式.
         * i = 0, ... , n
         */
        T[] alternateError = provider.createArray(thinnedNode.length);
        for (int i = 0; i < alternateError.length; i++) {
            //Targetの規約上, 例外は発生しない
            T scale = this.target.scale(thinnedNode[i]);
            alternateError[i] = (i & 1) == 1 ? scale.negated() : scale;
        }
        Polynomial<T> p2 = NewtonPolynomial.from(thinnedNode, alternateError, provider);

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
        return NewtonPolynomial.from(thinnedNode, f, provider);
    }
}
