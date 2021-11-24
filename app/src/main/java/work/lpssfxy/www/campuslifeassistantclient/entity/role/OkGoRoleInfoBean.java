package work.lpssfxy.www.campuslifeassistantclient.entity.role;

import java.util.List;

import lombok.Data;

/**
 * created by on 2021/11/24
 * 描述：OkGo网络请求返回角色信息的Json数据
 *
 * @author ZSAndroid
 * @create 2021-11-24-22:11
 */

@Data
public class OkGoRoleInfoBean {

    private int code;
    private List<Data> data;//角色对象数据集合
    private String msg;
    private String nowTime;

    @lombok.Data
    public static class Data {
        private String createTime;//添加时间
        private String trDescription;//角色描述
        private int trId;//角色ID
        private String trName;//角色名称
        private String updateTime;//更新时间
    }
}
