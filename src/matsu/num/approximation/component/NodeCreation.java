/*
 * Copyright (c) 2024 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */
/*
 * 2024.6.23
 */
package matsu.num.approximation.component;

import matsu.num.approximation.DoubleFiniteClosedInterval;

/**
 * ノードを作成するユーティリティクラス.
 * 
 * @author Matsuura Y.
 * @version 18.0
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
     * @param size ノードの数, 2以上でなければならない
     * @param interval ノードを配置する区間
     * @return ノード, 昇順に並んでいる
     * @throws IllegalArgumentException sizeが不正の場合
     * @throws NullPointerException nullを含む場合
     */
    public static double[] execute(int size, DoubleFiniteClosedInterval interval) {
        if (size < 2) {
            throw new IllegalArgumentException("sizeが不正");
        }

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
