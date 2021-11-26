package work.lpssfxy.www.campuslifeassistantclient.entity.dto;

import lombok.Data;

/**
 * created by on 2021/11/26
 * 描述：拥有该角色全部的用户信息列表适配器RecyclerView绑定View视图中的控件id需要用到的实体
 *
 * @author ZSAndroid
 * @create 2021-11-26-21:51
 */
@Data
public class RoleUserInfoBean {
    private int ulId;
    private String ulUsername;
    private String ulRealname;
    private int trId;
    private String trName;

    public RoleUserInfoBean(int ulId, String ulUsername, int trId, String trName) {
    }

    /**
     * 创建列表适配用户和角色对象数据
     *
     * @param ulId       用户ID
     * @param ulUsername 用户名
     * @param ulRealname 真实姓名
     * @param trId       角色ID
     * @param trName     角色名称
     */
    public RoleUserInfoBean(int ulId, String ulUsername, String ulRealname, int trId, String trName) {
        this.ulId = ulId;
        this.ulUsername = ulUsername;
        this.ulRealname = ulRealname;
        this.trId = trId;
        this.trName = trName;
    }
}
