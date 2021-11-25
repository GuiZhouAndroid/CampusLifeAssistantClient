package work.lpssfxy.www.campuslifeassistantclient.entity.other;

import lombok.Data;

/**
 * created by on 2021/9/2
 * 描述：校园资讯实体类
 *
 * @author ZSAndroid
 * @create 2021-09-02-16:26
 */

@Data
public class CampusInformationBean {
    public String infoTitle;//资讯标题
    public String infoSource;//资讯来源
    public String infoIssueTime;//资讯发布时间
    public String infoContent;//资讯内容
}
