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

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;

/**
 * File input/output routines
 */
public final class FileIO {
    public static String readFileToString(String path) {
        java.io.File file = new java.io.File(path);
        if (file.exists()) {
            try {
                byte[] data = Files.readAllBytes(file.toPath());
                return new String(data);
            } catch (IOException e) {
                System.err.println("Can't read '" + path + '\'');
            }
        } else {
            System.err.println("Can't find file specified '" + path + '\'');
        }
        return null;
    }

    public static boolean writeStringToFile(String path, String data) {
        try {
            FileWriter writer = new FileWriter(path);
            writer.write(data);
            writer.close();
            return true;
        } catch (IOException e) {
            System.err.println("Can't write '" + path + '\'');
        }
        return false;
    }
}
