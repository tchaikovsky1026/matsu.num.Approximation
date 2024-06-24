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

/**
 * Newton polynomial interpolation of which the number of nodes is fixed.
 *
 * @author Matsuura Y.
 * @version 17.0
 */
public final class FixedSizeNewtonPolynomial implements NewtonPolynomial, Cloneable, Serializable {

    private static final long serialVersionUID = 6_00_01L;

    private final int size;

    private double[] node;
    private double[] newtonCoefficient;

    private transient double[] tempStoredNode;
    private transient double[] tempStoredNewtonCoefficient;

    /**
     * Create new Newton polynomial having the given node-number. <br>
     * The number of node is required to be &ge;1.
     *
     * @param numberOfNode the number of node (&ge; 1)
     */
    public FixedSizeNewtonPolynomial(int numberOfNode) {
        if (numberOfNode <= 0) {
            throw new IllegalArgumentException("illegal: node number");
        }
        this.size = numberOfNode;
        node = new double[numberOfNode];
        newtonCoefficient = new double[numberOfNode];
    }

    /**
     * Set cross points (<i>x</i>, <i>y</i>) to this, and calculate Newton
     * corfficients <i>b</i><sub>add</sub>. <br>
     * The array length of <i>x</i> and <i>y</i> are required to be equal to the
     * number of node which was set at constructor.
     *
     *
     * @param x x (= c<sub>add</sub>)
     * @param value y
     * @return this
     * @throws IllegalArgumentException illegal values, overlapped x, the array
     * length
     */
    public FixedSizeNewtonPolynomial setCrossPoints(double[] x, double[] value) {
        int thisSize = size;
        if (x.length != thisSize || value.length != thisSize) {
            throw new IllegalArgumentException("illegal: lengths of arrays.");
        }

        createTempStorage();
        System.arraycopy(node, 0, tempStoredNode, 0, size);
        System.arraycopy(newtonCoefficient, 0, tempStoredNewtonCoefficient, 0, size);

        double[] thisNode = node;
        double[] thisNewtonCoefficient = newtonCoefficient;

        for (int i = 0; i < thisSize; i++) {
            double value_i = value[i];
            double x_i = x[i];
            if (!(Double.isFinite(x_i)) || !(Double.isFinite(value_i))) {
                System.arraycopy(tempStoredNode, 0, node, 0, size);
                System.arraycopy(tempStoredNewtonCoefficient, 0, newtonCoefficient, 0, size);
                throw new IllegalArgumentException("arguments have illegal values.");
            }
            for (int k = 0; k < i; k++) {
                value_i -= thisNewtonCoefficient[k];
                double den = x_i - thisNode[k];
                if (den == 0) {
                    System.arraycopy(tempStoredNode, 0, node, 0, size);
                    System.arraycopy(tempStoredNewtonCoefficient, 0, newtonCoefficient, 0, size);
                    throw new IllegalArgumentException("controlPoint is overlapped.");
                }
                value_i /= den;
            }
            thisNode[i] = x_i;
            thisNewtonCoefficient[i] = value_i;
        }
        return this;
    }

    @Override
    public double value(double x) {
        double value = 0;
        double[] thisNode = node;
        double[] thisNewtonCoefficient = newtonCoefficient;
        for (int i = size - 1; i >= 0; i--) {
            value *= x - thisNode[i];
            value += thisNewtonCoefficient[i];
        }
        return value;
    }

    @Override
    public int degree() {
        return size - 1;
    }

    /**
     * Return a reference of the node <i>c</i><sub><i>i</i></sub>.
     */
    @Override
    public double[] getNode() {
        return node;
    }

    /**
     * Return a reference of the Newton coefficients
     * <i>b</i><sub><i>i</i></sub>.
     */
    @Override
    public double[] getNewtonCoefficient() {
        return newtonCoefficient;
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
            double cp_smim1 = node[thisSize - 1 - i];
            for (int j = 0; j < i; j++) {
                tempCoeff[j] -= cp_smim1 * polyCoeff[j];
            }
            for (int j = 1; j <= i; j++) {
                tempCoeff[j] += polyCoeff[j - 1];
            }
            tempCoeff[0] += newtonCoefficient[thisSize - 1 - i];
            System.arraycopy(tempCoeff, 0, polyCoeff, 0, i + 1);
        }
        return polyCoeff;
    }

    @Override
    public FixedSizeNewtonPolynomial clone() {
        FixedSizeNewtonPolynomial out;
        try {
            out = (FixedSizeNewtonPolynomial) super.clone();
            out.node = this.node.clone();
            out.newtonCoefficient = this.newtonCoefficient.clone();
        } catch (CloneNotSupportedException | ClassCastException e) {
            throw new IllegalStateException("failed: clone");
        }
        return out;
    }

    private void createTempStorage() {
        if (tempStoredNode == null) {
            tempStoredNode = new double[size];
        }
        if (tempStoredNewtonCoefficient == null) {
            tempStoredNewtonCoefficient = new double[size];
        }
    }
}
