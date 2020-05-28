package com.diamond.avenue.language.lib;

import com.diamond.avenue.language.AvenueSystem;

import java.util.ArrayList;

public final class Array implements Value {

    private ArrayList<Value> value;

    public Array() {
        this.value = new ArrayList<>();
    }

    public void append(Value value) {
        this.value.add(value);
    }

    public int length() {
        return this.value.size();
    }

    public Value get(NumberValue index) {
        if (index.asNumber() >= length()) throw new RuntimeException("Неправильный индекс");
        return this.value.get((int) index.asNumber());
    }

    @Override
    public double asNumber() {
        throw new RuntimeException("Попытка конвертирования Array в Number");
    }

    @Override
    public String asString() {
        String answer = "[";
        for (int i=0;i<this.value.size()-1;i++) {
            answer += this.value.get(i).asString()+", ";
        }
        answer += this.value.get(this.value.size()-1).asString()+"]";
        return answer;
    }
}
