/**
 * 関数の近似を扱うモジュール. <br><br>
 * <i>必須モジュール:</i> <br>
 * {@code matsu.num.Commons} <br>
 * {@code matsu.num.matrix.Base}
 * 
 * @author Matsuura Y.
 * @version 16.0
 */
module matsu.num.Approximation {
    requires matsu.num.Commons;
    requires matsu.num.matrix.Base;

    exports matsu.num.approximation;
    exports matsu.num.approximation.polynomial;
}