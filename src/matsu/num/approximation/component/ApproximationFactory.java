/*
 * Copyright (c) 2024 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */
/*
 * 2024.6.21
 */
package matsu.num.approximation.component;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Supplier;

import matsu.num.approximation.Approximation;

/**
 * {@link Approximation} のファクトリ.
 * 
 * @author Matsuura Y.
 * @version 18.0
 */
public final class ApproximationFactory {

    /**
     * 与えられた内容を保持した近似結果を返す (ラップ).
     * 
     * @param <T> 近似結果の内容の型パラメータ
     * @param content 近似結果の内容
     * @return 近似結果
     * @throws NullPointerException 引数がnullの場合
     */
    public static <T> Approximation<T> of(T content) {
        //NullPointerExが発生する場合がある
        return new ElementHoldingApproximation<>(content);
    }

    /**
     * <p>
     * 内容が無いことを表すを保近似結果を返す. <br>
     * メッセージを加える.
     * </p>
     * 
     * <p>
     * 引数が {@code null} の場合, 空のメッセージを割り当てる.
     * </p>
     * 
     * @param <T> 近似結果の内容の型パラメータ
     * @param message メッセージ
     * @return 近似結果
     */
    public static <T> Approximation<T> failed(String message) {
        return new EmptyApproximation<>(message);
    }

    /**
     * {@link Approximation} を実現するための骨格クラス. <br>
     * {@link Optional} をラップしている.
     */
    private static abstract class SkeletalApproximation<T> implements Approximation<T> {

        protected final Optional<T> content;

        protected SkeletalApproximation(Optional<T> content) {
            this.content = Objects.requireNonNull(content);
        }

        @Override
        public final T get() {
            return this.content.get();
        }

        @Override
        public final boolean isEmpty() {
            return this.content.isEmpty();
        }

        @Override
        public final boolean isPresent() {
            return this.content.isPresent();
        }

        @Override
        public final <X extends Throwable>
                T orElseThrow(Supplier<? extends X> exceptionSupplier) throws X {
            return this.content.orElseThrow(Objects.requireNonNull(exceptionSupplier));
        }
    }

    private static final class ElementHoldingApproximation<T> extends SkeletalApproximation<T> {
        /**
         * @throws NullPointerException null
         */
        ElementHoldingApproximation(T content) {
            super(Optional.of(content));
        }

        @Override
        public String message() {
            return "";
        }

        /**
         * 自身の文字列表現を返す.
         * 
         * @return 文字列表現
         */
        @Override
        public String toString() {
            return String.format(
                    "Approximation(%s)", this.content.get());
        }
    }

    private static final class EmptyApproximation<T> extends SkeletalApproximation<T> {

        private final String message;

        /**
         * @throws NullPointerException null
         */
        EmptyApproximation(String message) {
            super(Optional.empty());
            this.message =
                    Objects.nonNull(message) ? message : "";
        }

        @Override
        public String message() {
            return this.message;
        }

        @Override
        public String toString() {
            return "Approximation(EMPTY)";
        }
    }
}
