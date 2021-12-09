package work.lpssfxy.www.campuslifeassistantclient.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;

import java.util.List;

import work.lpssfxy.www.campuslifeassistantclient.R;
import work.lpssfxy.www.campuslifeassistantclient.entity.dto.UserAddressInfoBean;

/**
 * created by on 2021/12/9
 * 描述：自定义用户收货地址内容适配器
 *
 * @author ZSAndroid
 * @create 2021-12-09-23:11
 */
public class BaseUserAddressInfoAdapter extends BaseQuickAdapter<UserAddressInfoBean, BaseViewHolder> {

    /**
     * 有参实例，无集合数据
     *
     * @param layoutResId 数据需要自动适配的activity_user_address_recycler_view_item.xml视图布局的id
     */
    public BaseUserAddressInfoAdapter(int layoutResId) {
        super(layoutResId);
    }

    /**
     * 有参实例，有集合数据
     *
     * @param layoutResId 数据需要自动适配的activity_user_address_recycler_view_item.xml视图布局的id
     * @param data        OkGo网络请求SpringBoot后端接口，服务器返回的List<用户收货地址>对象数据集合
     */
    public BaseUserAddressInfoAdapter(int layoutResId, @Nullable List<UserAddressInfoBean> data) {
        super(layoutResId, data);
    }

    /**
     * 配置适配Item绑定View控件参数
     *
     * @param baseViewHolder      链式调用自定义绑定View，可以通过此实例调用View中的控件id，进行适配设置
     * @param userAddressInfoBean 收货地址对象实体
     */
    @Override
    protected void convert(@NonNull BaseViewHolder baseViewHolder, UserAddressInfoBean userAddressInfoBean) {
        //可链式调用赋值
        baseViewHolder
                .setText(R.id.tv_address_name, userAddressInfoBean.getAddressName())
                .setText(R.id.tv_address_gender, userAddressInfoBean.getGender())
                .setText(R.id.tv_address_mobile, userAddressInfoBean.getMobile())
                .setText(R.id.tv_address_district, userAddressInfoBean.getDistrict())
                .setText(R.id.tv_address_place, userAddressInfoBean.getPlace())
                .setText(R.id.tv_address_floor, userAddressInfoBean.getFloor())
                .setText(R.id.tv_address_street, userAddressInfoBean.getStreet());
    }
}
