/*
 * Copyright © 2024 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */
package matsu.num.approximation.polynomial;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

import java.util.function.UnaryOperator;

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
 * {@link RemezTypePolynomialFactory} クラスのテスト.
 */
@RunWith(Enclosed.class)
final class RemezTypePolynomialFactoryTest {

    private static final PseudoRealNumber.Provider<DoubleLike> PROVIDER = DoubleLike.elementProvider();

    private static final DoubleRelativeAssertion DOUBLE_RELATIVE_ASSERTION =
            new DoubleRelativeAssertion(1E-12);

    public static class 誤差の分布の検証を行うテスト_3次間数 {

        private ApproxTarget<DoubleLike> target;
        private DoubleLike[] node;

        private Polynomial<DoubleLike> remezResult;

        @Before
        public void before_ターゲットの作成() {
            final FiniteClosedInterval<DoubleLike> interval = FiniteClosedInterval.from(
                    PROVIDER.fromDoubleValue(1d), PROVIDER.fromDoubleValue(2d));

            final UnaryOperator<DoubleLike> func = x -> {
                // 1 + x + x~2 + x^3
                double dblX = x.asDouble();

                return PROVIDER.fromDoubleValue(
                        1 + dblX + dblX * dblX + dblX * dblX * dblX);
            };
            final UnaryOperator<DoubleLike> scale = x -> {
                // 1 + x
                double dblX = x.asDouble();

                return PROVIDER.fromDoubleValue(1 + dblX);
            };

            target = new ApproxTarget<DoubleLike>() {

                @Override
                protected DoubleLike calcValue(DoubleLike x) {
                    return func.apply(x);
                }

                @Override
                protected DoubleLike calcScale(DoubleLike x) {
                    return scale.apply(x);
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

        @Before
        public void before_ノードの作成() {
            node = PROVIDER.createArray(3);
            node[0] = PROVIDER.fromDoubleValue(1.25d);
            node[1] = PROVIDER.fromDoubleValue(1.5d);
            node[2] = PROVIDER.fromDoubleValue(1.75d);
        }

        @Test
        public void test_ノードでスケール付き誤差が交代的かつ値が一致しているかを検証() {
            remezResult = new RemezTypePolynomialFactory<>(target).create(node);

            double e = error(node[0]).asDouble();

            //eが0でない場合を扱いたい
            assertThat(Math.abs(e), is(greaterThan(1E-8)));

            DOUBLE_RELATIVE_ASSERTION.compareAndAssert(-e, error(node[1]).asDouble());
            DOUBLE_RELATIVE_ASSERTION.compareAndAssert(e, error(node[2]).asDouble());
        }

        private DoubleLike error(DoubleLike x) {
            return remezResult.value(x).minus(target.value(x)).dividedBy(target.scale(x));
        }
    }

}
