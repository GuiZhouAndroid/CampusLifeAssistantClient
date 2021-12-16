package work.lpssfxy.www.campuslifeassistantclient.view.fragment.store;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import butterknife.BindView;
import butterknife.OnClick;
import work.lpssfxy.www.campuslifeassistantclient.R;
import work.lpssfxy.www.campuslifeassistantclient.R2;
import work.lpssfxy.www.campuslifeassistantclient.view.BaseFragment;

/**
 * created by on 2021/12/16
 * 描述：
 *
 * @author ZSAndroid
 * @create 2021-12-16-14:22
 */
@SuppressLint("NonConstantResourceId")
public class SaleChartFragment extends BaseFragment {

    @BindView(R2.id.btn_bar_chart) Button mBtnBarChart;//柱状图
    @BindView(R2.id.btn_pie_chart1) Button mBtnPieChart1;//饼图1
    @BindView(R2.id.btn_pie_chart2) Button mBtnPieChart2;//饼图2
    @BindView(R2.id.btn_radar_chart) Button mBtnRadarChart;//雷达图

    @Override
    protected void lazyLoad() {

    }

    @Override
    protected int bindLayout() {
        return R.layout.fragment_sale_chart;
    }

    @Override
    protected void prepareData(Bundle savedInstanceState) {

    }

    @Override
    protected void initView(View rootView) {

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

    //R.anim.card_flip_right_in,
    //R.anim.card_flip_left_out,
    //R.anim.card_flip_left_in,
    //R.anim.card_flip_right_out

    @OnClick({R2.id.btn_bar_chart,R2.id.btn_pie_chart1,R2.id.btn_pie_chart2,R2.id.btn_radar_chart})
    public void onShopChartCenterViewClick(View view) {
        switch (view.getId()) {
            case R.id.btn_bar_chart://柱状图
                getFragmentManager()
                        .beginTransaction()
                        .setCustomAnimations(R.anim.slide_right_in, R.anim.slide_left_out, R.anim.slide_left_in, R.anim.slide_right_out)
                        .addToBackStack(null).replace(R.id.fl_chart_show,new SimpleBarChartFragment()).commit();
                break;
            case R.id.btn_pie_chart1://饼图1
                getFragmentManager()
                        .beginTransaction()
                        .setCustomAnimations(R.anim.slide_right_in, R.anim.slide_left_out, R.anim.slide_left_in, R.anim.slide_right_out)
                        .addToBackStack(null).replace(R.id.fl_chart_show,new BasicPieChartFragment()).commit();
                break;
            case R.id.btn_pie_chart2://饼图2
                getFragmentManager()
                        .beginTransaction()
                        .setCustomAnimations(R.anim.slide_right_in, R.anim.slide_left_out, R.anim.slide_left_in, R.anim.slide_right_out)
                        .addToBackStack(null).replace(R.id.fl_chart_show,new HalfPieChartFragment()).commit();
                break;
            case R.id.btn_radar_chart://雷达图
                getFragmentManager()
                        .beginTransaction()
                        .setCustomAnimations(R.anim.slide_right_in, R.anim.slide_left_out, R.anim.slide_left_in, R.anim.slide_right_out)
                        .addToBackStack(null).replace(R.id.fl_chart_show,new RadarChartFragment()).commit();
                break;
        }
    }
}
