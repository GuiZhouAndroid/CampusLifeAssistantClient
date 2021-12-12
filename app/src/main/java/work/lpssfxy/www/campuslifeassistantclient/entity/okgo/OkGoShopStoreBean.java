package work.lpssfxy.www.campuslifeassistantclient.entity.okgo;

import lombok.Data;

/**
 * created by on 2021/12/4
 * 描述：商铺信息实体
 *
 * @author ZSAndroid
 * @create 2021-12-04-22:36
 */
@Data
public class OkGoShopStoreBean {


    private int code;
    private Data data;
    private String msg;
    private String nowTime;

    @lombok.Data
    public static class Data {
        private String createTime;
        private int scId;
        private String ssAddress;
        private String ssBeginTime;
        private String ssDesc;
        private String ssEndTime;
        private int ssId;
        private String ssMobile;
        private String ssName;
        private String ssNotice;
        private String ssPic;
        private String updateTime;
        private int userId;
    }
}
