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

import com.kniazkov.antcore.basic.DataPrefix;
import com.kniazkov.antcore.basic.Fragment;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * The data set
 */
public class DataSet extends Node {
    public DataSet(Fragment fragment, DataPrefix prefix, List<Field> fields) {
        super(fragment);
        this.prefix = prefix;
        this.fieldList = fields;

        this.fieldMap = new TreeMap<>();
        for (Field field : fields) {
            fieldMap.put(field.getName(), field);
        }
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
        buff.append(i).append("END DATA\n");
    }

    private DataPrefix prefix;
    private Map<String, Field> fieldMap;
    private List<Field> fieldList;
}
