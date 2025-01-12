/*
 * Copyright © 2024 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */
package matsu.num.approximation.polynomial;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

import matsu.num.approximation.DoubleApproxTarget;
import matsu.num.approximation.DoubleFiniteClosedInterval;
import matsu.num.approximation.DoubleRelativeAssertion;

/**
 * {@link DoubleApproxCalculationByRemezMinimax} クラスのテスト.
 */
@RunWith(Enclosed.class)
final class DoubleApproxCalculationByRemezMinimaxTest {

    public static final Class<?> TEST_CLASS = DoubleApproxCalculationByRemezMinimax.class;

    private static final DoubleRelativeAssertion DOUBLE_RELATIVE_ASSERTION =
            new DoubleRelativeAssertion(1E-12);

    public static class 構築のテスト_定数関数の近似 {

        private DoubleApproxTarget target;

        @Before
        public void before_ターゲットを作成() {
            //定数関数
            target = new DoubleApproxTarget() {

                @Override
                protected double calcValue(double x) {
                    return 2d;
                }

                @Override
                protected double calcScale(double x) {
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
            DoubleApproxCalculationByRemezMinimax calc = new DoubleApproxCalculationByRemezMinimax(target, 0);
            calc.calculate();
            DoublePolynomial poly = calc.getResult();

            assertThat(poly.degree(), is(0));
            DOUBLE_RELATIVE_ASSERTION.compareAndAssert(2d, poly.value(1d));
            DOUBLE_RELATIVE_ASSERTION.compareAndAssert(2d, poly.value(3d));
        }

        @Test
        public void test_1次関数で近似() throws Exception {
            DoubleApproxCalculationByRemezMinimax calc = new DoubleApproxCalculationByRemezMinimax(target, 1);
            calc.calculate();
            DoublePolynomial poly = calc.getResult();

            assertThat(poly.degree(), is(1));
            DOUBLE_RELATIVE_ASSERTION.compareAndAssert(2d, poly.value(1d));
            DOUBLE_RELATIVE_ASSERTION.compareAndAssert(2d, poly.value(2d));
            DOUBLE_RELATIVE_ASSERTION.compareAndAssert(2d, poly.value(3d));
        }

        @Test
        public void test_2次関数で近似() throws Exception {
            DoubleApproxCalculationByRemezMinimax calc = new DoubleApproxCalculationByRemezMinimax(target, 2);
            calc.calculate();
            DoublePolynomial poly = calc.getResult();

            assertThat(poly.degree(), is(2));
            DOUBLE_RELATIVE_ASSERTION.compareAndAssert(2d, poly.value(1d));
            DOUBLE_RELATIVE_ASSERTION.compareAndAssert(2d, poly.value(2d));
            DOUBLE_RELATIVE_ASSERTION.compareAndAssert(2d, poly.value(3d));
        }
    }

    public static class 構築のテスト_1次関数の近似 {

        private DoubleApproxTarget target;

        @Before
        public void before_ターゲットを作成() {
            //定数関数
            target = new DoubleApproxTarget() {

                @Override
                protected double calcValue(double x) {
                    return x;
                }

                @Override
                protected double calcScale(double x) {
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
            DoubleApproxCalculationByRemezMinimax calc = new DoubleApproxCalculationByRemezMinimax(target, 0);
            calc.calculate();
            DoublePolynomial poly = calc.getResult();

            assertThat(poly.degree(), is(0));
            DOUBLE_RELATIVE_ASSERTION.compareAndAssert(1.5d, poly.value(1d));
            DOUBLE_RELATIVE_ASSERTION.compareAndAssert(1.5d, poly.value(3d));
        }

        @Test
        public void test_1次関数で近似() throws Exception {
            DoubleApproxCalculationByRemezMinimax calc = new DoubleApproxCalculationByRemezMinimax(target, 1);
            calc.calculate();
            DoublePolynomial poly = calc.getResult();

            assertThat(poly.degree(), is(1));
            DOUBLE_RELATIVE_ASSERTION.compareAndAssert(1d, poly.value(1d));
            DOUBLE_RELATIVE_ASSERTION.compareAndAssert(2d, poly.value(2d));
            DOUBLE_RELATIVE_ASSERTION.compareAndAssert(3d, poly.value(3d));
        }

        @Test
        public void test_2次関数で近似() throws Exception {
            DoubleApproxCalculationByRemezMinimax calc = new DoubleApproxCalculationByRemezMinimax(target, 2);
            calc.calculate();
            DoublePolynomial poly = calc.getResult();

            assertThat(poly.degree(), is(2));
            DOUBLE_RELATIVE_ASSERTION.compareAndAssert(1d, poly.value(1d));
            DOUBLE_RELATIVE_ASSERTION.compareAndAssert(2d, poly.value(2d));
            DOUBLE_RELATIVE_ASSERTION.compareAndAssert(3d, poly.value(3d));
        }
    }
}
