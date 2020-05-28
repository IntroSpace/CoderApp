package com.diamond.avenue.language.expr;

public class WhileStatement implements Statement {
    private final Expression expression;
    private final Statement statement;

    public WhileStatement(Expression expression, Statement statement) {
        this.expression = expression;
        this.statement = statement;
    }

    @Override
    public void execute() {
        while(expression.eval().asNumber() != 0) {
            try {
                statement.execute();
            } catch (BreakStatement Break) {
                break;
            } catch (ContinueStatement Continue) { }
        }
    }
}
