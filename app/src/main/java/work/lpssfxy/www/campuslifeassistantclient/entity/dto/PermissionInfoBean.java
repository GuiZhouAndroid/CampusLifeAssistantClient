package work.lpssfxy.www.campuslifeassistantclient.entity.dto;

import lombok.Data;

/**
 * created by on 2021/11/27
 * 描述：权限信息列表适配器RecyclerView绑定View视图中的控件id需要用到的实体
 *
 * @author ZSAndroid
 * @create 2021-11-27-15:03
 */
@Data
public class PermissionInfoBean {
    private int tpId;
    private String tpName;
    private String tpDescription;
    private String createTime;
    private String updateTime;

    public PermissionInfoBean() {

    }

    /**
     * 创建列表适配权限对象数据
     *
     * @param tpId          权限ID
     * @param tpName        权限名称
     * @param tpDescription 权限描述
     * @param createTime    添加时间
     * @param updateTime    更新时间
     */
    public PermissionInfoBean(int tpId, String tpName, String tpDescription, String createTime, String updateTime) {
        this.tpId = tpId;
        this.tpName = tpName;
        this.tpDescription = tpDescription;
        this.createTime = createTime;
        this.updateTime = updateTime;
    }
}
