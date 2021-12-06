package work.lpssfxy.www.campuslifeassistantclient.base.glide;

import android.content.Context;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.Registry;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.integration.okhttp3.OkHttpUrlLoader;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.module.AppGlideModule;

import java.io.InputStream;

import okhttp3.OkHttpClient;

/**
 * created by on 2021/11/20
 * 描述：自定义Glide
 *
 * @author ZSAndroid
 * @create 2021-11-20-16:05
 */

@GlideModule
public class MyGlideModule extends AppGlideModule {


    @Override
    public void applyOptions(Context context, GlideBuilder builder) {

        super.applyOptions(context, builder);
    }


    @Override
    public void registerComponents(@NonNull Context context, @NonNull Glide glide, @NonNull Registry registry) {
        /* 1.Glide官网方式 */
         OkHttpClient okHttpClient= UnsafeOkHttpClient.getUnsafeOkHttpClient();
         registry.replace(GlideUrl.class, InputStream.class, new  OkHttpUrlLoader.Factory(okHttpClient));
        /* 2.查看大图官网方式 */
        // 替换底层网络框架为okhttp3，这步很重要！如果不添加会无法正常显示原图的加载百分比，或者卡在1%
        // 如果你的app中已经存在了自定义的GlideModule，你只需要把这一行代码，添加到对应的重载方法中即可。
        //registry.replace(GlideUrl.class, InputStream.class, new OkHttpUrlLoader.Factory(ProgressManager.getOkHttpClient()));
    }
}
