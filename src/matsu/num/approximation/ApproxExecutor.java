/*
 * Copyright (c) 2024 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */
/*
 * 2024.6.20
 */
package matsu.num.approximation;

/**
 * 関数近似の実行を扱う.
 * 
 * @author Matsuura Y.
 * @version 18.0
 * @param <T> 近似結果の内容の型パラメータ
 */
public interface ApproxExecutor<T> {

    /**
     * <p>
     * 与えられたターゲット関数を近似する.
     * </p>
     * 
     * <p>
     * 近似の計算中に不具合が出た場合は, 空の {@link Approximation} が返る.
     * </p>
     * 
     * @param target ターゲット関数
     * @return 近似結果, 計算に失敗した場合は空
     * @throws NullPointerException 引数がnullの場合
     */
    public abstract Approximation<T> apply(TargetFunction target);
}
