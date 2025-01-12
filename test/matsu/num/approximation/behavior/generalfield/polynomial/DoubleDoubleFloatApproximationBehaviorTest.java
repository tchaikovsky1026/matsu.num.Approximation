/*
 * Copyright © 2024 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */
package matsu.num.approximation.behavior.generalfield.polynomial;

import org.junit.Ignore;

import matsu.num.approximation.ApproxResult;
import matsu.num.approximation.ApproxTarget;
import matsu.num.approximation.FiniteClosedInterval;
import matsu.num.approximation.PseudoRealNumber;
import matsu.num.approximation.PseudoRealNumber.Provider;
import matsu.num.approximation.behavior.generalfield.DoubleDoubleFloatRealNumber;
import matsu.num.approximation.polynomial.MinimaxPolynomialApproxExecutor;
import matsu.num.approximation.polynomial.Polynomial;

/**
 * <p>
 * {@link DoubleDoubleFloatRealNumber} を用いた関数近似のテスト.
 * </p>
 * 
 * <p>
 * このクラスは自動テストではない.
 * </p>
 */
@Ignore
final class DoubleDoubleFloatApproximationBehaviorTest {

    private static final PseudoRealNumber.Provider<DoubleDoubleFloatRealNumber> PROVIDER =
            DoubleDoubleFloatRealNumber.elementProvider();

    private final ApproxTarget<DoubleDoubleFloatRealNumber> target;

    public static void main(String[] args) {
        new DoubleDoubleFloatApproximationBehaviorTest().execute();
    }

    DoubleDoubleFloatApproximationBehaviorTest() {
        this.target = target_exp();
    }

    void execute() {

        long startTimeMills = System.currentTimeMillis();
        int degree = 9;
        ApproxResult<Polynomial<DoubleDoubleFloatRealNumber>> result =
                MinimaxPolynomialApproxExecutor.of(degree).apply(target);
        long endTimeMills = System.currentTimeMillis();
        System.out.println((endTimeMills - startTimeMills) + "ms");

        DoubleDoubleFloatRealNumber x_min = target.interval().lower();
        DoubleDoubleFloatRealNumber x_max = target.interval().upper();
        DoubleDoubleFloatRealNumber deltaX = x_max.minus(x_min).times(1d / 256);

        System.out.println(target.toString());
        System.out.println("coeff");
        for (DoubleDoubleFloatRealNumber polyCoeff : result.get().coefficient()) {
            System.out.println(polyCoeff);
        }
        System.out.println("");

        System.out.println("x\tf\th\te");
        for (DoubleDoubleFloatRealNumber x = x_min; x.compareTo(x_max) <= 0; x = x.plus(deltaX)) {
            System.out.println(
                    x + "\t" +
                            target.value(x) + "\t" +
                            result.get().value(x) + "\t" +
                            target.value(x).minus(result.get().value(x))
                                    .dividedBy(target.scale(x)));
        }
        System.out.println("");
    }

    /**
     * ターゲット関数: exp(x) [0d, 1d]
     */
    static ApproxTarget<DoubleDoubleFloatRealNumber> target_exp() {
        final FiniteClosedInterval<DoubleDoubleFloatRealNumber> interval =
                FiniteClosedInterval.from(
                        PROVIDER.fromDoubleValue(0d), PROVIDER.fromDoubleValue(1d));

        return new ApproxTarget<DoubleDoubleFloatRealNumber>() {

            @Override
            protected DoubleDoubleFloatRealNumber calcValue(DoubleDoubleFloatRealNumber x) {
                return exp(x);
            }

            @Override
            protected DoubleDoubleFloatRealNumber calcScale(DoubleDoubleFloatRealNumber x) {
                return exp(x);
            }

            @Override
            public FiniteClosedInterval<DoubleDoubleFloatRealNumber> interval() {
                return interval;
            }

            @Override
            public Provider<DoubleDoubleFloatRealNumber> elementProvider() {
                return PROVIDER;
            }

            @Override
            public String toString() {
                return "Exponential Function exp(x)";
            }
        };
    }

    private static DoubleDoubleFloatRealNumber exp(DoubleDoubleFloatRealNumber x) {

        int kMax = 30;

        DoubleDoubleFloatRealNumber sum = PROVIDER.zero();
        for (int k = kMax + 1; k >= 1; k--) {
            sum = sum.times(x).dividedBy(k);
            sum = sum.plus(1d);
        }
        return sum;
    }

}
