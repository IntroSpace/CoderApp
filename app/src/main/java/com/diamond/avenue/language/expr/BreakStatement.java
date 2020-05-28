package com.diamond.avenue.language.expr;

public final class BreakStatement extends RuntimeException implements Statement {
    @Override
    public void execute() {
        throw this;
    }
}
