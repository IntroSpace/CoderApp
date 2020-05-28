package com.diamond.avenue.language.expr;

import com.diamond.avenue.language.lib.NumberValue;
import com.diamond.avenue.language.lib.StringValue;
import com.diamond.avenue.language.lib.Variables;

public class InputStatement implements Statement {
    private final Expression expression;

    public InputStatement(Expression expression) {
        this.expression = expression;
    }

    @Override
    public void execute() {
        this.execute("");
    }

    public void execute(String input) {
        //String input = "";
        //if (expression instanceof ConstantExpression) input = AvenueSystem.in.read();
        try {
            Variables.set(((ConstExpression) expression).name, new NumberValue(Integer.parseInt(input)));
        } catch (Exception e) {
            Variables.set(((ConstExpression) expression).name, new StringValue(input));
        }
    }
}
