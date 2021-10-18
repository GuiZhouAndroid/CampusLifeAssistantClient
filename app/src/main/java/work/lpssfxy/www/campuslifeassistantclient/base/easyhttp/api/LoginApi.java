package work.lpssfxy.www.campuslifeassistantclient.base.easyhttp.api;

import com.hjq.http.config.IRequestApi;

import work.lpssfxy.www.campuslifeassistantclient.base.constant.Constant;

/**
 * created by on 2021/10/17
 * 描述：实现这个接口可以指定这个请求的API接口
 * @ HttpHeader：标记这个字段是一个请求头参数
 * @ HttpIgnore：标记这个字段不会被发送给后台
 * @ HttpRename：重新定义这个字段发送给后台的参数名称
 *
 * @author ZSAndroid
 * @create 2021-10-17-22:45
 */
public class LoginApi implements IRequestApi {

    @Override
    public String getApi() {
        return Constant.LOGIN_USERNAME_PASSWORD;
    }

    /** 登录用户名 */

    private String ulUsername;

    /** 登录密码 */
    private String ulPassword;

    public LoginApi setUserName(String ulUsername) {
        this.ulUsername = ulUsername;
        return this;
    }

    public LoginApi setPassword(String ulUsername) {
        this.ulPassword = ulUsername;
        return this;
    }
}
