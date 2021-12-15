package work.lpssfxy.www.campuslifeassistantclient.entity.okgo;

import java.util.List;

import lombok.Data;

/**
 * created by on 2021/12/15
 * 描述：全部店铺实体信息
 *
 * @author ZSAndroid
 * @create 2021-12-15-10:54
 */
@Data
public class OkGoAllShopInfoBean {

    private int code;
    private List<Data> data;
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
        private String ssLogo;
        private String ssMobile;
        private String ssName;
        private String ssNotice;
        private String ssPic;
        private String ssRecommend;
        private String updateTime;
        private int userId;
    }
}
