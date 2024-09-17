/*
 * Copyright (c) 2024 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */
/*
 * 2024.9.15
 */
package matsu.num.approximation.generalfield.polynomial;

import matsu.num.approximation.generalfield.FiniteClosedInterval;
import matsu.num.approximation.generalfield.PseudoRealNumber;

/**
 * ノードを作成するユーティリティクラス.
 * 
 * @author Matsuura Y.
 * @version 18.2
 */
public final class NodeCreation {

    private NodeCreation() {
        //インスタンス化不可
        throw new AssertionError();
    }

    /**
     * 与えた区間にノードを配置する. <br>
     * 区間の端には必ず配置される
     * (すなわち, ノードの数は2以上必要).
     * 
     * @param <T> 体を表す型パラメータ
     * @param size ノードの数, 2以上でなければならない
     * @param interval ノードを配置する区間
     * @param provider 体の元に関するプロバイダ
     * @return ノード, 昇順に並んでいる
     * @throws IllegalArgumentException sizeが不正の場合
     * @throws NullPointerException nullを含む場合
     */
    public static <T extends PseudoRealNumber<T>> T[] execute(
            int size, FiniteClosedInterval<T> interval, PseudoRealNumber.Provider<T> provider) {
        if (size < 2) {
            throw new IllegalArgumentException("sizeが不正");
        }

        T[] out = provider.createArray(size);

        T lower = interval.lower();
        T upper = interval.upper();
        T halfGap = upper.minus(lower).times(0.5);
        T mid = lower.plus(halfGap);

        for (int i = 1; i < size - 1; i++) {
            double cos = Math.cos(Math.PI * (size - 1 - i) / (size - 1));
            out[i] = halfGap.times(cos).plus(mid);
        }
        out[0] = lower;
        out[size - 1] = upper;

        return out;
    }
}
