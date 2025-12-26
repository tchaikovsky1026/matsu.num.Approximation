/*
 * Copyright © 2024 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */

/**
 * 多項式関数による近似を扱うパッケージ.
 * 
 * <p>
 * 関数近似に関する,
 * 多項式を近似関数空間とした関数近似を提供する. <br>
 * {@link matsu.num.approximation.DoubleApproxTarget},
 * {@link matsu.num.approximation.ApproxTarget}
 * を与え, 各種の誤差基準にしたがって近似を試みる.
 * </p>
 * 
 * <p>
 * 多項式は,
 * {@code double} 型数体の場合は
 * {@link matsu.num.approximation.polynomial.DoublePolynomial},
 * 独自の数体 {@link matsu.num.approximation.PseudoRealNumber}
 * の場合は
 * {@link matsu.num.approximation.polynomial.Polynomial}
 * により表現される.
 * </p>
 */
package matsu.num.approximation.polynomial;
