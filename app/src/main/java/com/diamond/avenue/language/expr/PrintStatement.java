package com.diamond.avenue.language.expr;

import com.diamond.avenue.language.AvenueSystem;

public class PrintStatement implements Statement {
    private final Expression expression;

    public PrintStatement(Expression expression) {
        this.expression = expression;
    }

    @Override
    public void execute() {
        AvenueSystem.out.print(expression.eval().asString());
    }
}
