/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package matsu.num.approximation.polynomial;

import static org.junit.Assert.*;

import java.util.function.DoubleUnaryOperator;

import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author Matsuura, Y.
 */
public class FixedSizeNewtonPolynomialTest {

    private int size;
    private double[] polyCoefficient;
    private DoubleUnaryOperator supplier;

    private double[] node;
    private double[] value;
    private FixedSizeNewtonPolynomial polynomial;

    @Before
    public void createPolynomial() {
        final int thisSize = 6;

        this.size = thisSize;
        polyCoefficient = new double[thisSize];
        for (int i = 0; i < thisSize; i++) {
            polyCoefficient[i] = Math.random();
        }
        supplier = (double x) -> {
            double val = 0;
            for (int i = thisSize - 1; i >= 0; i--) {
                val *= x;
                val += polyCoefficient[i];
            }
            return val;
        };

        node = new double[thisSize];
        value = new double[thisSize];
        for (int i = 0; i < thisSize; i++) {
            node[i] = Math.random() - 0.5 + i - (thisSize / 2);
        }
        for (int i = 0; i < thisSize; i++) {
            value[i] = supplier.applyAsDouble(node[i]);
        }
        polynomial = new FixedSizeNewtonPolynomial(thisSize);
        polynomial.setCrossPoints(node, value);
    }

    @Test
    public void testValue() {
        for (int i = 0; i < 10; i++) {
            double x = Math.random();
            double v1 = supplier.applyAsDouble(x);
            double v2 = polynomial.value(x);
            assertEquals(0, v1 - v2, 1E-12);
        }
    }

    @Test
    public void testDegree() {
        assertEquals(size - 1, polynomial.degree());
    }

    @Test
    public void testGetPolynomialCoefficient() {
        double[] pc1 = polyCoefficient;
        double[] pc2 = polynomial.getPolynomialCoefficient();
        assertArrayEquals(pc1, pc2, 1E-12);
    }

}
