package work.lpssfxy.www.campuslifeassistantclient.view.activity;

import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import work.lpssfxy.www.campuslifeassistantclient.R;

public class waimai extends BaseActivity {

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

    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        showImg();
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

    }

    /**
     * 使用Glide加载显示网络图片 记得加网络权限和http地址url访问许可
     */
    private void showImg() {
        Glide.with(this)
                .load("http://gank.io/images/2c924db2a1b84c5d8fdb9f8c5f6d1b71")
                .into((ImageView) findViewById(R.id.iv_one));
        Glide.with(this)
                .load("http://gank.io/images/92989b6a707b44dfb1c734e8d53d39a2")
                .into((ImageView) findViewById(R.id.iv_two));
        Glide.with(this)
                .load("http://gank.io/images/4817628a6762410895f814079a6690a1")
                .into((ImageView) findViewById(R.id.iv_three));
        Glide.with(this)
                .load("http://gank.io/images/f9523ebe24a34edfaedf2dd0df8e2b99")
                .into((ImageView) findViewById(R.id.iv_four));
        Glide.with(this)
                .load("http://gank.io/images/4002b1fd18544802b80193fad27eaa62")
                .into((ImageView) findViewById(R.id.iv_five));
    }
}