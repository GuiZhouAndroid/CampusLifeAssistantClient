package work.lpssfxy.www.campuslifeassistantclient.entity.okgo;

import lombok.Data;

/**
 * created by on 2021/12/11
 * 描述：添加收货地址信息实体
 *
 * @author ZSAndroid
 * @create 2021-12-11-10:52
 */
@Data
public class OkGoAddressBean {

    private int code;
    private Data data;
    private String msg;
    private String nowTime;

    @lombok.Data
    public static class Data {
        private int addressId;
        private String addressName;
        private String createTime;
        private String district;
        private String floor;
        private String gender;
        private String mobile;
        private String place;
        private String street;
        private String updateTime;
        private int userId;
    }
}
