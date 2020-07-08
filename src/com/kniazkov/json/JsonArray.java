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
package com.kniazkov.json;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class JsonArray extends JsonContainer {

	public JsonArray(JsonElement parent) {
		super(parent);
		elements = new ArrayList<JsonElement>();
	}

	@Override
	protected void buildString(StringBuilder bld) {
		bld.append('[');
		boolean flag = false;
		for (JsonElement elem : elements)
		{
			if (flag)
			{
				bld.append(',');
			}
			elem.buildString(bld);
			flag = true;
		}
		bld.append(']');
	}

	@Override
	protected void buildString(StringBuilder bld, int indent) {
		if (elements.isEmpty())
		{
			bld.append("[ ]");
			return;
		}
		bld.append('[');
		boolean flag = false;
		for (JsonElement elem : elements)
		{
			if (flag)
				bld.append(',');
			bld.append('\n');
			buildIndent(bld, indent + 1);
			elem.buildString(bld, indent + 1);
			flag = true;
		}
		bld.append('\n');
		buildIndent(bld, indent);
		bld.append(']');
	}

	@Override
	public JsonArray toJsonArray() {
		return this;
	}

	@Override
	public int size() {
		return elements.size();
	}

	public JsonElement getAt(int index) {
		return elements.get(index);
	}

	@Override
	public boolean isEmpty() {
		return elements.isEmpty();
	}
	
	public JsonString createString(String value) {
		JsonString elem = new JsonString(this, value);
		elements.add(elem);
		return elem;
	}

	public JsonNumber createNumber(double value) {
		JsonNumber elem = new JsonNumber(this, value);
		elements.add(elem);
		return elem;
	}

	public JsonBoolean createBoolean(boolean value) {
		JsonBoolean elem = new JsonBoolean(this, value);
		elements.add(elem);
		return elem;
	}

	public JsonNull createNull() {
		JsonNull elem = new JsonNull(this);
		elements.add(elem);
		return elem;
	}
	
	public JsonObject createObject() {
		JsonObject elem = new JsonObject(this);
		elements.add(elem);
		return elem;
	}
	
	public JsonArray createArray() {
		JsonArray elem = new JsonArray(this);
		elements.add(elem);
		return elem;
	}

	public void add(JsonElement elem) {
		elem.setParent(this);
		elements.add(elem);
	}

	void addElement(JsonElement elem) {
		elements.add(elem);
	}

	public List<JsonElement> getElements() {
		return Collections.unmodifiableList(elements);
	}

	private List<JsonElement> elements;
}
