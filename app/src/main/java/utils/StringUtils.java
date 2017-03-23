package utils;

import android.os.Bundle;
import android.text.TextUtils;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;


/**
 * 字符处理工具
 */
public class StringUtils {

    /**
     * 将string转换为int
     *
     * @param str          转换的字符
     * @param defaultValue 如果转换失败，则使用默认值
     * @return int类型
     */
    public static int getInt(String str, int defaultValue) {
        int result = defaultValue;
        try {
            result = Integer.parseInt(str);
        } catch (Exception e) {
            // TODO: handle exception
        }
        return result;
    }

    /**
     * 将string转换为long
     *
     * @param str          转换的字符
     * @param defaultValue 如果转换失败，则使用默认值
     * @return long类型
     */
    public static long getLong(String str, long defaultValue) {
        long result = defaultValue;
        try {
            result = Long.parseLong(str);
        } catch (Exception e) {
            // TODO: handle exception
        }
        return result;
    }

    public static String Int2Str(int i) {
        return new StringBuilder().append(i).toString();
    }

    public static String Long2Str(long i) {
        return new StringBuilder().append(i).toString();
    }

    /**
     * 判断char是否为ascii码
     *
     * @param c 字符
     * @return boolean类型，true:是ascii码，false:不是ascii码
     */
    public static boolean isLetter(char c) {
        int k = 0x80;
        return c / k == 0 ? true : false;
    }

    /**
     * 判断字符串是否为空
     *
     * @param str 字符内容
     * @return boolean类型，true:字符是null，false:字符不是null
     */
    public static boolean isEmpty(String str) {
        return TextUtils.isEmpty(str);
    }

    /**
     * 判断字符串是否为空格字符串
     *
     * @param str
     * @return
     */
    public static boolean isBlankSpace(String str) {
        if (!TextUtils.isEmpty(str) && str.trim().equals("")) {
            return true;
        } else {
            return false;
        }

    }

    /**
     * 判断字符串是否为空或者空格字符串
     *
     * @param str
     * @return
     */
    public static boolean isEmptyOrBlankSpace(String str) {
        return isEmpty(str) || isBlankSpace(str);
    }

    /**
     * 得到一个字符串的长度,显示的长度,一个汉字或日韩文长度为2,英文字符长度为1
     *
     * @param s 需要得到长度的字符串
     * @return int 得到的字符串长度
     */
    public static int getStringLength(String s) {
        if (s == null)
            return 0;
        char[] c = s.toCharArray();
        int len = 0;
        for (int i = 0; i < c.length; i++) {
            len++;
            if (!isLetter(c[i])) {
                len++;
            }
        }
        return len;
    }

    /**
     * 安全地截断字符串
     */
    public static String subString(String string, int start, int end) {
        if (isEmpty(string)) {
            return "";
        }

        if (string.length() < end) {
            end = string.length();
        }

        return string.substring(start, end);
    }

//    /**
//     * 得到一个字符串的长度,显示的长度,一个汉字或日韩文长度为1,英文字符长度为0.5
//     *
//     * @param s 需要得到长度的字符串
//     * @return int 得到的字符串长度
//     */
//    public static double getLength(String s) {
//        double valueLength = 0;
//        String chinese = "[\u4e00-\u9fa5]";
//        // 获取字段值的长度，如果含中文字符，则每个中文字符长度为2，否则为1
//        for (int i = 0; i < s.length(); i++) {
//            // 获取一个字符
//            String temp = s.substring(i, i + 1);
//            // 判断是否为中文字符
//            if (temp.matches(chinese)) {
//                // 中文字符长度为1
//                valueLength += 1;
//            } else {
//                // 其他字符长度为0.5
//                valueLength += 0.5;
//            }
//        }
//        // 进位取整
//        return Math.ceil(valueLength);
//    }

    /**
     * 截取字符串
     *
     * @param str    要截取的字符串
     * @param len    要截取的字符串长度 汉字占2个字符长度
     * @param suffix 字符串结尾可以添加省略符 如：...
     */
    public static String cutString(String str, int len, String suffix) {
        if (str == null) {
            return "";
        }

        // 如果len不够截取，则直接返回截取后附加字符
        if (len <= 0) {
            return suffix;
        }

        int k = getStringLength(str);

        if (len >= k) {
            return str;
        }

        int i = 0, j = 0;

        for (char c : str.toCharArray()) {
            if (c >= 19968 && c <= 40869) {
                // 中文字符
                j += 2;
            } else {
                j++;
            }

            i++;

            if (j >= len) {
                break;
            }
        }

        return str.substring(0, i) + suffix;
    }

    /**
     * 获取非空字符串。
     *
     * @param str 要判断的字符串信息
     * @return str为null或长度为0，返回“”，反之返回str原串
     */
    public static String getString(String str) {
        if (TextUtils.isEmpty(str)) {
            return "";
        }
        return str;
    }

    /**
     * 用bundle获取字符串的时候, 如果没有这个值,返回空字符串
     *
     * @param bundle
     * @param key
     * @return
     */
    public static String getBundleString(Bundle bundle, String key) {
        if (bundle == null)
            return "";
        if (TextUtils.isEmpty(key))
            return "";
        return getString(bundle.getString(key));
    }

    /**
     * 截取字符串
     *
     * @param str    要截取的字符串
     * @param len    要截取的字符串长度 汉字占2个字符长度
     * @param suffix 字符串中间要添加的省略符 如：...
     */
    public static String cutCentreString(String str, int len, String suffix) {
        if (str == null) {
            return "";
        }

        // 如果len不够截取，则直接返回截取后附加字符
        if (len <= 0) {
            return suffix;
        }

        int k = getStringLength(str);

        if (len >= k) {
            return str;
        }

        String strLeft = "";
        String strRight = "";

        int i = 0, j = 0;
        char[] strArray = str.toCharArray();
        int strLastIndex = str.length() - 1;

        for (char c : strArray) {
            if (c >= 19968 && c <= 40869) {
                // 中文字符
                j += 2;
            } else {
                j++;
            }
            i++;
            if (j >= len / 2.0) {
                break;
            }
        }
        strLeft = str.substring(0, i);
        i = strLastIndex;
        j = 0;
        for (int n = strLastIndex; n >= 0; n--) {
            char c = strArray[n];
            if (c >= 19968 && c <= 40869) {
                // 中文字符
                j += 2;
            } else {
                j++;
            }
            if (j >= len / 2.0) {
                break;
            }
            i--;
        }
        strRight = str.substring(i, strLastIndex + 1);

        return strLeft + suffix + strRight;
    }

    /**
     * encoded in utf-8
     *
     * @param str
     * @return
     * @throws UnsupportedEncodingException if an error occurs
     */
    public static String utf8Encode(String str) {
        if (!TextUtils.isEmpty(str) && str.getBytes().length != str.length()) {
            try {
                return URLEncoder.encode(str, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException("UnsupportedEncodingException occurred. ", e);
            }
        }
        return str;
    }

    /**
     * @param str
     * @param color
     * @return String
     * @MethodName: str2Html
     * @Description: 生成html字符串, 主要改变字符串颜色
     * @author yedr
     */
    public static String str2Html(String str, String color) {
        if (TextUtils.isEmpty(str))
            return "";
        if (TextUtils.isEmpty(color))
            return str;
        return "<font color=\"" + color + "\">" + str + "</font>";
    }
}
