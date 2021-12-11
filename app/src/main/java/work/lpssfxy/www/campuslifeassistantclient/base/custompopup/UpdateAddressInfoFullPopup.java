package work.lpssfxy.www.campuslifeassistantclient.base.custompopup;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.hjq.toast.ToastUtils;
import com.lxj.xpopup.impl.FullScreenPopupView;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;
import com.xuexiang.xui.adapter.simple.AdapterItem;
import com.xuexiang.xui.adapter.simple.XUISimpleAdapter;
import com.xuexiang.xui.utils.ResUtils;
import com.xuexiang.xui.widget.button.ButtonView;
import com.xuexiang.xui.widget.edittext.ClearEditText;
import com.xuexiang.xui.widget.flowlayout.FlowTagLayout;
import com.xuexiang.xui.widget.popupwindow.popup.XUISimplePopup;

import java.util.List;

import work.lpssfxy.www.campuslifeassistantclient.R;
import work.lpssfxy.www.campuslifeassistantclient.base.Constant;
import work.lpssfxy.www.campuslifeassistantclient.base.flow.FlowTagAdapter;
import work.lpssfxy.www.campuslifeassistantclient.entity.dto.UserAddressInfoBean;
import work.lpssfxy.www.campuslifeassistantclient.entity.okgo.OkGoAddressBean;
import work.lpssfxy.www.campuslifeassistantclient.utils.MyXPopupUtils;
import work.lpssfxy.www.campuslifeassistantclient.utils.XToastUtils;
import work.lpssfxy.www.campuslifeassistantclient.utils.gson.GsonUtil;
import work.lpssfxy.www.campuslifeassistantclient.view.activity.UserAddressActivity;

/**
 * created by on 2021/12/10
 * 描述：全屏底部弹窗--->添加用户收货地址
 *
 * @author ZSAndroid
 * @create 2021-12-10-19:13
 */
public class UpdateAddressInfoFullPopup extends FullScreenPopupView {

    /* 先生或女士文本内容 */
    private String strUpdateGender;
    /* 初始化所在校区弹出框视图 */
    private XUISimplePopup mUpdateMenuPopup;
    /* 输入框控件 */
    private ClearEditText mEditUpdateAddressName, mEditUpdateAddressMobile, mTvUpdateAddressPlace, mTvUpdateAddressFloor, mEditUpdateAddressStreet;
    /* 流布局控件 */
    private FlowTagLayout mFlowUpdateTagLayoutGender;
    /* 校区控件 */
    private TextView mTvUpdateAddressDistrict;
    /* Activity传递过来的待编辑收获地址对象 */
    private UserAddressInfoBean userAddressInfoBean;
    /* 待更新的地址ID 和 用户ID */
    private int addressId,userId;


    public UpdateAddressInfoFullPopup(@NonNull Context context) {
        super(context);
    }

    public UpdateAddressInfoFullPopup(@NonNull Context context, UserAddressInfoBean userAddressInfoBean) {
        super(context);
        this.userAddressInfoBean = userAddressInfoBean;
    }


    @Override
    protected int getImplLayoutId() {
        return R.layout.activity_address_update_user_address_info_fullscreen_popup;
    }


    @Override
    protected void beforeShow() {
        super.beforeShow();
        mEditUpdateAddressName = findViewById(R.id.edit_address_update_name);//姓名
        mFlowUpdateTagLayoutGender = findViewById(R.id.flowlayout_address_update_gender);//性别
        mEditUpdateAddressMobile = findViewById(R.id.edit_address_update_mobile); //联系电话
        mTvUpdateAddressDistrict = findViewById(R.id.tv_address_update_district);//校区文本
        mTvUpdateAddressPlace = findViewById(R.id.edit_address_update_place);//地点
        mTvUpdateAddressFloor = findViewById(R.id.edit_address_update_floor);//楼层
        mEditUpdateAddressStreet = findViewById(R.id.edit_address_update_street); //门牌号
        initSingleFlowTagLayout();//初始化性别流布局
        initAddressDistrict();//初始化所在校区弹出框视图
        initUpdateData();//初始化收货地址更新数据
    }

