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

import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;

public final class JsonObject extends JsonContainer
{
	public JsonObject(JsonElement parent) {
		super(parent);
		elements = new TreeMap<>();
	}

	protected void buildString(StringBuilder bld) {
		bld.append('{');
		boolean flag = false;
		for (Map.Entry<String, JsonElement> entry : elements.entrySet()) {
			
			if (flag)
				bld.append(',');
			String name = entry.getKey();
			JsonElement elem = entry.getValue();
			bld.append('"');
			bld.append(name);
			bld.append("\":");
			elem.buildString(bld);
			flag = true;
		}
		bld.append('}');
	}

	protected void buildString(StringBuilder bld, int indent) {
		if (elements.isEmpty()) {
			bld.append("{ }");
			return;
		}
		bld.append('{');
		boolean flag = false;
		for (Map.Entry<String, JsonElement> entry : elements.entrySet()) {
			if (flag)
				bld.append(',');
			bld.append('\n');
			buildIndent(bld, indent + 1);
			String name = entry.getKey();
			JsonElement elem = entry.getValue();
			buildJsString(bld, name);
			JsonContainer jc = elem.toJsContainer();
			if (jc != null && !jc.isEmpty()) {
				bld.append(" :\n");
				buildIndent(bld, indent + 1);
			}
			else {
				bld.append(" : ");
			}
			elem.buildString(bld, indent + 1);
			flag = true;
		}
		bld.append('\n');
		buildIndent(bld, indent);
		bld.append('}');
	}
	
	public JsonObject toJsObject() {
		return this;
	}
	
	public int size() {
		return elements.size();
	}
	
	public boolean containsKey(String key) {
		return elements.containsKey(key);
	}
	
	public JsonElement get(String key) {
		return elements.get(key);
	}
	
	public boolean isEmpty() {
		return elements.isEmpty();
	}

	public JsonString createString(String key, String value) {
		JsonString elem = new JsonString(this, value);
		elements.put(key, elem);
		return elem;
	}

	public JsonNumber createNumber(String key, double value) {
		JsonNumber elem = new JsonNumber(this, value);
		elements.put(key, elem);
		return elem;
	}

	public JsonBoolean createBoolean(String key, boolean value) {
		JsonBoolean elem = new JsonBoolean(this, value);
		elements.put(key, elem);
		return elem;
	}

	public JsonNull createNull(String key) {
		JsonNull elem = new JsonNull(this);
		elements.put(key, elem);
		return elem;
	}
	
	public JsonObject createObject(String key) {
		JsonObject elem = new JsonObject(this);
		elements.put(key, elem);
		return elem;
	}
	
	public JsonArray createArray(String key) {
		JsonArray elem = new JsonArray(this);
		elements.put(key, elem);
		return elem;
	}
	
	public void addElement(String key, JsonElement elem) {
		elements.put(key, elem);
	}

	public Map<String, JsonElement> getElements() {
		return Collections.unmodifiableMap(elements);
	}

	private Map<String, JsonElement> elements;
}
