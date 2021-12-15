package work.lpssfxy.www.campuslifeassistantclient.entity.dto;

import lombok.Data;

/**
 * created by on 2021/12/13
 * 描述：商家商品信息单个实体信息
 *
 * @author ZSAndroid
 * @create 2021-12-13-20:20
 */

@Data
public class StoreInfoBean {
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

    public StoreInfoBean() {
    }

    public StoreInfoBean(String createTime, int scId, String ssAddress, String ssBeginTime, String ssDesc, String ssEndTime, int ssId, String ssLogo, String ssMobile, String ssName, String ssNotice, String ssPic, String ssRecommend, String updateTime, int userId) {
        this.createTime = createTime;
        this.scId = scId;
        this.ssAddress = ssAddress;
        this.ssBeginTime = ssBeginTime;
        this.ssDesc = ssDesc;
        this.ssEndTime = ssEndTime;
        this.ssId = ssId;
        this.ssLogo = ssLogo;
        this.ssMobile = ssMobile;
        this.ssName = ssName;
        this.ssNotice = ssNotice;
        this.ssPic = ssPic;
        this.ssRecommend = ssRecommend;
        this.updateTime = updateTime;
        this.userId = userId;
    }
}
