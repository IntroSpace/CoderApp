package com.diamond.avenue.language.expr;

import java.util.ArrayList;
import java.util.List;

public class BlockStatement implements Statement {
    private final List<Statement> statements;

    public BlockStatement () {
        statements = new ArrayList<>();
    }

    public void add (Statement obj) {
        statements.add(obj);
    }

    public List<Statement> getStatements () {
        return this.statements;
    }

    @Override
    public void execute () {
        for (Statement statement : statements) {
            statement.execute();
        }
    }
}
