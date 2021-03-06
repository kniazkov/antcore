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

public final class JsonBoolean extends JsonElement {

	public JsonBoolean(JsonElement parent, boolean value) {
		super(parent);
		this.value = value;
	}

	@Override
	protected void buildString(StringBuilder bld) {
		bld.append(value);
	}

	@Override
	protected void buildString(StringBuilder bld, int indent) {
		bld.append(value);
	}

	@Override
	public boolean booleanValue() {
		return value;
	}

	@Override
	public boolean isBoolean() {
		return true;
	}
	
	private boolean value;
}
