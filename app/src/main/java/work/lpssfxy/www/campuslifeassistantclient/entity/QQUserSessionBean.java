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
public class QQUserSessionBean {
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

    public QQUserSessionBean(int ret, String openid, String access_token, String pay_token, int expires_in, String pf, String pfkey, String msg, int login_cost, int query_authority_cost, int authority_cost, long expires_time) {
        this.ret = ret;
        this.openid = openid;
        this.access_token = access_token;
        this.pay_token = pay_token;
        this.expires_in = expires_in;
        this.pf = pf;
        this.pfkey = pfkey;
        this.msg = msg;
        this.login_cost = login_cost;
        this.query_authority_cost = query_authority_cost;
        this.authority_cost = authority_cost;
        this.expires_time = expires_time;
    }
}
