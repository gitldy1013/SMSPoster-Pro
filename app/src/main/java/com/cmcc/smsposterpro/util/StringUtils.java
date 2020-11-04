package com.cmcc.smsposterpro.util;

public class StringUtils {
    public static String[] splitStrs(String strings) {
        if (StringUtils.isEmpty(strings)) {
            return new String[0];
        }
        return strings.split("\\|");
    }

    public static boolean isEmpty(String str) {
        return str == null || str.length() == 0;
    }
}
