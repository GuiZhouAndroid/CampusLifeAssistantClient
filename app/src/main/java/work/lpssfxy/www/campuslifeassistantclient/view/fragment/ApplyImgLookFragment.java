package work.lpssfxy.www.campuslifeassistantclient.view.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.github.chrisbanes.photoview.PhotoView;

import butterknife.BindView;
import butterknife.OnClick;
import work.lpssfxy.www.campuslifeassistantclient.R;
import work.lpssfxy.www.campuslifeassistantclient.R2;
import work.lpssfxy.www.campuslifeassistantclient.view.BaseFragment;

/**
 * created by on 2021/12/6
 * 描述：申请跑腿截图放大查看
 *
 * @author ZSAndroid
 * @create 2021-12-06-9:01
 */

@SuppressLint("NonConstantResourceId")
public class ApplyImgLookFragment extends BaseFragment {

    @BindView(R2.id.pv_apply_img) PhotoView mPvApplyImg;

    public static ApplyImgLookFragment getInstance(String key, String value) {
        ApplyImgLookFragment fragment = new ApplyImgLookFragment();
        Bundle args = new Bundle();
        args.putString(key, value);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int bindLayout() {
        return R.layout.fragment_apply_img_look;
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
        //设置MainActivity传来的数据
        if (getArguments() != null) {
            String strGetUrlData = getArguments().getString("urlData");
            Log.i("urlData", strGetUrlData);
            //(占位图 + 无缓存)
            RequestOptions options = new RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE);
            // 加载核酸检测URL，CenterCrop()完全填充，但图像可能不会完整显示，fitCenter()图像将会完全显示，但可能不会填满整个ImageView
            Glide.with(this).load(strGetUrlData).fitCenter().apply(options).into(mPvApplyImg);
        }
    }

    /**
     * 使用 ButterKnife注解式监听单击事件
     *
     * @param view 控件Id
     */
    @OnClick({R2.id.pv_apply_img})
    public void onApplyImgViewClick(View view) {
        switch (view.getId()) {
            case R.id.pv_apply_img://点击获取验证码
                //点击PhotoView时把切换的Fragment设置在栈顶状态，与Activity实现不重叠返回Fragment问题
                getActivity().getSupportFragmentManager().popBackStack();
                break;
        }
    }
}
