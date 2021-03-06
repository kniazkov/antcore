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
package com.kniazkov.antcore.runtime.web;

import java.util.LinkedList;

/**
 * The container, i.e. widget that contains other widgets
 */
public abstract class Container extends Widget {
    public Container(int id) {
        super(id);
        widgets = new LinkedList<>();
    }

    @Override
    boolean appendChild(Widget child) {
        widgets.addLast(child);
        return true;
    }

    private LinkedList<Widget> widgets;
}
