package com.diamond.avenue.language.lib;

public final class StringValue implements Value {

    private String value;

    public StringValue (String value) {
        this.value = value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public double asNumber() {
        try {
            return Double.parseDouble(value);
        } catch (Exception e) {
            return 0;
        }
    }

    @Override
    public String asString() {
        return value;
    }

    @Override
    public String toString () {
        return asString();
    }
}