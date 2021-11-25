package work.lpssfxy.www.campuslifeassistantclient.entity.okgo;

import java.util.List;

import lombok.Data;

/**
 * created by on 2021/11/25
 * 描述：拥有该角色全部的用户信息实体
 *
 * @author ZSAndroid
 * @create 2021-11-25-23:38
 */
@Data
public class OkGoRoleUserInfoBean {

    private int code;
    private List<Data> data;
    private String msg;
    private String nowTime;

    @lombok.Data
    public static class Data {
        private String createTime;
        private List<RoleInfo> roleInfo;
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

        @lombok.Data
        public static class RoleInfo {
            private String createTime;
            private String trDescription;
            private int trId;
            private String trName;
            private String updateTime;
        }
    }
}
