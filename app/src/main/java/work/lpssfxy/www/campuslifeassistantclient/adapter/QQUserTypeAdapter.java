package work.lpssfxy.www.campuslifeassistantclient.adapter;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

import work.lpssfxy.www.campuslifeassistantclient.entity.QQUserSessionBean;

/**
 * created by on 2021/9/4
 * 描述：
 *
 * @author ZSAndroid
 * @create 2021-09-04-15:42
 */
public class QQUserTypeAdapter extends TypeAdapter<QQUserSessionBean> {

    @Override
    public void write(JsonWriter out, QQUserSessionBean value) throws IOException {
        out.beginObject();
        //按自定义顺序输出字段信息

        out.name("ret").value(value.ret);
        out.name("openid").value(value.openid);
        out.name("access_token").value(value.access_token);
        out.name("pay_token").value(value.pay_token);
        out.name("expires_in").value(value.expires_in);
        out.name("pf").value(value.pf);
        out.name("pfkey").value(value.pfkey);
        out.name("msg").value(value.msg);
        out.name("login_cost").value(value.login_cost);
        out.name("query_authority_cost").value(value.query_authority_cost);
        out.name("authority_cost").value(value.authority_cost);
        out.name("expires_time").value(value.expires_time);

        out.endObject();
    }

    @Override
    public QQUserSessionBean read(JsonReader in) throws IOException {
        return null;
    }

    /**
     * 使用当前适配器
     *       Gson gson = new GsonBuilder().registerTypeAdapter(QQUserSessionBean.class, new QQUserTypeAdapter())
     *                 //registerTypeAdapter可以重复使用
     *                 .create();
     */
}
