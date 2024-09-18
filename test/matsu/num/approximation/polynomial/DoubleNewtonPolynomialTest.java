package matsu.num.approximation.polynomial;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.*;

import java.util.function.DoubleUnaryOperator;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;

import matsu.num.approximation.component.ApproximationFailedException;

/**
 * {@link DoubleNewtonPolynomial} クラスのテスト.
 * 
 * @author Matsuura Y.
 */
@RunWith(Enclosed.class)
final class DoubleNewtonPolynomialTest {

    public static final Class<?> TEST_CLASS = DoubleNewtonPolynomial.class;

    @RunWith(Theories.class)
    public static class 多項式のテスト_サイズ1 {

        private static final double[] NODES = {
                2d
        };

        private static final DoubleUnaryOperator FUNCTION =
                x -> {
                    // y = 3
                    return 3d;
                };

        private static DoubleNewtonPolynomial polynomial;

        @DataPoints
        public static final double[][] DATA = {
                { 1, 3 },
                { 4, 3 }
        };

        private static final double[] COEFF_EXPECTED = { 3d };

        @BeforeClass
        public static void before_多項式の作成() throws ApproximationFailedException {
            double[] values = new double[NODES.length];
            for (int i = 0; i < values.length; i++) {
                values[i] = FUNCTION.applyAsDouble(NODES[i]);
            }

            polynomial = DoubleNewtonPolynomial.from(NODES, values);
        }

        @Theory
        public void test_多項式の値のテスト(double[] pair) {
            assertThat(
                    polynomial.value(pair[0]),
                    is(pair[1]));
        }

        @Test
        public void test_多項式の係数のテスト() {
            double[] coeffResult = polynomial.coefficient();
            assertThat(coeffResult.length, is(COEFF_EXPECTED.length));
            for (int i = 0; i < coeffResult.length; i++) {
                assertThat(coeffResult[i], is(COEFF_EXPECTED[i]));
            }
        }
    }

    @RunWith(Theories.class)
    public static class 多項式のテスト_サイズ2 {

        private static final double[] NODES = {
                2d,
                4d
        };

        private static final DoubleUnaryOperator FUNCTION =
                x -> {

                    // y = 5 + 3x
                    return 5 + 3 * x;
                };

        private static DoubleNewtonPolynomial polynomial;

        @DataPoints
        public static final double[][] DATA = {
                { 1, 8 },
                { -1, 2 }
        };

        private static final double[] COEFF_EXPECTED = { 5d, 3d };

        @BeforeClass
        public static void before_多項式の作成() throws ApproximationFailedException {
            double[] values = new double[NODES.length];
            for (int i = 0; i < values.length; i++) {
                values[i] = FUNCTION.applyAsDouble(NODES[i]);
            }

            polynomial = DoubleNewtonPolynomial.from(NODES, values);
        }

        @Theory
        public void test_多項式の値のテスト(double[] pair) {
            assertThat(
                    polynomial.value(pair[0]),
                    is(pair[1]));
        }

        @Test
        public void test_多項式の係数のテスト() {
            double[] coeffResult = polynomial.coefficient();
            assertThat(coeffResult.length, is(COEFF_EXPECTED.length));
            for (int i = 0; i < coeffResult.length; i++) {
                assertThat(coeffResult[i], is(COEFF_EXPECTED[i]));
            }
        }
    }

    @RunWith(Theories.class)
    public static class 多項式のテスト_サイズ3 {

        private static final double[] NODES = {
                1d,
                -1d,
                0d
        };

        private static final DoubleUnaryOperator FUNCTION =
                x -> {

                    // y = 2 + x - x^2
                    return 2 + x - x * x;
                };

        private static DoubleNewtonPolynomial polynomial;

        @DataPoints
        public static final double[][] DATA = {
                { 0, 2 },
                { -1, 0 },
                { 1, 2 },
        };

        private static final double[] COEFF_EXPECTED = { 2d, 1d, -1d };

        @BeforeClass
        public static void before_多項式の作成() throws ApproximationFailedException {
            double[] values = new double[NODES.length];
            for (int i = 0; i < values.length; i++) {
                values[i] = FUNCTION.applyAsDouble(NODES[i]);
            }

            polynomial = DoubleNewtonPolynomial.from(NODES, values);
        }

        @Theory
        public void test_多項式の値のテスト(double[] pair) {
            assertThat(
                    polynomial.value(pair[0]),
                    is(pair[1]));
        }

        @Test
        public void test_多項式の係数のテスト() {
            double[] coeffResult = polynomial.coefficient();
            assertThat(coeffResult.length, is(COEFF_EXPECTED.length));
            for (int i = 0; i < coeffResult.length; i++) {
                assertThat(coeffResult[i], is(COEFF_EXPECTED[i]));
            }
        }
    }

    @RunWith(Theories.class)
    public static class 多項式のテスト_サイズ4 {

        private static final double[] NODES = {
                -1d,
                3d,
                2d,
                1d
        };

        private static final DoubleUnaryOperator FUNCTION =
                x -> {

                    // y = x - x^2 + 2x^3
                    return x - x * x + 2 * x * x * x;
                };

        private static DoubleNewtonPolynomial polynomial;

        @DataPoints
        public static final double[][] DATA = {
                { 0, 0 },
                { -1, -4 },
                { 1, 2 },
                { 2, 14 }
        };

        private static final double[] COEFF_EXPECTED = { 0, 1, -1, 2 };

        @BeforeClass
        public static void before_多項式の作成() throws ApproximationFailedException {
            double[] values = new double[NODES.length];
            for (int i = 0; i < values.length; i++) {
                values[i] = FUNCTION.applyAsDouble(NODES[i]);
            }

            polynomial = DoubleNewtonPolynomial.from(NODES, values);
        }

        @Theory
        public void test_多項式の値のテスト(double[] pair) {
            assertThat(
                    polynomial.value(pair[0]),
                    is(pair[1]));
        }

        @Test
        public void test_多項式の係数のテスト() {
            double[] coeffResult = polynomial.coefficient();
            assertThat(coeffResult.length, is(COEFF_EXPECTED.length));
            for (int i = 0; i < coeffResult.length; i++) {
                assertThat(coeffResult[i], is(COEFF_EXPECTED[i]));
            }
        }
    }
}
