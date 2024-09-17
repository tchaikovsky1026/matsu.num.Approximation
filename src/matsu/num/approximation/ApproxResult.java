/*
 * Copyright (c) 2024 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */
/*
 * 2024.9.2
 */
package matsu.num.approximation;

import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.Supplier;

/**
 * <p>
 * 近似結果を扱う.
 * </p>
 * 
 * <p>
 * {@link java.util.Optional} を模したラッパーである. <br>
 * 結果が存在する場合は内容が入っており, 結果が存在しない場合は空である. <br>
 * 結果が存在しない場合はメッセージの取得が可能である.
 * </p>
 * 
 * @author Matsuura Y.
 * @version 18.2
 * @param <T> 近似結果の内容の型パラメータ
 */
public abstract class ApproxResult<T> {

    /**
     * ネストクラスからでしかオーバーライドできないようにする.
     */
    private ApproxResult() {
        super();
    }

    /**
     * 近似結果の内容を取得する.
     * 
     * @return 近似結果の内容
     * @throws NoSuchElementException 内容が存在しない場合
     */
    public final T get() {
        if (this.isEmpty()) {
            throw new NoSuchElementException("resultは空である");
        }
        return this.getInner();
    }

    /**
     * {@link #isPresent()} が {@code true} の場合のみ内部から呼ばれる抽象メソッド. <br>
     * 近似結果の内容を返すように実装する.
     * 
     * @return 近似結果の内容
     */
    abstract T getInner();

    /**
     * 近似結果の内容が存在するかどうかを判定する.
     *
     * @return 内容が存在する場合はtrue
     */
    public abstract boolean isPresent();

    /**
     * 近似結果の内容が存在しないかどうかを判定する.
     *
     * @return 内容が存在しない場合はtrue
     */
    public final boolean isEmpty() {
        return !this.isPresent();
    }

    /**
     * 近似結果の内容が存在する場合は値を返し, そうでない場合はサプライヤが生成した例外をスローする.
     *
     * @param <X> スローされる例外の型
     * @param exceptionSupplier 例外のサプライヤ
     * @return 内容 (存在する場合)
     * @throws X 内容が存在しない場合
     * @throws NullPointerException 内容が存在せず, かつ引数がnullの場合,
     *             例外サプライヤによってnullが生成された場合
     */
    public final <X extends Throwable>
            T orElseThrow(Supplier<? extends X> exceptionSupplier) throws X {
        if (this.isEmpty()) {
            throw exceptionSupplier.get();
        }

        return this.get();
    }

    /**
     * この結果に関するメッセージを返す. <br>
     * 結果が存在する場合はおそらく空文字が返るだろう.
     * 
     * @return メッセージ
     */
    public abstract String message();

    /**
     * このインスタンスの固定名を返す(固定値).
     * 
     * @return 固定名
     */
    final String fixedObjName() {
        return this.getClass().getSimpleName();
    }

    /**
     * 与えられた内容を保持した近似結果を返す (ラップ).
     * 
     * @param <T> 近似結果の内容の型パラメータ
     * @param content 近似結果の内容
     * @return 近似結果
     * @throws NullPointerException 引数がnullの場合
     */
    public static <T> ApproxResult<T> of(T content) {
        return new ElementHoldingApproxResult<>(Objects.requireNonNull(content));
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
    public static <T> ApproxResult<T> failed(String message) {
        return new EmptyApproxResult<>(
                Objects.nonNull(message) ? message : "");
    }

    /**
     * 内容を保持することを表す {@link ApproxResult}.
     */
    private static final class ElementHoldingApproxResult<T> extends ApproxResult<T> {

        private final T content;

        /**
         * 内容を与えて近似結果を生成する. <br>
         * nullを渡してはいけない.
         */
        ElementHoldingApproxResult(T content) {
            assert Objects.nonNull(content);
            this.content = content;
        }

        @Override
        T getInner() {
            return this.content;
        }

        @Override
        public boolean isPresent() {
            return true;
        }

        @Override
        public String message() {
            return "";
        }

        @Override
        public String toString() {
            return String.format(
                    "%s(%s)", this.fixedObjName(), this.content);
        }
    }

    /**
     * 内容を持たないことを表す {@link ApproxResult}.
     */
    private static final class EmptyApproxResult<T> extends ApproxResult<T> {

        private final String message;

        /**
         * メッセージを与えて, 空の近似結果を生成する. <br>
         * nullを渡してはいけない.
         */
        EmptyApproxResult(String message) {
            assert Objects.nonNull(message);
            this.message = message;
        }

        @Override
        T getInner() {
            throw new AssertionError("Bug: 到達不能のはず");
        }

        @Override
        public boolean isPresent() {
            return false;
        }

        @Override
        public String message() {
            return this.message;
        }

        @Override
        public String toString() {
            return String.format(
                    "%s(empty)", this.fixedObjName());
        }
    }
}
