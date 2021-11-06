package work.lpssfxy.www.campuslifeassistantclient.view.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import work.lpssfxy.www.campuslifeassistantclient.R;

/**
 * created by on 2021/8/21
 * 描述：Fragment基类
 *
 * @author ZSAndroid
 * @create 2021-08-21-17:55
 */
public abstract class BaseFragment extends Fragment implements View.OnClickListener {

    /**
     * 获取TAG的fragment名称
     */
    protected final String mRecyclerView = this.getClass().getSimpleName();
    /**
     * 解绑ButterKnife
     */
    private Unbinder mUnbinder;

    public Context context;
    private View mContextView = null;

    /**
     * Fragment 生命周期的第一步
     * Fragment 和 Activity 建立关联传递数据时调用
     *
     * @param ctx
     */
    @Override
    public void onAttach(@NonNull Context ctx) {
        super.onAttach(ctx);
        context = ctx;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        /** 初始化布局 */
        mContextView = inflater.inflate(bindLayout(), container, false);
        /** 初始化ButterKnife */
        mUnbinder = ButterKnife.bind(this, mContextView);
        /** 准备数据 */
        prepareData(savedInstanceState);
        /** 初始化控件 */
        initView(mContextView);
        /** 初始化数据 */
        initData(savedInstanceState);
        /** 初始化事件监听器 */
        initEvent();
        /** 业务操作 */
        doBusiness(getActivity());
        return mContextView;
    }

    /**
     * 绑定Fragment布局
     *
     * @return 返回布局文件id
     */
    protected abstract int bindLayout();

    /**
     * 准备数据
     *
     * @param savedInstanceState
     */
    protected abstract void prepareData(Bundle savedInstanceState);

    /**
     * 初始化控件
     *
     * @param rootView
     */
    protected abstract void initView(View rootView);

    /**
     * 初始化数据
     *
     * @param savedInstanceState
     */
    protected abstract void initData(Bundle savedInstanceState);

    /**
     * 初始化事件监听器
     */
    protected abstract void initEvent();

    /**
     * 业务操作
     *
     * @param context
     */
    protected abstract void doBusiness(Context context);



    public void startActivityAnimLeftToRight(Activity activity, Intent intent) {
        startActivity(intent);
        activity.overridePendingTransition(R.anim.anim_left_in, R.anim.anim_left_out);
    }

    /**
     * 解绑ButterKnife
     */
    @Override
    public void onDestroyView() {
        if (mUnbinder != null) {
            mUnbinder.unbind();
        }
        super.onDestroyView();
    }
}
