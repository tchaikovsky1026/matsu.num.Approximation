/*
 * Copyright © 2025 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */

package matsu.num.approximation;

import org.junit.Test;
import org.junit.Test.None;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

import matsu.num.approximation.PseudoRealNumber.Provider;
import matsu.num.approximation.PseudoRealNumber.TypeProvider;

/**
 * {@link ApproxTarget} のテスト
 */
@RunWith(Enclosed.class)
final class ApproxTargetTest {

    @Deprecated
    public static class 実装における元プロバイダに関する_実装無し {

        /*
         * elementProvider と elementTypeProvider の両方を維持している間に意味のあるテスト.
         * 不要になったら削除する.
         */

        private static final class ApproxTargetImpl extends ApproxTarget<DoubleLike> {

            @Override
            protected DoubleLike calcValue(DoubleLike x) {
                throw new AssertionError("unreachable");
            }

            @Override
            protected DoubleLike calcScale(DoubleLike x) {
                throw new AssertionError("unreachable");
            }

            @Override
            public FiniteClosedInterval<DoubleLike> interval() {
                throw new AssertionError("unreachable");
            }

        }

        @Test(expected = AssertionError.class)
        public void test_実装しない場合アサーションが投げられる_elementProvider() {
            new ApproxTargetImpl().elementProvider();
        }

        @Test(expected = AssertionError.class)
        public void test_実装しない場合アサーションが投げられる_elementTypeProvider() {
            new ApproxTargetImpl().elementTypeProvider();
        }
    }

    @Deprecated
    public static class 実装における元プロバイダに関する_elementProvider実装 {

        /*
         * elementProvider と elementTypeProvider の両方を維持している間に意味のあるテスト.
         * 不要になったら削除する.
         */

        private static final class ApproxTargetImpl extends ApproxTarget<DoubleLike> {

            @Override
            protected DoubleLike calcValue(DoubleLike x) {
                throw new AssertionError("unreachable");
            }

            @Override
            protected DoubleLike calcScale(DoubleLike x) {
                throw new AssertionError("unreachable");
            }

            @Override
            public FiniteClosedInterval<DoubleLike> interval() {
                throw new AssertionError("unreachable");
            }

            @Override
            public Provider<DoubleLike> elementProvider() {
                return DoubleLike.elementTypeProvider();
            }
        }

        @Test(expected = None.class)
        public void test_正常に生成_elementProvider() {
            new ApproxTargetImpl().elementProvider();
        }

        @Test(expected = None.class)
        public void test_正常に生成_elementTypeProvider() {
            new ApproxTargetImpl().elementTypeProvider();
        }
    }

    @Deprecated
    public static class 実装における元プロバイダに関する_elementTypeProvider実装 {

        /*
         * elementProvider と elementTypeProvider の両方を維持している間に意味のあるテスト.
         * 不要になったら削除する.
         */

        private static final class ApproxTargetImpl extends ApproxTarget<DoubleLike> {

            @Override
            protected DoubleLike calcValue(DoubleLike x) {
                throw new AssertionError("unreachable");
            }

            @Override
            protected DoubleLike calcScale(DoubleLike x) {
                throw new AssertionError("unreachable");
            }

            @Override
            public FiniteClosedInterval<DoubleLike> interval() {
                throw new AssertionError("unreachable");
            }

            @Override
            public TypeProvider<DoubleLike> elementTypeProvider() {
                return DoubleLike.elementTypeProvider();
            }
        }

        @Test(expected = None.class)
        public void test_正常に生成_elementProvider() {
            new ApproxTargetImpl().elementProvider();
        }

        @Test(expected = None.class)
        public void test_正常に生成_elementTypeProvider() {
            new ApproxTargetImpl().elementTypeProvider();
        }
    }

}
