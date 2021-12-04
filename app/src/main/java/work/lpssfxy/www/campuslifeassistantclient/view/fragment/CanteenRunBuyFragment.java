package work.lpssfxy.www.campuslifeassistantclient.view.fragment;

import static android.R.layout.simple_list_item_2;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import com.scwang.smartrefresh.layout.api.OnTwoLevelListener;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.RefreshState;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.scwang.smartrefresh.layout.header.TwoLevelHeader;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.scwang.smartrefresh.layout.listener.SimpleMultiPurposeListener;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import work.lpssfxy.www.campuslifeassistantclient.R;
import work.lpssfxy.www.campuslifeassistantclient.R2;
import work.lpssfxy.www.campuslifeassistantclient.adapter.BaseRecyclerAdapter;
import work.lpssfxy.www.campuslifeassistantclient.adapter.SmartViewHolder;
import work.lpssfxy.www.campuslifeassistantclient.utils.statusbarutils.StatusBarUtil;

/**
 * created by on 2021/11/23
 * 描述：食堂跑腿代购下拉二楼
 *
 * @author ZSAndroid
 * @create 2021-11-23-17:51
 */

@SuppressLint("NonConstantResourceId")
public class CanteenRunBuyFragment extends BaseFragment {
    /* 下拉刷新核心 */
    @BindView(R2.id.refreshLayout_canteen) RefreshLayout mRefreshLayoutCanteen;
    /* 二楼控件 */
    @BindView(R2.id.header_canteen) TwoLevelHeader mHeaderCanteen;
    /* 二楼背景图片 */
    @BindView(R2.id.second_floor_canteen) ImageView mSecondFloorCanteen;
    /* 二楼内容 */
    @BindView(R2.id.second_floor_content_canteen) FrameLayout mSecondFloorContentCanteen;
    /* 二楼列表 */
    @BindView(R2.id.recyclerView_canteen) RecyclerView mRecyclerViewCanteen;
    /* Toolbar标题栏 */
    @BindView(R2.id.toolbar_canteen) Toolbar mToolbarCanteen;
    /* Fragment刷新头 */
    @BindView(R2.id.classics_canteen) ClassicsHeader mClassicsCanteen;
    /* Fragment内容 */
    @BindView(R2.id.contentPanel_canteen) FrameLayout mContentPanelCanteen;


    @Override
    protected int bindLayout() {
        return R.layout.fragment_canteen_run_buy_second_floor;
    }

    @Override
    protected void prepareData(Bundle savedInstanceState) {

    }

    @Override
    protected void initView(View rootView) {
        //状态栏透明和间距处理
        StatusBarUtil.immersive(getActivity());
        StatusBarUtil.setMargin(getActivity(),  mClassicsCanteen);
        StatusBarUtil.setPaddingSmart(getActivity(), mToolbarCanteen);
        StatusBarUtil.setPaddingSmart(getActivity(), mContentPanelCanteen);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {

    }

    @Override
    protected void initEvent() {
        mToolbarCanteen.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });

        mRefreshLayoutCanteen.setOnMultiPurposeListener(new SimpleMultiPurposeListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                refreshLayout.finishLoadMore(2000);
            }
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                Toast.makeText(getContext(),"触发刷新事件",Toast.LENGTH_SHORT).show();
                refreshLayout.finishRefresh(2000);
            }
            @Override
            public void onHeaderMoving(RefreshHeader header, boolean isDragging, float percent, int offset, int headerHeight, int maxDragHeight) {
                mToolbarCanteen.setAlpha(1 - Math.min(percent, 1));
                mSecondFloorCanteen.setTranslationY(Math.min(offset - mSecondFloorCanteen.getHeight() + mToolbarCanteen.getHeight(), mRefreshLayoutCanteen.getLayout().getHeight() - mSecondFloorCanteen.getHeight()));
            }
            @Override
            public void onStateChanged(@NonNull RefreshLayout refreshLayout, @NonNull RefreshState oldState, @NonNull RefreshState newState) {
                if (oldState == RefreshState.TwoLevel) {
                    mSecondFloorContentCanteen.animate().alpha(0).setDuration(1000);
                }
            }
        });

        mHeaderCanteen.setOnTwoLevelListener(new OnTwoLevelListener() {
            @Override
            public boolean onTwoLevel(@NonNull RefreshLayout refreshLayout) {
                Toast.makeText(getContext(),"触发二楼事件",Toast.LENGTH_SHORT).show();
                mSecondFloorContentCanteen.animate().alpha(1).setDuration(2000);
//                refreshLayout.getLayout().postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        mHeaderCanteen.finishTwoLevel();
//                        mSecondFloorContentCanteen.animate().alpha(0).setDuration(1000);
//                    }
//                },5000);
                return true;//true 将会展开二楼状态 false 关闭刷新
            }
        });

        mRefreshLayoutCanteen.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                Toast.makeText(getContext(),"触发刷新事件",Toast.LENGTH_SHORT).show();
                refreshLayout.finishRefresh(2000);
            }
        });
    }

    @Override
    protected void doBusiness(Context context) {
        if (mRecyclerViewCanteen != null) {
            mRecyclerViewCanteen.setNestedScrollingEnabled(false);
            List<Void> voids = Arrays.asList(null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
            mRecyclerViewCanteen.setAdapter(new BaseRecyclerAdapter<Void>(voids, simple_list_item_2) {
                @Override
                protected void onBindViewHolder(SmartViewHolder holder, Void model, int position) {
                    holder.text(android.R.id.text1, getString(R.string.item_example_number_title, position));
                    holder.text(android.R.id.text2, getString(R.string.item_example_number_abstract, position));
                    holder.textColorId(android.R.id.text2, R.color.gray);
                }
            });
        }
    }

    /**
     * 使用 ButterKnife注解式监听单击事件
     *
     * @param view 控件Id
     */
    @OnClick({R2.id.toolbar_canteen})
    public void onMyInfoViewClick(View view) {
        switch (view.getId()) {
            case R.id.toolbar_canteen://主动打开二楼
                mHeaderCanteen.openTwoLevel(true);
                break;
        }
    }

    @Override
    public boolean onBackPressed() {
        return false;
    }
}
