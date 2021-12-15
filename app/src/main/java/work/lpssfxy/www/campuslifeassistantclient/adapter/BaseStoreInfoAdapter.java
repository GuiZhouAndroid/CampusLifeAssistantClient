package work.lpssfxy.www.campuslifeassistantclient.adapter;

import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.module.DraggableModule;
import com.chad.library.adapter.base.module.LoadMoreModule;
import com.chad.library.adapter.base.module.UpFetchModule;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;

import java.util.List;

import work.lpssfxy.www.campuslifeassistantclient.R;
import work.lpssfxy.www.campuslifeassistantclient.entity.dto.StoreInfoBean;

/**
 * created by on 2021/11/26
 * 描述：店铺信息列表适配器
 *
 * @author ZSAndroid
 * @create 2021-11-26-22:19
 */
public class BaseStoreInfoAdapter extends BaseQuickAdapter<StoreInfoBean, BaseViewHolder> implements LoadMoreModule, DraggableModule, UpFetchModule {

    /**
     * 有参实例，无集合数据
     *
     * @param layoutResId 数据需要自动适配视图布局的id
     */
    public BaseStoreInfoAdapter(int layoutResId) {
        super(layoutResId);
    }

    public BaseStoreInfoAdapter(int layoutResId, @Nullable List<StoreInfoBean> data) {
        super(layoutResId, data);
        //通过子View的Id绑定适配器，创建适配器实例时，自动添加子view监听事件
        addChildClickViewIds(R.id.tv_canteen_store_mobile);
        addChildClickViewIds(R.id.tv_canteen_address);
        addChildClickViewIds(R.id.iv_canteen_address);
        addChildClickViewIds(R.id.ll_canteen_address);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder baseViewHolder, StoreInfoBean storeInfoBean) {
        baseViewHolder
                .setText(R.id.tv_canteen_store_name, storeInfoBean.getSsName())
                .setText(R.id.tv_canteen_time, "(" + storeInfoBean.getSsBeginTime() + "~" + storeInfoBean.getSsEndTime() + ")")
                .setText(R.id.tv_canteen_notice, storeInfoBean.getSsNotice())
                .setText(R.id.tv_canteen_recommend, "("+storeInfoBean.getSsRecommend() + ")")
                .setText(R.id.tv_canteen_store_mobile, storeInfoBean.getSsMobile())
                .setText(R.id.tv_canteen_address, storeInfoBean.getSsAddress());
//        //阿里云OSS图片设置店铺Logo
        Glide.with(getContext()).load(storeInfoBean.getSsLogo()).placeholder(R.mipmap.goodszwt).into((ImageView) baseViewHolder.getView(R.id.iv_canteen_store_logo));
    }
}
