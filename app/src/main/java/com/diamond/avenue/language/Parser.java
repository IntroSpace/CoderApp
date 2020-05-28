package com.diamond.avenue.language;

import com.diamond.avenue.language.expr.*;
import com.diamond.avenue.language.lib.Array;
import com.diamond.avenue.language.lib.NumberValue;
import com.diamond.avenue.language.lib.Value;
import com.diamond.avenue.language.lib.Variables;

import java.util.ArrayList;
import java.util.List;

public final class Parser {

    private static final Token EOF = new Token(Token.Type.EOF, "");

    private final List<Token> tokens;
    private final int size;

    private int pos;

    public Parser(List<Token> tokens) {
        this.tokens = tokens;
        size = tokens.size();
    }

    public Statement parse() {
        final BlockStatement result = new BlockStatement();
        while (!match(Token.Type.EOF)) {
            result.add(statement());
        }
        return result;
    }

    private Statement block () {
        final BlockStatement statement = new BlockStatement();
        consume(Token.Type.LBRACE);
        while (!match(Token.Type.RBRACE)) {
            statement.add(statement());
        }
        return statement;
    }

    private Statement statementOrBlock () {
        if (get(0).getType() == Token.Type.LBRACE) return block();
        return statement();
    }

    private Statement statement () {
        if (match(Token.Type.IF))
            return ifElse();
        if (match(Token.Type.WHILE))
            return whileStatement();
        if (match(Token.Type.DO))
            return doWhileStatement();
        if (match(Token.Type.FOR))
            return forStatement();
        if (match(Token.Type.FUNC))
            return createFunction();
        if (match(Token.Type.BREAK))
            return new BreakStatement();
        if (match(Token.Type.CONTINUE))
            return new ContinueStatement();
        if (match(Token.Type.RETURN))
            return new ReturnStatement(expression());
        if (get(0).getType() == Token.Type.WORD && get(1).getType() == Token.Type.LBRACK)
            return new FunctionStatement(function());
        if (match(Token.Type.PRINT))
            return new PrintStatement(expression());
        if (match(Token.Type.INPUT))
            return new InputStatement(expression());
        return assignmentStatement ();
    }

    private Statement assignmentStatement () {
        final Token current = get(0);
        if (current.getType()== Token.Type.WORD && get(1).getType()== Token.Type.EQ) {
            match(Token.Type.WORD);
            final String variable = current.getText();
            match(Token.Type.EQ);
            return new AssignmentStatement(variable, expression());
        }
        throw new RuntimeException("Unknown statement");
    }

    private Statement ifElse () {
        final Expression condition = expression();
        final Statement ifStatement = statementOrBlock();
        final Statement elseStatement;
        if (match(Token.Type.ELSE)) {
            elseStatement = statementOrBlock();
        } else {
            elseStatement = null;
        }
        return new IfStatement(condition, ifStatement, elseStatement);
    }


    private Statement whileStatement () {
        final Expression condition = expression();
        final Statement statement = statementOrBlock();
        return new WhileStatement(condition, statement);
    }

    private Statement doWhileStatement () {
        final Statement statement = statementOrBlock();
        consume(Token.Type.WHILE);
        final Expression condition = expression();
        return new DoWhileStatement(condition, statement);
    }

    private Statement forStatement () {
        final Statement initialization = assignmentStatement();
        consume(Token.Type.BAR);
        final Expression termination = expression();
        consume(Token.Type.BAR);
        final Statement increment = assignmentStatement();
        final Statement statement = statementOrBlock();
        return new ForStatement(initialization, termination, increment, statement);
    }

    private NewFunctionStatement createFunction () {
        final String name = consume(Token.Type.WORD).getText();
        consume(Token.Type.LBRACK);
        final List<String> argNames = new ArrayList<>();
        while (!match(Token.Type.RBRACK)) {
            argNames.add(consume(Token.Type.WORD).getText());
            match(Token.Type.COMMA);
        }
        final Statement body = statementOrBlock();
        return new NewFunctionStatement(name, argNames, body);
    }

    private FunctionalExpression function () {
        final String name = consume(Token.Type.WORD).getText();
        consume(Token.Type.LBRACK);
        final FunctionalExpression function = new FunctionalExpression(name);
        while (!match(Token.Type.RBRACK)) {
            function.addArgument(expression());
            match(Token.Type.COMMA);
        }
        return function;
    }

