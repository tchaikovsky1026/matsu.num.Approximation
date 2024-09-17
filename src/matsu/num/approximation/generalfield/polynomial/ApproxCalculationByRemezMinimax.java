/*
 * Copyright (c) 2024 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */
/*
 * 2024.9.17
 */
package matsu.num.approximation.generalfield.polynomial;

import java.util.Objects;
import java.util.function.UnaryOperator;

import matsu.num.approximation.generalfield.ApproxTarget;
import matsu.num.approximation.generalfield.PseudoRealNumber;

/**
 * 多項式関数による近似の計算処理を扱う. <br>
 * スレッドセーフでないので, 単一スレッド内でインスタンスが共有されるようにしなければならない.
 * 
 * @author Matsuura Y.
 * @version 18.2
 * @param <T> 体を表す型パラメータ
 */
final class ApproxCalculationByRemezMinimax<T extends PseudoRealNumber<T>> {

    private final ApproxTarget<T> target;
    private final int order;

    private final RemezPolynomialFactory<T> remezPolynomialFactory;

    private Polynomial<T> result;

    /**
     * 
     * @param target ターゲット関数, nullであってはいけない
     * @param order 多項式の次数, 0以上の適切な値でなければならない
     */
    ApproxCalculationByRemezMinimax(ApproxTarget<T> target, int order) {
        super();
        this.target = target;
        this.order = order;

        this.remezPolynomialFactory = new RemezPolynomialFactory<>(this.target);
    }

    /**
     * @throws ArithmeticException 計算が破綻した場合
     */
    void calculate() {
        RemezIterator remezIterator =
                new RemezIterator(NodeCreation.execute(this.order + 2, target.interval(), target.elementProvider()));

        int iteration = 1000;
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
    Polynomial<T> getResult() {
        assert Objects.nonNull(this.result);

        return this.result;
    }

    private final class RemezIterator {

        private T[] node;

        /**
         * 初期ノードを与えてイテレータを生成する.
         * 
         * @param node ノード (両端は区間両端に一致する)
         */
        RemezIterator(T[] node) {
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
         * @throws ArithmeticException
         */
        void iteration(double relativeDelta) {
            assert 1E-4 <= relativeDelta;
            assert relativeDelta <= 0.1;

            //ノードからRemez多項式を構築する
            Polynomial<T> remezPolynomial = remezPolynomialFactory.create(node);
            ApproximationError error = new ApproximationError(remezPolynomial::value);

            //近似誤差の分布を表す
            boolean err_sign_is_positive = this.errSignIsPositive(error);

            //端を除くノードをわずかに動かす処理
            T[] nextNodes = this.node.clone();
            for (int i = 0; i < node.length; i++) {
                //偶数番目のノードはそのまま, 奇数番目のノードは反転させる
                boolean node_sign = err_sign_is_positive ^ ((i & 1) == 1);

                T x_prev = i == 0
                        ? target.interval().lower()
                        : node[i - 1];
                T x_mid = node[i];
                T x_next = i == node.length - 1
                        ? target.interval().upper()
                        : node[i + 1];

                T gap_l = x_mid.minus(x_prev);
                T gap_u = x_next.minus(x_mid);

                T x_l = x_mid.minus(gap_l.times(relativeDelta));
                T x_u = x_mid.plus(gap_u.times(relativeDelta));

                T e_l = error.value(x_l);
                T e_mid = error.value(x_mid);
                T e_u = error.value(x_u);

                if (!node_sign) {
                    e_l = e_l.negated();
                    e_mid = e_mid.negated();
                    e_u = e_u.negated();
                }

                if (e_l.compareTo(e_mid) > 0 && e_l.compareTo(e_u) > 0) {
                    nextNodes[i] = x_l;
                } else if (e_u.compareTo(e_mid) > 0 && e_u.compareTo(e_l) > 0) {
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
         * @throws ArithmeticException
         */
        private boolean errSignIsPositive(ApproximationError error) {
            T sum = target.elementProvider().zero();
            for (int i = 0; i < node.length; i++) {
                T err = error.value(node[i]);
                sum = sum.plus((i & 1) == 0 ? err : err.negated());
            }

            return sum.compareTo(target.elementProvider().zero()) > 0;
        }

        /**
         * 最適化された多項式関数を返す.
         * 
         * @return 最適化された多項式関数
         * @throws ArithmeticException
         */
        Polynomial<T> calcResult() {
            return remezPolynomialFactory.create(node);
        }
    }

    /**
     * <p>
     * 関数の近似誤差を扱う.
     * </p>
     * 
     * <p>
     * ターゲット関数を <i>f</i>, そのスケールを
     * <i>s</i><sub><i>f</i></sub>
     * として,
     * テスト関数 <i>h</i> の近似誤差: <br>
     * (<i>f</i> - <i>h</i>) /
     * <i>s</i><sub><i>f</i></sub> <br>
     * を表す.
     * </p>
     */
    private final class ApproximationError {

        private final UnaryOperator<T> approxFunction;

        /**
         * ターゲット関数とテスト関数を与えて, 近似誤差評価を構築する.
         * 
         * @param approxFunction テスト関数
         * @throws NullPointerException 引数にnullが含まれる場合
         */
        ApproximationError(UnaryOperator<T> approxFunction) {
            this.approxFunction = Objects.requireNonNull(approxFunction);
        }

        /**
         * 与えられた <i>x</i> に対する関数の近似誤差を返す.
         * 
         * @param x <i>x</i>, 引数
         * @return 近似誤差
         * @throws IllegalArgumentException xが範囲外の場合
         * @throws ArithmeticException 演算が破綻した場合
         */
        public T value(T x) {

            //xがtargetの範囲外の場合, valueあるいはscaleの呼び出しでIAExが発生する
            //計算が破綻する場合, ArithExが発生
            T delta = target.value(x).minus(approxFunction.apply(x));
            T scale = target.scale(x);

            return delta.dividedBy(scale);
        }
    }

}
