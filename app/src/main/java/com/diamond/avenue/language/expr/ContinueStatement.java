package com.diamond.avenue.language.expr;

public final class ContinueStatement extends RuntimeException implements Statement {
    @Override
    public void execute() {
        throw this;
    }
}
