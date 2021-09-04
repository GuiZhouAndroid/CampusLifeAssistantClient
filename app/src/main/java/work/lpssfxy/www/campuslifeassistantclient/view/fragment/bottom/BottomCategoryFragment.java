package work.lpssfxy.www.campuslifeassistantclient.view.fragment.bottom;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;

import butterknife.BindView;
import work.lpssfxy.www.campuslifeassistantclient.R;
import work.lpssfxy.www.campuslifeassistantclient.R2;
import work.lpssfxy.www.campuslifeassistantclient.view.fragment.BaseFragment;


/**
 * created by on 2021/8/20
 * 描述：
 *
 * @author ZSAndroid
 * @create 2021-08-20-15:01
 */
@SuppressLint("NonConstantResourceId")
public class BottomCategoryFragment extends BaseFragment {

    @BindView(R2.id.iv_one) ImageView mIv_one;
    @BindView(R2.id.iv_two) ImageView mIv_two;
    @BindView(R2.id.iv_three) ImageView mIv_three;
    @BindView(R2.id.iv_four) ImageView mIv_four;
    @BindView(R2.id.iv_five) ImageView mIv_five;

    @Override
    protected int bindLayout() {
        return R.layout.index_fragment_bottom_category;
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
        //showImg();//显示网络图片
    }

    /**
     * 使用Glide加载显示网络图片 记得加网络权限和http地址url访问许可
     */
    private void showImg() {
        Glide.with(this)
                .load("http://gank.io/images/2c924db2a1b84c5d8fdb9f8c5f6d1b71")
                .into(mIv_one);
        Glide.with(this)
                .load("http://gank.io/images/92989b6a707b44dfb1c734e8d53d39a2")
                .into(mIv_two);
        Glide.with(this)
                .load("http://gank.io/images/4817628a6762410895f814079a6690a1")
                .into(mIv_three);
        Glide.with(this)
                .load("http://gank.io/images/f9523ebe24a34edfaedf2dd0df8e2b99")
                .into(mIv_four);
        Glide.with(this)
                .load("http://gank.io/images/4002b1fd18544802b80193fad27eaa62")
                .into(mIv_five);
    }

    @Override
    public void onClick(View view) {

    }
}
