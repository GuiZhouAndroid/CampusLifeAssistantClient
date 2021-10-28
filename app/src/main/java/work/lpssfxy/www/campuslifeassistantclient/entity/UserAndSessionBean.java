package work.lpssfxy.www.campuslifeassistantclient.entity;

import lombok.Data;

/**
 * created by on 2021/10/28
 * 描述：用户信息+QQ会话实体类
 *
 * @author ZSAndroid
 * @create 2021-10-28-20:28
 */
@Data
public class UserAndSessionBean {

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
        private int qqOauth2Id;
        private int queryAuthorityCost;
        private int ret;
        private int ulId;
        private String updateTime;
        private Object userInfo;
    }
}
