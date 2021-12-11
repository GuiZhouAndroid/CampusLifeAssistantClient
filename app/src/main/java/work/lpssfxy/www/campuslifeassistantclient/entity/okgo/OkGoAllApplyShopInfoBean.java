package work.lpssfxy.www.campuslifeassistantclient.entity.okgo;

import java.util.List;

import lombok.Data;

/**
 * created by on 2021/12/11
 * 描述：OkGo网络请求返回全部商家认证申请信息的Json数据
 *
 * @author ZSAndroid
 * @create 2021-12-11-23:42
 */
@Data
public class OkGoAllApplyShopInfoBean {

    private int code;
    private List<Data> data;
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
