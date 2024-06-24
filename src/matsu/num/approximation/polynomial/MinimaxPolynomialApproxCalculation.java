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

import java.util.Objects;

import matsu.num.approximation.PolynomialFunction;
import matsu.num.approximation.TargetFunction;
import matsu.num.approximation.component.ApproximationError;
import matsu.num.approximation.component.ApproximationFailedException;
import matsu.num.approximation.component.NodeCreation;

/**
 * 多項式関数による近似の計算処理を扱う. <br>
 * スレッドセーフでないので, 単一スレッド内でインスタンスが共有されるようにしなければならない.
 * 
 * @author Matsuura Y.
 * @version 18.0
 */
final class MinimaxPolynomialApproxCalculation {

    private final TargetFunction target;
    private final int order;

    private final RemezPolynomialFactory remezPolynomialFactory;

    private PolynomialFunction result;

    /**
     * 
     * @param target ターゲット関数, nullであってはいけない
     * @param order 多項式の次数, 0以上の適切な値でなければならない
     */
    MinimaxPolynomialApproxCalculation(TargetFunction target, int order) {
        super();
        this.target = target;
        this.order = order;

        this.remezPolynomialFactory = new RemezPolynomialFactory(this.target);
    }

    void calculate() throws ApproximationFailedException {
        RemezIterator remezIterator =
                new RemezIterator(NodeCreation.execute(this.order + 2, target.interval()));

        int iteration = 100;
        double[] relativeDeltas = { 0.1, 0.03, 0.01, 0.003, 0.001, 3E-4, 1E-4 };
        for (double rd : relativeDeltas) {
            for (int c = 0; c < iteration; c++) {
                remezIterator.iteration(rd);
            }
        }
        this.result = remezIterator.calcResult();
    }

    /**
     * 近似結果を返す. <br>
     * 呼び出すタイミングに注意が必要.
     * 
     * @return 近似結果
     * @throws IllegalStateException calculateが未実行の場合, 失敗した場合
     */
    PolynomialFunction getResult() {
        if (Objects.isNull(this.result)) {
            throw new IllegalStateException("Bug: 今このメソッドを呼んではいけない");
        }
        return this.result;
    }

    private final class RemezIterator {

        private double[] node;

        /**
         * 初期ノードを与えてイテレータを生成する.
         * 
         * @param node ノード (両端は区間両端に一致する)
         */
        RemezIterator(double[] node) {
            super();
            this.node = node;
        }

        /**
         * <p>
         * 1回のイテレーションを行う.
         * </p>
         * 
         * <p>
         * 初期状態: ノードを保持している. <br>
         * 状態変化: ノードを解の方向に変化させる
         * </p>
         * 
         * <ul>
         * <li>ノードからRemez多項式を構成.</li>
         * <li>誤差が大きい方向にノードを動かす.</li>
         * </ul>
         * 
         * @param relativeDelta 1E-4から0.1の範囲
         * @throws ApproximationFailedException
         */
        void iteration(double relativeDelta) throws ApproximationFailedException {
            if (!(1E-4 <= relativeDelta && relativeDelta <= 0.1)) {
                throw new AssertionError("Bug: relativeDelta");
            }

            //ノードからRemez多項式を構築する
            PolynomialFunction remezPolynomial = remezPolynomialFactory.create(node);
            ApproximationError error = new ApproximationError(target, remezPolynomial::value);

            //近似誤差の分布を表す
            boolean err_sign_is_positive = this.errSignIsPositive(error);

            //端を除くノードをわずかに動かす処理
            double[] nextNodes = this.node.clone();
            for (int i = 1; i < node.length - 1; i++) {
                //偶数番目のノードはそのまま, 奇数番目のノードは反転させる
                boolean node_sign = err_sign_is_positive ^ ((i & 1) == 1);

                double x_prev = node[i - 1];
                double x_mid = node[i];
                double x_next = node[i + 1];

                double x_l = x_mid + (x_prev - x_mid) * relativeDelta;
                double x_u = x_mid + (x_next - x_mid) * relativeDelta;

                double e_l = error.value(x_l);
                double e_mid = error.value(x_mid);
                double e_u = error.value(x_u);

                if (!node_sign) {
                    e_l = -e_l;
                    e_mid = -e_mid;
                    e_u = -e_u;
                }

                if (e_l > e_mid && e_l > e_u) {
                    nextNodes[i] = x_l;
                } else if (e_u > e_mid && e_u > e_l) {
                    nextNodes[i] = x_u;
                }
            }
            node = nextNodes;
        }

        /**
         * <p>
         * ノードにおける近似誤差の符号を判定し, 正負を返す.
         * </p>
         * 
         * <p>
         * 近似誤差が正であるとは, 0, 2, 4, ... 番目のノードの誤差が正であり,
         * 1, 3, 5, ... 番目のそれが負であることをいう.
         * </p>
         * 
         * @param error 近似誤差の計算
         * @return 正の場合はtrue
         * @throws ApproximationFailedException 近似誤差を適切に計算できない場合
         */
        private boolean errSignIsPositive(ApproximationError error) throws ApproximationFailedException {
            double sum = 0d;
            for (int i = 0; i < node.length; i++) {
                double err = error.value(node[i]);
                sum += (i & 1) == 0 ? err : -err;
            }
            if (!Double.isFinite(sum)) {
                throw new ApproximationFailedException("近似誤差が適切に計算できない");
            }

            return sum >= 0;
        }

        /**
         * 最適化された多項式関数を返す.
         * 
         * @return 最適化された多項式関数
         * @throws ApproximationFailedException
         */
        PolynomialFunction calcResult() throws ApproximationFailedException {
            return remezPolynomialFactory.create(node);
        }
    }
}
