package com.tregz.miksing.base.text;

import android.util.Log;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

@RunWith(AndroidJUnit4.class)
public class TextUtilAndroidTest {
    private String TAG = TextUtilAndroidTest.class.getSimpleName();


    @Test
    public void stringOrder_isCorrect() {
        String[] strArr = new String[] {"1, 3, 4, 7, 13", "1, 2, 4, 13, 15"};
        //char[] charArr = new char[] {};
        List<Character> charArr = new ArrayList<>();
        for (String str : strArr) {
            for (int i = 0; i < str.length(); i++)
                if (charArr.isEmpty() || charArr.get(charArr.size()- 1) < str.charAt(i)) {
                    charArr.add(str.charAt(i));
                }
        }
        System.out.println("charArr:" + charArr);
        Log.d(TAG, "charArr:" + charArr);

    }
}
