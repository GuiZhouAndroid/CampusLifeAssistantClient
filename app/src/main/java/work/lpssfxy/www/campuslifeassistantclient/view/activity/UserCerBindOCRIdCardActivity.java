package work.lpssfxy.www.campuslifeassistantclient.view.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Vibrator;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.hjq.toast.ToastUtils;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;
import com.shouzhong.scanner.Callback;
import com.shouzhong.scanner.IViewFinder;
import com.shouzhong.scanner.Result;
import com.shouzhong.scanner.ScannerView;
import com.xuexiang.xui.widget.button.ButtonView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.OnClick;
import work.lpssfxy.www.campuslifeassistantclient.R;
import work.lpssfxy.www.campuslifeassistantclient.R2;
import work.lpssfxy.www.campuslifeassistantclient.base.Constant;
import work.lpssfxy.www.campuslifeassistantclient.entity.dto.OCRFrontIdCardBean;
import work.lpssfxy.www.campuslifeassistantclient.entity.dto.OCRRearIdCardBean;
import work.lpssfxy.www.campuslifeassistantclient.entity.okgo.OkGoResponseBean;
import work.lpssfxy.www.campuslifeassistantclient.utils.MyXPopupUtils;
import work.lpssfxy.www.campuslifeassistantclient.utils.XXPermissionsAction;
import work.lpssfxy.www.campuslifeassistantclient.utils.dialog.DialogPrompt;
import work.lpssfxy.www.campuslifeassistantclient.utils.gson.GsonUtil;
import work.lpssfxy.www.campuslifeassistantclient.utils.okhttp.OkGoErrorUtil;
import work.lpssfxy.www.campuslifeassistantclient.view.BaseActivity;

/**
 * created by on 2021/12/2
 * 描述：用户使用OCR扫描身份证，进行实名认证
 *
 * @author ZSAndroid
 * @create 2021-12-02-22:22
 */
@SuppressLint("NonConstantResourceId")
public class UserCerBindOCRIdCardActivity extends BaseActivity {

    private static final String TAG = "UserCerBindOCRIdCardActivity";

    //父布局
    @BindView(R2.id.rl_ocr_bind_show) RelativeLayout mRlOcrBindShow;
    //ORC扫描框
    @BindView(R2.id.sv_ocr_id_card) ScannerView mScannerOCRIdCardView;
    //识别身份证前后类型
    @BindView(R2.id.tv_ocr_type) TextView tvOcrType;
    //OCR身份证号
    @BindView(R2.id.edit_bind_card_number) EditText mEditBindCardNumber;
    //OCR姓名
    @BindView(R2.id.edit_bind_card_name) EditText mEditBindCardName;
    //OCR性别
    @BindView(R2.id.edit_bind_card_sex) EditText mEditBindCardSex;
    //OCR民族
    @BindView(R2.id.edit_bind_card_nation) EditText mEditBindCardNation;
    //OCR出生日期
    @BindView(R2.id.edit_bind_card_birth) EditText mEditBindCardBirth;
    //OCR家庭住址
    @BindView(R2.id.edit_bind_card_address) EditText mEditBindCardAddress;
    //OCR公安局
    @BindView(R2.id.edit_bind_card_psb) EditText mEditBindCardPsd;
    //OCR身份证有效日期
    @BindView(R2.id.edit_bind_card_valid_data) EditText mEditBindCardValidData;
    //执行认证
    @BindView(R2.id.btn_start_yes_do_cer) ButtonView mBtnStartYesDoCer;
    //Android振动绘制扫码诓
    private Vibrator vibrator;
    //实名认证提交参数
    private String strNowDoCerUserRealName,strCardNumber,strCardName,strCardSex,strCardNation,strCardBirth,strCardAddress,strCardPsd,strCardValidData;
    private int strNowDoCerUserId;
    @Override
    protected Boolean isSetSwipeBackLayout() {
        return true;
    }

    @Override
    protected Boolean isSetStatusBarState() {
        return true;
    }

    @Override
    protected Boolean isSetBottomNaviCationState() {
        return false;
    }

    @Override
    protected Boolean isSetBottomNaviCationColor() {
        return false;
    }

    @Override
    protected Boolean isSetImmersiveFullScreen() {
        return false;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        XXPermissionsAction.getInstance().camera(this);
        super.onCreate(savedInstanceState);
    }

    @Override
    public int bindLayout() {
        return R.layout.activity_user_cer_bind_ocr_idcard;
    }

