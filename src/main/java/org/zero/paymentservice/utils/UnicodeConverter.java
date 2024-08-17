package org.zero.paymentservice.utils;

public class UnicodeConverter {
    public static String apply(String unicodeString) {
        StringBuilder sb = new StringBuilder();
        int i = 0;
        while (i < unicodeString.length()) {
            char ch = unicodeString.charAt(i);
            if (ch == '\\' && (i + 1) < unicodeString.length() && unicodeString.charAt(i + 1) == 'u') {
                String unicode = unicodeString.substring(i + 2, i + 6);
                sb.append((char) Integer.parseInt(unicode, 16));
                i += 6;
            } else {
                sb.append(ch);
                i++;
            }
        }
        return sb.toString();
    }
}
