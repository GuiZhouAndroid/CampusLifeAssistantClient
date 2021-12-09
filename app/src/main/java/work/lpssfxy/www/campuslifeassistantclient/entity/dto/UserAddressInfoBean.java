package work.lpssfxy.www.campuslifeassistantclient.entity.dto;

import lombok.Data;

/**
 * created by on 2021/12/9
 * 描述：用户收货地址单个实体
 *
 * @author ZSAndroid
 * @create 2021-12-09-23:02
 */
@Data
public class UserAddressInfoBean {
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

    public UserAddressInfoBean() {
    }

    public UserAddressInfoBean(int addressId, String addressName, String createTime, String district, String floor, String gender, String mobile, String place, String street, String updateTime, int userId) {
        this.addressId = addressId;
        this.addressName = addressName;
        this.createTime = createTime;
        this.district = district;
        this.floor = floor;
        this.gender = gender;
        this.mobile = mobile;
        this.place = place;
        this.street = street;
        this.updateTime = updateTime;
        this.userId = userId;
    }
}
