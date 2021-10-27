package work.lpssfxy.www.campuslifeassistantclient.entity;

import lombok.Data;

/**
 * created by on 2021/10/17
 * 描述：网络请求响应实体类
 *
 * @author ZSAndroid
 * @create 2021-10-17-23:38
 */
@Data
public class ResponseBean {

    private int code;
    private String data;
    private String msg;
    private String nowTime;
}
