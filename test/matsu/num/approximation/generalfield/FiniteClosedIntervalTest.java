package matsu.num.approximation.generalfield;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

import org.junit.Test;
import org.junit.Test.None;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

import matsu.num.approximation.generalfield.PseudoRealNumber.Provider;

/**
 * {@link FiniteClosedInterval} クラスのテスト.
 * 
 * @author Matsuura Y.
 */
@RunWith(Enclosed.class)
final class FiniteClosedIntervalTest {

    public static final Class<?> TEST_CLASS = FiniteClosedInterval.class;

    private static final Provider<DoubleLike> ELEMENT_PROVIDER = DoubleLike.elementProvider();

    public static class 生成に関するテスト {

        @Test
        public void test_同一値は受け入れ不可判定() {
            assertThat(
                    FiniteClosedInterval.acceptsBoundaryValues(
                            ELEMENT_PROVIDER.fromDoubleValue(1d), ELEMENT_PROVIDER.fromDoubleValue(1d)),
                    is(false));
        }

        @Test(expected = IllegalArgumentException.class)
        public void test_同一値は受け入れ不可IAEx() {
            FiniteClosedInterval.from(
                    ELEMENT_PROVIDER.fromDoubleValue(1d), ELEMENT_PROVIDER.fromDoubleValue(1d));
        }

        @Test
        public void test_異値は受け入れ可能判定() {
            assertThat(
                    FiniteClosedInterval.acceptsBoundaryValues(
                            ELEMENT_PROVIDER.fromDoubleValue(2d), ELEMENT_PROVIDER.fromDoubleValue(1d)),
                    is(true));
        }

        @Test(expected = None.class)
        public void test_異値は受け入れ可能() {
            FiniteClosedInterval.from(
                    ELEMENT_PROVIDER.fromDoubleValue(2d), ELEMENT_PROVIDER.fromDoubleValue(1d));
        }
    }

    public static class 等価性に関するテスト {

        @Test
        public void test_生成時の境界が一致していれば等価() {
            DoubleLike x1 = ELEMENT_PROVIDER.fromDoubleValue(1d);
            DoubleLike x2 = ELEMENT_PROVIDER.fromDoubleValue(2d);

            assertThat(
                    FiniteClosedInterval.from(x1, x2),
                    is(FiniteClosedInterval.from(x1, x2)));
            assertThat(
                    FiniteClosedInterval.from(x2, x1),
                    is(FiniteClosedInterval.from(x1, x2)));
        }
    }

    public static class 区間内外の判定に関するテスト {

        private final DoubleLike x1 = ELEMENT_PROVIDER.fromDoubleValue(1d);
        private final DoubleLike x2 = ELEMENT_PROVIDER.fromDoubleValue(2d);

        private final FiniteClosedInterval<DoubleLike> interval =
                FiniteClosedInterval.from(x2, x1);

        @Test
        public void test_境界内の判定() {
            assertThat(
                    interval.accepts(ELEMENT_PROVIDER.fromDoubleValue(1.5d)),
                    is(true));
        }

        @Test
        public void test_境界外の判定() {
            assertThat(
                    interval.accepts(ELEMENT_PROVIDER.fromDoubleValue(0.5d)),
                    is(false));
            assertThat(
                    interval.accepts(ELEMENT_PROVIDER.fromDoubleValue(2.5d)),
                    is(false));
        }

        @Test
        public void test_境界は含む() {
            assertThat(
                    interval.accepts(ELEMENT_PROVIDER.fromDoubleValue(1d)),
                    is(true));
            assertThat(
                    interval.accepts(ELEMENT_PROVIDER.fromDoubleValue(2d)),
                    is(true));
        }
    }

    public static class toString表示 {

        @Test
        public void test_toString() {
            System.out.println(TEST_CLASS.getName());
            System.out.println(
                    FiniteClosedInterval.from(
                            ELEMENT_PROVIDER.fromDoubleValue(2d), ELEMENT_PROVIDER.fromDoubleValue(1d)));
            System.out.println();
        }
    }
}
