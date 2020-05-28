package com.diamond.avenue.language.expr;

import com.diamond.avenue.language.lib.Value;
import com.diamond.avenue.language.lib.Variables;

public class ConstExpression implements Expression {

    public final String name;

    public ConstExpression(String name) {
        this.name = name;
    }

    @Override
    public Value eval() {
        return Variables.get(name);
    }
}