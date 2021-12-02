package work.lpssfxy.www.campuslifeassistantclient.view.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.shouzhong.scanner.Callback;
import com.shouzhong.scanner.IViewFinder;
import com.shouzhong.scanner.Result;
import com.shouzhong.scanner.ScannerView;

import butterknife.BindView;
import work.lpssfxy.www.campuslifeassistantclient.R;
import work.lpssfxy.www.campuslifeassistantclient.R2;
import work.lpssfxy.www.campuslifeassistantclient.entity.dto.OCRIdCardBean;
import work.lpssfxy.www.campuslifeassistantclient.utils.XXPermissionsAction;
import work.lpssfxy.www.campuslifeassistantclient.utils.gson.GsonUtil;

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

    @BindView(R2.id.sv) ScannerView scannerView;
    @BindView(R2.id.tv_result) TextView tvResult;

    @BindView(R2.id.edit_bind_card_number) EditText edit_bind_card_number;
    @BindView(R2.id.edit_bind_card_name) EditText edit_bind_card_name;
    @BindView(R2.id.edit_bind_card_sex) EditText edit_bind_card_sex;
    @BindView(R2.id.edit_bind_card_nation) EditText edit_bind_card_nation;
    @BindView(R2.id.edit_bind_card_birth) EditText edit_bind_card_birth;
    @BindView(R2.id.edit_bind_card_address) EditText edit_bind_card_address;


    private Vibrator vibrator;
    //实名认证用户ID
    private String strNowDoCerUserId;

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
    public int bindLayout() {
        return R.layout.user_cer_bind_ocr_idcard_activity;
    }

    @Override
    protected void prepareData() {
        XXPermissionsAction.getInstance().camera(this);
        //当前登录需要绑定身份证进行实名认证的校园帮注册用户的自增ID
        strNowDoCerUserId = getIntent().getStringExtra("NowDoCerUserId");
        Log.i("当前登录需要绑定身份证进行实用户的自增ID: ",strNowDoCerUserId);
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
        new Thread(new Runnable() {
            @Override
            public void run() {
                setParamOcrIdCard();
                oCRResult();
            }
        }).start();
    }

    /**
     * 设置身份证识别参数
     */
    private void setParamOcrIdCard() {
        scannerView.setEnableIdCard2(true);
        scannerView.setShouldAdjustFocusArea(true);
        scannerView.setSaveBmp(false);
        scannerView.setRotateDegree90Recognition(true);
        scannerView.setViewFinder(new ViewFinder(UserCerBindOCRIdCardActivity.this));
        scannerView.setRotateDegree90Recognition(true);
    }

    /**
     * OCR识别结果
     */
    private void oCRResult() {
        scannerView.setCallback(new Callback() {
            @Override
            public void result(Result result) {
                String strOcrResult = result.toString();
                tvResult.setText(strOcrResult);
                //切割字符串，为Gson解析进行字符串格式化
                String strJsonOcrIdCard = strOcrResult.substring(strOcrResult.indexOf("{"), strOcrResult.lastIndexOf("}") + 1);
                //识别后的Json字符串使用Gson转为对象数据
                OCRIdCardBean ocrIdCardBean = GsonUtil.gsonToBean(strJsonOcrIdCard, OCRIdCardBean.class);
                edit_bind_card_number.setText(ocrIdCardBean.getCardNumber());
                edit_bind_card_name.setText(ocrIdCardBean.getName());
                edit_bind_card_sex.setText(ocrIdCardBean.getSex());
                edit_bind_card_nation.setText(ocrIdCardBean.getNation());
                edit_bind_card_birth.setText(ocrIdCardBean.getBirth());
                edit_bind_card_address.setText(ocrIdCardBean.getAddress());
                startVibrator();
                scannerView.restartPreviewAfterDelay(2000);

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
        scannerView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        scannerView.onPause();
    }

    @Override
    protected void onDestroy() {
        if (vibrator != null) {
            vibrator.cancel();
            vibrator = null;
        }
        super.onDestroy();
    }
}
