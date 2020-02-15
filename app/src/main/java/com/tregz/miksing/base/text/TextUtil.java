package com.tregz.miksing.base.text;

public final class TextUtil {

    public static String stripQuotes(String str) {
        if (str.startsWith("\"")) { str = str.substring(1, str.length()); }
        if (str.endsWith("\""))  { str = str.substring(0, str.length() - 1); }
        return str;
    }
}
