package work.lpssfxy.www.campuslifeassistantclient.view.activity;

import android.os.Bundle;
import android.view.KeyEvent;

import com.hjq.toast.ToastUtils;

import work.lpssfxy.www.campuslifeassistantclient.R;
import work.lpssfxy.www.campuslifeassistantclient.view.fragment.CanteenRunBuyFragment;

/**
 * created by on 2021/11/23
 * 描述：食堂跑腿代购
 *
 * @author ZSAndroid
 * @create 2021-11-23-17:47
 */
public class CanteenRunBuyActivity extends BaseActivity {

    /**
     * 防触碰使用的变量
     */
    private long firstTime;

    @Override
    protected Boolean isSetSwipeBackLayout() {
        return false;
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
        return true;
    }

    @Override
    protected Boolean isSetImmersiveFullScreen() {
        return true;
    }

    @Override
    public int bindLayout() {
        return R.layout.canteen_run_buy_activity;
    }

    @Override
    protected void prepareData() {

    }

    @Override
    protected void initView() {
        //加载跑腿代购二楼Fragment页面
        getSupportFragmentManager().beginTransaction().replace(R.id.fl_canteen_show, new CanteenRunBuyFragment()).commit();
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
     * 防触碰处理
     * 再按一次返回首页
     */
    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            long secondTime = System.currentTimeMillis();
            if (secondTime - firstTime > 3000) {
                ToastUtils.show("再按一次返回首页");
                firstTime = secondTime;
                return true;
            } else {
                CanteenRunBuyActivity.this.finish();
                overridePendingTransition(R.anim.anim_right_in, R.anim.anim_right_out);
            }
        }
        return super.onKeyUp(keyCode, event);
    }
}
