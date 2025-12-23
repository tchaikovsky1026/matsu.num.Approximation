/*
 * Copyright © 2024 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */
package matsu.num.approximation.polynomial;

import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

import matsu.num.approximation.DoubleLike;
import matsu.num.approximation.DoubleRelativeAssertion;
import matsu.num.approximation.FiniteClosedInterval;
import matsu.num.approximation.PseudoRealNumber;

/**
 * {@link NodeCreation} クラスのテスト.
 */
@RunWith(Enclosed.class)
final class NodeCreationTest {

    private static final PseudoRealNumber.TypeProvider<DoubleLike> PROVIDER =
            DoubleLike.elementTypeProvider();

    private static final DoubleRelativeAssertion DOUBLE_RELATIVE_ASSERTION =
            new DoubleRelativeAssertion(1E-12);

    public static class ノード生成に関するテスト {

        private static final FiniteClosedInterval<DoubleLike> interval = FiniteClosedInterval.from(
                PROVIDER.fromDoubleValue(2d),
                PROVIDER.fromDoubleValue(5d));

        @Test
        public void test() {
            DoubleLike[] node = NodeCreation.execute(4, interval, PROVIDER);

            DOUBLE_RELATIVE_ASSERTION.compareAndAssert(2d, node[0].asDouble());
            DOUBLE_RELATIVE_ASSERTION.compareAndAssert(2.75d, node[1].asDouble());
            DOUBLE_RELATIVE_ASSERTION.compareAndAssert(4.25d, node[2].asDouble());
            DOUBLE_RELATIVE_ASSERTION.compareAndAssert(5d, node[3].asDouble());
        }
    }
}
