package com.monenco.insights;

import com.monenco.insights.models.Translator;

public class StringHelper {
    public static boolean notNone(String str) {
        return str != null && !str.isEmpty() && !str.equals("null");
    }


    public static String toLanguageNumber(String text) {
        if (Translator.isPersian()) {
            return toPersianNumber(text);
        } else {
            return toEnglishNumber(text);
        }
    }

    private static String[] persianNumbers = new String[]{"۰", "۱", "۲", "۳", "۴", "۵", "۶", "۷", "۸", "۹"};

    private static String toPersianNumber(String text) {
        if (!notNone(text)) {
            return "";
        } else {
            StringBuilder out = new StringBuilder();
            int length = text.length();
            for (int i = 0; i < length; i++) {
                char c = text.charAt(i);
                if ('0' <= c && c <= '9') {
                    int number = Integer.parseInt(String.valueOf(c));
                    out.append(persianNumbers[number]);
                } else if (c == '٫') {
                    out.append('،');
                } else {
                    out.append(c);
                }
            }
            return out.toString();
        }
    }

    private static String toEnglishNumber(String text) {
        if (!notNone(text)) {
            return "";
        } else {
            StringBuilder out = new StringBuilder();
            int length = text.length();
            for (int i = 0; i < length; i++) {
                String c = text.substring(i, i + 1);
                boolean isNumber = false;
                for (int j = 0; j < 10; j++) {
                    if (persianNumbers[j].equals(c)) {
                        out.append(j);
                        isNumber = true;
                    }
                }
                if (!isNumber) {
                    out.append(c);
                }

            }
            return out.toString();
        }
    }

    public static String getPrice(int price) {
        StringBuilder p = new StringBuilder();
        while (price >= 1000) {
            int r = price % 1000;
            price -= r;
            price /= 1000;
            p.insert(0, r);
            if (price < 10) {
                p.insert(0, "0");
            }
            if (price < 100) {
                p.insert(0, "0");
            }
            p.insert(0, ",");
        }
        p.insert(0, price);
        return toLanguageNumber(p.toString());
    }
}
