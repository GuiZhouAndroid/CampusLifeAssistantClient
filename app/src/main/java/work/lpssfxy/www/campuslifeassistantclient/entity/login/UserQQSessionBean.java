package work.lpssfxy.www.campuslifeassistantclient.entity.login;

import lombok.Data;

/**
 * created by on 2021/10/31
 * 描述：
 *
 * @author ZSAndroid
 * @create 2021-10-31-14:06
 */
@Data
public class UserQQSessionBean{

    private int code;
    private Data data;
    private String msg;
    private String nowTime;
    @lombok.Data
    public static class Data {
        private String accessToken;
        private int authorityCost;
        private String createTime;
        private int expiresIn;
        private long expiresTime;
        private int loginCost;
        private String msg;
        private String openid;
        private String payToken;
        private String pf;
        private String pfkey;
        private int queryAuthorityCost;
        private int ret;
        private String updateTime;
        private UserInfo userInfo;
        @lombok.Data
        public static class UserInfo {
            private String createTime;
            private String lastLoginTime;
            private String ulClass;
            private String ulDept;
            private String ulEmail;
            private int ulId;
            private String ulIdcard;
            private String ulRealname;
            private String ulSex;
            private String ulStuno;
            private String ulTel;
            private String ulUsername;
            private String updateTime;
        }
    }
}
