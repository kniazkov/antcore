/*
 * Copyright (C) 2020 Ivan Kniazkov
 *
 * This file is part of Antcore.
 *
 * Antcore is free software: you can redistribute it and/or modify it under the terms of
 * the GNU General Public License as published by the Free Software Foundation,
 * either version 3 of the License, or (at your option) any later version.
 *
 * Antcore is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with Antcore.
 * If not, see <http://www.gnu.org/licenses/>.
 */
package com.kniazkov.antcore.lib;

/**
 * The fixed point number
 */
public final class FixedPoint {
    public FixedPoint() {
        X = 0;
    }

    public void setInteger(long value) {
        X = value * factor;
    }

    public void setFloat(double value) {
        X = Math.round(value * factor);
    }

    public double getFloat() {
        return (double)X / factor;
    }

    public void setFixedAsLong(long value) {
        X = value;
    }

    public long getFixedAsLong() {
        return X;
    }

    public boolean isInteger() {
        return X % factor == 0;
    }

    public long getInteger() {
        return X / factor;
    }

    public String toString(int accuracy, boolean printZeros) {
        long val = X;
        boolean neg = false;
        if (val < 0) {
            val = -val;
            neg = true;
        }
        if (accuracy < 0)
            accuracy = 0;
        long intPart;
        long fractionPart;
        if (accuracy >= precision) {
            accuracy = precision;
            intPart = (val / factor);
            fractionPart = val - intPart * factor;
        }
        else {
            int cut = precision - accuracy;
            val = val / Math2.powerOf10(cut - 1);
            if (val % 10 >= 5) {
                val = val / 10;
                val++;
            } else {
                val = val / 10;
            }
            long factor2 = Math2.powerOf10(accuracy);
            intPart = (val / factor2);
            fractionPart = val - intPart * factor2;
        }
        StringBuilder buff = new StringBuilder();
        boolean flag = false;
        for (int k = 0; k < accuracy; k++) {
             long dig = fractionPart % 10;
             if (dig > 0 || printZeros) {
                 char ch = (char)(dig + '0');
                 buff.append(ch);
                 printZeros = true;
                 flag = true;
             }
            fractionPart = fractionPart / 10;
        }
        if (flag)
            buff.append('.');
        do {
            char ch = (char)(intPart % 10 + '0');
            buff.append(ch);
            intPart = intPart / 10;
        } while(intPart != 0);
        if (neg)
            buff.append('-');
        buff.reverse();
        return buff.toString();
    }

    @Override
    public String toString() {
        return toString(precision, false);
    }

    public byte sign() {
        if (X == 0)
            return 0;
        return (byte) (X > 0 ? 1 : -1);
    }

    public static void add(FixedPoint dst, FixedPoint op1, FixedPoint op2) {
        dst.X = op1.X + op2.X;
    }

    public static void sub(FixedPoint dst, FixedPoint op1, FixedPoint op2) {
        dst.X = op1.X - op2.X;
    }

    public static void mul(FixedPoint dst, FixedPoint op1, FixedPoint op2) {
        dst.X = (op1.X * op2.X) / factor;
    }

    public static void div(FixedPoint dst, FixedPoint op1, FixedPoint op2) {
        dst.X = op1.X * factor / op2.X;
    }

    private static final int precision = 4;
    private static final long factor = 10000;
    public static final long MAX_VALUE = Long.MAX_VALUE / factor;
    public static final long MIN_VALUE = Long.MIN_VALUE / factor;

    private long X;
}
