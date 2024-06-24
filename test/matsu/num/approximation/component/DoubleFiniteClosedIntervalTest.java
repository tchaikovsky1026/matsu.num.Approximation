package matsu.num.approximation.component;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.*;

import org.junit.experimental.runners.Enclosed;
import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;

/**
 * {@link DoubleFiniteClosedInterval} クラスのテスト.
 * 
 * @author Matsuura Y.
 */
@RunWith(Enclosed.class)
final class DoubleFiniteClosedIntervalTest {

    public static final Class<?> TEST_CLASS = DoubleFiniteClosedInterval.class;

    @RunWith(Theories.class)
    public static class 生成に関するテスト_例外パターン {

        @DataPoints
        public static double[][] data = {
                { 0d, 0d },
                { 2d, 2d },
                { -2d, -2d },
                { Math.nextDown(1d), 1d },
                { Math.nextDown(-1d), -1d },
                { Double.MIN_VALUE, Double.MIN_NORMAL },
                { -Double.MIN_VALUE, Double.MIN_NORMAL },
                { Double.MIN_VALUE, -Double.MIN_NORMAL }
        };

        @Theory
        public void test_受け入れ判定(double[] x) {
            assertThat(DoubleFiniteClosedInterval.acceptsBoundaryValues(x[0], x[1]), is(false));
            assertThat(DoubleFiniteClosedInterval.acceptsBoundaryValues(x[1], x[0]), is(false));
        }

        @Theory
        public void test_生成(double[] x) {
            try {
                DoubleFiniteClosedInterval.from(x[0], x[1]);
                throw new AssertionError("ここに到達してはいけない");
            } catch (IllegalArgumentException ignore) {
            }

            try {
                DoubleFiniteClosedInterval.from(x[1], x[0]);
                throw new AssertionError("ここに到達してはいけない");
            } catch (IllegalArgumentException ignore) {
            }
        }
    }

    @RunWith(Theories.class)
    public static class 生成に関するテスト_成功パターン {

        @DataPoints
        public static double[][] data = {
                { 0d, 1d },
                { -1d, 1d },
                { Double.MAX_VALUE, 1d },
                { -Double.MAX_VALUE, -1d }
        };

        @Theory
        public void test_受け入れ判定(double[] x) {
            assertThat(DoubleFiniteClosedInterval.acceptsBoundaryValues(x[0], x[1]), is(true));
            assertThat(DoubleFiniteClosedInterval.acceptsBoundaryValues(x[1], x[0]), is(true));
        }

        @Theory
        public void test_生成(double[] x) {
            try {
                DoubleFiniteClosedInterval.from(x[0], x[1]);
                DoubleFiniteClosedInterval.from(x[1], x[0]);
            } catch (IllegalArgumentException ignore) {
                throw new AssertionError("ここに到達してはいけない");
            }
        }
    }
}
