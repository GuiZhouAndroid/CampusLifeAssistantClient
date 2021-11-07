package work.lpssfxy.www.campuslifeassistantclient.entity;

import android.os.Parcel;
import android.os.Parcelable;

import lombok.Data;

/**
 * created by on 2021/11/6
 * 描述：
 *
 * @author ZSAndroid
 * @create 2021-11-06-20:14
 */
@Data
public class ParcelableData implements Parcelable {

    private String createTime;
    private String ulClass;
    private String ulDept;
    private String ulEmail;
    private int ulId;
    private String ulIdcard;
    private String ulRealname;
    private String ulSex;
    private String ulStuno;
    private String ulTel;
    private String ulUsername;
    private String updateTime;

    public ParcelableData() {

    }

    public ParcelableData(String createTime, String ulClass, String ulDept, String ulEmail, int ulId, String ulIdcard, String ulRealname, String ulSex, String ulStuno, String ulTel, String ulUsername, String updateTime) {
        this.createTime = createTime;
        this.ulClass = ulClass;
        this.ulDept = ulDept;
        this.ulEmail = ulEmail;
        this.ulId = ulId;
        this.ulIdcard = ulIdcard;
        this.ulRealname = ulRealname;
        this.ulSex = ulSex;
        this.ulStuno = ulStuno;
        this.ulTel = ulTel;
        this.ulUsername = ulUsername;
        this.updateTime = updateTime;
    }

    /**
     * 这里的读的顺序必须与writeToParcel(Parcel dest, int flags)方法中
     * 写的顺序一致，否则数据会有差错，比如你的读取顺序如果是：
     * nickname = source.readString();
     * username=source.readString();
     * age = source.readInt();
     * 即调换了username和nickname的读取顺序，那么你会发现你拿到的username是nickname的数据，
     * 而你拿到的nickname是username的数据
     * @param in
     */
    protected ParcelableData(Parcel in) {
        createTime = in.readString();
        ulClass = in.readString();
        ulDept = in.readString();
        ulEmail = in.readString();
        ulId = in.readInt();
        ulIdcard = in.readString();
        ulRealname = in.readString();
        ulSex = in.readString();
        ulStuno = in.readString();
        ulTel = in.readString();
        ulUsername = in.readString();
        updateTime = in.readString();
    }

    public static final Creator<ParcelableData> CREATOR = new Creator<ParcelableData>() {

        /**
         * 从Parcel中读取数据
         */

        @Override
        public ParcelableData createFromParcel(Parcel in) {
            return new ParcelableData(in);
        }

        /**
         * 供外部类反序列化本类数组使用
         */
        @Override
        public ParcelableData[] newArray(int size) {
            return new ParcelableData[size];
        }
    };

    /**
     * 这里默认返回0即可
     */
    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * 把值写入Parcel中
     */
    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(createTime);
        parcel.writeString(ulClass);
        parcel.writeString(ulDept);
        parcel.writeString(ulEmail);
        parcel.writeInt(ulId);
        parcel.writeString(ulIdcard);
        parcel.writeString(ulRealname);
        parcel.writeString(ulSex);
        parcel.writeString(ulStuno);
        parcel.writeString(ulTel);
        parcel.writeString(ulUsername);
        parcel.writeString(updateTime);
    }
}
