package io.github.metheax.utils;

import io.github.metheax.constant.Constant;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Author: Kuylim TITH
 * Date: 5/28/2021
 */
public class ChhankitekUtils {

    private ChhankitekUtils(){}

    public static <A, B> Map<A, B> asMap(Object... keysAndValues) {
        Map<A, B> map = new LinkedHashMap<>();
        for (int i = 0; i < keysAndValues.length - 1; i++) {
            map.put((A) keysAndValues[i], (B) keysAndValues[++i]);
        }
        return map;
    }

    public static <B> Map<Integer, B> indexAsKeyMap(Object... values) {
        Map<Integer, B> map = new LinkedHashMap<>();
        for (int i = 0; i < values.length; i++) {
            map.put(i, (B) values[i]);
        }
        return map;
    }

    public static <A> Map<A, Integer> indexAsValueMap(Object... keys) {
        Map<A, Integer> map = new LinkedHashMap<>();
        for (int i = 0; i < keys.length; i++) {
            map.put((A) keys[i], i);
        }
        return map;
    }

    public static String convertIntegerToKhmerNumber(int number) {
        String result = "";
        String num = String.valueOf(number);
        for (int i = 0; i < num.length(); i++) {
            char c = num.charAt(i);
            result = result.concat(Constant.NUMBERS.get(String.valueOf(c)));
        }
        return result;
    }

    public static String getDayOfWeekInKhmer(LocalDateTime dateTime) {
        DateTimeFormatter dayOfWeekFormat = DateTimeFormatter.ofPattern("EEEE");
        String dayOfWeek = dayOfWeekFormat.format(dateTime);
        return Constant.DAY_OF_WEEK.get(dayOfWeek.toUpperCase());
    }
}