    @Override
    protected void onShow() {

        //校区父布局
        LinearLayout mLlAddressDistrict = findViewById(R.id.ll_address_choose_update_district);
        mLlAddressDistrict.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                mUpdateMenuPopup.showDown(mEditUpdateAddressMobile);
            }
        });
        //确认更新
        ButtonView mBtnAddress = findViewById(R.id.btn_do_update_address);
        mBtnAddress.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                doUpdateAddressInfo();/* 开始更新收货地址 */
            }
        });
        //返回
        ImageView mIvAddressAddBack = findViewById(R.id.iv_address_update_back);
        mIvAddressAddBack.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

    }

    /**
     * 初始化收货地址更新数据
     */
    private void initUpdateData() {
        addressId = userAddressInfoBean.getAddressId();
        userId = userAddressInfoBean.getUserId();
        mEditUpdateAddressName.setText(userAddressInfoBean.getAddressName());//设置收货人
        mEditUpdateAddressMobile.setText(userAddressInfoBean.getMobile());//设置联系电话
        mTvUpdateAddressDistrict.setText(userAddressInfoBean.getDistrict());//设置校区
        mTvUpdateAddressPlace.setText(userAddressInfoBean.getPlace());//设置地点
        mTvUpdateAddressFloor.setText(userAddressInfoBean.getFloor());//设置楼层
        mEditUpdateAddressStreet.setText(userAddressInfoBean.getStreet());//设置门牌号
    }

    /**
     * 初始化性别流布局
     */
    private void initSingleFlowTagLayout() {
        FlowTagAdapter tagAdapter = new FlowTagAdapter(getContext());
        mFlowUpdateTagLayoutGender.setAdapter(tagAdapter);//设置流布局适配
        mFlowUpdateTagLayoutGender.setTagCheckedMode(FlowTagLayout.FLOW_TAG_CHECKED_SINGLE);//设置标签选中模式--->支持单个选择,不可取消
        tagAdapter.addTags(ResUtils.getStringArray(R.array.add_address_tags));//增加流布局的标签数据
        if (userAddressInfoBean.getGender().equals("先生")){//设置初始化选中的标签索引
            tagAdapter.setSelectedPositions(0);//设置初始化选中的标签索引 先生--->索引0 1 女士
            strUpdateGender = tagAdapter.getItem(0);
        }else if (userAddressInfoBean.getGender().equals("女士")){
            tagAdapter.setSelectedPositions(1);
            strUpdateGender = tagAdapter.getItem(1);
        }
        /* 流布局标签监听事件 */
        mFlowUpdateTagLayoutGender.setOnTagSelectListener(new FlowTagLayout.OnTagSelectListener() {
            @Override
            public void onItemSelect(FlowTagLayout parent, int position, List<Integer> selectedList) {
                strUpdateGender = getSelectedText(parent, selectedList);//选中的先生或女士原有数据
            }
        });
    }

    /**
     * 遍历取选择的对应流布局文本内容
     *
     * @param parent       流布局View
     * @param selectedList 选中的集合
     * @return 流布局选中的结果字符串 先生或女士
     */
    private String getSelectedText(FlowTagLayout parent, List<Integer> selectedList) {
        StringBuilder sb = new StringBuilder();
        for (int index : selectedList) {
            sb.append(parent.getAdapter().getItem(index));
        }
        return sb.toString();
    }

    /**
     * 初始化所在校区弹出框视图
     */
    private void initAddressDistrict() {
        //设置弹出框数据 + 选择的数据
        mUpdateMenuPopup = new XUISimplePopup(getContext(), Constant.menuItems).create(new XUISimplePopup.OnPopupItemClickListener() {
            @Override
            public void onItemClick(XUISimpleAdapter adapter, AdapterItem item, int position) {
                //设置TextView上，提供添加收货地址使用
                mTvUpdateAddressDistrict.setText(item.getTitle().toString());
            }
        });
    }


    /**
     * 执行添加收货地址
     */
    private void doUpdateAddressInfo() {
        /*1.获取文本内容*/
        String strGetUpdateName = mEditUpdateAddressName.getText().toString().trim();//收货人数据
        String strGetUpdateMobile = mEditUpdateAddressMobile.getText().toString().trim();//联系电话数据
        String strUpdateDistrict = mTvUpdateAddressDistrict.getText().toString().trim();//校区数据
        String strGetUpdatePlace = mTvUpdateAddressPlace.getText().toString().trim();//详细地点数据
        String strGetUpdateFloor = mTvUpdateAddressFloor.getText().toString().trim();//楼层数据
        String strGetUpdateStreet = mEditUpdateAddressStreet.getText().toString().trim();//门牌号
        /* 2.调用接口添加 */
        OkGo.<String>post(Constant.USER_ADD_ONCE_ADDRESS_INFO
                + "/" + strGetUpdateName + "/" + strUpdateGender + "/" + strGetUpdateMobile + "/" + strUpdateDistrict
                + "/" + strGetUpdatePlace + "/" + strGetUpdateFloor + "/" + strGetUpdateStreet)
                .tag("添加收货地址")
                .execute(new StringCallback() {
                    @Override
                    public void onStart(Request<String, ? extends Request> request) {
                        MyXPopupUtils.getInstance().setShowDialog((Activity) getContext(), "正在添加收货地址...");
                    }

                    @Override
                    public void onSuccess(Response<String> response) {
                        OkGoAddressBean okGoAddressBean = GsonUtil.gsonToBean(response.body(), OkGoAddressBean.class);
                        if (200 == okGoAddressBean.getCode() && "收货信息添加成功".equals(okGoAddressBean.getMsg())) {
                            XToastUtils.success("添加成功");
                            dismiss();
                            //解析JSON，封装实体，传入列表适配器，更新UI
                            UserAddressInfoBean userAddressInfoBean = new UserAddressInfoBean(
                                    okGoAddressBean.getData().getAddressId(), okGoAddressBean.getData().getAddressName(), okGoAddressBean.getData().getCreateTime(),
                                    okGoAddressBean.getData().getDistrict(), okGoAddressBean.getData().getFloor(), okGoAddressBean.getData().getGender(),
                                    okGoAddressBean.getData().getMobile(), okGoAddressBean.getData().getPlace(), okGoAddressBean.getData().getStreet(),
                                    okGoAddressBean.getData().getUpdateTime(), okGoAddressBean.getData().getUserId());
                            //Activity中实现接口，传输数据并发起更新UI请求
                            ((UserAddressActivity) (getContext())).doSetAddressData(userAddressInfoBean);
                            return;
                        }
                        if (200 == okGoAddressBean.getCode() && "收货信息添加失败".equals(okGoAddressBean.getMsg())) {
                            XToastUtils.success("添加失败");
                        }
                    }

                    @Override
                    public void onFinish() {
                        MyXPopupUtils.getInstance().setSmartDisDialog();
                    }

                    @Override
                    public void onError(Response<String> response) {
                        XToastUtils.error("服务器请求错误，地址添加失败！");
                    }
                });
    }
}
