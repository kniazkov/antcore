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
 * Mathematical routines that not covered by the standard Java libraries
 */
public class Math2 {
    private  static final long powersOf10[] = {
            1L,
            10L,
            100L,
            1000L,
            10000L,
            100000L,
            1000000L,
            10000000L,
            100000000L,
            1000000000L,
            10000000000L,
            100000000000L,
            1000000000000L,
            10000000000000L,
            100000000000000L,
            1000000000000000L,
            10000000000000000L,
            100000000000000000L,
            1000000000000000000L
    };

    public static long powerOf10(int n) {
        return powersOf10[n];
    }

    /**
     * Calculates sign of numeric object
     * @param obj the object
     * @return 0 for zeros, -1 for negative numbers, 1 for positive numbers, null for not numeric objects
     */
    public static Byte sign(Object obj) {
        if (obj instanceof Byte) {
            byte value = (Byte) obj;
            if (value == 0)
                return 0;
            else
                return (byte) (value > 0 ? 1 : -1);
        }
        if (obj instanceof Short) {
            short value = (Short) obj;
            if (value == 0)
                return 0;
            else
                return (byte) (value > 0 ? 1 : -1);
        }
        if (obj instanceof Integer) {
            int value = (Integer) obj;
            if (value == 0)
                return 0;
            else
                return (byte) (value > 0 ? 1 : -1);
        }
        if (obj instanceof Long) {
            long value = (Long) obj;
            if (value == 0)
                return 0;
            else
                return (byte) (value > 0 ? 1 : -1);
        }
        if (obj instanceof FixedPoint) {
            return ((FixedPoint) obj).sign();
        }
        return null;
    }
}
