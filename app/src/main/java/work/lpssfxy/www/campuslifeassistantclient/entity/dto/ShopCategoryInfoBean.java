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
public class ShopCategoryInfoBean {
    private int scId;
    private String scName;
    private String scDesc;


    public ShopCategoryInfoBean() {
    }



    public ShopCategoryInfoBean(int scId, String scName, String scDesc) {
        this.scId = scId;
        this.scName = scName;
        this.scDesc = scDesc;
    }
}
