/*
 * Copyright © 2024 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */
package matsu.num.approximation;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

import org.junit.Test;
import org.junit.Test.None;
import org.junit.experimental.runners.Enclosed;
import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;

import matsu.num.approximation.PseudoRealNumber.TypeProvider;

/**
 * {@link DoubleLikeTest} クラスのテスト.
 */
@RunWith(Enclosed.class)
final class DoubleLikeTest {

    public static final Class<?> TEST_CLASS = DoubleLike.class;

    private static final TypeProvider<DoubleLike> ELEMENT_PROVIDER = DoubleLike.elementTypeProvider();

    public static class 生成のテスト {

        @Test(expected = None.class)
        public void test_有限値は生成可能() {
            ELEMENT_PROVIDER.fromDoubleValue(0d);
            ELEMENT_PROVIDER.fromDoubleValue(-1d);
            ELEMENT_PROVIDER.fromDoubleValue(Double.MAX_VALUE);
        }

        @Test(expected = IllegalArgumentException.class)
        public void test_正の無限大はIAEx() {
            ELEMENT_PROVIDER.fromDoubleValue(Double.POSITIVE_INFINITY);
        }

        @Test(expected = IllegalArgumentException.class)
        public void test_負の無限大はIAEx() {
            ELEMENT_PROVIDER.fromDoubleValue(Double.NEGATIVE_INFINITY);
        }

        @Test(expected = IllegalArgumentException.class)
        public void test_NaNはIAEx() {
            ELEMENT_PROVIDER.fromDoubleValue(Double.NaN);
        }

        @Test
        public void test_配列の型を検証() {
            assertThat(
                    ELEMENT_PROVIDER.createArray(0).getClass().getComponentType(),
                    is(DoubleLike.class));
        }
    }

    public static class 等価性のテスト {

        @Test
        public void test_同一値から生成された値は等価() {
            assertThat(
                    ELEMENT_PROVIDER.fromDoubleValue(1d),
                    is(ELEMENT_PROVIDER.fromDoubleValue(1d)));
            assertThat(
                    ELEMENT_PROVIDER.fromDoubleValue(1d).hashCode(),
                    is(ELEMENT_PROVIDER.fromDoubleValue(1d).hashCode()));
        }

        @Test
        public void test_異なる値から生成された値は等価でない() {
            assertThat(
                    ELEMENT_PROVIDER.fromDoubleValue(1d),
                    is(not(ELEMENT_PROVIDER.fromDoubleValue(2d))));
        }

        @Test
        public void test_0とm0は等価() {
            assertThat(
                    ELEMENT_PROVIDER.fromDoubleValue(-0d),
                    is(ELEMENT_PROVIDER.fromDoubleValue(0d)));
        }

        @Test
        public void test_nullとの比較() {
            assertThat(
                    ELEMENT_PROVIDER.fromDoubleValue(1d).equals(null),
                    is(false));
        }
    }

    public static class 比較のテスト {

        @Test
        public void test_1_equals_1() {
            assertThat(
                    ELEMENT_PROVIDER.fromDoubleValue(1d),
                    is(lessThanOrEqualTo(ELEMENT_PROVIDER.fromDoubleValue(1d))));
            assertThat(
                    ELEMENT_PROVIDER.fromDoubleValue(1d),
                    is(greaterThanOrEqualTo(ELEMENT_PROVIDER.fromDoubleValue(1d))));
        }

        @Test
        public void test_1_less_than_2() {
            assertThat(
                    ELEMENT_PROVIDER.fromDoubleValue(1d),
                    is(lessThan(ELEMENT_PROVIDER.fromDoubleValue(2d))));
        }

        @Test
        public void test_0_equals_m0() {
            assertThat(
                    ELEMENT_PROVIDER.fromDoubleValue(-0d),
                    is(lessThanOrEqualTo(ELEMENT_PROVIDER.fromDoubleValue(0d))));
            assertThat(
                    ELEMENT_PROVIDER.fromDoubleValue(-0d),
                    is(greaterThanOrEqualTo(ELEMENT_PROVIDER.fromDoubleValue(0d))));
        }
    }

    @RunWith(Theories.class)
    public static class 二項演算のテスト {

        @DataPoints
        public static double[][] valueSet = {
                { 1d, 2d },
                { 3d, 4d },
                { 2d, 1d },
                { 5d, 0.5d },
        };

        @Theory
        public void test_和のテスト(double[] values) {
            final double v1 = values[0];
            final double v2 = values[1];

            assertThat(
                    ELEMENT_PROVIDER.fromDoubleValue(v1).plus(v2),
                    is(ELEMENT_PROVIDER.fromDoubleValue(v1 + v2)));
        }

        @Theory
        public void test_差のテスト(double[] values) {
            final double v1 = values[0];
            final double v2 = values[1];

            assertThat(
                    ELEMENT_PROVIDER.fromDoubleValue(v1).minus(v2),
                    is(ELEMENT_PROVIDER.fromDoubleValue(v1 - v2)));
        }

        @Theory
        public void test_積のテスト(double[] values) {
            final double v1 = values[0];
            final double v2 = values[1];

            assertThat(
                    ELEMENT_PROVIDER.fromDoubleValue(v1).times(v2),
                    is(ELEMENT_PROVIDER.fromDoubleValue(v1 * v2)));
        }

        @Theory
        public void test_商のテスト(double[] values) {
            final double v1 = values[0];
            final double v2 = values[1];

            assertThat(
                    ELEMENT_PROVIDER.fromDoubleValue(v1).dividedBy(v2),
                    is(ELEMENT_PROVIDER.fromDoubleValue(v1 / v2)));
        }
    }

    public static class 二項演算のテスト_特殊値 {

        @Test(expected = ArithmeticException.class)
        public void test_0割りで例外AriEx() {
            ELEMENT_PROVIDER.fromDoubleValue(1d).dividedBy(0d);
        }

        @Test(expected = ArithmeticException.class)
        public void test_0割る0で例外AriEx() {
            ELEMENT_PROVIDER.fromDoubleValue(0d).dividedBy(0d);
        }
    }

    public static class toString表示 {

        @Test
        public void test_toString() {
            System.out.println(TEST_CLASS.getName());
            System.out.println(ELEMENT_PROVIDER.fromDoubleValue(1d));
            System.out.println();
        }
    }
}
