package matsu.num.approximation.component;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;

import matsu.num.approximation.DoubleRelativeAssertion;
import matsu.num.approximation.PolynomialFunction;

/**
 * {@link NewtonPolynomial} クラスのテスト.
 * 
 * @author Matsuura Y.
 */
@RunWith(Enclosed.class)
final class NewtonPolynomialTest {

    public static final Class<?> TEST_CLASS = NewtonPolynomial.class;

    private static final DoubleRelativeAssertion DOUBLE_RELATIVE_ASSERTION =
            new DoubleRelativeAssertion(1E-12);

    public static class 生成の例外テスト {

        @Test(expected = IllegalArgumentException.class)
        public void test_サイズ0は受け入れられない() {
            NewtonPolynomial.from(new double[0], new double[0]);
        }

        @Test(expected = IllegalArgumentException.class)
        public void test_サイズが異なると受け入れられない() {
            NewtonPolynomial.from(new double[1], new double[0]);
        }
    }

    @RunWith(Theories.class)
    public static class 多項式のテスト {

        private static final double[][] nodes = {
                { 0d },
                { 0d, 2d },
                { -1d, 2d, 3d },
                { -1d, -2d, 4d, 3d }
        };
        private static final double[][] coeffCandidates = {
                { 2d },
                { 1d, 2d },
                { -3d, 2d, 3d },
                { -1d, 1d, 3d, -2d }
        };

        @DataPoints
        public static List<NodeCoeffPair> pairs;

        @BeforeClass
        public static void before_ペアの作成() {
            pairs = new ArrayList<>();
            for (double[] node : nodes) {
                for (double[] coeffCandidate : coeffCandidates) {
                    pairs.add(new NodeCoeffPair(node, coeffCandidate));
                }
            }
        }

        @Theory
        public void test_多項式の値のテスト(NodeCoeffPair pair) {
            PolynomialFunction expected = SimplePolynomial.of(pair.coeff);
            NewtonPolynomial result = NewtonPolynomial.from(pair.node, expected::value);

            double x_min = -10d;
            double x_max = 10d;
            double x_delta = 0.125;
            for (double x = x_min; x <= x_max; x += x_delta) {
                DOUBLE_RELATIVE_ASSERTION.compareAndAssert(
                        expected.value(x),
                        result.value(x));
            }
        }

        @Theory
        public void test_多項式の係数のテスト(NodeCoeffPair pair) {
            PolynomialFunction baseFunction = SimplePolynomial.of(pair.coeff);
            NewtonPolynomial newton = NewtonPolynomial.from(pair.node, baseFunction::value);

            double[] expectedCoeff = baseFunction.coefficient();
            double[] resultCoeff = newton.coefficient();

            assertThat(resultCoeff.length, is(expectedCoeff.length));
            for (int i = 0; i < resultCoeff.length; i++) {
                DOUBLE_RELATIVE_ASSERTION.compareAndAssert(
                        expectedCoeff[i], resultCoeff[i]);
            }
        }

        private static final class NodeCoeffPair {
            final double[] node;
            final double[] coeff;

            NodeCoeffPair(double[] node, double[] coeffCandidate) {
                super();
                this.node = node;
                this.coeff = Arrays.copyOf(coeffCandidate, node.length);
            }
        }
    }
}
