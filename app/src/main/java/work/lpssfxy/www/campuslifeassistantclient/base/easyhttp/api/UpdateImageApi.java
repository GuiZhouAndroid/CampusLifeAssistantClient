package work.lpssfxy.www.campuslifeassistantclient.base.easyhttp.api;

/**
 * created by on 2021/10/17
 * 描述：上传图片
 *
 * @author ZSAndroid
 * @create 2021-10-17-23:13
 */

import com.hjq.http.config.IRequestApi;
import com.hjq.http.config.IRequestServer;

import java.io.File;

public final class UpdateImageApi implements IRequestServer, IRequestApi {

    @Override
    public String getHost() {
        return "https://graph.baidu.com/";
    }

    @Override
    public String getApi() {
        return "upload/";
    }

    /** 本地图片 */
    private File image;

    public UpdateImageApi(File image) {
        this.image = image;
    }

    public UpdateImageApi setImage(File image) {
        this.image = image;
        return this;
    }
}
