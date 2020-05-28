package com.diamond.avenue.language.expr;

import com.diamond.avenue.language.lib.Function;
import com.diamond.avenue.language.lib.Functions;
import com.diamond.avenue.language.lib.UsersFunction;
import com.diamond.avenue.language.lib.Value;
import com.diamond.avenue.language.lib.Variables;

import java.util.ArrayList;
import java.util.List;

public final class FunctionalExpression implements Expression {

    private final String name;
    private final List<Expression> arguments;

    public FunctionalExpression(String name) {
        this.name = name;
        this.arguments = new ArrayList<>();
    }

    public void addArgument(Expression i) {
        arguments.add(i);
    }

    @Override
    public Value eval() {
        final int size = arguments.size();
        final Value[] values = new Value[size];
        for (int i = 0; i < size; i++) {
            values[i] = arguments.get(i).eval();
        }

        final Function function = Functions.get(name);
        if (function instanceof UsersFunction) {
            final UsersFunction userFunction = (UsersFunction) function;
            if (size != userFunction.getArgsCount()) throw new RuntimeException("Args count mismatch");

            Variables.push();
            for (int i = 0; i < size; i++) {
                Variables.set(userFunction.getArgsName(i), values[i]);
            }
            final Value result = userFunction.execute(values);
            Variables.pop();
            return result;
        }
        return function.execute(values);
    }
}
