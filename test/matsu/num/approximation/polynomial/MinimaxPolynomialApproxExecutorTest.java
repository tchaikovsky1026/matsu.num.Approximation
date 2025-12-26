/*
 * Copyright © 2025 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */

package matsu.num.approximation.polynomial;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

import java.util.function.Function;

import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

import matsu.num.approximation.ApproxTarget;
import matsu.num.approximation.DoubleLike;
import matsu.num.approximation.FiniteClosedInterval;
import matsu.num.approximation.PseudoRealNumber.TypeProvider;

/**
 * {@link MinimaxPolynomialApproxExecutor} のテスト
 */
@RunWith(Enclosed.class)
final class MinimaxPolynomialApproxExecutorTest {

    public static class sinの近似 {

        private static final TypeProvider<DoubleLike> TYPE_PROVIDER =
                DoubleLike.elementTypeProvider();

        private ApproxTarget<DoubleLike> target;

        @Before
        public void before_ターゲットを用意する() {
            var interval = FiniteClosedInterval.from(
                    TYPE_PROVIDER.fromDoubleValue(-1d),
                    TYPE_PROVIDER.fromDoubleValue(1d));

            Function<DoubleLike, DoubleLike> op =
                    ((Function<DoubleLike, Double>) DoubleLike::asDouble)
                            .andThen(Math::sin)
                            .andThen(TYPE_PROVIDER::fromDoubleValue);

            target = new ApproxTarget<>() {

                @Override
                public TypeProvider<DoubleLike> elementTypeProvider() {
                    return TYPE_PROVIDER;
                }

                @Override
                public FiniteClosedInterval<DoubleLike> interval() {
                    return interval;
                }

                @Override
                protected DoubleLike calcValue(DoubleLike x) {
                    return op.apply(x);
                }

                @Override
                protected DoubleLike calcScale(DoubleLike x) {
                    return TYPE_PROVIDER.one();
                }
            };
        }

        @Test
        public void test_近似をテストする() {
            Polynomial<DoubleLike> polynomial = MinimaxPolynomialApproxExecutor.of(11)
                    .apply(target)
                    .get();

            double xMin = target.interval().lower().asDouble();
            double xMax = target.interval().upper().asDouble();
            double deltaX = (xMax - xMin) * 0.01d;

            for (double x = xMin; x <= xMax; x += deltaX) {
                DoubleLike xObj = TYPE_PROVIDER.fromDoubleValue(x);
                
                double approxValue = polynomial.value(xObj).asDouble();
                double refValue = target.value(xObj).asDouble();
                double res = Math.abs(approxValue - refValue);

                assertThat(
                        "res (x = %s, approxValue = %s, refValue = %s)".formatted(x, approxValue, refValue),
                        res, is(lessThan(1E-12)));
            }
        }
    }
}
