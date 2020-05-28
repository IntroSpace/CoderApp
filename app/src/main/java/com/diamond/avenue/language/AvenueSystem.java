package com.diamond.avenue.language;

import com.diamond.avenue.AvenueResult;
import com.diamond.avenue.language.expr.BlockStatement;
import com.diamond.avenue.language.expr.Statement;

import java.util.ArrayList;

public final class AvenueSystem {

    public static BlockStatement statements;
    private static String conclusion = "";

    public static class out {
        public static void print (String text) {
            conclusion += text;
        }
    }

    public static String getConclusion() {
        String result = conclusion;
        conclusion = "";
        return result;
    }

    public static class in {
        public static String read () { return ""; }
    }
}