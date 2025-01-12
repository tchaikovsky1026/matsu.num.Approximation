/*
 * Copyright © 2024 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */
package matsu.num.approximation;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

import org.junit.Ignore;

/**
 * テストにおけるdouble値のアサーションを助けるユーティリティ.
 */
@Ignore
public final class DoubleRelativeAssertion {

    private final double relativeError;

    /**
     * インスタンス生成.
     * 
     * @param relativeError 許容相対誤差
     * @throws IllegalArgumentException 引数が0以上の有限数でない場合
     */
    public DoubleRelativeAssertion(double relativeError) {
        if (!(Double.isFinite(relativeError) && relativeError >= 0)) {
            throw new IllegalArgumentException();
        }
        this.relativeError = relativeError;
    }

    /**
     * double値に関する相対誤差によるアサーションを行う.
     * 
     * @param expected 予測値, 期待値
     * @param result 計算結果
     */
    public void compareAndAssert(double expected, double result) {
        //数なら範囲比較, Inf,非数の場合はequal比較
        if (Double.isFinite(expected)) {
            assertThat(result, is(closeTo(expected, Math.abs(expected) * relativeError)));
        } else {
            assertThat(result, is(expected));
        }
    }

    /**
     * double値が有限であることを期待してアサーションを行う.
     * 
     * @param value 評価値
     */
    public void assertFinite(double value) {
        if (!Double.isFinite(value)) {
            throw new AssertionError(
                    String.format("有限値でない: value = %s", value));
        }
    }
}
