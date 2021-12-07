package work.lpssfxy.www.campuslifeassistantclient.utils;

import java.util.UUID;

/**
 * created by on 2021/12/7
 * 描述：UUID工具类
 *
 * @author ZSAndroid
 * @create 2021-12-07-21:13
 */
public abstract class UUIDUtil {
    /**
     * 获取32位UUID
     */
    public static String UUID32() {
        return UUID64().replace("-", "");
    }
    /**
     * 获取UUID:默认64为位
     */
    public static String UUID64() {
        return UUID.randomUUID().toString();
    }

}
