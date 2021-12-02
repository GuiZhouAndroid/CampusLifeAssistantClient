package work.lpssfxy.www.campuslifeassistantclient.entity.okgo;

import lombok.Data;

/**
 * created by on 2021/12/2
 * 描述：
 *
 * @author ZSAndroid
 * @create 2021-12-02-21:01
 */
@Data
public class OkGoUserCerBean {

    private int code;
    private Data data;
    private String msg;
    private String nowTime;

    @lombok.Data
    public static class Data {
        private int ulId;
        private String ulIdcard;
        private String ulRealname;
        private String ulSex;
        private String ulUsername;
        private UserCertificationBean userCertificationBean;

        @lombok.Data
        public static class UserCertificationBean {
            private String createTime;
            private int tucId;
            private String tucIdcard;
            private boolean tucState;
            private int tucUserId;
            private String updateTime;
        }
    }
}
