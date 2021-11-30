package work.lpssfxy.www.campuslifeassistantclient.entity.dto;

import lombok.Data;

/**
 * created by on 2021/11/29
 * 描述：用户实体类
 *
 * @author ZSAndroid
 * @create 2021-11-29-12:39
 */

@Data
public class UserInfoBean {
    private int ulId;//用户ID
    private String ulUsername; //用户名
    private String ulRealname; //真实姓名
    private String ulSex; //性别
    private String ulIdcard; //身份证号
    private String ulStuno; //学号
    private String ulTel; //手机号
    private String ulEmail; //QQ邮箱
    private String ulClass; //班级
    private String ulDept; //院系
    private String createTime; //注册时间
    private String updateTime; //修改时间

    public UserInfoBean() {
    }

    public UserInfoBean(int ulId, String ulUsername, String ulRealname, String ulSex, String ulIdcard, String ulStuno, String ulTel, String ulEmail, String ulClass, String ulDept, String createTime, String updateTime) {
        this.ulId = ulId;
        this.ulUsername = ulUsername;
        this.ulRealname = ulRealname;
        this.ulSex = ulSex;
        this.ulIdcard = ulIdcard;
        this.ulStuno = ulStuno;
        this.ulTel = ulTel;
        this.ulEmail = ulEmail;
        this.ulClass = ulClass;
        this.ulDept = ulDept;
        this.createTime = createTime;
        this.updateTime = updateTime;
    }
}
