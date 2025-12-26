/*
 * Copyright © 2025 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */

package matsu.num.approximation.polynomial;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

import java.util.function.DoubleUnaryOperator;

import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

import matsu.num.approximation.DoubleApproxTarget;
import matsu.num.approximation.DoubleFiniteClosedInterval;

/**
 * {@link MinimaxDoublePolynomialApproxExecutor} のテスト
 */
@RunWith(Enclosed.class)
final class MinimaxDoublePolynomialApproxExecutorTest {

    public static class sinの近似 {

        private DoubleApproxTarget target;

        @Before
        public void before_ターゲットを用意する() {
            var interval = DoubleFiniteClosedInterval.from(-1d, 1d);
            DoubleUnaryOperator op = Math::sin;

            target = new DoubleApproxTarget() {

                @Override
                public DoubleFiniteClosedInterval interval() {
                    return interval;
                }

                @Override
                protected double calcValue(double x) {
                    return op.applyAsDouble(x);
                }

                @Override
                protected double calcScale(double x) {
                    return 1d;
                }
            };
        }

        @Test
        public void test_近似をテストする() {
            DoublePolynomial polynomial = MinimaxDoublePolynomialApproxExecutor.of(11)
                    .apply(target)
                    .get();

            double xMin = target.interval().lower();
            double xMax = target.interval().upper();
            double deltaX = (xMax - xMin) * 0.01d;

            for (double x = xMin; x <= xMax; x += deltaX) {
                double approxValue = polynomial.value(x);
                double refValue = target.value(x);
                double res = Math.abs(approxValue - refValue);

                assertThat(
                        "res (x = %s, approxValue = %s, refValue = %s)".formatted(x, approxValue, refValue),
                        res, is(lessThan(1E-12)));
            }
        }
    }
}
