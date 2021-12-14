package work.lpssfxy.www.campuslifeassistantclient.view.fragment.canteen;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.enums.PopupPosition;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;

import java.util.Arrays;
import java.util.Collection;

import butterknife.BindView;
import butterknife.OnClick;
import work.lpssfxy.www.campuslifeassistantclient.R;
import work.lpssfxy.www.campuslifeassistantclient.R2;
import work.lpssfxy.www.campuslifeassistantclient.adapter.SmartViewHolder;
import work.lpssfxy.www.campuslifeassistantclient.adapter.canteen.BaseRecyclerAdapter;
import work.lpssfxy.www.campuslifeassistantclient.base.custompopup.CustomChaoYangFilterDrawerPopupView;
import work.lpssfxy.www.campuslifeassistantclient.utils.statusbarutils.StatusBarUtil;
import work.lpssfxy.www.campuslifeassistantclient.view.BaseFragment;

/**
 * created by on 2021/12/14
 * 描述：朝阳食堂
 *
 * @author ZSAndroid
 * @create 2021-12-14-21:30
 */
@SuppressLint("NonConstantResourceId")
public class CanteenChaoYangFragment extends BaseFragment {

    @BindView(R2.id.ll_chao_yang_show) LinearLayout mLlChaoYangShow;//父布局
    @BindView(R2.id.tv_all_chao_yang) TextView mTvAllChaoYang;//综合
    @BindView(R2.id.tv_price_chao_yang) TextView mTvPriceChaoYang;//价格
    @BindView(R2.id.tv_sales_chao_yang) TextView mTvSalesChaoYang;//销量
    @BindView(R2.id.tv_filter_chao_yang) TextView mTvFilterChaoYang;//筛选

    @BindView(R2.id.refreshLayout_chao_yang) RefreshLayout mRefreshLayoutChaoYang;
    @BindView(R2.id.recyclerView_chao_yang) RecyclerView mRecyclerViewChaoYang;

    private class Model {
        int imageId;
        int avatarId;
        String name;
        String nickname;
    }

    private BaseRecyclerAdapter<Model> mAdapter;

    /**
     * @return 单例对象
     */
    public static CanteenChaoYangFragment newInstance() {
        return new CanteenChaoYangFragment();
    }

    @Override
    protected void lazyLoad() {

    }

    @Override
    protected int bindLayout() {
        return R.layout.fragment_cantten_chao_yang;
    }

    @Override
    protected void prepareData(Bundle savedInstanceState) {

    }

    @Override
    protected void initView(View rootView) {
        mRefreshLayoutChaoYang.setEnableFooterFollowWhenNoMoreData(true);

        mRefreshLayoutChaoYang.autoRefresh();

        //初始化列表和监听
        mRecyclerViewChaoYang.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerViewChaoYang.setItemAnimator(new DefaultItemAnimator());
        mRecyclerViewChaoYang.setAdapter(mAdapter = new BaseRecyclerAdapter<Model>(loadModels(), R.layout.activity_repast_practice_rcy_view_item) {
            @Override
            protected void onBindViewHolder(SmartViewHolder holder, Model model, int position) {
                holder.text(R.id.name, model.name);
                holder.text(R.id.nickname, model.nickname);
                holder.image(R.id.image, model.imageId);
                holder.image(R.id.avatar, model.avatarId);
            }
        });

        mRefreshLayoutChaoYang.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onRefresh(@NonNull final RefreshLayout refreshLayout) {
                refreshLayout.getLayout().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        refreshLayout.finishRefresh();
                        refreshLayout.resetNoMoreData();//setNoMoreData(false);//恢复上拉状态
                    }
                }, 2000);
            }
            @Override
            public void onLoadMore(@NonNull final RefreshLayout refreshLayout) {
                refreshLayout.getLayout().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (mAdapter.getCount() > 12) {
                            Toast.makeText(getActivity().getBaseContext(), "数据全部加载完毕", Toast.LENGTH_SHORT).show();
                            refreshLayout.finishLoadMoreWithNoMoreData();//设置之后，将不会再触发加载事件
                        } else {
                            mAdapter.loadMore(loadModels());
                            refreshLayout.finishLoadMore();
                        }
                    }
                }, 1000);
            }
        });

