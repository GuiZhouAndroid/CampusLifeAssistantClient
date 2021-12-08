package work.lpssfxy.www.campuslifeassistantclient.entity.okgo;

import java.util.List;

import lombok.Data;

/**
 * created by on 2021/12/8
 * 描述：OkGo网络请求返回全部用户权限信息的Json数据
 *
 * @author ZSAndroid
 * @create 2021-12-08-20:55
 */
@Data
public class OkGoAllUserPermissionInfoBean {

    private int code;
    private List<Data> data;
    private String msg;
    private String nowTime;

    @lombok.Data
    public static class Data {
        private String createTime;
        private List<PermissionInfo> permissionInfo;
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
        public static class PermissionInfo {
            private String createTime;
            private String tpDescription;
            private int tpId;
            private String tpName;
            private String updateTime;
        }

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
