/*
 * Copyright © 2024 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */
/*
 * 2024.12.28
 */
package matsu.num.approximation.polynomial;

import java.util.Arrays;

import matsu.num.approximation.DoubleApproxTarget;
import matsu.num.approximation.component.ApproximationFailedException;

/**
 * Remez アルゴリズムで使われる, 与えたノードで誤差の最大値をとることを期待する多項式を扱う.
 * 
 * <p>
 * <i>n</i> 次の Remez 多項式は, <br>
 * (<i>n</i> + 2)個のノード
 * </p>
 * 
 * <p>
 * 各メソッドの引数に対する契約は, プログラミングエラーによるものについてはアサーションで対応している. <br>
 * したがって, このクラスは外部に公開されてはいけない.
 * </p>
 * 
 * @author Matsuura Y.
 * @version 22.0
 */
final class RemezTypeDoublePolynomialFactory {

    private final DoubleApproxTarget target;

    RemezTypeDoublePolynomialFactory(DoubleApproxTarget target) {
        this.target = target;
    }

    /**
     * Remez 多項式を構成する.
     * 
     * @param node ノード, 2以上でなければならない
     * @return Remez多項式
     * @throws ApproximationFailedException 多項式の構成に破綻した場合
     * @throws NullPointerException null
     */
    DoublePolynomial create(double[] node) throws ApproximationFailedException {
        assert node.length >= 2 : "ノード数が2未満";

        //ノードを検証し,ソートする
        node = node.clone();
        Arrays.sort(node);

        assert Arrays.stream(node).allMatch(this.target::accepts) : "ノードが定義域内でない";

        double[] thinnedNode = Arrays.copyOf(node, node.length - 1);

        /*
         * p1は, p(x_i) = f(x_i)を満たすような多項式.
         * i = 0, ... , n
         */
        double[] f = new double[thinnedNode.length];
        for (int i = 0; i < f.length; i++) {
            double v = this.target.value(thinnedNode[i]);
            if (!Double.isFinite(v)) {
                throw new ApproximationFailedException("valueが不正");
            }
            f[i] = v;
        }
        DoublePolynomial p1 = DoubleNewtonPolynomial.from(thinnedNode, f);

        /*
         * p2は, p(x_i) = (-1)^i * scale(x_i)を満たすような多項式.
         * i = 0, ... , n
         */
        double[] alternateError = new double[thinnedNode.length];
        for (int i = 0; i < alternateError.length; i++) {
            double scale = this.target.scale(thinnedNode[i]);
            if (!(Double.isFinite(scale))) {
                throw new ApproximationFailedException("scaleが不正");
            }
            alternateError[i] = (i & 1) == 1 ? -scale : scale;
        }
        DoublePolynomial p2 = DoubleNewtonPolynomial.from(thinnedNode, alternateError);

        //x_{n+1}からEを求める
        double x_last = node[node.length - 1];
        double sign_scale = (node.length - 1 & 1) == 1
                ? -this.target.scale(x_last)
                : this.target.scale(x_last);
        double e = (p1.value(x_last) - this.target.value(x_last)) /
                (p2.value(x_last) - sign_scale);
        if (!(Double.isFinite(e))) {
            throw new ApproximationFailedException("誤差の値が不正");
        }

        for (int i = 0; i < f.length; i++) {
            f[i] -= alternateError[i] * e;
        }
        return DoubleNewtonPolynomial.from(thinnedNode, f);
    }
}
