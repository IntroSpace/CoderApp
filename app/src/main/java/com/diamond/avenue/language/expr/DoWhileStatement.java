package com.diamond.avenue.language.expr;

public class DoWhileStatement implements Statement {
    private final Expression expression;
    private final Statement statement;

    public DoWhileStatement(Expression expression, Statement statement) {
        this.expression = expression;
        this.statement = statement;
    }

    @Override
    public void execute() {
        do {
            try {
                statement.execute();
            } catch (BreakStatement Break) {
                break;
            } catch (ContinueStatement Continue) { }
        } while(expression.eval().asNumber() != 0);
    }
}
