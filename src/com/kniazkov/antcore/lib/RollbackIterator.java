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

import java.util.Iterator;
import java.util.Stack;

/**
 * An iterator into which a previously taken object can be returned
 * if this object was not used
 */
public class RollbackIterator<T> implements Iterator<T> {
    public RollbackIterator(Iterator<T> iterator) {
        this.iterator = iterator;
    }

    @Override
    public boolean hasNext() {
        if (stack != null && !stack.empty())
            return true;
        return iterator.hasNext();
    }

    @Override
    public T next() {
        if (stack != null && !stack.empty())
            return stack.pop();
        return iterator.hasNext() ? iterator.next() : null;
    }

    public void rollback(T object) {
        if (stack == null)
            stack = new Stack<>();
        stack.push(object);
    }

    public T peek() {
        if (stack == null) {
            if (iterator.hasNext()) {
                T object = iterator.next();
                stack = new Stack<>();
                stack.push(object);
                return object;
            }
            return null;
        }
        else {
            if (!stack.empty())
                return stack.peek();
            if (iterator.hasNext()) {
                T object = iterator.next();
                stack.push(object);
                return object;
            }
            return null;
        }
    }

    private Iterator<T> iterator;
    private Stack<T> stack;
}
