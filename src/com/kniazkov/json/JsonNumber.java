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

public final class JsonNumber extends JsonElement {
	public JsonNumber(JsonElement parent, double value) {
		super(parent);
		this.value = value;
	}

	@Override
	protected void buildString(StringBuilder bld) {
		String tmp;
		if (value == (long)value)
			tmp = String.format("%d", (long)value);
		else
			tmp = String.format("%s", value);
		bld.append(tmp);
	}

	@Override
	protected void buildString(StringBuilder bld, int indent) {
		buildString(bld);
	}
	
	public int intValue() {
		int intVal = (int)value;
		return intVal == value ? intVal : 0;
	}
	
	public long longValue() {
		long longVal = (long)value;
		return longVal == value ? longVal : 0;
	}
	
	public double doubleValue() {
		return value;
	}
	
	public boolean isNumber() {
		return true;
	}
	
	public boolean isInteger() {
		return value == (int)value;
	}
	
	public boolean isLongInteger() {
		return value == (long)value;
	}
	
	private double value;
}
