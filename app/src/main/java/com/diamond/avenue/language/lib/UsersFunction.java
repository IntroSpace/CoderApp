package com.diamond.avenue.language.lib;

import com.diamond.avenue.language.expr.ReturnStatement;
import com.diamond.avenue.language.expr.Statement;

import java.util.List;

public final class UsersFunction implements Function {

    private final List<String> argNames;
    private final Statement body;

    public UsersFunction(List<String> argNames, Statement body) {
        this.argNames = argNames;
        this.body = body;
    }

    public int getArgsCount() {
        return argNames.size();
    }

    public String getArgsName(int index) {
        if (index < 0 || index >= getArgsCount()) return "";
        return argNames.get(index);
    }

    @Override
    public Value execute(Value... args) {
        try {
            body.execute();
            return new NumberValue(0);
        } catch (ReturnStatement Return) {
            return Return.getResult();
        }
    }
}
