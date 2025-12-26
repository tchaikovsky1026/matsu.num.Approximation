/*
 * Copyright © 2025 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */

/*
 * 2025.12.26
 */
package matsu.num.approximation.component;

import java.util.Objects;
import java.util.function.UnaryOperator;

import matsu.num.approximation.ApproxTarget;
import matsu.num.approximation.PseudoRealNumber;

/**
 * 任意実数型の関数の近似誤差を扱う.
 * 
 * <p>
 * ターゲット関数を <i>f</i>, そのスケールを
 * <i>s</i><sub><i>f</i></sub>
 * として,
 * テスト関数 <i>h</i> の近似誤差: <br>
 * (<i>f</i> - <i>h</i>) /
 * <i>s</i><sub><i>f</i></sub> <br>
 * を扱う.
 * </p>
 * 
 * @author Matsuura Y.
 * @param <T> 体の元を表現する型パラメータ
 */
public final class ApproximationErrorCalc<T extends PseudoRealNumber<T>> {

    private final ApproxTarget<T> target;
    private final UnaryOperator<T> approxFunction;

    /**
     * ターゲット関数とテスト関数を与えて, 近似誤差評価を構築する.
     * 
     * @param target ターゲット関数
     * @param approxFunction テスト関数
     * @throws NullPointerException 引数にnullが含まれる場合
     */
    public ApproximationErrorCalc(ApproxTarget<T> target, UnaryOperator<T> approxFunction) {
        this.target = Objects.requireNonNull(target);
        this.approxFunction = Objects.requireNonNull(approxFunction);
    }

    /**
     * 与えられた <i>x</i> に対する関数の近似誤差を返す. <br>
     * 値やスケールが極端あるいは不正値であり計算が続行できない場合は例外をスローする.
     * 
     * @param x <i>x</i>, 引数
     * @return 近似誤差
     * @throws IllegalArgumentException xが範囲外の場合
     * @throws ApproximationFailedException 計算に失敗した場合
     * @throws NullPointerException 引数がnullの場合
     */
    public T value(T x) throws ApproximationFailedException {

        //xがtargetの範囲外の場合IAExが発生する
        //計算が破綻する場合, ArithExが発生 -> 例外翻訳
        try {
            T delta = target.value(x).minus(approxFunction.apply(x));
            T scale = target.scale(x);

            return delta.dividedBy(scale);
        } catch (ArithmeticException e) {
            throw new ApproximationFailedException("error-calc-failure");
        }
    }
}
