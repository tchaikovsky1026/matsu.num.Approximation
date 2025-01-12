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
 * {@link RemezTypeDoublePolynomialFactory} クラスのテスト.
 */
@RunWith(Enclosed.class)
final class RemezTypeDoublePolynomialFactoryTest {

    public static final Class<?> TEST_CLASS = RemezTypeDoublePolynomialFactory.class;

    private static final DoubleRelativeAssertion DOUBLE_RELATIVE_ASSERTION =
            new DoubleRelativeAssertion(1E-12);

    public static class 構築のテスト_定数関数の近似 {

        private RemezTypeDoublePolynomialFactory remezFactory;

        @Before
        public void before_remezを作成() {
            //定数関数
            DoubleApproxTarget targetFunction = new DoubleApproxTarget() {

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
            remezFactory = new RemezTypeDoublePolynomialFactory(targetFunction);
        }

        @Test
        public void test_定数関数() throws Exception {
            DoublePolynomial poly = remezFactory.create(new double[] { 1, 3 });

            assertThat(poly.degree(), is(0));
            DOUBLE_RELATIVE_ASSERTION.compareAndAssert(2d, poly.value(1d));
            DOUBLE_RELATIVE_ASSERTION.compareAndAssert(2d, poly.value(3d));
        }

        @Test
        public void test_1次関数() throws Exception {
            DoublePolynomial poly = remezFactory.create(new double[] { 1, 2, 3 });

            assertThat(poly.degree(), is(1));
            DOUBLE_RELATIVE_ASSERTION.compareAndAssert(2d, poly.value(1d));
            DOUBLE_RELATIVE_ASSERTION.compareAndAssert(2d, poly.value(2d));
            DOUBLE_RELATIVE_ASSERTION.compareAndAssert(2d, poly.value(3d));
        }
    }

    public static class 構築のテスト_1次関数の近似 {

        private RemezTypeDoublePolynomialFactory remezFactory;

        @Before
        public void before_remezを作成() {
            DoubleApproxTarget targetFunction = new DoubleApproxTarget() {

                @Override
                protected double calcValue(double x) {
                    return x;
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
            remezFactory = new RemezTypeDoublePolynomialFactory(targetFunction);
        }

        @Test
        public void test_定数関数() throws Exception {
            DoublePolynomial poly = remezFactory.create(new double[] { 1, 3 });

            assertThat(poly.degree(), is(0));
            DOUBLE_RELATIVE_ASSERTION.compareAndAssert(2d, poly.value(1d));
            DOUBLE_RELATIVE_ASSERTION.compareAndAssert(2d, poly.value(3d));
        }

        @Test
        public void test_1次関数() throws Exception {
            DoublePolynomial poly = remezFactory.create(new double[] { 1, 2, 3 });

            assertThat(poly.degree(), is(1));
            DOUBLE_RELATIVE_ASSERTION.compareAndAssert(1d, poly.value(1d));
            DOUBLE_RELATIVE_ASSERTION.compareAndAssert(2d, poly.value(2d));
            DOUBLE_RELATIVE_ASSERTION.compareAndAssert(3d, poly.value(3d));
        }
    }

    public static class 構築のテスト_1次関数の近似_スケールあり {

        private RemezTypeDoublePolynomialFactory remezFactory;

        @Before
        public void before_remezを作成() {
            DoubleApproxTarget targetFunction = new DoubleApproxTarget() {

                @Override
                protected double calcValue(double x) {
                    return x;
                }

                @Override
                protected double calcScale(double x) {
                    return Math.abs(x);
                }

                @Override
                public DoubleFiniteClosedInterval interval() {
                    return DoubleFiniteClosedInterval.from(1, 3);
                }
            };
            remezFactory = new RemezTypeDoublePolynomialFactory(targetFunction);
        }

        @Test
        public void test_定数関数() throws Exception {
            DoublePolynomial poly = remezFactory.create(new double[] { 1, 3 });

            assertThat(poly.degree(), is(0));
            DOUBLE_RELATIVE_ASSERTION.compareAndAssert(1.5d, poly.value(1d));
            DOUBLE_RELATIVE_ASSERTION.compareAndAssert(1.5d, poly.value(3d));
        }

        @Test
        public void test_1次関数() throws Exception {
            DoublePolynomial poly = remezFactory.create(new double[] { 1, 2, 3 });

            assertThat(poly.degree(), is(1));
            DOUBLE_RELATIVE_ASSERTION.compareAndAssert(1d, poly.value(1d));
            DOUBLE_RELATIVE_ASSERTION.compareAndAssert(2d, poly.value(2d));
            DOUBLE_RELATIVE_ASSERTION.compareAndAssert(3d, poly.value(3d));
        }
    }
}
