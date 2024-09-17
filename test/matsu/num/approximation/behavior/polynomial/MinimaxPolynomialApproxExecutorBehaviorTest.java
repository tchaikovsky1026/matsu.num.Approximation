package matsu.num.approximation.behavior.polynomial;

import org.junit.Ignore;

import matsu.num.approximation.Approximation;
import matsu.num.approximation.DoubleFiniteClosedInterval;
import matsu.num.approximation.PolynomialFunction;
import matsu.num.approximation.TargetFunction;
import matsu.num.approximation.polynomial.MinimaxPolynomialApproxExecutor;

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

    private final TargetFunction target;

    public static void main(String[] args) {
        new MinimaxPolynomialApproxExecutorBehaviorTest().execute();
    }

    MinimaxPolynomialApproxExecutorBehaviorTest() {
        this.target = exp();
    }

    void execute() {

        int degree = 9;
        Approximation<PolynomialFunction> result =
                MinimaxPolynomialApproxExecutor.of(degree).apply(target);

        double x_min = target.interval().lower();
        double x_max = target.interval().upper();
        double deltaX = 0.015625 * (x_max - x_min);

        System.out.println(target.toString());
        System.out.println("coeff");
        for (double polyCoeff : result.get().coefficient()) {
            System.out.println(polyCoeff);
        }
        System.out.println("");

        System.out.println("x\tf\th\te");
        for (double x = x_min; x <= x_max; x += deltaX) {
            System.out.println(
                    x + "\t" +
                            target.value(x) + "\t" +
                            result.get().value(x) + "\t" +
                            (target.value(x) - result.get().value(x)) /
                                    target.scale(x));
        }
        System.out.println("");
    }

    /**
     * ターゲット関数: exp(x) [0d, 1d]
     */
    static TargetFunction exp() {
        final DoubleFiniteClosedInterval interval = DoubleFiniteClosedInterval.from(0d, 1d);
        return new TargetFunction() {

            @Override
            public double value(double x) {
                return Math.exp(x);
            }

            @Override
            public double scale(double x) {
                return Math.exp(x);
            }

            @Override
            public DoubleFiniteClosedInterval interval() {
                return interval;
            }

            @Override
            public String toString() {
                return "Exponential Function exp(x)";
            }
        };
    }

}
