package work.lpssfxy.www.campuslifeassistantclient.entity.dto;

import lombok.Data;

/**
 * created by on 2021/12/12
 * 描述：商铺分类单个实体
 *
 * @author ZSAndroid
 * @create 2021-12-12-22:01
 */
@Data
public class GoodsCategoryInfoBean {
    private String gcDesc;
    private int gcId;
    private String gcName;

    public GoodsCategoryInfoBean() {
    }

    public GoodsCategoryInfoBean(String gcDesc, int gcId, String gcName) {
        this.gcDesc = gcDesc;
        this.gcId = gcId;
        this.gcName = gcName;
    }

    public GoodsCategoryInfoBean(int gcId, String gcName) {
        this.gcId = gcId;
        this.gcName = gcName;
    }
}
