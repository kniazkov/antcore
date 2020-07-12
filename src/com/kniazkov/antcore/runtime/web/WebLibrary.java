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
            synchronized (ant) {
                ant.instructions.add(new Print(stringData.toString()));
            }
        });

        functions.put("createWidget", (memory, SP) -> {
            int address = memory.getInt(SP + 4);
            StringData type = StringData.read(memory, address);
            Widget widget = ant.createWidget(type.toString());
            if (widget != null) {
                synchronized (ant) {
                    ant.instructions.add(new CreateWidget(widget.getId(), widget.getType()));
                }
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
            if (container instanceof Container && widget != null) {
                ((Container) container).appendWidget(widget);
                synchronized (ant) {
                    ant.instructions.add(new AppendWidget(containerId, widgetId));
                }
                result = true;
            }
            memory.set(SP + 12, (byte) (result ? 1 : 0));
        });

        return functions;
    }
}
