package com.diamond.avenue.language.lib;

import com.diamond.avenue.language.AvenueSystem;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public final class Functions {

    private static final Map<String, Function> functions;

    static {
        functions = new HashMap<>();
        functions.put("sin", new Function() {
            @Override
            public Value execute(Value... args) {
                if (args.length != 1) throw new RuntimeException("Нужен 1 аргумент");
                return new NumberValue(Math.sin(args[0].asNumber()));
            }
        });
        functions.put("cos", new Function() {
            @Override
            public Value execute(Value... args) {
                if (args.length != 1) throw new RuntimeException("Нужен 1 аргумент");
                return new NumberValue(Math.cos(args[0].asNumber()));
            }
        });
        functions.put("abs", new Function() {
            @Override
            public Value execute(Value... args) {
                if (args.length != 1) throw new RuntimeException("Нужен 1 аргумент");
                return new NumberValue(Math.abs(args[0].asNumber()));
            }
        });
        functions.put("echo", new Function() {
            @Override
            public Value execute(Value... args) {
                for (int i=0;i<args.length-1;i++) {
                    AvenueSystem.out.print(args[i].asString()+" ");
                }
                AvenueSystem.out.print(args[args.length-1].asString());
                return new NumberValue(0);
            }
        });
        functions.put("println", new Function() {
            @Override
            public Value execute(Value... args) {
                for (int i=0;i<args.length-1;i++) {
                    AvenueSystem.out.print(args[i].asString()+" ");
                }
                AvenueSystem.out.print(args[args.length-1].asString()+"\n");
                return new NumberValue(0);
            }
        });
        functions.put("printlns", new Function() {
            @Override
            public Value execute(Value... args) {
                for (Value value :
                        args) {
                    AvenueSystem.out.print(value.asString() + "\n");
                }
                return new NumberValue(0);
            }
        });
        functions.put("random", new Function() {
            @Override
            public Value execute(Value... args) {
                if (args.length == 1) return new NumberValue(new Random().nextInt(Integer.parseInt(args[0].asString())));
                return new NumberValue(new Random().nextInt());
            }
        });
        functions.put("replace", new Function() {
            @Override
            public Value execute(Value... args) {
                if (args.length != 3) throw new RuntimeException("Требуется 3 аргумента");
                if (args[0] instanceof StringValue) Variables.set(args[0].asString(), new StringValue(Variables.get(args[0].asString()).asString().replace(args[1].asString(), args[2].asString())));
                else throw new RuntimeException("Нужен строковый тип");
                return new NumberValue(0);
            }
        });
        functions.put("userIn", new Function() {
            @Override
            public Value execute(Value... args) {
                if (args.length == 1) {
                    Variables.set(args[0].asString(), new StringValue(AvenueSystem.in.read()));
                }
                if (args[0] instanceof StringValue) Variables.set(args[0].asString(), new StringValue(Variables.get(args[0].asString()).asString().replace(args[1].asString(), args[2].asString())));
                return new NumberValue(0);
            }
        });
        functions.put("append", new Function() {
            @Override
            public Value execute(Value... args) {
                if (args.length >= 2) {
                    Array array = (Array) args[0];
                    for (int i=1;i<args.length;i++)
                        array.append(args[i]);
                    Variables.set(args[0].asString(), array);
                    return new NumberValue(1);
                }
                return new NumberValue(0);
            }
        });
        functions.put("length", new Function() {
            @Override
            public Value execute(Value... args) {
                if (args.length == 1) {
                    if (Variables.get(args[0].asString()) instanceof Array) {
                        return new NumberValue(((Array) Variables.get(args[0].asString())).length());
                    }
                }
                return new NumberValue(0);
            }
        });
        functions.put("array", new Function() {
            @Override
            public Value execute(Value... args) {
                Array array = new Array();
                if (args.length >= 1) {
                    for (Value arg : args) array.append(arg);
                }
                return array;
            }
        });
        functions.put("get", new Function() {
            @Override
            public Value execute(Value... args) {
                if (args.length == 2) {
                    Array array = (Array) Variables.get(args[0].asString());
                    return array.get(new NumberValue(args[1].asNumber()));
                }
                return new NumberValue(0);
            }
        });
    }

    public static boolean isExists (String name) {
        return functions.containsKey(name);
    }

    public static Function get (String name) {
        if (!isExists(name)) throw new RuntimeException("Неизвестная функция " + name);
        return functions.get(name);
    }

    public static void set(String variable, Function function) {
        functions.put(variable, function);
    }
}