package matsu.num.approximation.polynomial;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

import matsu.num.approximation.DoubleFiniteClosedInterval;
import matsu.num.approximation.DoubleRelativeAssertion;
import matsu.num.approximation.PolynomialFunction;
import matsu.num.approximation.TargetFunction;

/**
 * {@link RemezPolynomialFactory} クラスのテスト.
 * 
 * @author Matsuura Y.
 */
@RunWith(Enclosed.class)
final class RemezPolynomialFactoryTest {

    public static final Class<?> TEST_CLASS = RemezPolynomialFactory.class;

    private static final DoubleRelativeAssertion DOUBLE_RELATIVE_ASSERTION =
            new DoubleRelativeAssertion(1E-12);

    public static class 構築のテスト_定数関数の近似 {

        private RemezPolynomialFactory remezFactory;

        @Before
        public void before_remezを作成() {
            //定数関数
            TargetFunction targetFunction = new TargetFunction() {

                @Override
                public double value(double x) {
                    return 2d;
                }

                @Override
                public double scale(double x) {
                    return 2d;
                }

                @Override
                public DoubleFiniteClosedInterval interval() {
                    return DoubleFiniteClosedInterval.from(1, 3);
                }
            };
            remezFactory = new RemezPolynomialFactory(targetFunction);
        }

        @Test
        public void test_定数関数() throws Exception {
            PolynomialFunction poly = remezFactory.create(new double[] { 1, 3 });

            assertThat(poly.degree(), is(0));
            DOUBLE_RELATIVE_ASSERTION.compareAndAssert(2d, poly.value(1d));
            DOUBLE_RELATIVE_ASSERTION.compareAndAssert(2d, poly.value(3d));
        }

        @Test
        public void test_1次関数() throws Exception {
            PolynomialFunction poly = remezFactory.create(new double[] { 1, 2, 3 });

            assertThat(poly.degree(), is(1));
            DOUBLE_RELATIVE_ASSERTION.compareAndAssert(2d, poly.value(1d));
            DOUBLE_RELATIVE_ASSERTION.compareAndAssert(2d, poly.value(2d));
            DOUBLE_RELATIVE_ASSERTION.compareAndAssert(2d, poly.value(3d));
        }
    }

    public static class 構築のテスト_1次関数の近似 {

        private RemezPolynomialFactory remezFactory;

        @Before
        public void before_remezを作成() {
            TargetFunction targetFunction = new TargetFunction() {

                @Override
                public double value(double x) {
                    return x;
                }

                @Override
                public double scale(double x) {
                    return 2d;
                }

                @Override
                public DoubleFiniteClosedInterval interval() {
                    return DoubleFiniteClosedInterval.from(1, 3);
                }
            };
            remezFactory = new RemezPolynomialFactory(targetFunction);
        }

        @Test
        public void test_定数関数() throws Exception {
            PolynomialFunction poly = remezFactory.create(new double[] { 1, 3 });

            assertThat(poly.degree(), is(0));
            DOUBLE_RELATIVE_ASSERTION.compareAndAssert(2d, poly.value(1d));
            DOUBLE_RELATIVE_ASSERTION.compareAndAssert(2d, poly.value(3d));
        }

        @Test
        public void test_1次関数() throws Exception {
            PolynomialFunction poly = remezFactory.create(new double[] { 1, 2, 3 });

            assertThat(poly.degree(), is(1));
            DOUBLE_RELATIVE_ASSERTION.compareAndAssert(1d, poly.value(1d));
            DOUBLE_RELATIVE_ASSERTION.compareAndAssert(2d, poly.value(2d));
            DOUBLE_RELATIVE_ASSERTION.compareAndAssert(3d, poly.value(3d));
        }
    }

    public static class 構築のテスト_1次関数の近似_スケールあり {

        private RemezPolynomialFactory remezFactory;

        @Before
        public void before_remezを作成() {
            TargetFunction targetFunction = new TargetFunction() {

                @Override
                public double value(double x) {
                    return x;
                }

                @Override
                public double scale(double x) {
                    return Math.abs(x);
                }

                @Override
                public DoubleFiniteClosedInterval interval() {
                    return DoubleFiniteClosedInterval.from(1, 3);
                }
            };
            remezFactory = new RemezPolynomialFactory(targetFunction);
        }

        @Test
        public void test_定数関数() throws Exception {
            PolynomialFunction poly = remezFactory.create(new double[] { 1, 3 });

            assertThat(poly.degree(), is(0));
            DOUBLE_RELATIVE_ASSERTION.compareAndAssert(1.5d, poly.value(1d));
            DOUBLE_RELATIVE_ASSERTION.compareAndAssert(1.5d, poly.value(3d));
        }

        @Test
        public void test_1次関数() throws Exception {
            PolynomialFunction poly = remezFactory.create(new double[] { 1, 2, 3 });

            assertThat(poly.degree(), is(1));
            DOUBLE_RELATIVE_ASSERTION.compareAndAssert(1d, poly.value(1d));
            DOUBLE_RELATIVE_ASSERTION.compareAndAssert(2d, poly.value(2d));
            DOUBLE_RELATIVE_ASSERTION.compareAndAssert(3d, poly.value(3d));
        }
    }
}
