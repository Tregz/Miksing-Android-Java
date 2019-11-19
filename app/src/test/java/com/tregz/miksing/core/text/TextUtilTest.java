package com.tregz.miksing.core.text;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;

public class TextUtilTest {
    //private String TAG = TextUtilTest.class.getSimpleName();

    @Test
    public void stringReverted_isCorrect() {
        String test = "ABCdef";
        char[] sequence = test.toCharArray();
        List<String> chars = new ArrayList<>();
        for (char c : sequence) chars.add(String.valueOf(c));
        Collections.sort(chars, Collections.reverseOrder());
        StringBuilder result = new StringBuilder();
        for (String c : chars) result.append(c);
        assertEquals("fedCBA", result.toString());
    }

}