    private Value arrayValue () {
        final String name = consume(Token.Type.WORD).getText();
        consume(Token.Type.LSQUARE);
        final Array array = (Array) Variables.get(name);
        Value answer = array.get(new NumberValue(equality().eval().asNumber()));
        consume(Token.Type.RSQUARE);
        return answer;
    }

    private Expression expression() {
        return logicalOr();
    }

    private Expression logicalOr () {
        Expression result = logicalAnd();

        while (match(Token.Type.BARS)) {
            result = new ConditionalExpression(ConditionalExpression.Operator.OR, result, logicalAnd());
        }

        return result;
    }

    private Expression logicalAnd () {
        Expression result = equality();

        while (match(Token.Type.AMPS)) {
            result = new ConditionalExpression(ConditionalExpression.Operator.AND, result, equality());
        }

        return result;
    }

    private Expression equality () {
        Expression result = condition();

        if (match(Token.Type.EQEQ)) {
            return new ConditionalExpression(ConditionalExpression.Operator.EQUALS, result, additive());
        }
        if (match(Token.Type.EXCLEQ)) {
            return new ConditionalExpression(ConditionalExpression.Operator.NOT_EQUALS, result, additive());
        }

        return result;
    }

    private Expression condition() {
        Expression result = additive();

        while (true) {
            if (match(Token.Type.EQEQ)) {
                result = new ConditionalExpression(ConditionalExpression.Operator.EQUALS, result, additive());
                continue;
            }
            if (match(Token.Type.EXCLEQ)) {
                result = new ConditionalExpression(ConditionalExpression.Operator.NOT_EQUALS, result, additive());
                continue;
            }
            if (match(Token.Type.LT)) {
                result = new ConditionalExpression(ConditionalExpression.Operator.LT, result, additive());
                continue;
            }
            if (match(Token.Type.GT)) {
                result = new ConditionalExpression(ConditionalExpression.Operator.GT, result, additive());
                continue;
            }
            if (match(Token.Type.LTEQ)) {
                result = new ConditionalExpression(ConditionalExpression.Operator.LTEQ, result, additive());
                continue;
            }
            if (match(Token.Type.GTEQ)) {
                result = new ConditionalExpression(ConditionalExpression.Operator.GTEQ, result, additive());
                continue;
            }
            break;
        }

        return result;
    }

    private Expression additive() {
        Expression result = multiplicative();

        while (true) {
            if (match(Token.Type.PLUS)) {
                result = new BinaryExpression('+', result, multiplicative());
                continue;
            }
            if (match(Token.Type.MINUS)) {
                result = new BinaryExpression('-', result, multiplicative());
                continue;
            }
            break;
        }

        return result;
    }

    private Expression multiplicative() {
        Expression result = unary();

        while (true) {
            if (match(Token.Type.STAR)) {
                result = new BinaryExpression('*', result, unary());
                continue;
            }
            if (match(Token.Type.SLASH)) {
                result = new BinaryExpression('/', result, unary());
                continue;
            }
            break;
        }

        return result;
    }

    private Expression unary() {
        if (match(Token.Type.MINUS)) {
            return new UnaryExpression('-', primary());
        }
        return primary();
    }

    private Expression primary() {
        final Token current = get(0);
        if (match(Token.Type.NUMBER)) {
            return new ValueExpression(Double.parseDouble(current.getText()));
        }
        if (get(0).getType() == Token.Type.WORD && get(1).getType() == Token.Type.LBRACK) {
            return function();
        }
        if (get(0).getType() == Token.Type.WORD && get(1).getType() == Token.Type.LSQUARE) {
            return new ValueExpression(arrayValue());
        }
        if (match(Token.Type.WORD)) {
            return new ConstExpression(current.getText());
        }
        if (match(Token.Type.TEXT)) {
            return new ValueExpression(current.getText());
        }
        if (match(Token.Type.HEX)) {
            return new ValueExpression(Long.parseLong(current.getText(), 16));
        }
        if (match(Token.Type.LBRACK)) {
            Expression result = expression();
            match(Token.Type.RBRACK);
            return result;
        }
        throw new RuntimeException("Unknown expression");
    }

    private boolean match(Token.Type type) {
        final Token current = get(0);
        if (type != current.getType()) return false;
        pos++;
        return true;
    }

    private Token consume(Token.Type type) {
        final Token current = get(0);
        if (type != current.getType()) throw new RuntimeException();
        pos++;
        return current;
    }

    private Token get(int relativePosition) {
        final int position = pos + relativePosition;
        if (position >= size) return EOF;
        return tokens.get(position);
    }
}
