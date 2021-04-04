package com.nullptr.utils.lang;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 正则工具类, 只用于Unicode编码, 默认匹配空白字符
 *
 * @author nullptr
 * @version 1.0 2020-10-30
 * @since 1.1
 */
public class RegExUtils extends org.apache.commons.lang3.RegExUtils {
    /** 大写字母 */
    public static final Pattern UPPERCASE = Pattern.compile("[A-Z\\s]+");
    /** 小写字母 */
    public static final Pattern LOWERCASE = Pattern.compile("[a-z\\s]+");
    /** 英文字母 */
    public static final Pattern ENGLISH = Pattern.compile("[a-zA-Z\\s]*");
    /** 英文和数字 */
    public static final Pattern ENGLISH_NUMBER = Pattern.compile("[\\w\\s]*");
    /** 英文、数字和下划线 */
    public static final Pattern ENGLISH_NUMBER_UNDERLINE = Pattern.compile("[_A-Za-z\\d\\s]*");
    /** 英文字符串，包括英文字母、数字和英文符号 */
    public static final Pattern ENGLISH_STR = Pattern.compile("[\\w\\s`~!@#$%^&*()_\\-+={}\\[\\];:'\"|/?,<>\\\\]*");

    /** 中文字符 */
    public static final Pattern CHINESE = Pattern.compile("[\\u4e00-\\u9fa5\\s]*");
    /** 中文和数字 */
    public static final Pattern CHINESE_NUMBER = Pattern.compile("[\\u4e00-\\u9fa5\\d\\s]*");
    /** 中文字符串，包括中文字符、数字和中文符号 */
    public static final Pattern CHINESE_STR = Pattern.compile("[\\u4e00-\\u9fa5\\d\\s·~！@#￥%…&*（）—+-=、|【】{}：；“‘/？。，《》]*");

    /** 特殊符号 */
    public static final Pattern SYMBOL = Pattern.compile("[\\s`~!@#$%^&*()_\\-+={}\\[\\];:'\"|/?,<>\\\\]+");
    /** 数字和符号 */
    public static final Pattern NUMBER_SYMBOL = Pattern.compile("[\\s\\d`~!@#$%^&*()_\\-+={}\\[\\];:'\"|/?,<>\\\\]+");

    /** 数字，不匹配空白 */
    public static final Pattern NUMBER = Pattern.compile("-?\\d*\\.?\\d+");
    /** 正数，不匹配空白 */
    public static final Pattern POSITIVE_NUMBER = Pattern.compile("\\d*\\.?\\d+");
    /** 负数，不匹配空白 */
    public static final Pattern NEGATIVE_NUMBER = Pattern.compile("-\\d*\\.?\\d+");

    /** 十进制，不匹配空白 */
    public static final Pattern DECIMAL = Pattern.compile("\\d+");
    /** 十六进制，不匹配空白 */
    public static final Pattern HEXADECIMAL = Pattern.compile("(0(x|X))?[a-fA-F0-9]+");
    /** 八进制，不匹配空白 */
    public static final Pattern OCTAL = Pattern.compile("(o|O)[0-7]+");
    /** 二进制，不匹配空白 */
    public static final Pattern BINARY = Pattern.compile("(0|1)+");

    /** 整数，不匹配空白 */
    public static final Pattern INTEGER = Pattern.compile("-?\\d+");
    /** 正整数，不匹配空白 */
    public static final Pattern POSITIVE_INTEGER = Pattern.compile("\\d+");
    /** 负整数，不匹配空白 */
    public static final Pattern NEGATIVE_INTEGER = Pattern.compile("-\\d+");

    /** 浮点数，不匹配空白 */
    public static final Pattern FLOAT = Pattern.compile("-?\\d*\\.\\d+");
    /** 正浮点数，不匹配空白 */
    public static final Pattern POSITIVE_FLOAT = Pattern.compile("\\d*\\.\\d+");
    /** 负浮点数，不匹配空白 */
    public static final Pattern NEGATIVE_FLOAT = Pattern.compile("-\\d*\\.\\d+");

    /** 电话 */
    public static final Pattern PHONE = Pattern.compile("[1]\\d{10}");
    /** 身份证号码 */
    public static final Pattern ID_CARD = Pattern.compile("\\d{17}([0-9]|X|x)");
    /** QQ号码 */
    public static final Pattern QQ_NUMBER = Pattern.compile("[1-9][0-9]{4,}");
    /** 微信号码 */
    public static final Pattern WECHAT_NUMBER = Pattern.compile("[a-zA-Z][-\\d]{6,}");
    /** 中国邮政编码 */
    public static final Pattern POSTAL_CODE = Pattern.compile("[1-9]\\d{5}(?!\\d)");

