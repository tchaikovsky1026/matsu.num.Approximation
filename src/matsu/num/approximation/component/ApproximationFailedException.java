/*
 * Copyright © 2024 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */

/*
 * 2026.6.24
 */
package matsu.num.approximation.component;

import java.util.Objects;

/**
 * 近似計算が失敗したことを表す例外.
 * 
 * @author Matsuura Y.
 */
public final class ApproximationFailedException extends Exception {

    private static final long serialVersionUID = -4826130658843263288L;

    private final String message;

    /**
     * 唯一のコンストラクタ. <br>
     * メッセージを渡してインスタンスを生成する.
     * 
     * @param message メッセージ
     * @throws NullPointerException 引数がnull
     */
    public ApproximationFailedException(String message) {
        this.message = Objects.requireNonNull(message);
    }

    /**
     * メッセージを取得する.
     * 
     * @return メッセージ
     */
    public String failuerMessage() {
        return this.message;
    }
}
