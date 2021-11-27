package work.lpssfxy.www.campuslifeassistantclient.entity.okgo;

import java.util.List;

import lombok.Data;

/**
 * created by on 2021/11/27
 * 描述：OkGo网络请求返回全部权限信息的Json数据
 *
 * @author ZSAndroid
 * @create 2021-11-27-14:56
 */
@Data
public class OkGoAllPermissionInfoBean {

    private int code;
    private List<Data> data;
    private String msg;
    private String nowTime;

    @lombok.Data
    public static class Data {
        private String createTime;
        private String tpDescription;
        private int tpId;
        private String tpName;
        private String updateTime;
    }
}