//        refreshLayout.getLayout().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                refreshLayout.setHeaderInsetStart(SmartUtil.px2dp(toolbar.getHeight()));
//            }
//        }, 500);


        //状态栏透明和间距处理
        StatusBarUtil.darkMode(getActivity());
//        StatusBarUtil.setPaddingSmart(getActivity(), recyclerView);
//        StatusBarUtil.setPaddingSmart(getActivity(), toolbar);
//        StatusBarUtil.setPaddingSmart(getActivity(), rootView.findViewById(R.id.blurView));
    }

    @Override
    protected void initData(Bundle savedInstanceState) {

    }

    @Override
    protected void initEvent() {

    }

    @Override
    protected void doBusiness(Context context) {

    }

    @OnClick({R2.id.tv_all_chao_yang,R2.id.tv_price_chao_yang,R2.id.tv_sales_chao_yang,R2.id.tv_filter_chao_yang})
    public void onChaoYangViewClick(View view) {
        switch (view.getId()) {
            case R.id.tv_all_chao_yang://点击综合
                doAllClick();
                break;
            case R.id.tv_price_chao_yang://点击价格
                doPriceClick();
                break;
            case R.id.tv_sales_chao_yang://点击销量
                doSalesClick();
                break;
            case R.id.tv_filter_chao_yang://点击筛选
                doFilterClick();
                break;
        }
    }

    /**
     * 点击综合
     */
    private void doAllClick() {

    }

    /**
     * 点击价格
     */
    private void doPriceClick() {
    }

    /**
     * 点击销量
     */
    private void doSalesClick() {
    }

    /**
     * 点击筛选
     */
    private void doFilterClick() {
        new XPopup.Builder(getContext())
                .isDestroyOnDismiss(true)
                .popupPosition(PopupPosition.Right)//右边
//                        .hasStatusBarShadow(true) //启用状态栏阴影
                .asCustom(new CustomChaoYangFilterDrawerPopupView(getActivity()))
                .show();
    }

    /**
     * 模拟数据
     */
    private Collection<Model> loadModels() {
        return Arrays.asList(
                new Model() {{
                    this.name = "但家香酥鸭";
                    this.nickname = "爱过那张脸";
                    this.imageId = R.mipmap.image_practice_repast_1;
                    this.avatarId = R.mipmap.image_avatar_1;
                }}, new Model() {{
                    this.name = "香菇蒸鸟蛋";
                    this.nickname = "淑女算个鸟";
                    this.imageId = R.mipmap.image_practice_repast_2;
                    this.avatarId = R.mipmap.image_avatar_2;
                }}, new Model() {{
                    this.name = "花溪牛肉粉";
                    this.nickname = "性感妩媚";
                    this.imageId = R.mipmap.image_practice_repast_3;
                    this.avatarId = R.mipmap.image_avatar_3;
                }}, new Model() {{
                    this.name = "破酥包";
                    this.nickname = "一丝丝纯真";
                    this.imageId = R.mipmap.image_practice_repast_4;
                    this.avatarId = R.mipmap.image_avatar_4;
                }}, new Model() {{
                    this.name = "盐菜饭";
                    this.nickname = "等着你回来";
                    this.imageId = R.mipmap.image_practice_repast_5;
                    this.avatarId = R.mipmap.image_avatar_5;
                }}, new Model() {{
                    this.name = "米豆腐";
                    this.nickname = "宝宝树人";
                    this.imageId = R.mipmap.image_practice_repast_6;
                    this.avatarId = R.mipmap.image_avatar_6;
                }});
    }
}
