/*
 * Copyright (c) 2024 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */
/*
 * 2024.6.24
 */
package matsu.num.approximation.polynomial;

import java.util.Arrays;
import java.util.Objects;
import java.util.function.DoublePredicate;
import java.util.function.DoubleUnaryOperator;

import matsu.num.approximation.DoubleFiniteClosedInterval;
import matsu.num.approximation.PolynomialFunction;
import matsu.num.approximation.TargetFunction;
import matsu.num.approximation.component.ApproximationFailedException;
import matsu.num.approximation.component.NewtonPolynomial;
import matsu.num.approximation.component.SimplePolynomial;

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
 * @version 18.0
 */
final class RemezPolynomialFactory {

    private final TargetFunction target;

    private final DoubleUnaryOperator funcValue;
    private final DoubleUnaryOperator funcScale;
    private final DoubleFiniteClosedInterval interval;
    private final DoublePredicate acceptsNode;

    RemezPolynomialFactory(TargetFunction target) {
        this.target = Objects.requireNonNull(target);

        this.funcValue = this.target::value;
        this.funcScale = this.target::scale;
        this.interval = this.target.interval();
        this.acceptsNode = this.interval::accepts;
    }

    /**
     * Remez 多項式を構成する.
     * 
     * @param node ノード
     * @return Remez多項式
     * @throws ApproximationFailedException 多項式の構成に破綻した場合
     *             (ノード数が2未満を含む)
     * @throws NullPointerException nullを含む
     */
    public PolynomialFunction create(double[] node) throws ApproximationFailedException {
        if (node.length < 2) {
            throw new ApproximationFailedException("ノード数が2未満");
        }

        //ノードを検証し,ソートする
        node = node.clone();
        Arrays.sort(node);
        if (!Arrays.stream(node).allMatch(this.acceptsNode)) {
            throw new ApproximationFailedException("ノードが定義域内でない");
        }

        double[] thinnedNode = Arrays.copyOf(node, node.length - 1);
        PolynomialFunction p1 = NewtonPolynomial.from(thinnedNode, funcValue);

        /*
         * p2は, p(x_i) = (-1)^i * scale(x_i)を満たすような多項式.
         * i = 0, ... , n
         */
        double[] alternateError = new double[thinnedNode.length];
        for (int i = 0; i < alternateError.length; i++) {
            double scale = funcScale.applyAsDouble(thinnedNode[i]);
            if (!(Double.isFinite(scale) && scale > 0d)) {
                throw new ApproximationFailedException("scaleに負数あるいは非有限数が混入");
            }
            alternateError[i] = (i & 1) == 1 ? -scale : scale;
        }
        PolynomialFunction p2 = NewtonPolynomial.from(thinnedNode, alternateError);

        //x_{n+1}からEを求める
        double x_last = node[node.length - 1];
        double sign = (node.length - 1 & 1) == 1 ? -1d : 1d;
        double e = (p1.value(x_last) - funcValue.applyAsDouble(x_last)) /
                (p2.value(x_last) - sign * funcScale.applyAsDouble(x_last));
        if (!(Double.isFinite(e))) {
            throw new ApproximationFailedException("誤差の値が不正");
        }

        double[] coeffP = p1.coefficient();
        {
            double[] coeffP2 = p2.coefficient();
            for (int i = 0; i < coeffP.length; i++) {
                coeffP[i] -= coeffP2[i] * e;
            }
        }
        return SimplePolynomial.of(coeffP);
    }
}
