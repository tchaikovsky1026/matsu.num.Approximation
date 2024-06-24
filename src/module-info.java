/**
 * <p>
 * 関数の近似を扱うモジュール.
 * </p>
 * 
 * <p>
 * <i>必須モジュール:</i> <br>
 * {@code matsu.num.Commons} <br>
 * {@code matsu.num.matrix.Base}
 * 
 * </p>
 * 
 * @author Matsuura Y.
 * @version 17.0
 */
module matsu.num.Approximation {
    requires matsu.num.Commons;
    requires matsu.num.matrix.Base;

    exports matsu.num.approximation;
    exports matsu.num.approximation.polynomial;
}