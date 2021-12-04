package work.lpssfxy.www.campuslifeassistantclient.base.glide;

import android.app.Activity;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.ListPreloader.PreloadModelProvider;
import com.bumptech.glide.RequestBuilder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * created by on 2021/11/20
 * 描述：
 *
 * @author ZSAndroid
 * @create 2021-11-20-16:22
 */
public class MyPreloadModelProvider implements PreloadModelProvider {
    private Activity activity;
    private List<String> myUrls = new ArrayList<>();
    private final int imageWidthPixels = 1024;
    private final int imageHeightPixels = 768;
    @NonNull
    @Override
    public List getPreloadItems(int position) {
        String url = myUrls.get(position);
        if (TextUtils.isEmpty(url)) {
            return Collections.emptyList();
        }
        return Collections.singletonList(url);
    }

    @Nullable
    @Override
    public RequestBuilder<?> getPreloadRequestBuilder(@NonNull Object item) {
        return Glide.with(activity).load(myUrls.get(0)).override(imageWidthPixels, imageHeightPixels);
    }
}
