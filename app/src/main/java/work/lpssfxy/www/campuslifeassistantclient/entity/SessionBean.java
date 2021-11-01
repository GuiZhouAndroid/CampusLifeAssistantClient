package work.lpssfxy.www.campuslifeassistantclient.entity;

import lombok.Data;

/**
 * created by on 2021/9/4
 * 描述：
 *
 * @author ZSAndroid
 * @create 2021-09-04-14:26
 */

@Data
public class SessionBean {
    public int ret;
    public String openid;
    public String access_token;
    public String pay_token;
    public int expires_in;
    public String pf;
    public String pfkey;
    public String msg;
    public int login_cost;
    public int query_authority_cost;
    public int authority_cost;
    public long expires_time;
}
