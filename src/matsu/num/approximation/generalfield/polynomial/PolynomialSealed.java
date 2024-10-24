/*
 * Copyright (c) 2024 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */
/*
 * 2024.10.24
 */
package matsu.num.approximation.generalfield.polynomial;

import matsu.num.approximation.generalfield.PseudoRealNumber;

/**
 * {@link Polynomial} をシールするための非公開インターフェース.
 * 
 * @author Matsuura Y.
 * @version 20.0
 * @param <T> 体の元を表現する型パラメータ
 */
non-sealed interface PolynomialSealed<T extends PseudoRealNumber<T>> extends Polynomial<T> {

}
