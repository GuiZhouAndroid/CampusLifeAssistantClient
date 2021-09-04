package work.lpssfxy.www.campuslifeassistantclient.entity;

import java.io.Serializable;

import lombok.Data;

/**
 * created by on 2021/8/26
 * 描述：
 *
 * @author ZSAndroid
 * @create 2021-08-26-3:30
 */
@Data
public class QQUserBean implements Serializable {

    private int ret;
    private String msg;
    private int is_lost;
    private String nickname;
    private String gender;
    private int gender_type;
    private String province;
    private String city;
    private String year;
    private String constellation;
    private String figureurl;
    private String figureurl_1;
    private String figureurl_2;
    private String figureurl_qq_1;
    private String figureurl_qq_2;
    private String figureurl_qq;
    private String figureurl_type;
    private String is_yellow_vip;
    private String vip;
    private String yellow_vip_level;
    private String level;
    private String is_yellow_year_vip;

    public QQUserBean(int ret, String msg, int is_lost, String nickname, String gender, int gender_type, String province, String city, String year, String constellation, String figureurl, String figureurl_1, String figureurl_2, String figureurl_qq_1, String figureurl_qq_2, String figureurl_qq, String figureurl_type, String is_yellow_vip, String vip, String yellow_vip_level, String level, String is_yellow_year_vip) {
        this.ret = ret;
        this.msg = msg;
        this.is_lost = is_lost;
        this.nickname = nickname;
        this.gender = gender;
        this.gender_type = gender_type;
        this.province = province;
        this.city = city;
        this.year = year;
        this.constellation = constellation;
        this.figureurl = figureurl;
        this.figureurl_1 = figureurl_1;
        this.figureurl_2 = figureurl_2;
        this.figureurl_qq_1 = figureurl_qq_1;
        this.figureurl_qq_2 = figureurl_qq_2;
        this.figureurl_qq = figureurl_qq;
        this.figureurl_type = figureurl_type;
        this.is_yellow_vip = is_yellow_vip;
        this.vip = vip;
        this.yellow_vip_level = yellow_vip_level;
        this.level = level;
        this.is_yellow_year_vip = is_yellow_year_vip;
    }
}
