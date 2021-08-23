package work.lpssfxy.www.campuslifeassistantclient.view.activity;

import android.os.Bundle;

/**
 * created by on 2021/8/18
 * 描述：功能首页
 *
 * @author ZSAndroid
 * @create 2021-08-18-15:55
 */
public class IndexActivity extends BaseActivity {

    /**
     * 开启滑动返回
     *
     * @return
     */
    @Override
    protected Boolean isSetSwipeBackLayout() {
        return true;
    }

    /**
     * 开启沉浸状态栏
     *
     * @return
     */
    @Override
    protected Boolean isSetStatusBarState() {
        return false;
    }

    /**
     * 开启自动隐藏底部导航栏
     *
     * @return
     */
    @Override
    protected Boolean isSetBottomNaviCationState() {
        return false;
    }

    @Override
    protected Boolean isSetBottomNaviCationColor() {
        return null;
    }

    @Override
    protected Boolean isSetImmersiveFullScreen() {
        return null;
    }

    @Override
    public int bindLayout() {
        return 0;
    }

    @Override
    protected void prepareData() {

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
}
