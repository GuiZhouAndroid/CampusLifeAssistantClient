package work.lpssfxy.www.campuslifeassistantclient.entity.dto;

import lombok.Data;

/**
 * created by on 2021/12/2
 * 描述：身份证OCR识别人像面
 *
 * @author ZSAndroid
 * @create 2021-12-02-23:37
 */
@Data
public class OCRFrontIdCardBean {
    private String cardNumber;//身份证号
    private String name;//姓名
    private String sex;//性别
    private String nation;//民族
    private String birth;//出生日期
    private String address;//家庭住址
}
