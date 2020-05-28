package com.diamond.avenue.language.expr;

import com.diamond.avenue.language.lib.Value;
import com.diamond.avenue.language.lib.Variables;

public final class AssignmentStatement implements Statement {

    private final String variable;
    private final Expression expression;

    public AssignmentStatement(String variable, Expression expression) {
        this.variable = variable;
        this.expression = expression;
    }

    @Override
    public void execute() {
        final Value result = expression.eval();
        Variables.set(this.variable, result);
    }
}
