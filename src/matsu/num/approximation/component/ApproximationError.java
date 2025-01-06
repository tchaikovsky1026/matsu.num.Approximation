/*
 * Copyright © 2024 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */
/*
 * 2024.9.18
 */
package matsu.num.approximation.component;

import java.util.Objects;
import java.util.function.DoubleUnaryOperator;

import matsu.num.approximation.DoubleApproxTarget;

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
 * を扱う.
 * </p>
 * 
 * @author Matsuura Y.
 * @version 19.0
 */
public final class ApproximationError {

    private final DoubleApproxTarget target;
    private final DoubleUnaryOperator approxFunction;

    /**
     * ターゲット関数とテスト関数を与えて, 近似誤差評価を構築する.
     * 
     * @param target ターゲット関数
     * @param approxFunction テスト関数
     * @throws NullPointerException 引数にnullが含まれる場合
     */
    public ApproximationError(DoubleApproxTarget target, DoubleUnaryOperator approxFunction) {
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
     */
    public double value(double x) throws ApproximationFailedException {

        double delta = this.target.value(x) - approxFunction.applyAsDouble(x);
        double scale = this.target.scale(x);
        double out = delta / scale;
        if (!(Double.isFinite(out))) {
            throw new ApproximationFailedException("計算に失敗");
        }
        return out;
    }
}
