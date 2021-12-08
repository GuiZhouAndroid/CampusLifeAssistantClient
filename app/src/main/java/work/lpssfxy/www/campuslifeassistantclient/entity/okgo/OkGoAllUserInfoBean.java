package work.lpssfxy.www.campuslifeassistantclient.entity.okgo;

import java.util.List;

import lombok.Data;

/**
 * created by on 2021/11/29
 * 描述：OkGo网络请求返回全部用户信息的Json数据
 *
 * @author ZSAndroid
 * @create 2021-11-29-12:37
 */
@Data
public class OkGoAllUserInfoBean {

    private int code;
    private List<Data> data;
    private String msg;
    private String nowTime;

    @lombok.Data
    public static class Data {
        private String createTime;
        private String ulClass;
        private String ulDept;
        private String ulEmail;
        private int ulId;
        private String ulIdcard;
        private String ulPassword;
        private String ulRealname;
        private String ulSex;
        private String ulStuno;
        private String ulTel;
        private String ulUsername;
        private String updateTime;
    }
}
