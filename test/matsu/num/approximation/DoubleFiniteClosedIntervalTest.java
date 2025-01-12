/*
 * Copyright © 2024 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */
package matsu.num.approximation;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.*;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;

/**
 * {@link DoubleFiniteClosedInterval} クラスのテスト.
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

    @RunWith(Theories.class)
    public static class 等価性に関するテスト_等価なパターン {

        private static DoubleFiniteClosedInterval reference =
                DoubleFiniteClosedInterval.from(1d, 2d);

        @DataPoints
        public static DoubleFiniteClosedInterval[] data;

        @BeforeClass
        public static void before_データの準備() {
            data = new DoubleFiniteClosedInterval[] {
                    DoubleFiniteClosedInterval.from(1d, 2d),
                    DoubleFiniteClosedInterval.from(2d, 1d)
            };
        }

        @Theory
        public void test_equalsとhashCode(DoubleFiniteClosedInterval obj) {
            assertThat(obj, is(reference));
            assertThat(obj.hashCode(), is(reference.hashCode()));
        }
    }

    @RunWith(Theories.class)
    public static class 等価性に関するテスト_等価でないパターン {

        private static DoubleFiniteClosedInterval reference =
                DoubleFiniteClosedInterval.from(1d, 2d);

        @DataPoints
        public static DoubleFiniteClosedInterval[] data;

        @BeforeClass
        public static void before_データの準備() {
            data = new DoubleFiniteClosedInterval[] {
                    DoubleFiniteClosedInterval.from(1d, 3d),
                    DoubleFiniteClosedInterval.from(3d, 1d),
                    DoubleFiniteClosedInterval.from(0d, 2d),
                    DoubleFiniteClosedInterval.from(2d, 0d)
            };
        }

        @Theory
        public void test_equals(DoubleFiniteClosedInterval obj) {
            assertThat(obj, is(not(reference)));
        }
    }

    public static class 区間内判定に関する {

        private static DoubleFiniteClosedInterval reference =
                DoubleFiniteClosedInterval.from(2d, 1d);

        @Test
        public void test_下側境界は含む() {
            assertThat(reference.accepts(1d), is(true));
        }

        @Test
        public void test_上側境界は含む() {
            assertThat(reference.accepts(2d), is(true));
        }

        @Test
        public void test_内部は含む() {
            assertThat(reference.accepts(1.5d), is(true));
        }

        @Test
        public void test_外部は含まない() {
            assertThat(reference.accepts(0d), is(false));
            assertThat(reference.accepts(3d), is(false));
        }

        @Test
        public void test_NaNは含まない() {
            assertThat(reference.accepts(Double.NaN), is(false));
        }
    }

    public static class toString表示 {

        @Test
        public void test_toString() {
            System.out.println(TEST_CLASS.getName());
            System.out.println(DoubleFiniteClosedInterval.from(0d, 1d));
            System.out.println();
        }
    }
}
