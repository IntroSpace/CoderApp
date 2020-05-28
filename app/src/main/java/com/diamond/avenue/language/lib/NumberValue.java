package com.diamond.avenue.language.lib;

public final class NumberValue implements Value {

    private double value;

    public NumberValue (double value) {
        this.value = value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public NumberValue (boolean value) {
        this.value = value ? 1 : 0;
    }

    @Override
    public double asNumber() {
        return value;
    }

    @Override
    public String asString() {
        return Double.toString(value);
    }
}
