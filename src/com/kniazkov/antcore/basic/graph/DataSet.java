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
package com.kniazkov.antcore.basic.graph;

import com.kniazkov.antcore.basic.common.DataPrefix;
import com.kniazkov.antcore.basic.common.Fragment;
import com.kniazkov.antcore.basic.common.SyntaxError;
import com.kniazkov.antcore.basic.exceptions.FieldCanNotBeAbstract;
import com.kniazkov.antcore.basic.exceptions.FieldCanNotBeConstant;
import com.kniazkov.antcore.basic.exceptions.RecursiveNesting;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * The data set
 */
public class DataSet extends Node implements ExpressionOwner {
    public DataSet(Fragment fragment, DataPrefix prefix, List<Field> fields) {
        this.fragment = fragment;
        this.prefix = prefix;
        this.fieldList = Collections.unmodifiableList(fields);
        this.fieldMap = new TreeMap<>();
        for (Field field : fields) {
            fieldMap.put(field.getName(), field);
            field.setOwner(this);
        }

        this.size = UNKNOWN_SIZE;
    }

    @Override
    public void accept(NodeVisitor visitor) throws SyntaxError {
        visitor.visit(this);
    }

    @Override
    public void dfs(NodeVisitor visitor) throws SyntaxError {
        for (Field field : fieldList) {
            field.dfs(visitor);
        }
        accept(visitor);
    }

    void setOwner(DataSetOwner owner) {
        this.owner = owner;
    }

    @Override
    public Node getOwner() {
        return (Node)owner;
    }

    @Override
    public Fragment getFragment() {
        return fragment;
    }

    @Override
    public void toSourceCode(StringBuilder buff, String i, String i0) {
        buff.append(i).append("DATA");
        if (prefix != DataPrefix.DEFAULT)
            buff.append(" ").append(prefix);
        buff.append("\n");
        String i1 = i + i0;
        for (Field field : fieldList) {
            field.toSourceCode(buff, i1, i0);
        }
        buff.append(i).append("END DATA").append('\n');
    }

    public List<Field> getFields() {
        return fieldList;
    }

    /**
     * Calculate offsets of all fields
     */
    void calculateOffsets() throws SyntaxError {
        int offset = 0;
        for (Field field : fieldList) {
            field.setOffset(offset);
            offset += field.getType().getSize();
        }
    }

    /**
     * Calculate size of the data set
     */
    public int getSize() throws SyntaxError {
        if (size != UNKNOWN_SIZE && size != INVALID_SIZE )
            return size;

        if (size == INVALID_SIZE) {
            String name = null;
            Node owner = getOwner();
            if (owner instanceof DataType)
                name = ((DataType) owner).getName();
            throw new RecursiveNesting(getFragment(), name);
        }

        size = INVALID_SIZE;
        int newSize = 0;
        for (Field field : fieldList) {
            newSize +=  field.getType().getSize();
        }
        size = newSize;
        return newSize;
    }

    public int getOffset() {
        return offset;
    }

    void setOffset(int offset) {
        assert(this.offset == null);
        this.offset = offset;
    }

    public Field getFieldByName(String name) {
        return fieldMap.get(name);
    }

    /**
     * Check types of all fields
     */
    void checkTypes() throws SyntaxError {
        for (Field field : fieldList) {
            DataType type = field.getType();
            if (type.isConstant())
                throw new FieldCanNotBeConstant(field.getFragment());
            if (type.isAbstract())
                throw new FieldCanNotBeAbstract(field.getFragment());
        }
    }

    private DataSetOwner owner;
    private Fragment fragment;
    private DataPrefix prefix;
    private Map<String, Field> fieldMap;
    private List<Field> fieldList;
    private int size;
    private Integer offset;

    private static final int UNKNOWN_SIZE = -1;
    private static final int INVALID_SIZE = -2;
}
