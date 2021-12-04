package work.lpssfxy.www.campuslifeassistantclient.entity.okgo;

import lombok.Data;

/**
 * created by on 2021/12/4
 * 描述：申请跑腿认证信息实体
 *
 * @author ZSAndroid
 * @create 2021-12-04-22:36
 */
@Data
public class OkGoApplyRunBean {

    private int code;
    private Data data;
    private String msg;
    private String nowTime;

    @lombok.Data
    public static class Data {
        private String arCar; //所属车辆
        private String arGraduationData; //毕业时间
        private String arHealthCode; //健康码
        private int arId; //自增ID
        private String arNucleicPic; //核酸截图
        private String arPostscript; // 审核回复内容
        private String arRunCode; //行程码截图
        private int arState;//审核状态 0：审核中 1:审核通过 2：审核失败
        private String arStuCard; //学生证
        private int arType;//申请跑腿角色ID
        private int arUserId; //用户ID
        private String createTime; //申请时间
        private String updateTime; //审核时间
    }
}
