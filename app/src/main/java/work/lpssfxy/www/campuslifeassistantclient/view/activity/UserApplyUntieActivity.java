package work.lpssfxy.www.campuslifeassistantclient.view.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import work.lpssfxy.www.campuslifeassistantclient.R;
import work.lpssfxy.www.campuslifeassistantclient.base.dialog.CustomAlertDialog;

/**
 * created by on 2021/10/29
 * 描述：用户申请解封账户--->联系本人开发者
 *
 * @author ZSAndroid
 * @create 2021-10-29-12:05
 */

public class UserApplyUntieActivity extends BaseActivity implements View.OnClickListener {

    /**
     * 仿ios中间弹框,标题/内容/两个按钮
     */
    private TextView tv_dialog_red;
    /**
     * 仿ios中间弹框,内容/两个按钮
     */
    private TextView tv_dialog_orange;
    /**
     * 仿ios中间弹框,标题/两个按钮
     */
    private TextView tv_dialog_yellow;
    /**
     * 仿ios中间弹框,标题加粗/内容加粗/两个按钮变色
     */
    private TextView tv_dialog_green;
    /**
     * 仿ios中间弹框,标题/内容/一个按钮
     */
    private TextView tv_dialog_cyan;
    /**
     * 仿ios中间弹框,标题/内容/无按钮
     */
    private TextView tv_dialog_blue;
    /**
     * 底部弹框,标题/内容/两个按钮
     */
    private TextView tv_dialog_purple;

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
        return R.layout.activity_user_apply_untie;
    }


    @Override
    protected void prepareData() {

    }

    @Override
    protected void initView() {
//        Toast.makeText(this, getIntent().getStringExtra("UntieBannedName"), Toast.LENGTH_LONG).show();
        tv_dialog_red = (TextView) findViewById(R.id.tv_dialog_red);
        tv_dialog_red.setOnClickListener(this);
        tv_dialog_orange = (TextView) findViewById(R.id.tv_dialog_orange);
        tv_dialog_orange.setOnClickListener(this);
        tv_dialog_yellow = (TextView) findViewById(R.id.tv_dialog_yellow);
        tv_dialog_yellow.setOnClickListener(this);
        tv_dialog_green = (TextView) findViewById(R.id.tv_dialog_green);
        tv_dialog_green.setOnClickListener(this);
        tv_dialog_cyan = (TextView) findViewById(R.id.tv_dialog_cyan);
        tv_dialog_cyan.setOnClickListener(this);
        tv_dialog_blue = (TextView) findViewById(R.id.tv_dialog_blue);
        tv_dialog_blue.setOnClickListener(this);
        tv_dialog_purple = (TextView) findViewById(R.id.tv_dialog_purple);
        tv_dialog_purple.setOnClickListener(this);
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

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
            case R.id.tv_dialog_red:
                new CustomAlertDialog(this)
                        .builder()
                        .setCancelable(true)
                        .setTitle("标题")
                        .setMsg("仿ios中间弹框,标题/内容/两个按钮")
                        .setOkButton("确定", 0, "", "", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                            }
                        })
                        .setCancleButton("取消", 0, "", "", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                            }
                        })
                        .show();
                break;
            case R.id.tv_dialog_orange:
                new CustomAlertDialog(this)
                        .builder()
                        .setCancelable(true)
//                        .setTitle("标题")
                        .setMsg("仿ios中间弹框,内容/两个按钮")
                        .setOkButton("确定", 0, "", "", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                            }
                        })
                        .setCancleButton("取消", 0, "", "", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                            }
                        })
                        .show();
                break;
            case R.id.tv_dialog_yellow:
                new CustomAlertDialog(this)
                        .builder()
                        .setCancelable(true)
                        .setTitle("仿ios中间弹框,标题/两个按钮")
//                        .setMsg("仿ios中间弹框,标题/两个按钮")
                        .setOkButton("确定", 0, "", "", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                            }
                        })
                        .setCancleButton("取消", 0, "", "", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                            }
                        })
                        .show();
                break;
            case R.id.tv_dialog_green:
                new CustomAlertDialog(this)
                        .builder()
                        .setCancelable(true)
                        .setTitle("标题加粗")
                        .setTitleTextBold(true)
                        .setMsg("仿ios中间弹框,标题加粗/内容加粗/两个按钮变色")
                        .setMsgTextBold(true)
                        .setOkButton("确定", 0, "#ffffff", "#F48F4A", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                            }
                        })
                        .setCancleButton("取消", 0, "#ffffff", "#fade0a", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                            }
                        })
                        .show();
                break;
            case R.id.tv_dialog_cyan:
                new CustomAlertDialog(this)
                        .builder()
                        .setCancelable(true)
//                        .setTitle("标题加粗")
                        .setTitleTextBold(true)
                        .setMsg("仿ios中间弹框,标题/内容/一个按钮")
//                        .setMsgTextBold(true)
                        .setOkButton("确定", 0, "", "", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                            }
                        })
                        /*.setCancleButton("取消", 0, "", "", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                            }
                        })*/
                        .show();
                break;
            case R.id.tv_dialog_blue:
                new CustomAlertDialog(this)
                        .builder()
                        .setCancelable(true)
                        .setTitle("标题加粗")
//                        .setTitleTextBold(true)
                        .setMsg("仿ios中间弹框,标题/内容/无按钮")
//                        .setMsgTextBold(true)
                        /*.setOkButton("确定", 0, "", "", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                            }
                        })*/
                        /*.setCancleButton("取消", 0, "", "", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                            }
                        })*/
                        .show();
                break;
            case R.id.tv_dialog_purple:
                new CustomAlertDialog(this)
                        .builderBottom()
                        .setCancelable(true)
                        .setTitle("标题加粗")
//                        .setTitleTextBold(true)
                        .setMsg("底部弹框,标题/内容/两个按钮,可以和中间弹窗一样设置属性")
//                        .setMsgTextBold(true)
                        .setOkButton("确定", 0, "", "", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                            }
                        })
                        .setCancleButton("取消", 0, "", "", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                            }
                        })
                        .show();
                break;
        }
    }
}
