package work.lpssfxy.www.campuslifeassistantclient.entity.okgo;

import lombok.Data;

/**
 * created by on 2021/10/17
 * 描述：标准OkGo网络请求响应实体
 *
 * @author ZSAndroid
 * @create 2021-10-17-23:38
 */
@Data
public class OkGoResponseBean {
    private int code;
    private String data;
    private String msg;
    private String nowTime;
}
