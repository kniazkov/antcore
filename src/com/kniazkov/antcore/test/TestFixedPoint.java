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
package com.kniazkov.antcore.test;

import com.kniazkov.antcore.lib.FixedPoint;

/**
 * Tests for FixedPoint class
 */
public class TestFixedPoint {
    public static void main(String[] args) {
        FixedPoint a = new FixedPoint();
        FixedPoint b = new FixedPoint();
        FixedPoint c = new FixedPoint();

/*
        FixedPoint pi = new FixedPoint();
        pi.setFloat(Math.PI);
        System.out.println(pi.toString(4, false));
        System.out.println(pi.toString());

        a.setInteger(0);
        System.out.println(a.toString());
        System.out.println(a.toString(2, false));
        System.out.println(a.toString(2, true));

        a.setInteger(1);
        b.setInteger(100);
        FixedPoint.div(a, a, b);
        System.out.println(a.toString());
        System.out.println(a.toString(1, false));
        System.out.println(a.toString(1, true));

        a.setInteger(777);
        b.setInteger(1000);
        FixedPoint.div(a, a, b);
        System.out.println(a.toString());
        System.out.println(a.toString(2, false));
        System.out.println(a.toString(1, false));
        System.out.println(a.toString(0, false));
 */

        a.setInteger(5);
        b.setInteger(1000000000);
        c.setFloat(0.001);
        FixedPoint.add(b, b, c);
        FixedPoint.mul(c, a, b);
        FixedPoint.div(c, c, b);
        System.out.println(c.toString());
    }
}
