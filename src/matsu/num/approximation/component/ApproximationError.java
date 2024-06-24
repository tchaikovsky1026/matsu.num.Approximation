/*
 * Copyright (c) 2024 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */
/*
 * 2024.6.24
 */
package matsu.num.approximation.component;

import java.util.Objects;
import java.util.function.DoubleUnaryOperator;

import matsu.num.approximation.DoubleFiniteClosedInterval;
import matsu.num.approximation.TargetFunction;

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
 * @version 18.0
 */
public final class ApproximationError {

    private final TargetFunction target;
    private final DoubleUnaryOperator approxFunction;

    private final DoubleUnaryOperator funcValue;
    private final DoubleUnaryOperator funcScale;
    private final DoubleFiniteClosedInterval interval;

    /**
     * ターゲット関数とテスト関数を与えて, 近似誤差評価を構築する.
     * 
     * @param target ターゲット関数
     * @param approxFunction テスト関数
     * @throws NullPointerException 引数にnullが含まれる場合
     */
    public ApproximationError(TargetFunction target, DoubleUnaryOperator approxFunction) {
        this.target = Objects.requireNonNull(target);
        this.approxFunction = Objects.requireNonNull(approxFunction);

        this.funcValue = this.target::value;
        this.funcScale = this.target::scale;
        this.interval = this.target.interval();
    }

    /**
     * 与えられた <i>x</i> に対する関数の近似誤差を返す.
     * 
     * @param x <i>x</i>, 引数
     * @return 近似誤差
     * @throws IllegalArgumentException xが範囲外の場合
     * @throws ApproximationFailedException スケールの実装がおかしい場合
     */
    public double value(double x) throws ApproximationFailedException {
        if (!this.interval.accepts(x)) {
            throw new IllegalArgumentException("xが範囲外");
        }

        double delta = funcValue.applyAsDouble(x) - approxFunction.applyAsDouble(x);
        double scale = funcScale.applyAsDouble(x);
        if (!(Double.isFinite(scale) && scale > 0d)) {
            throw new ApproximationFailedException("scaleに負数あるいは非有限数が混入");
        }
        double out = delta / scale;
        if (!(Double.isFinite(out))) {
            throw new ApproximationFailedException("誤差の値が不正");
        }
        
        return out;
    }
}
