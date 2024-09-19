package matsu.num.approximation.behavior.generalfield.polynomial;

import org.junit.Ignore;

import matsu.num.approximation.ApproxResult;
import matsu.num.approximation.generalfield.ApproxTarget;
import matsu.num.approximation.generalfield.Decimal128;
import matsu.num.approximation.generalfield.FiniteClosedInterval;
import matsu.num.approximation.generalfield.PseudoRealNumber;
import matsu.num.approximation.generalfield.PseudoRealNumber.Provider;
import matsu.num.approximation.generalfield.polynomial.MinimaxPolynomialApproxExecutor;
import matsu.num.approximation.generalfield.polynomial.Polynomial;

/**
 * <p>
 * {@link Decimal128} を用いた関数近似のテスト.
 * </p>
 * 
 * <p>
 * このクラスは自動テストではない.
 * </p>
 */
@Ignore
final class Decimal128ApproximationBehaviorTest {

    private static final PseudoRealNumber.Provider<Decimal128> PROVIDER = Decimal128.elementProvider();

    private final ApproxTarget<Decimal128> target;

    public static void main(String[] args) {
        new Decimal128ApproximationBehaviorTest().execute();
    }

    Decimal128ApproximationBehaviorTest() {
        this.target = target_exp();
    }

    void execute() {

        int degree = 15;
        ApproxResult<Polynomial<Decimal128>> result =
                MinimaxPolynomialApproxExecutor.of(degree).apply(target);

        Decimal128 x_min = target.interval().lower();
        Decimal128 x_max = target.interval().upper();
        Decimal128 deltaX = x_max.minus(x_min).times(1d / 256);

        System.out.println(target.toString());
        System.out.println("coeff");
        for (Decimal128 polyCoeff : result.get().coefficient()) {
            System.out.println(polyCoeff);
        }
        System.out.println("");

        System.out.println("x\tf\th\te");
        for (Decimal128 x = x_min; x.compareTo(x_max) <= 0; x = x.plus(deltaX)) {
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
    static ApproxTarget<Decimal128> target_exp() {
        final FiniteClosedInterval<Decimal128> interval = FiniteClosedInterval.from(
                PROVIDER.fromDoubleValue(0d), PROVIDER.fromDoubleValue(1d));

        return new ApproxTarget<Decimal128>() {

            @Override
            protected Decimal128 calcValue(Decimal128 x) {
                return exp(x);
            }

            @Override
            protected Decimal128 calcScale(Decimal128 x) {
                return exp(x);
            }

            @Override
            public FiniteClosedInterval<Decimal128> interval() {
                return interval;
            }

            @Override
            public Provider<Decimal128> elementProvider() {
                return PROVIDER;
            }

            @Override
            public String toString() {
                return "Exponential Function exp(x)";
            }
        };
    }

    private static Decimal128 exp(Decimal128 x) {

        int kMax = 30;

        Decimal128 sum = PROVIDER.zero();
        for (int k = kMax + 1; k >= 1; k--) {
            sum = sum.times(x).dividedBy(k);
            sum = sum.plus(1d);
        }
        return sum;
    }

}
