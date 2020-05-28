package com.diamond.avenue.language.expr;

import com.diamond.avenue.language.lib.NumberValue;
import com.diamond.avenue.language.lib.Value;

public final class UnaryExpression implements Expression {

    private final Expression expr1;
    private final char operation;

    public UnaryExpression(char operation, Expression expr1) {
        this.operation = operation;
        this.expr1 = expr1;
    }

    @Override
    public Value eval() {
        switch (operation) {
            case '-': return new NumberValue(-(expr1.eval()).asNumber());
            case '+':
            default:
                return expr1.eval();
        }
    }
}
