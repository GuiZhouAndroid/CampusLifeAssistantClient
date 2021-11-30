package work.lpssfxy.www.campuslifeassistantclient.entity.okgo;

import java.util.List;

import lombok.Data;

/**
 * created by on 2021/11/29
 * 描述：MP分页用户数据
 *
 * @author ZSAndroid
 * @create 2021-11-29-23:05
 */
@Data
public class OkGoUserInfoPageBean {

    private int code;
    private Data data;
    private String msg;
    private String nowTime;
    @lombok.Data
    public static class Data {
        private String countId;
        private int current;
        private int maxLimit;
        private boolean optimizeCountSql;
        private List<?> orders;
        private int pages;
        private List<Records> records;
        private boolean searchCount;
        private int size;
        private int total;
        @lombok.Data
        public static class Records {
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
}
