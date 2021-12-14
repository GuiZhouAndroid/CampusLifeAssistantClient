package work.lpssfxy.www.campuslifeassistantclient.view.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;


import com.google.gson.reflect.TypeToken;
import com.kunminx.linkage.LinkageRecyclerView;
import com.kunminx.linkage.adapter.viewholder.LinkagePrimaryViewHolder;
import com.kunminx.linkage.adapter.viewholder.LinkageSecondaryViewHolder;
import com.kunminx.linkage.bean.BaseGroupedItem;
import com.xuexiang.xui.utils.SnackbarUtils;
import com.xuexiang.xutil.net.JsonUtil;
import com.xuexiang.xutil.resource.ResourceUtils;

import java.util.List;

import butterknife.BindView;
import work.lpssfxy.www.campuslifeassistantclient.base.eleme.CustomLinkagePrimaryAdapterConfig;
import work.lpssfxy.www.campuslifeassistantclient.base.eleme.ElemeGroupedItem;
import work.lpssfxy.www.campuslifeassistantclient.base.eleme.ElemeSecondaryAdapterConfig;
import work.lpssfxy.www.campuslifeassistantclient.R;
import work.lpssfxy.www.campuslifeassistantclient.view.BaseActivity;

/**
 * created by on 2021/12/14
 * 描述：
 *
 * @author ZSAndroid
 * @create 2021-12-14-18:24
 */

@SuppressLint("NonConstantResourceId")
public class CanteenRunBuyActivity extends BaseActivity implements CustomLinkagePrimaryAdapterConfig.OnPrimaryItemClickListener, ElemeSecondaryAdapterConfig.OnSecondaryItemClickListener{


    @BindView(R.id.linkage) LinkageRecyclerView linkage;

    @Override
    protected Boolean isSetSwipeBackLayout() {
        return true;
    }

    @Override
    protected Boolean isSetStatusBarState() {
        return true;
    }

    @Override
    protected Boolean isSetBottomNaviCationState() {
        return false;
    }

    @Override
    protected Boolean isSetBottomNaviCationColor() {
        return false;
    }

    @Override
    protected Boolean isSetImmersiveFullScreen() {
        return false;
    }

    @Override
    public int bindLayout() {
        return R.layout.activity_canteen_run_buy;
    }

    @Override
    protected void prepareData() {

    }

    @Override
    protected void initView() {
        linkage.init(JsonUtil.fromJson(ResourceUtils.readStringFromAssert("eleme.json"), new TypeToken<List<ElemeGroupedItem>>() {}.getType()), new CustomLinkagePrimaryAdapterConfig(this), new ElemeSecondaryAdapterConfig(this));
    }

    @Override
    protected void initData(Bundle savedInstanceState) {

    }

    @Override
    protected void initEvent() {

    }

    @Override
    protected void initListener() {

    }

    @Override
    protected void doBusiness() {

    }

    @Override
    public void onPrimaryItemClick(LinkagePrimaryViewHolder holder, View view, String title) {
        SnackbarUtils.Short(view, title).show();
    }

    @Override
    public void onSecondaryItemClick(LinkageSecondaryViewHolder holder, ViewGroup view, BaseGroupedItem<ElemeGroupedItem.ItemInfo> item) {
        SnackbarUtils.Short(view, item.info.getTitle()).show();
    }

    @Override
    public void onGoodAdd(View view, BaseGroupedItem<ElemeGroupedItem.ItemInfo> item) {
        SnackbarUtils.Short(view, "添加：" + item.info.getTitle()).show();

    }
}
