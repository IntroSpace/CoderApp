package com.diamond.avenue.language.lib;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

public final class Variables {

    private static final Stack<Map<String, Value>> stack;
    private static Map<String, Value> variables;

    static {
        stack = new Stack<>();
        variables = new HashMap<>();
        variables.put("PI", new NumberValue(Math.PI));
        variables.put("E", new NumberValue(Math.E));
        variables.put("AVENUE", new NumberValue(14.01));
        variables.put("AUTHOR", new StringValue("zodiac33"));
    }

    public static void push () {
        stack.push(new HashMap<>(variables));
    }

    public static void pop () {
        variables = stack.pop();
    }

    public static void New () {
        variables = new HashMap<>();
    }

    public static boolean isExists (String name) {
        return variables.containsKey(name);
    }

    public static Value get (String name) {
        if (!isExists(name)) return new NumberValue(0);
        return variables.get(name);
    }

    public static void set(String variable, Value result) {
        variables.put(variable, result);
    }
}