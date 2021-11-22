package work.lpssfxy.www.campuslifeassistantclient.utils;

import android.app.Activity;
import android.content.DialogInterface;

import work.lpssfxy.www.campuslifeassistantclient.R;
import work.lpssfxy.www.campuslifeassistantclient.base.dialog.AlertDialog;
import work.lpssfxy.www.campuslifeassistantclient.base.dialog.CustomDialog;

/**
 * created by on 2021/11/22
 * 描述：自定义对话框工具类
 *
 * @author ZSAndroid
 * @create 2021-11-22-19:16
 */
public class CustomAlertDialogUtil {

    /**
     * 自定义对话框
     */
    private static AlertDialog mDialog;

    //一个按钮提示框，本方法只适用于单击Positive，消失窗口，无业务逻辑，如需另外执行，请Copy方法体，自定义onClick事件
    public static void notification1(Activity activity, String strTitle, String strMessage, String strPositiveText) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle(strTitle)//这里设置标题
                .setMessage(strMessage)//这里设置提示信息
                .setTopImage(R.drawable.icon_tanchuang_tanhao)//这里设置顶部图标
                .setPositiveButton(strPositiveText, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mDialog.dismiss();
                    }
                });
        mDialog = builder.create();
        mDialog.show();
    }

    //自定义的弹窗（两个按钮的选择框），本方法只适用于单击Positive，消失窗口，无业务逻辑，如需另外执行，请Copy方法体，自定义onClick事件
    public void notification2(Activity activity, String strTitle, String strMessage, String strPositiveText, String strNegativeText) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle(strTitle)
                .setMessage(strMessage)
                .setTopImage(R.drawable.icon_tanchuang_wenhao)
                .setNegativeButton(strPositiveText, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mDialog.dismiss();
                    }
                })
                .setPositiveButton(strNegativeText, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mDialog.dismiss();
                    }
                });
        mDialog = builder.create();
        mDialog.show();
    }

    //没有提示信息，没有顶部图标
    public void notification3(Activity activity, String strTitle, String strPositiveText, String strNegativeText) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle(strTitle)
                .setPositiveButton(strPositiveText, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mDialog.dismiss();
                    }
                })
                .setNegativeButton(strNegativeText, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mDialog.dismiss();
                    }
                });
        mDialog = builder.create();
        mDialog.show();
    }

    //自定义的弹窗（鲜艳版）
    public static void notification4(Activity activity, String strTitle, String strMessage, String strPositiveText, String strNegativeText) {
        CustomDialog.Builder builder = new CustomDialog.Builder(activity);
        builder.setTitle(strTitle);
        builder.setMessage(strMessage);
        builder.setPositiveButton(strPositiveText, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                //设置你的操作事项
                //列如：Toast.makeText(activity,"queding",Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton(strNegativeText,
                new android.content.DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        builder.create().show();
    }
}
