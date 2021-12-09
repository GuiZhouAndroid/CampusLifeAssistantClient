package work.lpssfxy.www.campuslifeassistantclient.entity.okgo;

import java.util.List;

import lombok.Data;

/**
 * created by on 2021/12/9
 * 描述：用户全部收货地址信息
 *
 * @author ZSAndroid
 * @create 2021-12-09-22:57
 */
@Data
public class OkGoAllAddressInfoBean {

    private int code;
    private List<Data> data;
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
