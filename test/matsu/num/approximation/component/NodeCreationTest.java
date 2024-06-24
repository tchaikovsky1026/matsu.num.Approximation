package matsu.num.approximation.component;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.experimental.runners.Enclosed;
import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;

import matsu.num.approximation.DoubleFiniteClosedInterval;
import matsu.num.approximation.DoubleRelativeAssertion;

/**
 * {@link NodeCreation} クラスのテスト.
 * 
 * @author Matsuura Y.
 */
@RunWith(Enclosed.class)
final class NodeCreationTest {

    public static final Class<?> TEST_CLASS = NodeCreation.class;

    private static final DoubleRelativeAssertion DOUBLE_RELATIVE_ASSERTION =
            new DoubleRelativeAssertion(1E-12);

    @RunWith(Theories.class)
    public static class ノードの値のテスト {

        private static final DoubleFiniteClosedInterval INTERVAL =
                DoubleFiniteClosedInterval.from(1, 3);

        @DataPoints
        public static List<DataSet> list;

        @BeforeClass
        public static void before() {
            list = new ArrayList<>();

            list.add(new DataSet(2, new double[] { 1, 3 }));
            list.add(new DataSet(3, new double[] { 1, 2, 3 }));
            list.add(new DataSet(4, new double[] { 1, 1.5, 2.5, 3 }));

        }

        @Theory
        public void test_ノードのサイズと値をテスト(DataSet dataSet) {
            double[] expected = dataSet.node;
            double[] result = NodeCreation.execute(dataSet.size, INTERVAL);

            assertThat(result.length, is(expected.length));
            for (int i = 0; i < result.length; i++) {
                DOUBLE_RELATIVE_ASSERTION.compareAndAssert(
                        expected[i],
                        result[i]);
            }
        }

        private static final class DataSet {
            int size;
            double[] node;

            public DataSet(int size, double[] node) {
                super();
                this.size = size;
                this.node = node;
            }
        }
    }
}
