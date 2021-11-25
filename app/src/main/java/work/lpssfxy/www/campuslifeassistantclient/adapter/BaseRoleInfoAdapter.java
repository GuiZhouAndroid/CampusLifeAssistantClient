package work.lpssfxy.www.campuslifeassistantclient.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;

import java.util.List;

import work.lpssfxy.www.campuslifeassistantclient.R;
import work.lpssfxy.www.campuslifeassistantclient.entity.dto.RoleInfoBean;

/**
 * created by on 2021/11/24
 * 描述：角色列表适配器，自动填充展示全部角色信息
 *
 * @author ZSAndroid
 * @create 2021-11-24-22:29
 */

public class BaseRoleInfoAdapter extends BaseQuickAdapter<RoleInfoBean, BaseViewHolder> {

    /**
     * 有参实例，无集合数据
     *
     * @param layoutResId 数据需要自动适配的XML视图布局的id
     */
    public BaseRoleInfoAdapter(int layoutResId) {
        super(layoutResId);
    }

    /**
     * 有参实例，有集合数据
     *
     * @param layoutResId 数据需要自动适配的XML视图布局的id
     * @param data        OkGo网络请求SpringBoot后端接口，服务器返回的List<Role>对象数据集合
     */
    public BaseRoleInfoAdapter(int layoutResId, @Nullable List<RoleInfoBean> data) {
        super(layoutResId, data);
        //通过子View的Id绑定适配器，创建适配器实例时，自动添加子view监听事件
        addChildClickViewIds(R.id.btn_do_role);
    }

    /**
     * 配置适配Item绑定View控件参数
     *
     * @param baseViewHolder 链式调用自定义绑定View，可以通过此实例调用View中的控件id，进行适配设置
     * @param roleInfoBean   角色对象实体
     */
    @Override
    protected void convert(@NonNull BaseViewHolder baseViewHolder, RoleInfoBean roleInfoBean) {
        //可链式调用赋值
        baseViewHolder
                .setText(R.id.select_all_role_id, String.valueOf(roleInfoBean.getTrId()))
                .setText(R.id.select_all_role_name, roleInfoBean.getTrName())
                .setText(R.id.select_all_role_info, roleInfoBean.getTrDescription())
                .setText(R.id.select_all_role_create_time, roleInfoBean.getCreateTime())
                .setText(R.id.select_all_role_update_time, roleInfoBean.getUpdateTime());
    }
}
