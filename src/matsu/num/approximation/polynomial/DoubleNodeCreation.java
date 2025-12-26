/*
 * Copyright © 2024 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */
/*
 * 2025.12.26
 */
package matsu.num.approximation.polynomial;

import matsu.num.approximation.DoubleFiniteClosedInterval;

/**
 * ノードを作成するユーティリティクラス.
 * 
 * @author Matsuura Y.
 */
final class DoubleNodeCreation {

    private DoubleNodeCreation() {
        //インスタンス化不可
        throw new AssertionError();
    }

    /**
     * 与えた区間にノードを配置する. <br>
     * 区間の端には必ず配置される
     * (すなわち, ノードの数は2以上必要).
     * 
     * @param size ノードの数, 2以上でなければならない
     * @param interval ノードを配置する区間
     * @return ノード, 昇順に並んでいる
     * @throws NullPointerException null
     */
    public static double[] execute(int size, DoubleFiniteClosedInterval interval) {
        assert size >= 2 : "invalid size";

        double[] out = new double[size];
        for (int i = 1; i < size - 1; i++) {
            out[i] = Math.cos(Math.PI * (size - 1 - i) / (size - 1));
        }

        double lower = interval.lower();
        double upper = interval.upper();
        double halfGap = 0.5 * (upper - lower);
        double mid = lower + halfGap;

        for (int i = 1; i < size - 1; i++) {
            out[i] = out[i] * halfGap + mid;
        }
        out[0] = lower;
        out[size - 1] = upper;

        return out;
    }
}
