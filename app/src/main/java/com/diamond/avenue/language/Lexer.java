package com.diamond.avenue.language;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public final class Lexer {

    private static final String OPERATOR_CHARS = "+-*/(){}[]=<>!&|,";

    private static final Map<String, Token.Type> OPERATORS;
    static {
        OPERATORS = new HashMap<>();
        OPERATORS.put("+", Token.Type.PLUS);
        OPERATORS.put("-", Token.Type.MINUS);
        OPERATORS.put("*", Token.Type.STAR);
        OPERATORS.put("/", Token.Type.SLASH);
        OPERATORS.put("(", Token.Type.LBRACK);
        OPERATORS.put(")", Token.Type.RBRACK);
        OPERATORS.put("{", Token.Type.LBRACE);
        OPERATORS.put("}", Token.Type.RBRACE);
        OPERATORS.put("[", Token.Type.LSQUARE);
        OPERATORS.put("]", Token.Type.RSQUARE);
        OPERATORS.put("=", Token.Type.EQ);
        OPERATORS.put("<", Token.Type.LT);
        OPERATORS.put(">", Token.Type.GT);
        OPERATORS.put(",", Token.Type.COMMA);

        OPERATORS.put("!", Token.Type.EXCL);
        OPERATORS.put("&", Token.Type.AMP);
        OPERATORS.put("|", Token.Type.BAR);

        OPERATORS.put("==", Token.Type.EQEQ);
        OPERATORS.put("!=", Token.Type.EXCLEQ);
        OPERATORS.put("<=", Token.Type.LTEQ);
        OPERATORS.put(">=", Token.Type.GTEQ);

        OPERATORS.put("&&", Token.Type.AMPS);
        OPERATORS.put("||", Token.Type.BARS);
    }

    private final String input;
    private final int length;

    private final List<Token> tokens;

    private int pos;

    public Lexer(String input) {
        this.input = input;
        length = input.length();

        tokens = new ArrayList<>();
    }

    public List<Token> tokenize() {
        while (pos < length) {
            final char current = peek(0);
            if (Character.isDigit(current)) tokenizeNumber();
            else if (Character.isLetter(current)) tokenizeLetter();
            else if (current == '#') {
                next();
                tokenizeHexNumber();
            }
            else if (current == '"') {
                tokenizeText();
            }
            else if (OPERATOR_CHARS.indexOf(current) != -1) {
                tokenizeOperator();
            } else {
                // whitespaces
                next();
            }
        }
        return tokens;
    }

    private void tokenizeNumber() {
        final StringBuilder buffer = new StringBuilder();
        char current = peek(0);
        while (true) {
            if (current == '.') {
                if (buffer.indexOf(".") != -1) throw new RuntimeException("Invalid float number!");
            } else if (!Character.isDigit(current)) break;
            buffer.append(current);
            current = next();
        }
        addToken(Token.Type.NUMBER, buffer.toString());
    }

    private void tokenizeHexNumber() {
        final StringBuilder buffer = new StringBuilder();
        char current = peek(0);
        while (Character.isDigit(current) || isHexNumber(current)) {
            buffer.append(current);
            current = next();
        }
        addToken(Token.Type.HEX, buffer.toString());
    }

    private static boolean isHexNumber(char current) {
        return "abcdef".indexOf(Character.toLowerCase(current)) != -1;
    }

    private void tokenizeOperator() {
        char current = peek(0);
        if (current == '/') {
            if (peek(1) == '/') {
                next(); next();
                tokenizeComment();
                return;
            } else if (peek(1) == '*') {
                next(); next();
                tokenizeMultiComment();
                return;
            }
        }
        final StringBuilder buffer = new StringBuilder();
        while (true) {
            final String text = buffer.toString();
            if (!OPERATORS.containsKey(text+current) && !text.isEmpty()) {
                addToken(OPERATORS.get(text));
                return;
            }
            buffer.append(current);
            current = next();
        }
    }

    private void tokenizeLetter() {
        final StringBuilder buffer = new StringBuilder();
        char current = peek(0);
        while (true) {
            if (!Character.isLetterOrDigit(current) && current != '_' && current != '$') break;
            buffer.append(current);
            current = next();
        }
        final String word = buffer.toString();
        switch (word) {
            case "print": addToken(Token.Type.PRINT); break;
            case "if": addToken(Token.Type.IF); break;
            case "else": addToken(Token.Type.ELSE); break;
            case "while": addToken(Token.Type.WHILE); break;
            case "for": addToken(Token.Type.FOR); break;
            case "do": addToken(Token.Type.DO); break;
            case "break": addToken(Token.Type.BREAK); break;
            case "continue": addToken(Token.Type.CONTINUE); break;
            case "def": addToken(Token.Type.FUNC); break;
            case "return": addToken(Token.Type.RETURN); break;
            default: addToken(Token.Type.WORD, word); break;
        }
    }

    private void tokenizeText() {
        next();
        final StringBuilder buffer = new StringBuilder();
        char current = peek(0);
        while (true) {
            if (current == '\\') {
                current = next();
                switch (current) {
                    case '"': current=next(); buffer.append('"'); continue;
                    case 'n': current=next(); buffer.append('\n'); continue;
                    case 't': current=next(); buffer.append('\t'); continue;
                }
                buffer.append('\\');
                continue;
            }
            if (current == '"') break;
            buffer.append(current);
            current = next();
        }
        next();
        addToken(Token.Type.TEXT, buffer.toString());
    }

    private void tokenizeComment() {
        char current = peek(0);
        while ("\r\n\0".indexOf(current) == -1) {
            current = next();
        }
    }

    private void tokenizeMultiComment() {
        char current = peek(0);
        while (true) {
            if (current == '\0') throw new RuntimeException("Tag not closed");
            if (current == '*' && peek(1)=='/') break;
            current = next();
        }
        next(); next();
    }

    private char next() {
        pos++;
        return peek(0);
    }

    private char peek(int relativePosition) {
        final int position = pos + relativePosition;
        if (position >= length) return '\0';
        return input.charAt(position);
    }

    private void addToken(Token.Type type) {
        addToken(type, "");
    }

    private void addToken(Token.Type type, String text) {
        tokens.add(new Token(type, text));
    }
}