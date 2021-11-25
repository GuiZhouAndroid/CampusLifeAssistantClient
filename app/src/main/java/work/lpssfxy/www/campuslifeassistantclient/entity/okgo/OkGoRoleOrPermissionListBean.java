package work.lpssfxy.www.campuslifeassistantclient.entity.okgo;

import java.util.List;

import lombok.Data;

/**
 * created by on 2021/11/16
 * 描述：角色 + 权限 +List集合实体
 *
 * @author ZSAndroid
 * @create 2021-11-16-21:22
 */

@Data
public class OkGoRoleOrPermissionListBean {
    private int code;
    private List<String> data;
    private String msg;
    private String nowTime;
}
