package kr.co.treegames.core.util;

/**
 * Created by tigris on 2017-07-28.
 */
public class StringUtils {
    public static String toAlias(String data) {
        String result = "";
        boolean isConvert = false;

        for (char ch : data.toCharArray()) {
            if (isConvert) {
                isConvert = false;
                if (ch >= 'a' && ch <= 'z') {
                    result += (char) (ch - ('a' - 'A'));
                }
            } else {
                if (ch == '_') {
                    isConvert = true;
                } else if (ch >= 'A' && ch <= 'Z') {
                    result += "_" + (char) (ch + ('a' - 'A'));
                } else {
                    result += ch;
                }
            }
        }

        return result;
    }
    public static String getAliasWithUnderBar(String data) {
        String result = "";

        for (int i = 0; i < data.length(); i++) {
            char ch = data.charAt(i);

            if (ch > 64 && ch < 91 && i == 0) {
                result += Character.toString((char) (ch + 32));
            } else if (ch > 64 && ch < 91) {
                result += "_" + Character.toString((char) (ch + 32));
            } else {
                result += Character.toString(ch);
            }
        }
        return result;
    }
}
