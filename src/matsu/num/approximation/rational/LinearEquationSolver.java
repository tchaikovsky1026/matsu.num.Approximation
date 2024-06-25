/*
 * Copyright (c) 2024 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */
/*
 * 2024.6.25
 */
package matsu.num.approximation.rational;

/**
 * <p>
 * 連立方程式を解く.
 * </p>
 * 
 * <p>
 * このクラスは公開してはならない.
 * </p>
 * 
 * @author Matsuura Y.
 * @version 18.0
 * @deprecated このクラスの機能は使えない
 */
@Deprecated
final class LinearEquationSolver {

    private LinearEquationSolver() {
        //インスタンス化不可
        throw new AssertionError();
    }

    /**
     * <p>
     * 連立方程式をとく(予定).
     * </p>
     * 
     * <p>
     * 例外をスローする.
     * </p>
     * 
     * @param coeff 係数行列(正方), A_{ij} = coeff[i][j]
     * @param right 右辺
     * @return 解
     * @throws UnsupportedOperationException 常に (実装されていないため)
     */
    public static double[] execute(double[][] coeff, double[] right) {
        //実装されなければ使えない
        throw new UnsupportedOperationException("TODO: solve linear equation");
    }
}
