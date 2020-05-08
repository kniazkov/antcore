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

import com.kniazkov.antcore.basic.DataPrefix;
import com.kniazkov.antcore.basic.Fragment;
import com.kniazkov.antcore.basic.SyntaxError;
import com.kniazkov.antcore.basic.graph.DataSet;
import com.kniazkov.antcore.basic.graph.DataSetOwner;
import com.kniazkov.antcore.basic.graph.Field;
import com.kniazkov.antcore.basic.graph.Node;
import com.kniazkov.antcore.basic.exceptions.DuplicateField;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * The parsed list of fields
 */
public class RawDataSet extends Entity {
    RawDataSet(Fragment fragment, DataPrefix prefix, DataPrefix prefixDefault) {
        this.fragment = fragment;
        this.prefix = prefix;
        this.prefixDefault = prefixDefault;
        rawFields = new TreeMap<>();
    }

    public void addField(RawField field) throws SyntaxError {
        String name = field.getName();
        if (rawFields.containsKey(name))
            throw new DuplicateField(field.getFragment(), name);
        rawFields.put(name, field);
    }

    public Fragment getFragment() {
        return fragment;
    }

    public DataPrefix getPrefix() {
        return prefix;
    }

    public DataSet toNode() {
        List<Field> fields = new ArrayList<>();
        for (Map.Entry<String, RawField> rawFieldEntry : rawFields.entrySet()) {
            fields.add(rawFieldEntry.getValue().toNode());
        }
        return new DataSet(fragment, prefix != prefixDefault ? prefix : DataPrefix.DEFAULT, fields);
    }

    private Fragment fragment;
    private DataPrefix prefix;
    private DataPrefix prefixDefault;
    private Map<String, RawField> rawFields;
}
