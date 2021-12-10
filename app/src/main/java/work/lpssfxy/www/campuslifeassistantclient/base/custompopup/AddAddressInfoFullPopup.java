package work.lpssfxy.www.campuslifeassistantclient.base.custompopup;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;
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
import work.lpssfxy.www.campuslifeassistantclient.entity.okgo.OkGoResponseBean;
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
public class AddAddressInfoFullPopup extends FullScreenPopupView {

    /* 先生或女士文本内容 */
    private String strGender;
    /* 初始化所在校区弹出框视图 */
    private XUISimplePopup mMenuPopup;
    /* 输入框控件 */
    private ClearEditText mEditAddressName, mEditAddressMobile, mTvAddressPlace, mTvAddressFloor, mEditAddressStreet;
    /* 流布局控件 */
    private FlowTagLayout mFlowTagLayoutGender;
    /* 校区控件 */
    private TextView mTvAddressDistrict;

    public AddAddressInfoFullPopup(@NonNull Context context) {
        super(context);
    }


    @Override
    protected int getImplLayoutId() {
        return R.layout.activity_address_add_user_address_info_fullscreen_popup;
    }

    @Override
    protected void onShow() {
        super.onShow();
        mEditAddressName = findViewById(R.id.edit_address_name);//姓名
        mFlowTagLayoutGender = findViewById(R.id.flowlayout_address_gender);//性别
        mEditAddressMobile = findViewById(R.id.edit_address_mobile); //联系电话
        mTvAddressDistrict = findViewById(R.id.tv_address_district);//校区文本
        mTvAddressPlace = findViewById(R.id.edit_address_place);//地点
        mTvAddressFloor = findViewById(R.id.edit_address_floor);//楼层
        mEditAddressStreet = findViewById(R.id.edit_address_street); //门牌号
        initSingleFlowTagLayout();//初始化性别流布局
        initAddressDistrict();//初始化所在校区弹出框视图
        //校区父布局
        LinearLayout mLlAddressDistrict = findViewById(R.id.ll_address_choose_district);
        mLlAddressDistrict.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                mMenuPopup.showDown(mEditAddressMobile);
            }
        });
        //确认添加
        ButtonView mBtnAddress = findViewById(R.id.btn_do_add_address);
        mBtnAddress.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                doAddressInfo();/* 开始添加收货地址 */
            }
        });
        //返回
        ImageView mIvAddressAddBack = findViewById(R.id.iv_address_add_back);
        mIvAddressAddBack.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

    }

    /**
     * 初始化性别流布局
     */
    private void initSingleFlowTagLayout() {
        FlowTagAdapter tagAdapter = new FlowTagAdapter(getContext());
        mFlowTagLayoutGender.setAdapter(tagAdapter);//设置流布局适配
        mFlowTagLayoutGender.setTagCheckedMode(FlowTagLayout.FLOW_TAG_CHECKED_SINGLE);//设置标签选中模式--->支持单个选择,不可取消
        tagAdapter.addTags(ResUtils.getStringArray(R.array.add_address_tags));//增加流布局的标签数据
        tagAdapter.setSelectedPositions(0);//设置初始化选中的标签索引 先生--->索引0 1 女士
        strGender = tagAdapter.getItem(0);//默认为先生--->索引0 1 女士
        /* 流布局标签监听事件 */
        mFlowTagLayoutGender.setOnTagSelectListener(new FlowTagLayout.OnTagSelectListener() {
            @Override
            public void onItemSelect(FlowTagLayout parent, int position, List<Integer> selectedList) {
                strGender = getSelectedText(parent, selectedList);//选中的先生或女士原有数据
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
        mMenuPopup = new XUISimplePopup(getContext(), Constant.menuItems).create(new XUISimplePopup.OnPopupItemClickListener() {
            @Override
            public void onItemClick(XUISimpleAdapter adapter, AdapterItem item, int position) {
                //设置TextView上，提供添加收货地址使用
                mTvAddressDistrict.setText(item.getTitle().toString());
            }
        });
        //弹出框在所在校区周围
        mMenuPopup.showDown(mEditAddressMobile);
    }


    /**
     * 执行添加收货地址
     */
    private void doAddressInfo() {
        String strGetName = mEditAddressName.getText().toString().trim();//收货人数据
        String strGetMobile = mEditAddressMobile.getText().toString().trim();//联系电话数据
        String strDistrict = mTvAddressDistrict.getText().toString().trim();//校区数据
        String strGetPlace = mTvAddressPlace.getText().toString().trim();//详细地点数据
        String strGetFloor = mTvAddressFloor.getText().toString().trim();//楼层数据
        String strGetStreet = mEditAddressStreet.getText().toString().trim();//门牌号
        /* 2.判空处理 */
        if (TextUtils.isEmpty(strGetName)) {
            ToastUtils.show("请完善收货人信息");
            return;
        }
        if (TextUtils.isEmpty(strGender)) {
            ToastUtils.show("请完善称呼信息");
            return;
        }
        if (TextUtils.isEmpty(strGetMobile)) {
            ToastUtils.show("请完善联系电话信息");
            return;
        }
        if (TextUtils.isEmpty(strDistrict)) {
            ToastUtils.show("请完善所在校区信息");
            return;
        }
        if (TextUtils.isEmpty(strGetPlace)) {
            ToastUtils.show("请完善详细地点信息");
            return;
        }
        if (TextUtils.isEmpty(strGetFloor)) {
            ToastUtils.show("请完善楼层信息");
            return;
        }
        if (TextUtils.isEmpty(strGetStreet)) {
            ToastUtils.show("请完善门牌号信息");
            return;
        }
        /* 3.调用接口添加 */
        OkGo.<String>post(Constant.USER_ADD_ONCE_ADDRESS_INFO
                + "/" + strGetName + "/" + strGender + "/" + strGetMobile + "/" + strDistrict
                + "/" + strGetPlace + "/" + strGetFloor + "/" + strGetStreet)
                .tag("添加收货地址")
                .execute(new StringCallback() {
                    @Override
                    public void onStart(Request<String, ? extends Request> request) {
                        MyXPopupUtils.getInstance().setShowDialog((Activity) getContext(),"正在添加收货地址...");
                    }

                    @Override
                    public void onSuccess(Response<String> response) {
                        OkGoResponseBean okGoResponseBean = GsonUtil.gsonToBean(response.body(), OkGoResponseBean.class);
                        if (200 == okGoResponseBean.getCode() && "收货信息添加成功".equals(okGoResponseBean.getData()) && "success".equals(okGoResponseBean.getMsg())) {
                            XToastUtils.success("添加成功");
                            dismiss();
                            //Activity中实现接口,在Fragment中更新其所在Activity的UI --->此方法能实现没使用
                            //((UserAddressActivity)(getContext())).unReadChange();
                            return;
                        }
                        if (200 == okGoResponseBean.getCode() && "收货信息添加失败".equals(okGoResponseBean.getData()) && "error".equals(okGoResponseBean.getMsg())) {
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

    /**
     * 更新ActivityUI--->此方法能实现没使用
     */
    public interface updateActivityUIFromFragment{
        abstract void unReadChange();
    }
}
