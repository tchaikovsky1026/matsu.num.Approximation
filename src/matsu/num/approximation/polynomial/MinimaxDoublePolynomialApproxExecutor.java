/*
 * Copyright © 2024 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */
/*
 * 2025.12.25
 */
package matsu.num.approximation.polynomial;

import java.util.Objects;

import matsu.num.approximation.ApproxResult;
import matsu.num.approximation.DoubleApproxTarget;
import matsu.num.approximation.component.ApproximationFailedException;

/**
 * スケーリング付きミニマックス法 (重みづけ Chebyshev ノルム最小化) による,
 * {@code double} 型で表現された実数体に関する多項式関数による近似の実行を扱う.
 * 
 * <p>
 * <i>p</i>(<i>x</i>) を, (最高) 次数が定められた多項式関数空間の元とする. <br>
 * 近似誤差を, <i>e</i>(<i>x</i>) =
 * (<i>p</i>(<i>x</i>) - <i>f</i>(<i>x</i>))
 * /
 * <i>s</i><sub><i>f</i></sub>(<i>x</i>)
 * と定め, max<sub><i>x</i></sub> |<i>e</i>(<i>x</i>)|
 * が最小になるような <i>p</i>(<i>x</i>) を, このクラスの文脈における
 * <i>f</i>(<i>x</i>)
 * の近似多項式という.
 * </p>
 * 
 * <p>
 * このクラスのインスタンスは,
 * {@link #of(int)} メソッドにより取得する. <br>
 * 近似多項式の (最高) 次数はこのクラスのインスタンス生成時に確定する. <br>
 * 多項式近似は, {@link #apply(DoubleApproxTarget)} メソッドにより実行する. <br>
 * 近似に失敗した場合は "空" が返る.
 * </p>
 * 
 * <p>
 * 扱うことができる多項式の次数 <i>n</i> は, 次のとおりである. <br>
 * 0 &le; <i>n</i> &le; 100
 * </p>
 * 
 * @author Matsuura Y.
 */
public final class MinimaxDoublePolynomialApproxExecutor {

    /**
     * 扱うことができる次数の下限.
     */
    public static final int LOWER_LIMIT_OF_ORDER = 0;

    /**
     * 扱うことができる次数の上限.
     */
    public static final int UPPER_LIMIT_OF_ORDER = 100;

    private final int order;

    /**
     * 与えられた値を近似多項式の (最高) 次数とする, インスタンスを生成.
     * 
     * @throws IllegalArgumentException 次数が不適の場合
     */
    private MinimaxDoublePolynomialApproxExecutor(int order) {
        if (!(LOWER_LIMIT_OF_ORDER <= order &&
                order <= UPPER_LIMIT_OF_ORDER)) {
            throw new IllegalArgumentException("invalid order: order = " + order);
        }
        this.order = order;
    }

    /**
     * 多項式の近似次数を返す.
     * 
     * @return 近似の次数
     */
    public int order() {
        return this.order;
    }

    /**
     * 与えられたターゲット関数を近似する.
     * 
     * <p>
     * 近似結果の次数 {@link DoublePolynomial#degree()} は,
     * 自身の {@link #order()} に一致する. <br>
     * 近似の計算中に不具合が出た場合は, 空の {@link ApproxResult} が返る.
     * </p>
     * 
     * @param target ターゲット関数
     * @return 近似結果, 計算に失敗した場合は空
     * @throws NullPointerException 引数がnullの場合
     */
    public ApproxResult<DoublePolynomial> apply(DoubleApproxTarget target) {
        try {
            DoubleApproxCalculationByRemezMinimax calc = new DoubleApproxCalculationByRemezMinimax(
                    Objects.requireNonNull(target), this.order);
            //ここで例外が発生する可能性がある.
            calc.calculate();

            assert this.order() == calc.getResult().degree();

            return ApproxResult.of(calc.getResult());
        } catch (ApproximationFailedException afe) {
            return ApproxResult.failed(afe.failuerMessage());
        }
    }

    /**
     * 与えられた値を近似多項式の次数とする, ミニマックス法による多項式近似エグゼキュータを返す.
     * 
     * <p>
     * 扱うことができる次数の範囲はこのクラスの定数で規定されている.
     * </p>
     * 
     * @param order 近似多項式の次数
     * @return 多項式近似エグゼキュータ
     * @throws IllegalArgumentException 次数が不適の場合
     */
    public static MinimaxDoublePolynomialApproxExecutor of(int order) {
        return new MinimaxDoublePolynomialApproxExecutor(order);
    }
}
