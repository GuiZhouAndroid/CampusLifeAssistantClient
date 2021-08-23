package work.lpssfxy.www.campuslifeassistantclient.view.activity;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.SwitchPreference;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import butterknife.BindArray;
import butterknife.BindBitmap;
import butterknife.BindColor;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnLongClick;
import work.lpssfxy.www.campuslifeassistantclient.R;
import work.lpssfxy.www.campuslifeassistantclient.R2;
import work.lpssfxy.www.campuslifeassistantclient.utils.SharePreferenceUtil;
import work.lpssfxy.www.campuslifeassistantclient.utils.permission.PermissionUtils;

@SuppressLint("NonConstantResourceId")
public class IndexActivity extends BaseActivity{
    @BindView(R2.id.tv)
    TextView tv;//绑定TextView 控件
    @BindView(R2.id.btn)
    Button btn;//绑定Button 控件
    @BindView(R2.id.btn1)
    Button btn1;//绑定Button 控件
    @BindView(R2.id.iv)
    ImageView iv;//绑定ImageView 控件
    @BindString(R2.string.app_name)
    String str;//绑定资源文件中string字符串
    @BindArray(R2.array.city)
    String[] strArray;//绑定资源文件中string字符串数组
    @BindBitmap(R.mipmap.ic_launcher)
    Bitmap bitmap;//绑定资源文件中mipmap中的ic_launcher图片
    @BindColor(R2.color.purple_200)
    int BtnTextColor;
    /** 防触碰使用的变量 */
    private long firstTime;

    private ListPreference mTheme;
    private SwitchPreference mSpDayLight;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportFragmentManager().beginTransaction().replace(R.id.show, new Fragment()).commit();
        tv.setText(str);
        btn.setText(strArray[1]);
        btn.setTextColor(BtnTextColor);
        iv.setImageBitmap(bitmap);
    }

    /**
     * 关闭滑动返回
     *
     * @return false:右滑返回失效
     */
    @Override
    protected Boolean isSetSwipeBackLayout() {
        return false;
    }

    /**
     * 开启沉浸状态栏
     *
     * @return true:顶部状态栏全透明 false:顶部状态栏半透明
     */
    @Override
    protected Boolean isSetStatusBarState() {
        return true;
    }

    /**
     * 关闭自动隐藏底部导航栏
     * 须知：true时，必须关闭沉浸状态栏，false:必须开启沉浸式状态栏
     *
     * @return true:隐藏顶部状态栏+挤压底部导航栏 false:log打印日志“返回值不正确”
     */
    @Override
    protected Boolean isSetBottomNaviCationState() {
        return false;
    }

    /**
     * 开启设置底部导航栏白色
     *
     * @return true:底部导航栏白色 false:底部导航栏黑色半透明
     */
    @Override
    protected Boolean isSetBottomNaviCationColor() {
        return true;
    }

    /**
     * 关闭全屏沉浸
     *
     * @return true:顶部状态栏隐藏+底部导航栏隐藏  false:log打印日志“返回值不正确”
     */
    @Override
    protected Boolean isSetImmersiveFullScreen() {
        return false;
    }

    /**
     * 绑定布局
     *
     * @return
     */
    @Override
    public int bindLayout() {
        return R.layout.index_activity;
    }

    /**
     * 准备数据
     */
    @Override
    protected void prepareData() {

    }

    /**
     * 初始化View
     */
    @Override
    protected void initView() {
    }

    /**
     * 初始化数据
     *
     * @param savedInstanceState 界面非正常销毁时保存的数据
     */
    @Override
    protected void initData(Bundle savedInstanceState) {
//        //自动夜间模式
//        boolean isAutoDayLight = SharePreferenceUtil.getInstance().getBoolean(getString(R.string.pref_auto_day_light), false);
//        if (isAutoDayLight) {
//            mSpAutoDayLight.setChecked(true);
//            mSpAutoDayLight.setSummaryOn(SharePreferenceUtil.getInstance().optString(getString(R.string.summary_auto_day_light)));
//            mSpDayLight.setChecked(true);
//        } else {
//            mSpAutoDayLight.setChecked(false);
//        }

        //皮肤选择
        String skin = (String) SharePreferenceUtil.getInstance().optString("skin_cn");
        if (!TextUtils.isEmpty(skin)) {
            mTheme.setSummary(skin);
            mTheme.setValue(skin);
        }
    }

    /**
     * 初始化事件
     */
    @Override
    protected void initEvent() {

    }

    @OnClick(R2.id.btn)
    public void onViewOneClicked() {
        Toast.makeText(this, "我是单个Btn点击事件", Toast.LENGTH_SHORT).show();
        PermissionUtils.toAppSetting(this);
    }

    @OnClick({R2.id.btn, R2.id.tv})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn:
                Toast.makeText(this, "我是多个btn点击事件", Toast.LENGTH_SHORT).show();
                break;
            case R.id.tv:
                Toast.makeText(this, "我是多个tv点击事件", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    //多个控件对应公共事件
    @OnClick({R2.id.btn, R2.id.btn1})
    public void sayHi(Button btn) {
        btn.setText("Success!");
    }

//    @OnLongClick(R2.id.btn)
//    public void onViewOneLongClicked(){
//        Toast.makeText(this, "我是单个Btn长按事件", Toast.LENGTH_SHORT).show();
//    }

    @OnLongClick({R2.id.btn, R2.id.tv})
    public void onViewLongClicked(View view) {
        switch (view.getId()) {
            case R.id.btn:
                Toast.makeText(this, "我是多个长按Btn事件", Toast.LENGTH_SHORT).show();
                break;
            case R.id.tv:
                Toast.makeText(this, "我是多个长按tv事件", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    /**
     * 防触碰处理
     * 再按一次退出程序
     *
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                long secondTime = System.currentTimeMillis();
                if (secondTime - firstTime > 3000) {
                    BaseActivity.showToast("再按一次退出程序！");
                    firstTime = secondTime;
                    return true;
                } else {
                    System.exit(0);
                }
                break;
        }
        return super.onKeyUp(keyCode, event);
    }

}