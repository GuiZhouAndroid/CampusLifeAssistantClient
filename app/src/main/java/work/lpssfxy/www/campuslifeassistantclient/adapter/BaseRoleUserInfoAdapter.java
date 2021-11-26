package work.lpssfxy.www.campuslifeassistantclient.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;

import java.util.List;

import work.lpssfxy.www.campuslifeassistantclient.R;
import work.lpssfxy.www.campuslifeassistantclient.entity.dto.RoleUserInfoBean;

/**
 * created by on 2021/11/26
 * 描述：拥有该角色全部的用户信息列表适配器，自动填充展示全部用户和角色信息
 *
 * @author ZSAndroid
 * @create 2021-11-26-22:19
 */
public class BaseRoleUserInfoAdapter extends BaseQuickAdapter<RoleUserInfoBean, BaseViewHolder> {

    /**
     * 有参实例，无集合数据
     *
     * @param layoutResId 数据需要自动适配的developer_fragment_have_role_user_info_role_id_recycler_view_item.xml视图布局的id
     */
    public BaseRoleUserInfoAdapter(int layoutResId) {
        super(layoutResId);
    }

    public BaseRoleUserInfoAdapter(int layoutResId, @Nullable List<RoleUserInfoBean> data) {
        super(layoutResId, data);
        //通过子View的Id绑定适配器，创建适配器实例时，自动添加子view监听事件
        //addChildClickViewIds(R.id.select_role_user_info_user_id);
        //addChildClickViewIds(R.id.select_role_user_info_user_name);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder baseViewHolder, RoleUserInfoBean roleUserInfoBean) {
        //可链式调用赋值
        baseViewHolder
                .setText(R.id.select_role_user_info_user_id, String.valueOf(roleUserInfoBean.getTrId()))
                .setText(R.id.select_role_user_info_user_name, roleUserInfoBean.getUlUsername())
                .setText(R.id.select_role_user_info_real_name, roleUserInfoBean.getUlRealname())
                .setText(R.id.select_role_user_info_role_id, String.valueOf(roleUserInfoBean.getTrId()))
                .setText(R.id.select_role_user_info_role_name, roleUserInfoBean.getTrName());
    }
}
