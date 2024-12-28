package matsu.num.approximation.polynomial;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

import matsu.num.approximation.ApproxTarget;
import matsu.num.approximation.DoubleLike;
import matsu.num.approximation.DoubleRelativeAssertion;
import matsu.num.approximation.FiniteClosedInterval;
import matsu.num.approximation.PseudoRealNumber;
import matsu.num.approximation.PseudoRealNumber.Provider;

/**
 * {@link ApproxCalculationByRemezMinimax} クラスのテスト.
 */
@RunWith(Enclosed.class)
final class ApproxCalculationByRemezMinimaxTest {

    public static final Class<?> TEST_CLASS = ApproxCalculationByRemezMinimax.class;

    private static final PseudoRealNumber.Provider<DoubleLike> PROVIDER = DoubleLike.elementProvider();

    private static final DoubleRelativeAssertion DOUBLE_RELATIVE_ASSERTION =
            new DoubleRelativeAssertion(1E-12);

    public static class 構築のテスト_定数関数の近似 {

        private ApproxTarget<DoubleLike> target;

        @Before
        public void before_ターゲットを作成() {
            //定数関数
            target = new ApproxTarget<>() {

                private final DoubleLike value = PROVIDER.fromDoubleValue(2d);
                private final DoubleLike scale = PROVIDER.fromDoubleValue(2d);
                private final FiniteClosedInterval<DoubleLike> interval =
                        FiniteClosedInterval.from(
                                PROVIDER.fromDoubleValue(1d), PROVIDER.fromDoubleValue(3d));

                @Override
                protected DoubleLike calcValue(DoubleLike x) {
                    return value;
                }

                @Override
                protected DoubleLike calcScale(DoubleLike x) {
                    return scale;
                }

                @Override
                public FiniteClosedInterval<DoubleLike> interval() {
                    return interval;
                }

                @Override
                public Provider<DoubleLike> elementProvider() {
                    return PROVIDER;
                }
            };
        }

        @Test
        public void test_定数関数で近似() throws Exception {
            ApproxCalculationByRemezMinimax<DoubleLike> calc =
                    new ApproxCalculationByRemezMinimax<>(target, 0);
            calc.calculate();
            Polynomial<DoubleLike> poly = calc.getResult();

            assertThat(poly.degree(), is(0));
            DOUBLE_RELATIVE_ASSERTION.compareAndAssert(
                    2d,
                    poly.value(PROVIDER.fromDoubleValue(1d)).asDouble());
            DOUBLE_RELATIVE_ASSERTION.compareAndAssert(
                    2d,
                    poly.value(PROVIDER.fromDoubleValue(3d)).asDouble());
        }

        @Test
        public void test_1次関数で近似() throws Exception {
            ApproxCalculationByRemezMinimax<DoubleLike> calc =
                    new ApproxCalculationByRemezMinimax<>(target, 1);
            calc.calculate();
            Polynomial<DoubleLike> poly = calc.getResult();

            assertThat(poly.degree(), is(1));
            DOUBLE_RELATIVE_ASSERTION.compareAndAssert(
                    2d,
                    poly.value(PROVIDER.fromDoubleValue(1d)).asDouble());
            DOUBLE_RELATIVE_ASSERTION.compareAndAssert(
                    2d,
                    poly.value(PROVIDER.fromDoubleValue(2d)).asDouble());
            DOUBLE_RELATIVE_ASSERTION.compareAndAssert(
                    2d,
                    poly.value(PROVIDER.fromDoubleValue(3d)).asDouble());
        }

        @Test
        public void test_2次関数で近似() throws Exception {
            ApproxCalculationByRemezMinimax<DoubleLike> calc =
                    new ApproxCalculationByRemezMinimax<>(target, 2);
            calc.calculate();
            Polynomial<DoubleLike> poly = calc.getResult();

            assertThat(poly.degree(), is(2));
            DOUBLE_RELATIVE_ASSERTION.compareAndAssert(
                    2d,
                    poly.value(PROVIDER.fromDoubleValue(1d)).asDouble());
            DOUBLE_RELATIVE_ASSERTION.compareAndAssert(
                    2d,
                    poly.value(PROVIDER.fromDoubleValue(2d)).asDouble());
            DOUBLE_RELATIVE_ASSERTION.compareAndAssert(
                    2d,
                    poly.value(PROVIDER.fromDoubleValue(3d)).asDouble());
        }
    }

    public static class 構築のテスト_1次関数の近似 {

        private ApproxTarget<DoubleLike> target;

        @Before
        public void before_ターゲットを作成() {
            //定数関数
            target = new ApproxTarget<>() {

                private final FiniteClosedInterval<DoubleLike> interval =
                        FiniteClosedInterval.from(
                                PROVIDER.fromDoubleValue(1d), PROVIDER.fromDoubleValue(3d));

                @Override
                protected DoubleLike calcValue(DoubleLike x) {
                    return x;
                }

                @Override
                protected DoubleLike calcScale(DoubleLike x) {
                    return x;
                }

                @Override
                public FiniteClosedInterval<DoubleLike> interval() {
                    return interval;
                }

                @Override
                public Provider<DoubleLike> elementProvider() {
                    return PROVIDER;
                }
            };
        }

        @Test
        public void test_定数関数で近似() throws Exception {
            ApproxCalculationByRemezMinimax<DoubleLike> calc =
                    new ApproxCalculationByRemezMinimax<>(target, 0);
            calc.calculate();
            Polynomial<DoubleLike> poly = calc.getResult();

            assertThat(poly.degree(), is(0));
            DOUBLE_RELATIVE_ASSERTION.compareAndAssert(
                    1.5d,
                    poly.value(PROVIDER.fromDoubleValue(1d)).asDouble());
            DOUBLE_RELATIVE_ASSERTION.compareAndAssert(
                    1.5d,
                    poly.value(PROVIDER.fromDoubleValue(3d)).asDouble());
        }

        @Test
        public void test_1次関数で近似() throws Exception {
            ApproxCalculationByRemezMinimax<DoubleLike> calc =
                    new ApproxCalculationByRemezMinimax<>(target, 1);
            calc.calculate();
            Polynomial<DoubleLike> poly = calc.getResult();

            assertThat(poly.degree(), is(1));
            DOUBLE_RELATIVE_ASSERTION.compareAndAssert(
                    1d,
                    poly.value(PROVIDER.fromDoubleValue(1d)).asDouble());
            DOUBLE_RELATIVE_ASSERTION.compareAndAssert(
                    2d,
                    poly.value(PROVIDER.fromDoubleValue(2d)).asDouble());
            DOUBLE_RELATIVE_ASSERTION.compareAndAssert(
                    3d,
                    poly.value(PROVIDER.fromDoubleValue(3d)).asDouble());
        }

        @Test
        public void test_2次関数で近似() throws Exception {
            ApproxCalculationByRemezMinimax<DoubleLike> calc =
                    new ApproxCalculationByRemezMinimax<>(target, 2);
            calc.calculate();
            Polynomial<DoubleLike> poly = calc.getResult();

            assertThat(poly.degree(), is(2));
            DOUBLE_RELATIVE_ASSERTION.compareAndAssert(
                    1d,
                    poly.value(PROVIDER.fromDoubleValue(1d)).asDouble());
            DOUBLE_RELATIVE_ASSERTION.compareAndAssert(
                    2d,
                    poly.value(PROVIDER.fromDoubleValue(2d)).asDouble());
            DOUBLE_RELATIVE_ASSERTION.compareAndAssert(
                    3d,
                    poly.value(PROVIDER.fromDoubleValue(3d)).asDouble());
        }
    }
}
