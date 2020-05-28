package com.diamond.avenue.language;

public class Token {
    public enum Type {

        NUMBER,
        HEX,
        WORD,
        TEXT,

        PRINT,
        INPUT,

        IF,
        ELSE,

        WHILE,
        FOR,
        DO,
        BREAK,
        CONTINUE,

        FUNC,
        RETURN,

        PLUS,
        MINUS,
        STAR,
        SLASH,
        EQ,
        EQEQ,
        EXCL,
        EXCLEQ,
        LT,
        LTEQ,
        GT,
        GTEQ,

        BAR,
        BARS,
        AMP,
        AMPS,

        COMMA,

        LBRACK, // (
        RBRACK, // )
        LBRACE,
        RBRACE,
        LSQUARE, // [
        RSQUARE, // ]

        EOF
    }

    private Type type;
    private String text;

    Token (Type type, String text) {
        this.type = type;
        this.text = text;
    }

    public Type getType() {
        return type;
    }

    public String getText() {
        return text;
    }
}