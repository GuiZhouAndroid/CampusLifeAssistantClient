package work.lpssfxy.www.campuslifeassistantclient.base;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.chad.library.adapter.base.loadmore.BaseLoadMoreView;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;

import work.lpssfxy.www.campuslifeassistantclient.R;

/**
 * created by on 2021/12/10
 * 描述：设置自定义加载布局
 *
 * @author ZSAndroid
 * @create 2021-12-10-16:14
 */
public class CustomLoadMoreView extends BaseLoadMoreView {

    /**
     * 根本局
     */
    @NonNull
    @Override
    public View getRootView(@NonNull ViewGroup viewGroup) {
        return LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.custom_rcy_view_load_more, viewGroup, false);
    }

    /**
     * 下拉加载完成
     */
    @NonNull
    @Override
    public View getLoadComplete(@NonNull BaseViewHolder baseViewHolder) {
        return baseViewHolder.findView(R.id.load_more_load_complete_view);
    }

    /**
     * 下拉加载结束
     */
    @NonNull
    @Override
    public View getLoadEndView(@NonNull BaseViewHolder baseViewHolder) {
        return baseViewHolder.findView(R.id.load_more_load_end_view);
    }

    /**
     * 下拉加载失败
     */
    @NonNull
    @Override
    public View getLoadFailView(@NonNull BaseViewHolder baseViewHolder) {
        return baseViewHolder.findView(R.id.load_more_load_fail_view);
    }

    /**
     * 正在下拉加载
     */
    @NonNull
    @Override
    public View getLoadingView(@NonNull BaseViewHolder baseViewHolder) {
        return baseViewHolder.findView(R.id.load_more_loading_view);
    }

}
