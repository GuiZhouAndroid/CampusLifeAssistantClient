package work.lpssfxy.www.campuslifeassistantclient.entity.okgo;

import lombok.Data;

/**
 * created by on 2021/10/28
 * 描述：用户实体
 *
 * @author ZSAndroid
 * @create 2021-10-28-6:06
 */

@Data
public class OkGoUserBean {
    /**
     * 请求状态
     */
    private int code;
    private Data data;
    private String msg;
    private String nowTime;

    /**
     * 登录用户信息
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
        private String updateTime;
    }
}
