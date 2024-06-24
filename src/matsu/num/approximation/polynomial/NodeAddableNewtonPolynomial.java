/*
 * Copyright (c) 2024 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */
/*
 * 2024.6.20
 */
package matsu.num.approximation.polynomial;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Node-addable Newton polynomial interpolation. <br>
 * The node <i>c</i><sub><i>i</i></sub> can be added sequencially. <br>
 * The formula of Newton polynomial is <br>
 * <i>p</i>(<i>x</i>) = <i>b</i><sub>0</sub> +
 * <i>b</i><sub>1</sub>(<i>x</i> - <i>c</i><sub>0</sub>) +
 * <i>b</i><sub>2</sub>(<i>x</i> - <i>c</i><sub>0</sub>)(<i>x</i> -
 * <i>c</i><sub>1</sub>) + &hellip; +
 * <i>b</i><sub><i>n</i> - 1</sub>(<i>x</i> - <i>c</i><sub>0</sub>)(<i>x</i> -
 * <i>c</i><sub>1</sub>)...(<i>x</i> - <i>c</i><sub><i>n</i> - 2</sub>).
 * <br>
 * <i>c</i><sub><i>i</i></sub>: node. <br>
 * <i>b</i><sub><i>i</i></sub>: Newton coefficient.
 *
 * @author Matsuura Y.
 * @version 17.0
 */
public final class NodeAddableNewtonPolynomial implements NewtonPolynomial, Cloneable, Serializable {

    private static final long serialVersionUID = 6_00_01L;

    private int size = 0;

    private ArrayList<Double> listNode = new ArrayList<>();
    private ArrayList<Double> listNewtonCoefficient = new ArrayList<>();

    /**
     * Create new Newton polynomial having no node.
     */
    public NodeAddableNewtonPolynomial() {
    }

    /**
     * Add cross point (<i>x</i>, <i>y</i>) to this, and calculate additional
     * Newton corfficient <i>b</i><sub>add</sub>.
     *
     * @param x x (= c<sub>add</sub>, node)
     * @param value y
     * @return this
     * @throws IllegalArgumentException illegal values, overlapped x
     */
    public NodeAddableNewtonPolynomial addCrossPoint(double x, double value) {
        if (!Double.isFinite(x) || !Double.isFinite(value)) {
            throw new IllegalArgumentException("arguments have illegal values.");
        }
        for (int k = 0, thisSize = size; k < thisSize; k++) {
            value -= listNewtonCoefficient.get(k);
            double den = x - listNode.get(k);
            if (den == 0) {
                throw new IllegalArgumentException("node is overlapped.");
            }
            value /= den;
        }
        listNode.add(x);
        listNewtonCoefficient.add(value);
        size++;
        return this;
    }

    /**
     * Calculate the value of the polynomial at the given point. <br>
     * If having no node, return 0.
     */
    @Override
    public double value(double x) {
        double value = 0;
        for (int i = size - 1; i >= 0; i--) {
            value *= x - listNode.get(i);
            value += listNewtonCoefficient.get(i);
        }
        return value;
    }

    /**
     * Return degree of the polynomial. <br>
     * When having no node, return -1.
     */
    @Override
    public int degree() {
        return size - 1;

    }

    /**
     * Return a cooy of the node <i>c</i><sub><i>i</i></sub>.
     */
    @Override
    public double[] getNode() {
        double[] out;
        out = new double[size];
        int i = 0;
        for (double node : listNode) {
            out[i] = node;
            i++;
        }
        return out;
    }

    /**
     * Return a copy of the Newton coefficients <i>b</i><sub><i>i</i></sub>.
     */
    @Override
    public double[] getNewtonCoefficient() {
        double[] out;
        out = new double[size];
        int i = 0;
        for (double coeff : listNewtonCoefficient) {
            out[i] = coeff;
            i++;
        }
        return out;
    }

    @Override
    public double[] getPolynomialCoefficient() {
        final int thisSize = size;
        double[] polyCoeff = new double[thisSize];
        double[] tempCoeff = new double[thisSize];
        for (int i = 0; i < thisSize; i++) {
            for (int j = 0; j <= i; j++) {
                tempCoeff[j] = 0;
            }
            double cp_smim1 = listNode.get(thisSize - 1 - i);
            for (int j = 0; j < i; j++) {
                tempCoeff[j] -= cp_smim1 * polyCoeff[j];
            }
            for (int j = 1; j <= i; j++) {
                tempCoeff[j] += polyCoeff[j - 1];
            }
            tempCoeff[0] += listNewtonCoefficient.get(thisSize - 1 - i);
            System.arraycopy(tempCoeff, 0, polyCoeff, 0, i + 1);
        }
        return polyCoeff;
    }

    /**
     * Delete all nodes.
     */
    public void clear() {
        size = 0;
        listNode.clear();
        listNewtonCoefficient.clear();
    }

    @Override
    public NodeAddableNewtonPolynomial clone() {
        NodeAddableNewtonPolynomial out;
        try {
            out = (NodeAddableNewtonPolynomial) super.clone();
            out.listNewtonCoefficient = new ArrayList<>(this.listNewtonCoefficient.size());
            out.listNewtonCoefficient.addAll(this.listNewtonCoefficient);
            out.listNode = new ArrayList<>(this.listNode.size());
            out.listNode.addAll(this.listNode);
        } catch (CloneNotSupportedException | ClassCastException e) {
            throw new IllegalStateException("failed: clone");
        }
        return out;
    }
}
