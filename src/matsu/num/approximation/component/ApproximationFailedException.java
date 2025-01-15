/*
 * Copyright © 2024 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */
/*
 * 2024.6.23
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

    public ApproximationFailedException(String message) {
        this.message = Objects.requireNonNull(message);
    }

    public String failuerMessage() {
        return this.message;
    }
}
