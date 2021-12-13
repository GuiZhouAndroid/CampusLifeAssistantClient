package work.lpssfxy.www.campuslifeassistantclient.view;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.android.material.snackbar.Snackbar;
import com.lzy.okgo.OkGo;
import com.scwang.smartrefresh.layout.api.RefreshLayout;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import work.lpssfxy.www.campuslifeassistantclient.R;
import work.lpssfxy.www.campuslifeassistantclient.base.backfragment.BackHandlerHelper;
import work.lpssfxy.www.campuslifeassistantclient.base.backfragment.FragmentBackHandler;

/**
 * created by on 2021/8/21
 * 描述：Fragment基类
 *
 * @author ZSAndroid
 * @create 2021-08-21-17:55
 */
public abstract class BaseFragment extends Fragment implements FragmentBackHandler {

    private boolean BackMode;

    public boolean getBackMode() {
        return BackMode;
    }

    public void setBackMode(boolean backMode) {
        this.BackMode = backMode;
    }

    /**
     * 视图是否已经初初始化
     */
    protected boolean isInit = false;
    protected boolean isLoad = false;
    protected final String TAG = "BaseFragment";
    private View view = null;

    /**
     * 解绑ButterKnife
     */
    private Unbinder mUnbinder;

    public Context context;

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
        view = inflater.inflate(bindLayout(), container, false);
        /** 初始化ButterKnife */
        mUnbinder = ButterKnife.bind(this, view);
        /** 准备数据 */
        prepareData(savedInstanceState);
        /** 初始化控件 */
        initView(view);
        /** 初始化数据 */
        initData(savedInstanceState);
        /** 初始化事件监听器 */
        initEvent();
        /** 业务操作 */
        doBusiness(getActivity());
        /** 初始化的时候去加载数据 **/
        isInit = true;
        isCanLoadData();
        return view;
    }

    /**
     * 视图是否已经对用户可见，系统的方法
     */
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        isCanLoadData();
    }

    /**
     * 是否可以加载数据
     * 可以加载数据的条件：
     * 1.视图已经初始化
     * 2.视图对用户可见
     */
    private void isCanLoadData() {
        if (!isInit) {
            return;
        }
        if (getUserVisibleHint()) {
            lazyLoad();
            isLoad = true;
        } else {
            if (isLoad) {
                stopLoad();
            }
        }
    }

    /**
     * 当视图初始化并且对用户可见的时候去真正的加载数据
     */
    protected abstract void lazyLoad();

    /**
     * 当视图已经对用户不可见并且加载过数据，如果需要在切换到其他页面时停止加载数据，可以覆写此方法
     */
    protected void stopLoad() {

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

    /**
     * 设置Snackbar上提示的字体颜色
     *
     * @param snackbar
     * @param color
     */
    public void setSnackBarMessageTextColor(Snackbar snackbar, int color) {
        View view = snackbar.getView();
        ((TextView) view.findViewById(R.id.snackbar_text)).setTextColor(color);
    }

    /**
     * 设置下拉刷新主题颜色
     *
     * @param refreshLayout RefreshLayout实例
     * @param colorPrimary  RefreshLayout主题色
     */
    @SuppressLint("ObsoleteSdkInt")
    public void setThemeColor(RefreshLayout refreshLayout, int colorPrimary) {
        refreshLayout.setPrimaryColorsId(colorPrimary, android.R.color.white);
    }

    /**
     * 设置下拉刷新主题颜色
     *
     * @param activity         上下午文
     * @param toolbar          Androidx库Toolbar
     * @param refreshLayout    RefreshLayout实例
     * @param colorPrimary     Toolbar背景色
     * @param colorPrimaryDark RefreshLayout背景色
     */
    @SuppressLint("ObsoleteSdkInt")
    public void setThemeColor(Activity activity, Toolbar toolbar, RefreshLayout refreshLayout, int colorPrimary, int colorPrimaryDark) {
        toolbar.setBackgroundResource(colorPrimary);
        refreshLayout.setPrimaryColorsId(colorPrimary, android.R.color.white);
        if (Build.VERSION.SDK_INT >= 21) {
            activity.getWindow().setStatusBarColor(ContextCompat.getColor(activity, colorPrimaryDark));
        }
    }

    @Override
    public boolean onBackPressed() {
        return BackHandlerHelper.handleBackPress(this);
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
        isInit = false;
        isLoad = false;
        OkGo.getInstance().cancelAll();
    }
}
