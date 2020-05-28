package com.diamond.avenue.language.expr;

import com.diamond.avenue.language.lib.NumberValue;
import com.diamond.avenue.language.lib.StringValue;
import com.diamond.avenue.language.lib.Value;

public final class ConditionalExpression implements Expression {

    public enum Operator {
        EQUALS,         // ==
        NOT_EQUALS,     // !=

        LT,             // <
        LTEQ,           // <=
        GT,             // >
        GTEQ,           // >=

        AND,            // &&
        OR              // ||
    }

    private final Expression expr1, expr2;
    private final Operator operation;

    public ConditionalExpression(Operator operation, Expression expr1, Expression expr2) {
        this.operation = operation;
        this.expr1 = expr1;
        this.expr2 = expr2;
    }

    @Override
    public Value eval() {
        final Value value1 = expr1.eval();
        final Value value2 = expr2.eval();

        double number1, number2;
        if (value1 instanceof StringValue || value2 instanceof StringValue) {
            number1 = value1.asString().compareTo(value2.asString());
            number2 = 0;
        } else {
            number1 = value1.asNumber();
            number2 = value2.asNumber();
        }

        boolean result;
        switch (operation) {
            case LT: result = number1 < number2; break;
            case GT: result = number1 > number2; break;
            case LTEQ: result = number1 <= number2; break;
            case GTEQ: result = number1 >= number2; break;
            case NOT_EQUALS: result = number1 != number2; break;

            case AND: result = (number1 != 0) && (number2 != 0); break;
            case OR: result = (number1 != 0) || (number2 != 0); break;

            case EQUALS:
            default:
                result = number1 == number2;
        } return new NumberValue(result);
    }
}
