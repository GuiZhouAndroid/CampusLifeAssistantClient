package work.lpssfxy.www.campuslifeassistantclient.entity.dto;

import lombok.Data;

/**
 * created by on 2021/11/24
 * 描述：角色信息列表适配器RecyclerView绑定View视图中的控件id需要用到的实体
 *
 * @author ZSAndroid
 * @create 2021-11-24-23:09
 */

@Data
public class RoleInfoBean {
    private int trId;//角色ID
    private String trName;//角色名称
    private String trDescription;//
    private String createTime;//添加时间
    private String updateTime;//更新时间

    public RoleInfoBean() {
    }

    /**
     * 创建列表适配角色对象数据
     *
     * @param trId          角色ID
     * @param trName        角色名称
     * @param trDescription 角色描述
     * @param createTime    添加时间
     * @param updateTime    更新时间
     */
    public RoleInfoBean(int trId, String trName, String trDescription, String createTime, String updateTime) {
        this.trId = trId;
        this.trName = trName;
        this.trDescription = trDescription;
        this.createTime = createTime;
        this.updateTime = updateTime;
    }
}
