/*
 * Copyright © 2024 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */
/*
 * 2024.12.26
 */
package matsu.num.approximation.polynomial;

import matsu.num.approximation.PseudoRealNumber;

/**
 * {@link Polynomial} をシールするための非公開インターフェース.
 * 
 * @author Matsuura Y.
 * @version 22.0
 * @param <T> 体の元を表現する型パラメータ
 */
non-sealed interface PolynomialSealed<T extends PseudoRealNumber<T>> extends Polynomial<T> {

}
