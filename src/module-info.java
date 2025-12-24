/*
 * Copyright © 2024 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */

/**
 * 関数の近似を扱うモジュール.
 * 
 * <p>
 * このモジュールの主要なパッケージは次である. <br>
 * 詳細は各パッケージの説明文を参照すること.
 * </p>
 * <ul>
 * <li>{@link matsu.num.approximation}:
 * 関数近似のためのコンポーネントを定義するパッケージ.
 * </li>
 * <li>{@link matsu.num.approximation.polynomial}:
 * 関数の多項式近似に係るインターフェース, 機能を提供するパッケージ.
 * </li>
 * </ul>
 * 
 * <p>
 * <i>依存モジュール:</i> <br>
 * (無し)
 * </p>
 * 
 * @author Matsuura Y.
 * @version 24.2.0
 */
module matsu.num.Approximation {

    exports matsu.num.approximation;
    exports matsu.num.approximation.polynomial;
}
