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
 * {@link MinimaxPolynomialApproxCalculation} クラスのテスト.
 * 
 * @author Matsuura Y.
 */
@RunWith(Enclosed.class)
final class MinimaxPolynomialApproxCalculationTest {

    public static final Class<?> TEST_CLASS = MinimaxPolynomialApproxCalculation.class;

    private static final DoubleRelativeAssertion DOUBLE_RELATIVE_ASSERTION =
            new DoubleRelativeAssertion(1E-12);

    public static class 構築のテスト_定数関数の近似 {

        private TargetFunction target;

        @Before
        public void before_ターゲットを作成() {
            //定数関数
            target = new TargetFunction() {

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
        }

        @Test
        public void test_定数関数で近似() throws Exception {
            MinimaxPolynomialApproxCalculation calc = new MinimaxPolynomialApproxCalculation(target, 0);
            calc.calculate();
            PolynomialFunction poly = calc.getResult();

            assertThat(poly.degree(), is(0));
            DOUBLE_RELATIVE_ASSERTION.compareAndAssert(2d, poly.value(1d));
            DOUBLE_RELATIVE_ASSERTION.compareAndAssert(2d, poly.value(3d));
        }

        @Test
        public void test_1次関数で近似() throws Exception {
            MinimaxPolynomialApproxCalculation calc = new MinimaxPolynomialApproxCalculation(target, 1);
            calc.calculate();
            PolynomialFunction poly = calc.getResult();

            assertThat(poly.degree(), is(1));
            DOUBLE_RELATIVE_ASSERTION.compareAndAssert(2d, poly.value(1d));
            DOUBLE_RELATIVE_ASSERTION.compareAndAssert(2d, poly.value(2d));
            DOUBLE_RELATIVE_ASSERTION.compareAndAssert(2d, poly.value(3d));
        }

        @Test
        public void test_2次関数で近似() throws Exception {
            MinimaxPolynomialApproxCalculation calc = new MinimaxPolynomialApproxCalculation(target, 2);
            calc.calculate();
            PolynomialFunction poly = calc.getResult();

            assertThat(poly.degree(), is(2));
            DOUBLE_RELATIVE_ASSERTION.compareAndAssert(2d, poly.value(1d));
            DOUBLE_RELATIVE_ASSERTION.compareAndAssert(2d, poly.value(2d));
            DOUBLE_RELATIVE_ASSERTION.compareAndAssert(2d, poly.value(3d));
        }
    }

    public static class 構築のテスト_1次関数の近似 {

        private TargetFunction target;

        @Before
        public void before_ターゲットを作成() {
            //定数関数
            target = new TargetFunction() {

                @Override
                public double value(double x) {
                    return x;
                }

                @Override
                public double scale(double x) {
                    return x;
                }

                @Override
                public DoubleFiniteClosedInterval interval() {
                    return DoubleFiniteClosedInterval.from(1, 3);
                }
            };
        }

        @Test
        public void test_定数関数で近似() throws Exception {
            MinimaxPolynomialApproxCalculation calc = new MinimaxPolynomialApproxCalculation(target, 0);
            calc.calculate();
            PolynomialFunction poly = calc.getResult();

            assertThat(poly.degree(), is(0));
            DOUBLE_RELATIVE_ASSERTION.compareAndAssert(1.5d, poly.value(1d));
            DOUBLE_RELATIVE_ASSERTION.compareAndAssert(1.5d, poly.value(3d));
        }

        @Test
        public void test_1次関数で近似() throws Exception {
            MinimaxPolynomialApproxCalculation calc = new MinimaxPolynomialApproxCalculation(target, 1);
            calc.calculate();
            PolynomialFunction poly = calc.getResult();

            assertThat(poly.degree(), is(1));
            DOUBLE_RELATIVE_ASSERTION.compareAndAssert(1d, poly.value(1d));
            DOUBLE_RELATIVE_ASSERTION.compareAndAssert(2d, poly.value(2d));
            DOUBLE_RELATIVE_ASSERTION.compareAndAssert(3d, poly.value(3d));
        }

        @Test
        public void test_2次関数で近似() throws Exception {
            MinimaxPolynomialApproxCalculation calc = new MinimaxPolynomialApproxCalculation(target, 2);
            calc.calculate();
            PolynomialFunction poly = calc.getResult();

            assertThat(poly.degree(), is(2));
            DOUBLE_RELATIVE_ASSERTION.compareAndAssert(1d, poly.value(1d));
            DOUBLE_RELATIVE_ASSERTION.compareAndAssert(2d, poly.value(2d));
            DOUBLE_RELATIVE_ASSERTION.compareAndAssert(3d, poly.value(3d));
        }
    }
}
