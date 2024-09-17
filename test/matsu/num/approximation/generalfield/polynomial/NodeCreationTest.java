package matsu.num.approximation.generalfield.polynomial;

import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

import matsu.num.approximation.DoubleRelativeAssertion;
import matsu.num.approximation.generalfield.DoubleLike;
import matsu.num.approximation.generalfield.FiniteClosedInterval;
import matsu.num.approximation.generalfield.PseudoRealNumber;

/**
 * {@link NodeCreation} クラスのテスト.
 * 
 * @author Matsuura Y.
 */
@RunWith(Enclosed.class)
final class NodeCreationTest {

    private static final PseudoRealNumber.Provider<DoubleLike> PROVIDER = DoubleLike.elementProvider();

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
