package work.lpssfxy.www.campuslifeassistantclient.entity.okgo;

import lombok.Data;

/**
 * created by on 2021/12/13
 * 描述：用户表和商铺表实体
 *
 * @author ZSAndroid
 * @create 2021-12-13-13:56
 */
@Data
public class OkGoUserStoreInfoBean {

    private int code;
    private Data data;
    private String msg;
    private String nowTime;

    @lombok.Data
    public static class Data {
        private String createTime;
        private int scId;
        private String scName;
        private String ssAddress;
        private String ssBeginTime;
        private String ssDesc;
        private String ssEndTime;
        private String ssLogo;
        private String ssMobile;
        private String ssName;
        private String ssNotice;
        private String ssPic;
        private String ssRecommend;
        private String ulIdcard;
        private String ulRealname;
        private String ulTel;
        private String ulUsername;
        private String updateTime;
    }
}
