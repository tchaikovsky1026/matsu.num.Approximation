package matsu.num.approximation.behavior.generalfield.polynomial;

import org.junit.Ignore;

import matsu.num.approximation.ApproxResult;
import matsu.num.approximation.ApproxTarget;
import matsu.num.approximation.DoubleLike;
import matsu.num.approximation.FiniteClosedInterval;
import matsu.num.approximation.PseudoRealNumber;
import matsu.num.approximation.PseudoRealNumber.Provider;
import matsu.num.approximation.polynomial.MinimaxPolynomialApproxExecutor;
import matsu.num.approximation.polynomial.Polynomial;

/**
 * <p>
 * {@link MinimaxPolynomialApproxExecutor} の振る舞いテスト.
 * </p>
 * 
 * <p>
 * このクラスは自動テストではない.
 * </p>
 */
@Ignore
final class MinimaxPolynomialApproxExecutorBehaviorTest {

    private static final PseudoRealNumber.Provider<DoubleLike> PROVIDER = DoubleLike.elementProvider();

    private final ApproxTarget<DoubleLike> target;

    public static void main(String[] args) {
        new MinimaxPolynomialApproxExecutorBehaviorTest().execute();
    }

    MinimaxPolynomialApproxExecutorBehaviorTest() {
        this.target = exp();
    }

    void execute() {
        System.out.println(target.toString());

        long startTimeMills = System.currentTimeMillis();
        int degree = 9;
        ApproxResult<Polynomial<DoubleLike>> result =
                MinimaxPolynomialApproxExecutor.of(degree).apply(target);
        long endTimeMills = System.currentTimeMillis();
        System.out.println((endTimeMills - startTimeMills) + "ms");

        DoubleLike x_min = target.interval().lower();
        DoubleLike x_max = target.interval().upper();
        DoubleLike deltaX = x_max.minus(x_min).times(0.015625);

        System.out.println("coeff");
        for (DoubleLike polyCoeff : result.get().coefficient()) {
            System.out.println(polyCoeff);
        }
        System.out.println("");

        System.out.println("x\tf\th\te");
        for (DoubleLike x = x_min; x.compareTo(x_max) <= 0; x = x.plus(deltaX)) {
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
    static ApproxTarget<DoubleLike> exp() {
        final FiniteClosedInterval<DoubleLike> interval = FiniteClosedInterval.from(
                PROVIDER.fromDoubleValue(0d), PROVIDER.fromDoubleValue(1d));

        return new ApproxTarget<DoubleLike>() {

            @Override
            protected DoubleLike calcValue(DoubleLike x) {
                return exp(x);
            }

            @Override
            protected DoubleLike calcScale(DoubleLike x) {
                return exp(x);
            }

            @Override
            public FiniteClosedInterval<DoubleLike> interval() {
                return interval;
            }

            @Override
            public Provider<DoubleLike> elementProvider() {
                return PROVIDER;
            }

            @Override
            public String toString() {
                return "Exponential Function exp(x)";
            }
        };
    }

    private static DoubleLike exp(DoubleLike x) {

        int kMax = 30;

        DoubleLike sum = PROVIDER.zero();
        for (int k = kMax + 1; k >= 1; k--) {
            sum = sum.times(x).dividedBy(k);
            sum = sum.plus(1d);
        }
        return sum;
    }
}
