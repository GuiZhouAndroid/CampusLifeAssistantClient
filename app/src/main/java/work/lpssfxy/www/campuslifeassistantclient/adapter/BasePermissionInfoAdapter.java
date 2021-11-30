package work.lpssfxy.www.campuslifeassistantclient.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;

import java.util.List;

import work.lpssfxy.www.campuslifeassistantclient.R;
import work.lpssfxy.www.campuslifeassistantclient.entity.dto.PermissionInfoBean;
import work.lpssfxy.www.campuslifeassistantclient.entity.dto.RoleInfoBean;

/**
 * created by on 2021/11/24
 * 描述：角色列表适配器，自动填充展示全部角色信息
 *
 * @author ZSAndroid
 * @create 2021-11-24-22:29
 */

public class BasePermissionInfoAdapter extends BaseQuickAdapter<PermissionInfoBean, BaseViewHolder> {

    /**
     * 有参实例，无集合数据
     *
     * @param layoutResId 数据需要自动适配的XML视图布局的id
     */
    public BasePermissionInfoAdapter(int layoutResId) {
        super(layoutResId);
    }

    /**
     * 有参实例，有集合数据
     *
     * @param layoutResId 数据需要自动适配的developer_fragment_select_all_permission_info_recycler_view_item.xml视图布局的id
     * @param data        OkGo网络请求SpringBoot后端接口，服务器返回的List<Permission>对象数据集合
     */
    public BasePermissionInfoAdapter(int layoutResId, @Nullable List<PermissionInfoBean> data) {
        super(layoutResId, data);
        //通过子View的Id绑定适配器，创建适配器实例时，自动添加子view监听事件
        addChildClickViewIds(R.id.select_all_permission_name);
        addChildClickViewIds(R.id.select_all_permission_info);
    }

    /**
     * 配置适配Item绑定View控件参数
     *
     * @param baseViewHolder 链式调用自定义绑定View，可以通过此实例调用View中的控件id，进行适配设置
     * @param permissionInfoBean   权限对象实体
     */
    @Override
    protected void convert(@NonNull BaseViewHolder baseViewHolder, PermissionInfoBean permissionInfoBean) {
        //可链式调用赋值
        baseViewHolder
                .setText(R.id.select_all_permission_id, String.valueOf(permissionInfoBean.getTpId()))
                .setText(R.id.select_all_permission_name, permissionInfoBean.getTpName())
                .setText(R.id.select_all_permission_info, permissionInfoBean.getTpDescription())
                .setText(R.id.select_all_permission_create_time, permissionInfoBean.getCreateTime())
                .setText(R.id.select_all_permission_update_time, permissionInfoBean.getUpdateTime());
    }
}
