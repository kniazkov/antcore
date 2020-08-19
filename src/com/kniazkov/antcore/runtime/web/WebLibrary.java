/**
 * An ant, i.e. minimal execution unit that contains own memory space
 */
package com.kniazkov.antcore.runtime.web;

import com.kniazkov.antcore.basic.virtualmachine.NativeFunction;
import com.kniazkov.antcore.basic.virtualmachine.StandardLibrary;
import com.kniazkov.antcore.basic.virtualmachine.StringData;

import java.util.Map;
import java.util.TreeMap;

/**
 * The standard library for the web interface
 */
public class WebLibrary {
    /**
     * Constructs library for specific ant
     * @param ant the ant
     * @return set of functions
     */
    public static Map<String, NativeFunction> create(Ant ant) {
        Map<String, NativeFunction> functions = new TreeMap<>(StandardLibrary.getFunctions());

        functions.put("print", (memory, SP) -> {
            int address = memory.getInt(SP + 4);
            StringData stringData = StringData.read(memory, address);
            ant.instructions.add(new Print(stringData.toString()));
        });

        functions.put("createWidget", (memory, SP) -> {
            int address = memory.getInt(SP + 4);
            StringData type = StringData.read(memory, address);
            Widget widget = ant.createWidget(type.toString());
            if (widget != null) {
                ant.instructions.add(new CreateWidget(widget.getId(), widget.getType()));
                memory.setInt(SP + 4 + 4, widget.getId());
            }
            else {
                memory.setInt(SP + 4 + 4, -1);
            }
        });

        functions.put("appendWidget", (memory, SP) -> {
            int containerId = memory.getInt(SP + 4);
            int widgetId = memory.getInt(SP + 8);
            boolean result = false;
            Widget container = ant.widgets.get(containerId);
            Widget widget = ant.widgets.get(widgetId);
            if (container != null && widget != null) {
                result = container.appendChild(widget);
                if (result) {
                    ant.instructions.add(new AppendWidget(containerId, widgetId));
                }
            }
            memory.set(SP + 12, (byte) (result ? 1 : 0));
        });

        functions.put("setWidgetData", (memory, SP) -> {
            int widgetId = memory.getInt(SP + 4);
            int address = memory.getInt(SP + 8);
            StringData data = StringData.read(memory, address);
            String dataStr = data.toString();
            Widget widget = ant.widgets.get(widgetId);
            boolean result = false;
            if (widget != null) {
                result = widget.setData(dataStr);
                if (result) {
                    ant.instructions.add(new SetWidgetData(widgetId, dataStr));
                }
            }
            memory.set(SP + 12, (byte) (result ? 1 : 0));
        });

        functions.put("getWidgetData", (memory, SP) -> {
            int widgetId = memory.getInt(SP + 4);
            Widget widget = ant.widgets.get(widgetId);
            boolean result = false;
            if (widget != null) {
                String data = widget.getData();
                if (data != null) {
                    int length = data.length();
                    int address = memory.getInt(SP + 8);
                    int capacity = memory.getInt(address + 4);
                    if (length > capacity) length = capacity;
                    memory.setInt(address, length);
                    for (int k = 0; k < length; k++) {
                        char ch = data.charAt(k);
                        memory.setChar(address + 8 + k * 2, ch);
                    }
                    result = true;
                }
            }
            memory.set(SP + 12, (byte) (result ? 1 : 0));
        });

        functions.put("getWidgetClickCount", (memory, SP) -> {
            int widgetId = memory.getInt(SP + 4);
            Widget widget = ant.widgets.get(widgetId);
            int result = -1;
            if (widget != null) {
                result = widget.getClickCount();
            }
            memory.setInt(SP + 8, result);
        });

        return functions;
    }
}