    @Override
    protected void prepareData() {
        //当前登录需要绑定身份证进行实名认证的校园帮注册用户的自增ID
        strNowDoCerUserId = Integer.parseInt(getIntent().getStringExtra("NowDoCerUserId"));
        strNowDoCerUserRealName = getIntent().getStringExtra("NowDoCerUserRealName");
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData(Bundle savedInstanceState) {

    }

    @Override
    protected void initEvent() {
    }

    @Override
    protected void initListener() {

    }

    @Override
    protected void doBusiness() {
        setParamOcrIdCard();
        oCRResult();
    }

    /**
     * @param view 视图View
     */
    @OnClick({R2.id.btn_start_yes_do_cer, R2.id.edit_bind_card_address})
    public void onStartYesDoCerViewClick(View view) {
        switch (view.getId()) {
            case R.id.btn_start_yes_do_cer://执行认证
                getEditCerInfo();//获取OCR自动填入输入框的文本参数
                StartYesDoCerInfo();//调用SpringBoot后端接口
                break;
        }
    }

    /**
     * 获取OCR自动填入输入框的文本参数
     */
    private void getEditCerInfo() {
        strCardNumber = mEditBindCardNumber.getText().toString().trim();
        strCardName = mEditBindCardName.getText().toString().trim();//OCR姓名
        strCardSex = mEditBindCardSex.getText().toString().trim();//OCR性别
        strCardNation = mEditBindCardNation.getText().toString().trim(); //OCR民族
        strCardBirth = mEditBindCardBirth.getText().toString().trim();  //OCR出生日期
        strCardAddress = mEditBindCardAddress.getText().toString().trim();  //OCR家庭住址
        strCardPsd = mEditBindCardPsd.getText().toString().trim();//OCR公安局
        strCardValidData = mEditBindCardValidData.getText().toString().trim();//OCR身份证有效日期
    }

    /**
     * 调用SpringBoot后端接口，开始执行实名认证
     */
    private void StartYesDoCerInfo() {
        //OCR信息判空
        if (TextUtils.isEmpty(strCardNumber)) {
            ToastUtils.show("身份证号无参数，请识别获取后重试");
            return;
        }
        if (TextUtils.isEmpty(strCardName)) {
            ToastUtils.show("真实姓名无参数，请识别获取后重试");
            return;
        }
        if (TextUtils.isEmpty(strCardSex)) {
            ToastUtils.show("性别无参数，请识别获取后重试");
            return;
        }
        if (TextUtils.isEmpty(strCardNation)) {
            ToastUtils.show("民族无参数，请识别获取后重试");
            return;
        }
        if (TextUtils.isEmpty(strCardBirth)) {
            ToastUtils.show("出生日期无参数，请识别获取后重试");
            return;
        }
        if (TextUtils.isEmpty(strCardAddress)) {
            ToastUtils.show("家庭住址无参数，请识别获取后重试");
            return;
        }
        if (TextUtils.isEmpty(strCardPsd)) {
            ToastUtils.show("签发机关无参数，请识别获取后重试");
            return;
        }
        if (TextUtils.isEmpty(strCardValidData)) {
            ToastUtils.show("有效期限无参数，请识别获取后重试");
            return;
        }
        //开始请求网络访问服务器上传认证信息
        OkGo.<String>post(Constant.SELECT_CER_ALL_INFO_BY_OCR_DATA_IF_DO_CER + "/"
                + strNowDoCerUserId + "/" + strCardNumber + "/" + strCardName + "/" + strCardSex + "/"
                + strCardNation + "/" + strCardBirth + "/" + strCardAddress + "/" +  strCardPsd + "/" + strCardValidData)
                .tag("绑定实名认证")
                .execute(new StringCallback() {
                    @Override
                    public void onStart(Request<String, ? extends Request> request) {
                        MyXPopupUtils.getInstance().setShowDialog(UserCerBindOCRIdCardActivity.this, "正在校验实名信息...");
                    }

                    @Override
                    public void onSuccess(Response<String> response) {
                        OkGoResponseBean okGoResponseBean = GsonUtil.gsonToBean(response.body(), OkGoResponseBean.class);
                        if (200 == okGoResponseBean.getCode() && "error".equals(okGoResponseBean.getMsg()) && "实名认证失败，不存在该用户".equals(okGoResponseBean.getData())) {
                            DialogPrompt dialogPrompt = new DialogPrompt(UserCerBindOCRIdCardActivity.this, okGoResponseBean.getData(), 5);
                            dialogPrompt.showAndFinish(UserCerBindOCRIdCardActivity.this);
                            return;
                        }
                        if (200 == okGoResponseBean.getCode() && "error".equals(okGoResponseBean.getMsg()) && "实名认证未通过，真实姓名校验不匹配".equals(okGoResponseBean.getData())) {
                            DialogPrompt dialogPrompt = new DialogPrompt(UserCerBindOCRIdCardActivity.this, "实名认证未通过，真实姓名与您注册信息不匹配", 5);
                            dialogPrompt.showAndFinish(UserCerBindOCRIdCardActivity.this);
                            return;
                        }
                        if (200 == okGoResponseBean.getCode() && "error".equals(okGoResponseBean.getMsg()) && "实名认证未通过，性别校验不匹配".equals(okGoResponseBean.getData())) {
                            DialogPrompt dialogPrompt = new DialogPrompt(UserCerBindOCRIdCardActivity.this,"实名认证未通过，性别与您注册信息不匹配", 5);
                            dialogPrompt.showAndFinish(UserCerBindOCRIdCardActivity.this);
                            return;
                        }
                        if (200 == okGoResponseBean.getCode() && "error".equals(okGoResponseBean.getMsg()) && "实名认证未通过，身份证号校验不匹配".equals(okGoResponseBean.getData())) {
                            DialogPrompt dialogPrompt = new DialogPrompt(UserCerBindOCRIdCardActivity.this,"实名认证未通过，身份证号与您注册信息不匹配", 5);
                            dialogPrompt.showAndFinish(UserCerBindOCRIdCardActivity.this);
                            return;
                        }
                        if (200 == okGoResponseBean.getCode() && "error".equals(okGoResponseBean.getMsg()) && "实名认证发生未知错误，请尽快联系开发者解决".equals(okGoResponseBean.getData())) {
                            DialogPrompt dialogPrompt = new DialogPrompt(UserCerBindOCRIdCardActivity.this, okGoResponseBean.getData(), 5);
                            dialogPrompt.showAndFinish(UserCerBindOCRIdCardActivity.this);
                            return;
                        }
                        if (200 == okGoResponseBean.getCode() && "success".equals(okGoResponseBean.getMsg()) && "已经实名认证，无须重复操作".equals(okGoResponseBean.getData())) {
                            DialogPrompt dialogPrompt = new DialogPrompt(UserCerBindOCRIdCardActivity.this, okGoResponseBean.getData(), 5);
                            dialogPrompt.showAndFinish(UserCerBindOCRIdCardActivity.this);
                            return;
                        }
                        if (200 == okGoResponseBean.getCode() && "success".equals(okGoResponseBean.getMsg()) && "实名认证通过".equals(okGoResponseBean.getData())) {
                            DialogPrompt dialogPrompt = new DialogPrompt(UserCerBindOCRIdCardActivity.this, okGoResponseBean.getData(), 5);
                            dialogPrompt.showAndFinish(UserCerBindOCRIdCardActivity.this);
                            Intent intent = new Intent();
                            intent.putExtra("BindOCRRealName",strNowDoCerUserRealName );
                            UserCerBindOCRIdCardActivity.this.setResult(Constant.RESULT_CODE_OCR_BIND_ACCOUNT_SUCCESS, intent);
                            return;
                        }
                        DialogPrompt dialogPrompt = new DialogPrompt(UserCerBindOCRIdCardActivity.this, "实名认证错误，你处于离线状态，", 5);
                        dialogPrompt.showAndFinish(UserCerBindOCRIdCardActivity.this);
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        MyXPopupUtils.getInstance().setSmartDisDialog();
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        OkGoErrorUtil.CustomFragmentOkGoError(response, UserCerBindOCRIdCardActivity.this, mRlOcrBindShow, "请求错误，服务器连接失败！");
                    }
                });
    }

    /**
     * 设置身份证识别参数
     */
    private void setParamOcrIdCard() {
        mScannerOCRIdCardView.setEnableIdCard2(true);
        mScannerOCRIdCardView.setShouldAdjustFocusArea(true);
        mScannerOCRIdCardView.setViewFinder(new ViewFinder(UserCerBindOCRIdCardActivity.this));
        mScannerOCRIdCardView.setSaveBmp(false);
        mScannerOCRIdCardView.setRotateDegree90Recognition(true);
    }

    /**
     * OCR识别结果
     */
    private void oCRResult() {
        mScannerOCRIdCardView.setCallback(new Callback() {
            @SuppressLint("SetTextI18n")
            @Override
            public void result(Result result) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        startVibrator();
                        mScannerOCRIdCardView.restartPreviewAfterDelay(500);

                        String strOcrResult = result.toString();
                        String strTypeOcrIdCard = strOcrResult.substring(strOcrResult.indexOf("身"), strOcrResult.lastIndexOf("面") + 1);
                        //切割字符串，为Gson解析进行字符串格式化
                        if (strTypeOcrIdCard.equals("身份证人头面")) {
                            String strFrontJsonOcrIdCard = strOcrResult.substring(strOcrResult.indexOf("{"), strOcrResult.lastIndexOf("}") + 1);
                            //识别后的Json字符串使用Gson转为对象数据
                            OCRFrontIdCardBean ocrFrontIdCardBean = GsonUtil.gsonToBean(strFrontJsonOcrIdCard, OCRFrontIdCardBean.class);
                            mEditBindCardNumber.setText(ocrFrontIdCardBean.getCardNumber());//设置身份证号
                            mEditBindCardName.setText(ocrFrontIdCardBean.getName());//设置姓名
                            mEditBindCardSex.setText(ocrFrontIdCardBean.getSex());//设置性别
                            mEditBindCardNation.setText(ocrFrontIdCardBean.getNation());//设置民族
                            Date dateCardBirth = null;
                            try {
                                dateCardBirth = new SimpleDateFormat("yyyy-MM-dd").parse(ocrFrontIdCardBean.getBirth());
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            String strCardBirth = new SimpleDateFormat("yyyy年MM月dd日").format(dateCardBirth);
                            mEditBindCardBirth.setText(strCardBirth);//设置出生日期
                            mEditBindCardAddress.setText(ocrFrontIdCardBean.getAddress());//设置家庭住址
                            tvOcrType.setText("识别结果：已成功识别人像面信息");
                        } else if (strTypeOcrIdCard.equals("身份证国徽面")) {
                            String strRearJsonOcrIdCard = strOcrResult.substring(strOcrResult.indexOf("{"), strOcrResult.lastIndexOf("}") + 1);
                            //识别后的Json字符串使用Gson转为对象数据
                            OCRRearIdCardBean ocrRearIdCardBean = GsonUtil.gsonToBean(strRearJsonOcrIdCard, OCRRearIdCardBean.class);
                            mEditBindCardPsd.setText(ocrRearIdCardBean.getOrganization());//设置签发机关
                            String strStartData = ocrRearIdCardBean.getValidPeriod().substring(0, 10);//期限有效时间
                            String strEndData = ocrRearIdCardBean.getValidPeriod().substring(10, 20);//期限无效时间
                            mEditBindCardValidData.setText(strStartData + "—" + strEndData);//设置有效期限
                            tvOcrType.setText("识别结果：已成功识别国徽面信息");
                        }
                    }
                });
            }
        });
    }

    private void startVibrator() {
        if (vibrator == null)
            vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(300);
    }

    class ViewFinder extends View implements IViewFinder {
        private Rect framingRect;//扫码框所占区域
        private float widthRatio = 0.9f;//扫码框宽度占view总宽度的比例
        private float heightRatio = 0.8f;
        private float heightWidthRatio = 0.5626f;//扫码框的高宽比
        private int leftOffset = -1;//扫码框相对于左边的偏移量，若为负值，则扫码框会水平居中
        private int topOffset = -1;//扫码框相对于顶部的偏移量，若为负值，则扫码框会竖直居中

        private int laserColor = 0xff008577;// 扫描线颜色
        private int maskColor = 0x60000000;// 阴影颜色
        private int borderColor = 0xff008577;// 边框颜色
        private int borderStrokeWidth = 12;// 边框宽度
        private int borderLineLength = 72;// 边框长度

        private Paint laserPaint;// 扫描线
        private Paint maskPaint;// 阴影遮盖画笔
        private Paint borderPaint;// 边框画笔

        private int position;

        public ViewFinder(Context context) {
            super(context);
            setWillNotDraw(false);//需要进行绘制
            laserPaint = new Paint();
            laserPaint.setColor(laserColor);
            laserPaint.setStyle(Paint.Style.FILL);
            maskPaint = new Paint();
            maskPaint.setColor(maskColor);
            borderPaint = new Paint();
            borderPaint.setColor(borderColor);
            borderPaint.setStyle(Paint.Style.STROKE);
            borderPaint.setStrokeWidth(borderStrokeWidth);
            borderPaint.setAntiAlias(true);
        }

        @Override
        protected void onSizeChanged(int xNew, int yNew, int xOld, int yOld) {
            updateFramingRect();
        }

        @Override
        public void onDraw(Canvas canvas) {
            if (getFramingRect() == null) {
                return;
            }
            drawViewFinderMask(canvas);
            drawViewFinderBorder(canvas);
            drawLaser(canvas);
        }

        private void drawLaser(Canvas canvas) {
            Rect framingRect = getFramingRect();
            int top = framingRect.top + 10 + position;
            canvas.drawRect(framingRect.left + 10, top, framingRect.right - 10, top + 5, laserPaint);
            position = framingRect.bottom - framingRect.top - 25 < position ? 0 : position + 2;
            //区域刷新
            postInvalidateDelayed(20, framingRect.left + 10, framingRect.top + 10, framingRect.right - 10, framingRect.bottom - 10);
        }

        /**
         * 绘制扫码框四周的阴影遮罩
         */
        private void drawViewFinderMask(Canvas canvas) {
            int width = canvas.getWidth();
            int height = canvas.getHeight();
            Rect framingRect = getFramingRect();
            canvas.drawRect(0, 0, width, framingRect.top, maskPaint);//扫码框顶部阴影
            canvas.drawRect(0, framingRect.top, framingRect.left, framingRect.bottom, maskPaint);//扫码框左边阴影
            canvas.drawRect(framingRect.right, framingRect.top, width, framingRect.bottom, maskPaint);//扫码框右边阴影
            canvas.drawRect(0, framingRect.bottom, width, height, maskPaint);//扫码框底部阴影
        }

        /**
         * 绘制扫码框的边框
         */
        private void drawViewFinderBorder(Canvas canvas) {
            Rect framingRect = getFramingRect();

            // Top-left corner
            Path path = new Path();
            path.moveTo(framingRect.left, framingRect.top + borderLineLength);
            path.lineTo(framingRect.left, framingRect.top);
            path.lineTo(framingRect.left + borderLineLength, framingRect.top);
            canvas.drawPath(path, borderPaint);

            // Top-right corner
            path.moveTo(framingRect.right, framingRect.top + borderLineLength);
            path.lineTo(framingRect.right, framingRect.top);
            path.lineTo(framingRect.right - borderLineLength, framingRect.top);
            canvas.drawPath(path, borderPaint);

            // Bottom-right corner
            path.moveTo(framingRect.right, framingRect.bottom - borderLineLength);
            path.lineTo(framingRect.right, framingRect.bottom);
            path.lineTo(framingRect.right - borderLineLength, framingRect.bottom);
            canvas.drawPath(path, borderPaint);

            // Bottom-left corner
            path.moveTo(framingRect.left, framingRect.bottom - borderLineLength);
            path.lineTo(framingRect.left, framingRect.bottom);
            path.lineTo(framingRect.left + borderLineLength, framingRect.bottom);
            canvas.drawPath(path, borderPaint);
        }

        /**
         * 设置framingRect的值（扫码框所占的区域）
         */
        private synchronized void updateFramingRect() {
            Point viewSize = new Point(getWidth(), getHeight());
            int width = getWidth() * 801 / 1080, height = getWidth() * 811 / 1080;
            width = (int) (getWidth() * widthRatio);
//            height = (int) (getHeight() * heightRatio);
            height = (int) (heightWidthRatio * width);

            int left, top;
            if (leftOffset < 0) {
                left = (viewSize.x - width) / 2;//水平居中
            } else {
                left = leftOffset;
            }
            if (topOffset < 0) {
                top = (viewSize.y - height) / 2;//竖直居中
            } else {
                top = topOffset;
            }
            framingRect = new Rect(left, top, left + width, top + height);
        }

        @Override
        public Rect getFramingRect() {
            return framingRect;
        }
    }

    class ViewFinder2 implements IViewFinder {
        @Override
        public Rect getFramingRect() {
            return new Rect(240, 240, 840, 840);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mScannerOCRIdCardView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mScannerOCRIdCardView.onPause();
    }

    @Override
    protected void onDestroy() {
        if (vibrator != null) {
            vibrator.cancel();
            vibrator = null;
        }
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        mScannerOCRIdCardView.onPause();
        UserCerBindOCRIdCardActivity.this.finish();
    }
}
