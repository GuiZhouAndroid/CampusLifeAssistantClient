package work.lpssfxy.www.campuslifeassistantclient.utils;

/**
 * created by on 2021/12/12
 * 描述：字符串工具类
 *
 * @author ZSAndroid
 * @create 2021-12-12-22:35
 */
public class MyStringUtils {
    /**
     * 截取字符串str中指定字符 strStart、strEnd之间的字符串
     *
     * @param str      被截取指定字符
     * @param strStart 开始字符
     * @param strEnd   结束字符
     * @return
     */
    public static String subString(String str, String strStart, String strEnd) {

        /* 找出指定的2个字符在 该字符串里面的 位置 */
        int strStartIndex = str.indexOf(strStart);
        int strEndIndex = str.indexOf(strEnd);

        /* index 为负数 即表示该字符串中 没有该字符 */
        if (strStartIndex < 0) {
            return "字符串 :---->" + str + "<---- 中不存在 " + strStart + ", 无法截取目标字符串";
        }
        if (strEndIndex < 0) {
            return "字符串 :---->" + str + "<---- 中不存在 " + strEnd + ", 无法截取目标字符串";
        }
        /* 开始截取 */
        String result = str.substring(strStartIndex, strEndIndex).substring(strStart.length());
        return result;
    }

    public static String subString1(String str, String strStart, String strEnd) {

        String tempStr;
        tempStr = str.substring(str.indexOf(strStart) + 1, str.lastIndexOf(strEnd));
        return tempStr;
    }
}
