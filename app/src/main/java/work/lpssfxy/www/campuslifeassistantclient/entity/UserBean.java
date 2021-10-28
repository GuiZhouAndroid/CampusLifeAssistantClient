package work.lpssfxy.www.campuslifeassistantclient.entity;

import java.io.Serializable;

import lombok.Data;

/**
 * created by on 2021/10/28
 * 描述：QQ会话实体
 *
 * @author ZSAndroid
 * @create 2021-10-28-6:06
 */

@Data
public class UserBean implements Serializable {
    /**
     * 请求状态信息
     */
    private int code;
    private Data data;
    private String msg;
    private String nowTime;

    /**
     * QQ会话实体信息
     */
    @lombok.Data
    public static class Data {
        private String createTime;
        private String ulClass;
        private String ulDept;
        private String ulEmail;
        private int ulId;
        private String ulIdcard;
        private String ulPassword;
        private String ulRealname;
        private String ulSex;
        private String ulStuno;
        private String ulTel;
        private String ulUsername;
        private Object updateTime;
    }
}
