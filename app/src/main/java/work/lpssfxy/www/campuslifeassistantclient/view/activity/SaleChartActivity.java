package work.lpssfxy.www.campuslifeassistantclient.view.activity;

import android.os.Bundle;

import work.lpssfxy.www.campuslifeassistantclient.R;
import work.lpssfxy.www.campuslifeassistantclient.base.backfragment.BackHandlerHelper;
import work.lpssfxy.www.campuslifeassistantclient.view.BaseActivity;
import work.lpssfxy.www.campuslifeassistantclient.view.fragment.store.SaleChartFragment;

/**
 * created by on 2021/12/16
 * 描述：销售管理图表分析
 *
 * @author ZSAndroid
 * @create 2021-12-16-13:24
 */

public class SaleChartActivity extends BaseActivity {

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
        return R.layout.activity_sale_chart;
    }

    @Override
    protected void prepareData() {
        overridePendingTransition(R.anim.anim_left_in, R.anim.anim_left_out);
        getSupportFragmentManager().beginTransaction().setCustomAnimations(
                R.anim.slide_right_in,
                R.anim.slide_left_out,
                R.anim.slide_left_in,
                R.anim.slide_right_out
        ).replace(R.id.fl_chart_show, new SaleChartFragment()).commit();
    }

    @Override
    protected void initView() {

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

    /**
     * 监听Fragment分发事件，查看大图，返回上一页，而不是直接finish活动
     */
    @Override
    public void onBackPressed() {
        if (!BackHandlerHelper.handleBackPress(this)) {
            super.onBackPressed();
        }
    }
}
