package matsu.num.approximation.polynomial;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.*;

import java.util.function.UnaryOperator;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;

import matsu.num.approximation.DoubleLike;
import matsu.num.approximation.PseudoRealNumber;

/**
 * {@link NewtonPolynomial} クラスのテスト.
 */
@RunWith(Enclosed.class)
final class NewtonPolynomialTest {

    public static final Class<?> TEST_CLASS = NewtonPolynomial.class;

    private static final PseudoRealNumber.Provider<DoubleLike> PROVIDER = DoubleLike.elementProvider();

    @RunWith(Theories.class)
    public static class 多項式のテスト_サイズ1 {

        private static final DoubleLike[] NODES = {
                PROVIDER.fromDoubleValue(2d)
        };

        private static final UnaryOperator<DoubleLike> FUNCTION =
                x -> {
                    // y = 3
                    return PROVIDER.fromDoubleValue(3d);
                };

        private static NewtonPolynomial<DoubleLike> polynomial;

        @DataPoints
        public static final double[][] DATA = {
                { 1, 3 },
                { 4, 3 }
        };

        private static final double[] COEFF_EXPECTED = { 3d };

        @BeforeClass
        public static void before_多項式の作成() {
            polynomial = NewtonPolynomial.from(NODES, FUNCTION, PROVIDER);
        }

        @Theory
        public void test_多項式の値のテスト(double[] pair) {
            assertThat(
                    polynomial.value(PROVIDER.fromDoubleValue(pair[0])).asDouble(),
                    is(pair[1]));
        }

        @Test
        public void test_多項式の係数のテスト() {
            DoubleLike[] coeffResult = polynomial.coefficient();
            assertThat(coeffResult.length, is(COEFF_EXPECTED.length));
            for (int i = 0; i < coeffResult.length; i++) {
                assertThat(coeffResult[i].asDouble(), is(COEFF_EXPECTED[i]));
            }
        }
    }

    @RunWith(Theories.class)
    public static class 多項式のテスト_サイズ2 {

        private static final DoubleLike[] NODES = {
                PROVIDER.fromDoubleValue(2d),
                PROVIDER.fromDoubleValue(4d)
        };

        private static final UnaryOperator<DoubleLike> FUNCTION =
                x -> {
                    double dblX = x.asDouble();

                    // y = 5 + 3x
                    return PROVIDER.fromDoubleValue(5 + 3 * dblX);
                };

        private static NewtonPolynomial<DoubleLike> polynomial;

        @DataPoints
        public static final double[][] DATA = {
                { 1, 8 },
                { -1, 2 }
        };

        private static final double[] COEFF_EXPECTED = { 5d, 3d };

        @BeforeClass
        public static void before_多項式の作成() {
            polynomial = NewtonPolynomial.from(NODES, FUNCTION, PROVIDER);
        }

        @Theory
        public void test_多項式の値のテスト(double[] pair) {
            assertThat(
                    polynomial.value(PROVIDER.fromDoubleValue(pair[0])).asDouble(),
                    is(pair[1]));
        }

        @Test
        public void test_多項式の係数のテスト() {
            DoubleLike[] coeffResult = polynomial.coefficient();
            assertThat(coeffResult.length, is(COEFF_EXPECTED.length));
            for (int i = 0; i < coeffResult.length; i++) {
                assertThat(coeffResult[i].asDouble(), is(COEFF_EXPECTED[i]));
            }
        }
    }

    @RunWith(Theories.class)
    public static class 多項式のテスト_サイズ3 {

        private static final DoubleLike[] NODES = {
                PROVIDER.fromDoubleValue(1d),
                PROVIDER.fromDoubleValue(-1d),
                PROVIDER.fromDoubleValue(0d)
        };

        private static final UnaryOperator<DoubleLike> FUNCTION =
                x -> {
                    double dblX = x.asDouble();

                    // y = 2 + x - x^2
                    return PROVIDER.fromDoubleValue(2 + dblX - dblX * dblX);
                };

        private static NewtonPolynomial<DoubleLike> polynomial;

        @DataPoints
        public static final double[][] DATA = {
                { 0, 2 },
                { -1, 0 },
                { 1, 2 },
        };

        private static final double[] COEFF_EXPECTED = { 2d, 1d, -1d };

        @BeforeClass
        public static void before_多項式の作成() {
            polynomial = NewtonPolynomial.from(NODES, FUNCTION, PROVIDER);
        }

        @Theory
        public void test_多項式の値のテスト(double[] pair) {
            assertThat(
                    polynomial.value(PROVIDER.fromDoubleValue(pair[0])).asDouble(),
                    is(pair[1]));
        }

        @Test
        public void test_多項式の係数のテスト() {
            DoubleLike[] coeffResult = polynomial.coefficient();
            assertThat(coeffResult.length, is(COEFF_EXPECTED.length));
            for (int i = 0; i < coeffResult.length; i++) {
                assertThat(coeffResult[i].asDouble(), is(COEFF_EXPECTED[i]));
            }
        }
    }

    @RunWith(Theories.class)
    public static class 多項式のテスト_サイズ4 {

        private static final DoubleLike[] NODES = {
                PROVIDER.fromDoubleValue(-1d),
                PROVIDER.fromDoubleValue(3d),
                PROVIDER.fromDoubleValue(2d),
                PROVIDER.fromDoubleValue(1d)
        };

        private static final UnaryOperator<DoubleLike> FUNCTION =
                x -> {
                    double dblX = x.asDouble();

                    // y = x - x^2 + 2x^3
                    return PROVIDER.fromDoubleValue(dblX - dblX * dblX + 2 * dblX * dblX * dblX);
                };

        private static NewtonPolynomial<DoubleLike> polynomial;

        @DataPoints
        public static final double[][] DATA = {
                { 0, 0 },
                { -1, -4 },
                { 1, 2 },
                { 2, 14 }
        };

        private static final double[] COEFF_EXPECTED = { 0, 1, -1, 2 };

        @BeforeClass
        public static void before_多項式の作成() {
            polynomial = NewtonPolynomial.from(NODES, FUNCTION, PROVIDER);
        }

        @Theory
        public void test_多項式の値のテスト(double[] pair) {
            assertThat(
                    polynomial.value(PROVIDER.fromDoubleValue(pair[0])).asDouble(),
                    is(pair[1]));
        }

        @Test
        public void test_多項式の係数のテスト() {
            DoubleLike[] coeffResult = polynomial.coefficient();
            assertThat(coeffResult.length, is(COEFF_EXPECTED.length));
            for (int i = 0; i < coeffResult.length; i++) {
                assertThat(coeffResult[i].asDouble(), is(COEFF_EXPECTED[i]));
            }
        }
    }
}
