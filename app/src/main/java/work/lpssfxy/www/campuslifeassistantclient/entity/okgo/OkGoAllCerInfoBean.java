package work.lpssfxy.www.campuslifeassistantclient.entity.okgo;

import java.util.List;

import lombok.Data;

/**
 * created by on 2021/12/8
 * 描述：OkGo网络请求返回全部用户实名认证信息的Json数据
 *
 * @author ZSAndroid
 * @create 2021-12-08-18:30
 */
@Data
public class OkGoAllCerInfoBean {

    private int code;
    private List<Data> data;
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
            private String tucAddress;
            private String tucBirth;
            private int tucId;
            private String tucIdcard;
            private String tucName;
            private String tucNation;
            private String tucOrganization;
            private String tucSex;
            private String tucState;
            private int tucUserId;
            private String tucValidPeriod;
            private String updateTime;
        }
    }
}
