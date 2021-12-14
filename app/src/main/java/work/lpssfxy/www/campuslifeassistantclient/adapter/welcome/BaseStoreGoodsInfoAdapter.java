package work.lpssfxy.www.campuslifeassistantclient.adapter.welcome;

import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.module.DraggableModule;
import com.chad.library.adapter.base.module.LoadMoreModule;
import com.chad.library.adapter.base.module.UpFetchModule;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import java.util.List;

import work.lpssfxy.www.campuslifeassistantclient.R;
import work.lpssfxy.www.campuslifeassistantclient.base.Constant;
import work.lpssfxy.www.campuslifeassistantclient.entity.dto.StoreGoodsInfoBean;
import work.lpssfxy.www.campuslifeassistantclient.entity.okgo.OkGoResponseBean;
import work.lpssfxy.www.campuslifeassistantclient.utils.XToastUtils;
import work.lpssfxy.www.campuslifeassistantclient.utils.gson.GsonUtil;

/**
 * created by on 2021/11/26
 * 描述：商家商品信息列表适配器，自动填充展示全部用户和角色信息
 *
 * @author ZSAndroid
 * @create 2021-11-26-22:19
 */
public class BaseStoreGoodsInfoAdapter extends BaseQuickAdapter<StoreGoodsInfoBean, BaseViewHolder> implements LoadMoreModule, DraggableModule, UpFetchModule {

    /**
     * 有参实例，无集合数据
     *
     * @param layoutResId 数据需要自动适配视图布局的id
     */
    public BaseStoreGoodsInfoAdapter(int layoutResId) {
        super(layoutResId);
    }

    public BaseStoreGoodsInfoAdapter(int layoutResId, @Nullable List<StoreGoodsInfoBean> data) {
        super(layoutResId, data);
        //通过子View的Id绑定适配器，创建适配器实例时，自动添加子view监听事件
        //addChildClickViewIds(R.id.select_role_user_info_user_id);
        //addChildClickViewIds(R.id.select_role_user_info_user_name);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder baseViewHolder, StoreGoodsInfoBean storeGoodsInfoBean) {
        OkGo.<String>post(Constant.SELECT_GOODS_CATEGORY_NAME_BY_CATEGORY_ID + "/" + storeGoodsInfoBean.getCategoryId())
                .tag("查询全部商品分类表信息")
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        OkGoResponseBean okGoResponseBean = GsonUtil.gsonToBean(response.body(), OkGoResponseBean.class);
                        //提取商铺分类集合信息
                        if (200 == okGoResponseBean.getCode() && "success".equals(okGoResponseBean.getMsg())) {
                            //可链式调用赋值
                            baseViewHolder.setText(R.id.tv_goods_type, "(" + okGoResponseBean.getData() + ")");
                            return;
                        }
                        if (200 == okGoResponseBean.getCode() && "此商品分类不存在".equals(okGoResponseBean.getData()) && "success".equals(okGoResponseBean.getMsg())) {
                            //可链式调用赋值
                            baseViewHolder.setText(R.id.tv_goods_type, okGoResponseBean.getData());
                        }
                    }

                    @Override
                    public void onError(Response<String> response) {
                        XToastUtils.error("商品分类错误，请稍候再重试");
                    }
                });
        String strGoodsFlagState = null;
        if (storeGoodsInfoBean.getGoodsFlag() == 0) {
            strGoodsFlagState = "(未上架)";
        } else if (storeGoodsInfoBean.getGoodsFlag() == 1) {
            strGoodsFlagState = "(已上架)";
        } else if (storeGoodsInfoBean.getGoodsFlag() == -1) {
            strGoodsFlagState = "(已下架)";
        }
        baseViewHolder
                .setText(R.id.tv_goods_id, "(编号" + storeGoodsInfoBean.getGoodsId() + ")")
                .setText(R.id.tv_goods_name, storeGoodsInfoBean.getGoodsName())
                .setText(R.id.tv_goods_price, storeGoodsInfoBean.getGoodsPrice() + "元/份")
                .setText(R.id.tv_goods_repertory, "库存(" + storeGoodsInfoBean.getGoodsRepertory() + "份)")
                .setText(R.id.tv_goods_desc, "【商品简介】:" + storeGoodsInfoBean.getGoodsDesc())
                .setText(R.id.tv_goods_create, "上架时间(" + storeGoodsInfoBean.getCreateTime() + ")")
                .setText(R.id.tv_goods_flag, strGoodsFlagState);
        //阿里云OSS图片设置商品封面
        Glide.with(getContext()).load(storeGoodsInfoBean.getGoodsPic()).placeholder(R.mipmap.goodszwt).into((ImageView) baseViewHolder.getView(R.id.iv_goods_pic));
    }
}
