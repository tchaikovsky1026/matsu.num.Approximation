/*
 * Copyright © 2025 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */

package matsu.num.approximation;

import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

/**
 * {@link PseudoRealNumber} のテスト.
 */
@RunWith(Enclosed.class)
final class PseudoRealNumberTest {

    public static class プロバイダの実装に関する {

        /**
         * 数体.
         * ただし, 実装要件を守らず, サブクラスを作成可能.
         */
        private static class SuperTypeNumber extends PseudoRealNumber<SuperTypeNumber> {

            SuperTypeNumber() {
            }

            @Override
            public final SuperTypeNumber plus(SuperTypeNumber augend) {
                throw new AssertionError("unreachable");
            }

            @Override
            public final SuperTypeNumber minus(SuperTypeNumber subtrahend) {
                throw new AssertionError("unreachable");
            }

            @Override
            public final SuperTypeNumber times(SuperTypeNumber multiplicand) {
                throw new AssertionError("unreachable");
            }

            @Override
            public final SuperTypeNumber dividedBy(SuperTypeNumber divisor) {
                throw new AssertionError("unreachable");
            }

            @Override
            public final SuperTypeNumber negated() {
                throw new AssertionError("unreachable");
            }

            @Override
            public final SuperTypeNumber abs() {
                throw new AssertionError("unreachable");
            }

            @Override
            public final double asDouble() {
                return 0d;
            }

            @Override
            public final boolean equals(Object obj) {
                if (!(obj instanceof SuperTypeNumber)) {
                    return false;
                }
                return true;
            }

            @Override
            public final int hashCode() {
                return 0;
            }

            @Override
            public final int compareTo(SuperTypeNumber o) {
                // 型を検証
                SuperTypeNumber.class.cast(o);
                return 0;
            }

            @Override
            public final String toString() {
                return "0";
            }
        }

        /**
         * サブクラス.
         */
        private static final class SubTypeNumber extends SuperTypeNumber {
            private SubTypeNumber() {
            }
        }

        private static final class Provider implements PseudoRealNumber.Provider<SuperTypeNumber> {

            @Override
            public SuperTypeNumber fromDoubleValue(double value) {
                return new SuperTypeNumber();
            }

            @Override
            public SuperTypeNumber zero() {
                return new SuperTypeNumber();
            }

            @Override
            public SuperTypeNumber one() {
                return new SuperTypeNumber();
            }

            @Override
            public SuperTypeNumber[] createArray(int length) {
                // ここでサブタイプ
                return new SubTypeNumber[length];
            }
        }

        @Test(expected = ClassCastException.class)
        public void test_正しいProviderでない場合はアダプターに失敗() {
            PseudoRealNumber.TypeProvider.from(new Provider());
        }
    }
}
