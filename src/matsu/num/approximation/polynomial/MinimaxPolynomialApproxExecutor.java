/*
 * Copyright (c) 2024 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */
/*
 * 2024.9.19
 */
package matsu.num.approximation.polynomial;

import java.util.Objects;

import matsu.num.approximation.ApproxResult;
import matsu.num.approximation.DoubleApproxTarget;
import matsu.num.approximation.component.ApproximationFailedException;

/**
 * <p>
 * スケーリング付きミニマックス法 (重みづけ Chebyshev ノルム最小化) による,
 * {@code double} 型で表現された実数体に関する多項式関数による近似の実行を扱う.
 * </p>
 * 
 * <p>
 * 扱うことができる多項式の次数 <i>n</i> は, 次のとおりである. <br>
 * 0 &le; <i>n</i> &le; 100
 * </p>
 * 
 * @author Matsuura Y.
 * @version 19.1
 */
public final class MinimaxPolynomialApproxExecutor {

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
     * 与えられた値を近似多項式の次数とする, インスタンスを生成.
     * 
     * @throws IllegalArgumentException 次数が不適の場合
     */
    private MinimaxPolynomialApproxExecutor(int order) {
        if (!(LOWER_LIMIT_OF_ORDER <= order &&
                order <= UPPER_LIMIT_OF_ORDER)) {
            throw new IllegalArgumentException("次数が不適");
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
     * <p>
     * 与えられたターゲット関数を近似する.
     * </p>
     * 
     * <p>
     * 近似の計算中に不具合が出た場合は, 空の {@link ApproxResult} が返る.
     * </p>
     * 
     * @param target ターゲット関数
     * @return 近似結果, 計算に失敗した場合は空
     * @throws NullPointerException 引数がnullの場合
     */
    public ApproxResult<DoublePolynomial> apply(DoubleApproxTarget target) {
        try {
            MinimaxPolynomialApproxCalculation calc = new MinimaxPolynomialApproxCalculation(
                    Objects.requireNonNull(target), this.order);
            //ここで例外が発生する可能性がある.
            calc.calculate();
            return ApproxResult.of(calc.getResult());
        } catch (ApproximationFailedException afe) {
            return ApproxResult.failed(afe.failuerMessage());
        }
    }

    /**
     * <p>
     * 与えられた値を近似多項式の次数とする, ミニマックス法による多項式近似エグゼキュータを返す.
     * </p>
     * 
     * <p>
     * 扱うことができる次数の範囲はこのクラスの定数で規定されている.
     * </p>
     * 
     * @param order 近似多項式の次数
     * @return 多項式近似エグゼキュータ
     * @throws IllegalArgumentException 次数が不適の場合
     */
    public static MinimaxPolynomialApproxExecutor of(int order) {
        return new MinimaxPolynomialApproxExecutor(order);
    }
}
