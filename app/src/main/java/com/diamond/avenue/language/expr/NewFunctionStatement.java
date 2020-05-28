package com.diamond.avenue.language.expr;

import com.diamond.avenue.language.lib.Functions;
import com.diamond.avenue.language.lib.UsersFunction;

import java.util.List;

public final class NewFunctionStatement implements Statement {

    private final String name;
    private final List<String> argNames;
    private final Statement body;

    public NewFunctionStatement(String name, List<String> argNames, Statement body) {
        this.name = name;
        this.argNames = argNames;
        this.body = body;
    }

    @Override
    public void execute() {
        Functions.set(name, new UsersFunction(argNames, body));
    }
}