    /** 电子邮箱, @前可存在中文 */
    public static final Pattern EMAIL = Pattern.compile("[\\u4e00-\\u9fa5\\w!#$%&'*+/=?^_`{|}~-]+" +
            "(\\.[\\u4e00-\\u9fa5\\w!#$%&'*+/=?^_`{|}~-]+)*@([\\w]([\\w-]*[\\w])?\\.)+[\\w]([\\w-]*[\\w])?");
    /** URL */
    public static final Pattern URL = Pattern.compile("[a-zA-Z]+://[-A-Za-z0-9+&@#/%?=~_|!:,.;]+[-A-Za-z0-9+&@#/%=~_|]+");
    /** HTTP */
    public static final Pattern HTTP = Pattern.compile("(HTTP|http)(s|S)?://[-A-Za-z0-9+&@#/%?=~_|!:,.;]+[-A-Za-z0-9+&@#/%=~_|]+");
    /** FTP */
    public static final Pattern FTP = Pattern.compile("(FTP|ftp)(s|S)?://[-A-Za-z0-9+&@#/%?=~_|!:,.;]+[-A-Za-z0-9+&@#/%=~_|]+");
    /** FILE */
    public static final Pattern FILE = Pattern.compile("(FILE|file)://[-A-Za-z0-9+&@#/%?=~_|!:,.;]+[-A-Za-z0-9+&@#/%=~_|]+");
    /** IPV4地址 */
    public static final Pattern IPV4_ADDRESS = Pattern.compile("(\\d{1,3}\\.){3}\\d{1,3}");
    /** IPV6地址 */
    public static final Pattern IPV6_ADDRESS = Pattern.compile("((([a-fA-F\\d]{1,4}:){5,7}[a-fA-F\\d]{1,4})|(::[a-fA-F\\d]{1,4}))(/128)?");

    /** 日期，YYYY-MM-DD格式 */
    public static final Pattern DATE = Pattern.compile("\\d{4}-\\d{1,2}-\\d{1,2}");
    /** 时间：HH:mm:SS */
    public static final Pattern TIME = Pattern.compile("\\d{1,2}:\\d{1,2}:\\d{1,2}");
    /** 日期加时间：YYYY-MM-DD-HH:mm:SS */
    public static final Pattern DATE_TIME = Pattern.compile("\\d{4}-\\d{1,2}-\\d{1,2}-\\d{1,2}:\\d{1,2}:\\d{1,2}");

    /** 用户名,中文、数字、字母和下划线组成 */
    public static final Pattern USER_NAME = Pattern.compile("[a-zA-Z0-9_\\u4e00-\\u9fa5]+");
    /** 弱密码, 仅由数字或字母组成 */
    public static final Pattern WEAK_PASSWORD = Pattern.compile("\\d+|[a-zA-Z]+");
    /** 一般密码, 数字和字母或数字和特殊字符或字母和特殊字符组成 */
    public static final Pattern MID_PASSWORD = Pattern.compile("([a-zA-z\\d]+|[a-zA-z`~!@#$%^&*()_\\-+={}\\[\\];:'\"|/?,<>\\\\]+" +
            "|[\\d`~!@#$%^&*()_\\-+={}\\[\\];:'\"|/?,<>\\\\]+)");
    /** 强密码, 数字、字母和特殊字符组成 */
    public static final Pattern STRONG_PASSWORD = Pattern.compile("[a-zA-z\\d`~!@#$%^&*()_\\-+={}\\[\\];:'\"|/?,<>\\\\]+");

    private RegExUtils() {
    }

    /**
     * 判断输入字符串是否与表达式匹配
     *
     * @param pattern 正则表达式
     * @param input 输入字符串
     * @return 是否匹配
     * @since 1.0
     */
    public static boolean isMatch(final Pattern pattern, final CharSequence input) {
        return pattern.matcher(input).matches();
    }

    /**
     * 获取输入字符串中匹配的部分，如果不匹配则返回空数组
     *
     * @param pattern 正则表达式
     * @param input 输入字符串
     * @return 匹配的字符串数组
     * @since 1.0
     */
    public static String[] match(final Pattern pattern, final CharSequence input) {
        Matcher mather = pattern.matcher(input);
        List<String> list = new LinkedList<>();
        while (mather.find()) {
            list.add(mather.group());
        }
        String[] result = new String[list.size()];
        return list.toArray(result);
    }

    /**
     * 获取输入字符串中匹配的部分，如果不匹配则返回空数组
     *
     * @param pattern 正则表达式
     * @param input 输入字符串
     * @param limit 匹配上限
     * @return 匹配的字符串数组
     * @since 1.0
     */
    public static String[] match(final Pattern pattern, final CharSequence input, final int limit) {
        String[] result = match(pattern, input);
        if (limit > result.length) {
            return result;
        }
        return Arrays.copyOfRange(result, 0, limit);
    }

    /**
     * 获取输入字符串匹配的次数
     *
     * @param pattern 正则表达式
     * @param input 输入字符串
     * @return 匹配的次数
     * @since 1.0
     */
    public static int count(final Pattern pattern, final CharSequence input) {
        return match(pattern, input).length;
    }

    /**
     * 获取正则表达式字符串
     *
     * @param pattern 正则表达式
     * @return 正则表达式字符串
     * @since 1.0
     */
    public static String getRegex(final Pattern pattern) {
        return pattern.toString();
    }
}
