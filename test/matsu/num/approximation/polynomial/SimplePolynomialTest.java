package matsu.num.approximation.polynomial;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.*;

import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

import matsu.num.approximation.DoubleRelativeAssertion;

/**
 * {@link SimplePolynomial} クラスのテスト.
 */
@SuppressWarnings("removal")
@RunWith(Enclosed.class)
final class SimplePolynomialTest {

    public static final Class<?> TEST_CLASS = SimplePolynomial.class;

    private static final DoubleRelativeAssertion DOUBLE_RELATIVE_ASSERTION =
            new DoubleRelativeAssertion(1E-12);

    public static class 多項式の評価値のテスト {
        @Test
        public void test_定数関数() {
            /*
             * p(x) = 2
             */
            double[] coeff = { 2d };
            DoublePolynomial function = SimplePolynomial.of(coeff);

            assertThat(function.degree(), is(0));
            DOUBLE_RELATIVE_ASSERTION.compareAndAssert(2d, function.value(1d));
            DOUBLE_RELATIVE_ASSERTION.compareAndAssert(2d, function.value(2d));
        }

        @Test
        public void test_1次関数() {
            /*
             * p(x) = 4 + 3x
             */
            double[] coeff = { 4d, 3d };
            DoublePolynomial function = SimplePolynomial.of(coeff);

            assertThat(function.degree(), is(1));
            DOUBLE_RELATIVE_ASSERTION.compareAndAssert(7d, function.value(1d));
            DOUBLE_RELATIVE_ASSERTION.compareAndAssert(10d, function.value(2d));
        }

        @Test
        public void test_2次関数() {
            /*
             * p(x) = 4 + 3x + 5x^2
             */
            double[] coeff = { 4d, 3d, 5d };
            DoublePolynomial function = SimplePolynomial.of(coeff);

            assertThat(function.degree(), is(2));
            DOUBLE_RELATIVE_ASSERTION.compareAndAssert(12d, function.value(1d));
            DOUBLE_RELATIVE_ASSERTION.compareAndAssert(30d, function.value(2d));
        }
    }
}
