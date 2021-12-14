package work.lpssfxy.www.campuslifeassistantclient.view.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.hjq.toast.ToastUtils;
import com.xuexiang.xui.utils.ResUtils;
import com.xuexiang.xui.widget.spinner.DropDownMenu;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import work.lpssfxy.www.campuslifeassistantclient.R;
import work.lpssfxy.www.campuslifeassistantclient.base.dropdownmenu.CityDropDownAdapter;
import work.lpssfxy.www.campuslifeassistantclient.base.dropdownmenu.ConstellationAdapter;
import work.lpssfxy.www.campuslifeassistantclient.base.dropdownmenu.ListDropDownAdapter;
import work.lpssfxy.www.campuslifeassistantclient.view.BaseActivity;

@SuppressLint("NonConstantResourceId")
public class waimai extends BaseActivity {


    @BindView(R.id.ddm_content) DropDownMenu mDropDownMenu;
    @BindView(R.id.wai_show) LinearLayout wai_show;

    private String[] mHeaders = {"城市", "年龄", "性别", "星座"};
    private List<View> mPopupViews = new ArrayList<>();

    private CityDropDownAdapter mCityAdapter;
    private ListDropDownAdapter mAgeAdapter;
    private ListDropDownAdapter mSexAdapter;
    private ConstellationAdapter mConstellationAdapter;

    private String[] mCitys;
    private String[] mAges;
    private String[] mSexs;
    private String[] mConstellations;
    @Override
    protected Boolean isSetSwipeBackLayout() {
        return true;
    }

    @Override
    protected Boolean isSetStatusBarState() {
        return false;
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
        return true;
    }

    @Override
    public int bindLayout() {
        return R.layout.activity_waimai;
    }


    @Override
    protected void prepareData() {

    }

    @Override
    protected void initView() {
        mCitys = ResUtils.getStringArray(R.array.city_entry);
        mAges = ResUtils.getStringArray(R.array.age_entry);
        mSexs = ResUtils.getStringArray(R.array.sex_entry);
        mConstellations = ResUtils.getStringArray(R.array.constellation_entry);
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

    /**
     * 业务操作
     */
    @Override
    protected void doBusiness() {
        final ListView cityView = new ListView(this);
        mCityAdapter = new CityDropDownAdapter(this, mCitys);
        cityView.setDividerHeight(0);
        cityView.setAdapter(mCityAdapter);

        //init age menu
        final ListView ageView = new ListView(this);
        ageView.setDividerHeight(0);
        mAgeAdapter = new ListDropDownAdapter(this, mAges);
        ageView.setAdapter(mAgeAdapter);

        //init sex menu
        final ListView sexView = new ListView(this);
        sexView.setDividerHeight(0);
        mSexAdapter = new ListDropDownAdapter(this, mSexs);
        sexView.setAdapter(mSexAdapter);

        //init constellation
        final View constellationView = getLayoutInflater().inflate(R.layout.layout_drop_down_custom, null);
        GridView constellation = constellationView.findViewById(R.id.constellation);
        mConstellationAdapter = new ConstellationAdapter(this, mConstellations);
        constellation.setAdapter(mConstellationAdapter);
        constellationView.findViewById(R.id.btn_ok).setOnClickListener(v -> {
            mDropDownMenu.setTabMenuText(mConstellationAdapter.getSelectPosition() <= 0 ? mHeaders[3] : mConstellationAdapter.getSelectItem());
            ToastUtils.show(mConstellationAdapter.getSelectItem());
            mDropDownMenu.closeMenu();
        });

        //init mPopupViews
        mPopupViews.add(cityView);
        mPopupViews.add(ageView);
        mPopupViews.add(sexView);
        mPopupViews.add(constellationView);

        //add item click event
        cityView.setOnItemClickListener((parent, view, position, id) -> {
            mCityAdapter.setSelectPosition(position);
            ToastUtils.show(mCityAdapter.getSelectItem());
            mDropDownMenu.setTabMenuText(position == 0 ? mHeaders[0] : mCitys[position]);
            mDropDownMenu.closeMenu();
        });

        ageView.setOnItemClickListener((parent, view, position, id) -> {
            mAgeAdapter.setSelectPosition(position);
            ToastUtils.show(mAgeAdapter.getSelectItem());
            mDropDownMenu.setTabMenuText(position == 0 ? mHeaders[1] : mAges[position]);
            mDropDownMenu.closeMenu();
        });

        sexView.setOnItemClickListener((parent, view, position, id) -> {
            mSexAdapter.setSelectPosition(position);
            ToastUtils.show(mSexAdapter.getSelectItem());
            mDropDownMenu.setTabMenuText(position == 0 ? mHeaders[2] : mSexs[position]);
            mDropDownMenu.closeMenu();
        });

        constellation.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                mConstellationAdapter.setSelectPosition(position);


            }
        });

        //init context view
        LinearLayout contentView = new LinearLayout(this);
        contentView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        //init dropdownview
        mDropDownMenu.setDropDownMenu(mHeaders, mPopupViews, contentView);
    }

}