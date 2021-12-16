package work.lpssfxy.www.campuslifeassistantclient.view.fragment.store;

import static com.xuexiang.xaop.consts.PermissionConsts.STORAGE;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.Chart;
import com.github.mikephil.charting.charts.RadarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.RadarData;
import com.github.mikephil.charting.data.RadarDataSet;
import com.github.mikephil.charting.data.RadarEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IDataSet;
import com.github.mikephil.charting.interfaces.datasets.IRadarDataSet;
import com.xuexiang.xaop.annotation.Permission;
import com.xuexiang.xui.widget.dialog.bottomsheet.BottomSheet;

import java.util.ArrayList;

import butterknife.BindView;
import work.lpssfxy.www.campuslifeassistantclient.R;
import work.lpssfxy.www.campuslifeassistantclient.utils.XToastUtils;
import work.lpssfxy.www.campuslifeassistantclient.view.BaseFragment;

/**
 * created by on 2021/12/16
 * 描述：雷达图
 *
 * @author ZSAndroid
 * @create 2021-12-16-17:04
 */

@SuppressLint("NonConstantResourceId")
public class RadarChartFragment extends BaseFragment {

    @BindView(R.id.chart1) RadarChart chart;

    @Override
    protected void lazyLoad() {

    }

    @Override
    protected int bindLayout() {
        return R.layout.fragment_chart_radar;
    }

    @Override
    protected void prepareData(Bundle savedInstanceState) {

    }

    @Override
    protected void initView(View rootView) {
        initChartStyle();
        initChartLabel();
        setChartData(5, 80);

        // 设置雷达图显示的动画
        chart.animateXY(1400, 1400, Easing.EaseInOutQuad);
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
    /**
     * 初始化图表的样式
     */
    protected void initChartStyle() {
        // 设置雷达图的背景颜色
        chart.setBackgroundColor(Color.rgb(60, 65, 82));
        // 禁止图表旋转
        chart.setRotationEnabled(false);

        //设置雷达图网格的样式
        chart.getDescription().setEnabled(false);
        chart.setWebLineWidth(1f);
        chart.setWebColor(Color.LTGRAY);
        chart.setWebLineWidthInner(1f);
        chart.setWebColorInner(Color.LTGRAY);
        chart.setWebAlpha(100);

        // 设置标识雷达图上各点的数字控件
        MarkerView mv = new RadarMarkerView(getContext(), R.layout.marker_view_radar);
        mv.setChartView(chart);
        chart.setMarker(mv);

        initXYAxisStyle();
    }

    private void initXYAxisStyle() {
        //设置X轴（雷达图的项目点）的样式
        XAxis xAxis = chart.getXAxis();
        xAxis.setTextSize(9f);
        xAxis.setYOffset(0f);
        xAxis.setXOffset(0f);
        xAxis.setValueFormatter(new ValueFormatter() {
            private final String[] mActivities = new String[]{"Burger", "Steak", "Salad", "Pasta", "Pizza"};

            @Override
            public String getFormattedValue(float value) {
                return mActivities[(int) value % mActivities.length];
            }
        });
        xAxis.setTextColor(Color.WHITE);

        //设置Y轴（雷达图的分值）的样式
        YAxis yAxis = chart.getYAxis();
        yAxis.setLabelCount(5, false);
        yAxis.setTextSize(9f);
        //最小分值
        yAxis.setAxisMinimum(0f);
        //最大分值
        yAxis.setAxisMaximum(80f);
        //是否画出分值
        yAxis.setDrawLabels(false);
    }

    /**
     * 初始化图表的 标题 样式
     */
    protected void initChartLabel() {
        //设置图表数据 标题 的样式
        Legend l = chart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);
        l.setXEntrySpace(7f);
        l.setYEntrySpace(5f);
        l.setTextColor(Color.WHITE);
    }

    /**
     * 设置图表数据
     *
     * @param count 一组数据的数量
     * @param range
     */
    protected void setChartData(int count, float range) {
        float min = 20;

        ArrayList<RadarEntry> entries1 = new ArrayList<>();
        ArrayList<RadarEntry> entries2 = new ArrayList<>();
        //雷达图的数据一般都有最大值，数据在一定范围内
        for (int i = 0; i < count; i++) {
            float val1 = (float) (Math.random() * range) + min;
            entries1.add(new RadarEntry(val1));

            float val2 = (float) (Math.random() * range) + min;
            entries2.add(new RadarEntry(val2));
        }

        //设置两组数据的表现样式
        RadarDataSet set1 = new RadarDataSet(entries1, "Last Week");
        set1.setColor(Color.rgb(103, 110, 129));
        set1.setFillColor(Color.rgb(103, 110, 129));
        set1.setDrawFilled(true);
        set1.setFillAlpha(180);
        set1.setLineWidth(2f);
        set1.setDrawHighlightCircleEnabled(true);
        set1.setDrawHighlightIndicators(false);

        RadarDataSet set2 = new RadarDataSet(entries2, "This Week");
        set2.setColor(Color.rgb(121, 162, 175));
        set2.setFillColor(Color.rgb(121, 162, 175));
        set2.setDrawFilled(true);
        set2.setFillAlpha(180);
        set2.setLineWidth(2f);
        set2.setDrawHighlightCircleEnabled(true);
        set2.setDrawHighlightIndicators(false);

        //最终将两组数据填充进图表中
        ArrayList<IRadarDataSet> sets = new ArrayList<>();
        sets.add(set1);
        sets.add(set2);

        RadarData data = new RadarData(sets);
        data.setValueTextSize(8f);
        data.setDrawValues(false);
        data.setValueTextColor(Color.WHITE);

        chart.setData(data);
        chart.invalidate();
    }

    private void showBottomSheetList() {
        new BottomSheet.BottomListSheetBuilder(getActivity())
                .addItem(getResources().getString(R.string.chart_toggle_values))
                .addItem(getResources().getString(R.string.chart_toggle_x_values))
                .addItem(getResources().getString(R.string.chart_toggle_y_values))
                .addItem(getResources().getString(R.string.chart_animate_x))
                .addItem(getResources().getString(R.string.chart_animate_y))
                .addItem(getResources().getString(R.string.chart_animate_xy))
                .addItem(getResources().getString(R.string.chart_save))
                .setOnSheetItemClickListener((dialog, itemView, position, tag) -> {
                    dialog.dismiss();
                    switch (position) {
                        case 0:
                            for (IDataSet<?> set : chart.getData().getDataSets()) {
                                set.setDrawValues(!set.isDrawValuesEnabled());
                            }
                            chart.invalidate();
                            break;
                        case 1:
                            chart.getXAxis().setEnabled(!chart.getXAxis().isEnabled());
                            chart.invalidate();
                            break;
                        case 2:
                            chart.getYAxis().setEnabled(!chart.getYAxis().isEnabled());
                            chart.invalidate();
                            break;
                        case 3:
                            chart.animateX(1400);
                            break;
                        case 4:
                            chart.animateY(1400);
                            break;
                        case 5:
                            chart.animateXY(1400, 1400);
                            break;
                        case 6:
                            saveToGallery(chart, "RadarChart");
                            break;
                        default:
                            break;
                    }
                })
                .build()
                .show();
    }

    /**
     * 图表保存
     *
     * @param chart
     * @param name
     */
    @Permission(STORAGE)
    public void saveToGallery(Chart chart, String name) {
        if (chart.saveToGallery(name + "_" + System.currentTimeMillis(), 70)) {
            XToastUtils.toast("保存成功!");

        } else {
            XToastUtils.toast("保存失败!");
        }
    }
}
