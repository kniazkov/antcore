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
package com.kniazkov.antcore.basic.parser;

import com.kniazkov.antcore.basic.common.Fragment;
import com.kniazkov.antcore.basic.common.SyntaxError;
import com.kniazkov.antcore.basic.exceptions.DuplicateField;
import com.kniazkov.antcore.basic.graph.DataType;
import com.kniazkov.antcore.basic.graph.Field;
import com.kniazkov.antcore.basic.graph.Struct;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * The parsed struct
 */
public class RawStruct extends RawDataType {
    RawStruct(Fragment fragment, String name) {
        this.fragment = fragment;
        this.name = name;
        rawFieldsList = new ArrayList<>();
        rawFieldsMap = new TreeMap<>();
    }

    public void addField(RawField field) throws SyntaxError {
        String name = field.getName();
        if (rawFieldsMap.containsKey(name))
            throw new DuplicateField(field.getFragment(), name);
        rawFieldsList.add(field);
        rawFieldsMap.put(name, field);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public DataType toNode() {
        List<Field> fields = new ArrayList<>();
        for (RawField rawField : rawFieldsList) {
            fields.add(rawField.toNode());
        }
        return new Struct(fragment, name, fields);
    }

    private Fragment fragment;
    private String name;
    private List<RawField> rawFieldsList;
    private Map<String, RawField> rawFieldsMap;
}
