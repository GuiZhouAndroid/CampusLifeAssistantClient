package work.lpssfxy.www.campuslifeassistantclient.entity.okgo;

import lombok.Data;

/**
 * created by on 2021/12/11
 * 描述：商家入驻认证信息实体
 *
 * @author ZSAndroid
 * @create 2021-12-11-21:46
 */
@Data
public class OkGoApplyShopBean {

    private int code;
    private Data data;
    private String msg;
    private String nowTime;

    @lombok.Data
    public static class Data {
        private int asId;
        private String asLicence;
        private int asState;
        private int asType;
        private int asUserId;
        private String createTime;
        private String updateTime;
    }
}
