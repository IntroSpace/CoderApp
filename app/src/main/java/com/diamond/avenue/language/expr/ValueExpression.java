package com.diamond.avenue.language.expr;

import com.diamond.avenue.language.lib.NumberValue;
import com.diamond.avenue.language.lib.StringValue;
import com.diamond.avenue.language.lib.Value;

public final class ValueExpression implements Expression {

    private final Value value;

    public ValueExpression(double value) {
        this.value = new NumberValue(value);
    }

    public ValueExpression(String value) {
        this.value = new StringValue(value);
    }

    public ValueExpression(Value value) {
        this.value = value;
    }

    @Override
    public Value eval() {
        return value;
    }
}

