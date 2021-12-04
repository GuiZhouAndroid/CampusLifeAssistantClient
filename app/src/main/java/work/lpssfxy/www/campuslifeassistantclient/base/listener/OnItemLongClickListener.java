package work.lpssfxy.www.campuslifeassistantclient.base.listener;

import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

/**
 * created by on 2021/11/15
 * 描述：
 *
 * @author ZSAndroid
 * @create 2021-11-15-23:17
 */
public interface OnItemLongClickListener {
    void onItemLongClick(RecyclerView.ViewHolder holder, int position, View v);
}
