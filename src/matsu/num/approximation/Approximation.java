/*
 * Copyright (c) 2024 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */
/*
 * 2024.6.21
 */
package matsu.num.approximation;

import java.util.NoSuchElementException;
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
 * @version 18.0
 * @param <T> 近似結果の内容の型パラメータ
 */
public interface Approximation<T> {

    /**
     * 近似結果の内容を取得する.
     * 
     * @return 近似結果の内容
     * @throws NoSuchElementException 内容が存在しない場合
     */
    public abstract T get();

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
    public abstract boolean isEmpty();

    /**
     * 近似結果の内容が存在する場合は値を返し, そうでない場合はサプライヤが生成した例外をスローする.
     *
     * @param <X> スローされる例外の型
     * @param exceptionSupplier 例外のサプライヤ
     * @return 内容 (存在する場合)
     * @throws X 内容が存在しない場合
     * @throws NullPointerException 引数がnullの場合
     */
    public abstract <X extends Throwable>
            T orElseThrow(Supplier<? extends X> exceptionSupplier) throws X;

    /**
     * この結果に関するメッセージを返す. <br>
     * 結果が存在する場合はおそらく空文字が返るだろう.
     * 
     * @return メッセージ
     */
    public abstract String message();
}
