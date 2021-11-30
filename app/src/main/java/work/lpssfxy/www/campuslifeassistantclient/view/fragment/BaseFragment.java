package work.lpssfxy.www.campuslifeassistantclient.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.snackbar.Snackbar;
import com.hjq.toast.ToastUtils;
import com.lzy.okgo.OkGo;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import lombok.Data;
import work.lpssfxy.www.campuslifeassistantclient.base.backfragment.FragmentBackHandler;
import work.lpssfxy.www.campuslifeassistantclient.R;
import work.lpssfxy.www.campuslifeassistantclient.utils.BackHandlerHelper;
import work.lpssfxy.www.campuslifeassistantclient.view.activity.CanteenRunBuyActivity;

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

    /**
     * 设置Snackbar上提示的字体颜色
     * @param snackbar
     * @param color
     */
    public void setSnackBarMessageTextColor(Snackbar snackbar, int color){
        View view = snackbar.getView();
        ((TextView) view.findViewById(R.id.snackbar_text)).setTextColor(color);
    }

    @Override
    public boolean onBackPressed() {
        return false;
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
        OkGo.getInstance().cancelAll();
    }
}
