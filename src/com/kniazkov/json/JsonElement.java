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

public abstract class JsonElement {

	public JsonElement(JsonElement parent) {
		this.parent = parent;
	}
	
	public JsonElement getParent() {
		return parent;
	}
	
	public String toString() {
		StringBuilder bld = new StringBuilder();
		buildString(bld);
		return bld.toString();
	}

	public String toStringWithIndents() {
		StringBuilder bld = new StringBuilder();
		buildString(bld, 0);
		return bld.toString();
	}
	
	public String stringValue() {
		return toString();
	}
	
	public int intValue() {
		return 0;
	}

	public long longValue() {
		return 0;
	}
	
	public double doubleValue() {
		return 0;
	}
	
	public boolean booleanValue() {
		return false;
	}
	
	public boolean isString() {
		return false;
	}
	
	public boolean isNumber() {
		return false;
	}
	
	public boolean isInteger() {
		return false;
	}
	
	public boolean isLongInteger() {
		return false;
	}

	public boolean isBoolean() {
		return false;
	}
	
	public boolean isNull() {
		return false;
	}
	
	public JsonContainer toJsonContainer() {
		return null;
	}
	
	public JsonObject toJsonObject() {
		return null;
	}

	public JsonArray toJsonArray() {
		return null;
	}
	
	protected static void buildIndent(StringBuilder bld, int indent) {
		if (indent > 0) {
			for (int i = 0; i < indent; i++)
				bld.append("  ");
		}
	}
	
	protected static void buildJsonString(StringBuilder bld, String value) {
		bld.append('"');
		for (int i = 0, len = value.length(); i < len; i++) {
			char c = value.charAt(i);
			switch(c) {
			case '"':
				bld.append("\\\"");
				break;
			case '\\':
				bld.append("\\\\");
				break;
			case '\b':
				bld.append("\\b");
				break;
			case '\f':
				bld.append("\\f");
				break;
			case '\n':
				bld.append("\\n");
				break;
			case '\r':
				bld.append("\\r");
				break;
			case '\t':
				bld.append("\\t");
				break;
			default:
				if (c < ' ' || c > 127)
					bld.append(String.format("\\u%04x", (int)c));
				else
					bld.append(c);
				break;
			}
		}
		bld.append('"');
	}

	protected void setParent(JsonElement elem) {
		parent = elem;
	}
	
	protected abstract void buildString(StringBuilder bld);
	protected abstract void buildString(StringBuilder bld, int indent);

	private JsonElement parent;
}
