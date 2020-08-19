package com.kniazkov.antcore.runtime;

import com.kniazkov.antcore.basic.bytecode.Binding;
import com.kniazkov.antcore.basic.bytecode.FullAddress;
import com.kniazkov.antcore.basic.bytecode.ShortAddress;
import com.kniazkov.antcore.basic.virtualmachine.VirtualMachine;

/**
 * A channel that connected VM memory with other module
 */
public class Channel {
    public Channel(Runtime runtime, VirtualMachine vm, Binding binding) {
        machine = vm;
        size = binding.getSize();
        buffer = new byte[size];
        FullAddress source = binding.getSource();
        srcExecutor = runtime.getExecutorByName(source.getExecutor());
        srcAddress = source;
        dstOffset = binding.getDestination().getOffset();
    }

    /**
     * Transmit data
     */
    public void transmit() {
        if (srcExecutor.read(srcAddress, size, buffer)) {
            machine.write(dstOffset, size, buffer);
        }
    }

    private VirtualMachine machine;
    private int size;
    private byte[] buffer;
    private Executor srcExecutor;
    private ShortAddress srcAddress;
    private int dstOffset;
}
