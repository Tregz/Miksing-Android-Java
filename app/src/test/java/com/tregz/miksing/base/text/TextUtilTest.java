package com.tregz.miksing.base.text;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
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

    @Test
    public void numberOrderInStringArray_isCorrect() {
        String[] strArr = new String[] {"21, 22, 23, 25, 27, 28", "21, 24, 25, 29"};
        List<ArrayList<Integer>> numberArrays = twoIntegerArrays();
        int position = 0;
        for (String str : strArr) {
            String[] numArr = str.replaceAll("\\s+","").split(",");
            for (String number : numArr) numberArrays.get(position).add(Integer.parseInt(number));
            position++;
        }
        List<ArrayList<Integer>> orderedArrays = twoIntegerArrays();
        position = 0;
        for (List<Integer> list : numberArrays) {
            ArrayList<Integer> orderedArr = orderedArrays.get(position);
            for (Integer number: list)
                if (orderedArr.isEmpty() || orderedArr.get(orderedArr.size() -1) < number)
                    orderedArr.add(number);
            position++;
        }
        List<Integer> intersectionNumber = new ArrayList<>();
        for (Integer number : orderedArrays.get(0)) {
            if (orderedArrays.get(1).contains(number)) intersectionNumber.add(number);
        }
        StringBuilder intersection = new StringBuilder();
        for (Integer number : intersectionNumber) {
            if (intersection.length() > 0) intersection.append(",");
            intersection.append(number.toString());
        }
        System.out.println("arr:" + intersection.toString());
    }

    private List<ArrayList<Integer>> twoIntegerArrays() {
        return Arrays.asList(new ArrayList<Integer>(), new ArrayList<Integer>());
    }
}