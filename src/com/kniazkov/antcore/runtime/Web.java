package com.kniazkov.antcore.runtime;

import com.kniazkov.antcore.basic.bytecode.CompiledModule;

public class Web extends Executor {
    @Override
    protected boolean tick() {
        System.out.println("tick " + getTicks());
        return true;
    }

    @Override
    public String getName() {
        return "WEB";
    }

    @Override
    public void setModuleList(CompiledModule[] modules) {
        this.modules = modules;
    }

    private CompiledModule[] modules;
}
