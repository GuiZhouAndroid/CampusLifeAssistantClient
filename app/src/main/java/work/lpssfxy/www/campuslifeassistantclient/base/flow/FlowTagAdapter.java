package work.lpssfxy.www.campuslifeassistantclient.base.flow;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.xuexiang.xui.widget.flowlayout.BaseTagAdapter;

import work.lpssfxy.www.campuslifeassistantclient.R;

/**
 * created by on 2021/12/10
 * 描述：
 *
 * @author ZSAndroid
 * @create 2021-12-10-20:04
 */
public class FlowTagAdapter extends BaseTagAdapter<String, TextView> {

    public FlowTagAdapter(Context context) {
        super(context);
    }

    @Override
    protected TextView newViewHolder(View convertView) {
        return convertView.findViewById(R.id.tv_address_tag);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.custom_address_adapter_item_tag;
    }

    @Override
    protected void convert(TextView holder, String item, int position) {
        holder.setText(item);
    }
}
