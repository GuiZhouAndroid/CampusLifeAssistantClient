package work.lpssfxy.www.campuslifeassistantclient.adapter.welcome;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import work.lpssfxy.www.campuslifeassistantclient.R;

/**
 * created by on 2021/8/21
 * 描述：引导页ViewPager文字适配器
 *
 * @author ZSAndroid
 * @create 2021-08-21-18:10
 */
public class TextPagerAdapter extends PagerAdapter {

    private View view;

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        view = LayoutInflater.from(container.getContext()).inflate(R.layout.welcome_adapter_item_page_text, null);
        TextView mTitle = view.findViewById(R.id.pager_text_title);
        TextView mInfo = view.findViewById(R.id.pager_text_info);
        switch (position) {
            case 0:
                mTitle.setText(container.getResources().getString(R.string.welcome_guid_text_one_title));
                mInfo.setText(container.getResources().getString(R.string.welcome_guid_text_one_info));
                break;
            case 1:
                mTitle.setText(container.getResources().getString(R.string.welcome_guid_text_two_title));
                mInfo.setText(container.getResources().getString(R.string.welcome_guid_text_two_info));
                break;
            case 2:
                mTitle.setText(container.getResources().getString(R.string.welcome_guid_text_three_title));
                mInfo.setText(container.getResources().getString(R.string.welcome_guid_text_three_info));
                break;
            default:
                break;
        }
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView(view);
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }
}
