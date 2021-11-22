package work.lpssfxy.www.campuslifeassistantclient.base.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import work.lpssfxy.www.campuslifeassistantclient.R;

/**
 * created by on 2021/11/5
 * 描述：提示对话框(可以自定义布局)
 *  1，两个选项，顶部是问号图标
 *  2，单个选项
 *
 * @author ZSAndroid
 * @create 2021-11-05-22:02
 */
public class AlertDialog extends Dialog {

    public AlertDialog(Context context) {
        super(context);
    }

    public AlertDialog(Context context, int theme) {
        super(context, theme);
    }

    /**
     * 建造者类
     *
     * @author Administrator
     */
    public static class Builder {
        private Context context;
        private int topImageId;
        private String title;
        private String message;
        private Drawable drawable = null;
        private String positiveButtonText;
        private String negativeButtonText;
        private View contentView;
        private DialogInterface.OnClickListener positiveButtonClickListener;
        private DialogInterface.OnClickListener negativeButtonClickListener;

        public Builder(Context context) {
            this.context = context;
        }

        /**
         * 设置顶部图标
         */
        public Builder setTopImage(int id) {
            this.topImageId = id;
            return this;
        }

        /**
         * 设置消息内容
         *
         * @param title
         * @return
         */
        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        /**
         * 设置消息内容
         *
         * @param message
         * @return
         */
        public Builder setMessage(String message) {
            this.message = message;
            return this;
        }

        public Builder setDrawable(Drawable drawable_id) {
            this.drawable = drawable_id;
            return this;
        }


        public Builder setContentView(View v) {
            this.contentView = v;
            return this;
        }

        /**
         * 设置积极按钮
         *
         * @param positiveButtonText
         * @param listener
         * @return
         */
        public Builder setPositiveButton(String positiveButtonText,
                                         DialogInterface.OnClickListener listener) {
            this.positiveButtonText = positiveButtonText;
            this.positiveButtonClickListener = listener;
            return this;
        }

        /**
         * 设置消极按钮
         *
         * @param negativeButtonText
         * @param listener
         * @return
         */
        public Builder setNegativeButton(String negativeButtonText,
                                         DialogInterface.OnClickListener listener) {
            this.negativeButtonText = negativeButtonText;
            this.negativeButtonClickListener = listener;
            return this;
        }

        /**
         * 创建一个AlertDialog
         *
         * @return
         */
        public AlertDialog create() {
            LayoutInflater inflater = LayoutInflater.from(context);

            final AlertDialog dialog = new AlertDialog(context,
                    R.style.DialogStyle);

            View layout = null;
            if (null != contentView) {
                layout = contentView;
            } else {
                layout = inflater.inflate(R.layout.dialog_alert_layout, null);
            }


            // 设置顶部图标
            ImageView top_image = (ImageView) layout.findViewById(R.id.top_image);
            if (0 == topImageId) {
                top_image.setVisibility(View.GONE);
            } else {
                top_image.setImageResource(topImageId);
            }

            // 设置标题
            TextView titleView = (TextView) layout.findViewById(R.id.title);
            if (null == title) {
                titleView.setVisibility(View.GONE);
            } else {
                if (drawable != null) {
                    titleView.setCompoundDrawables(drawable, null, null, null);
                }
                titleView.setText(title);
            }
            // 设置内容
            TextView messageView = (TextView) layout.findViewById(R.id.message);
            if (null == message) {
                messageView.setVisibility(View.GONE);
            } else {
                messageView.setText(message);
            }
            // 设置积极按钮
            RelativeLayout sure_layout = (RelativeLayout) layout
                    .findViewById(R.id.sure_layout);
            TextView sure_text = (TextView) layout
                    .findViewById(R.id.sure_text);
            if (TextUtils.isEmpty(positiveButtonText)
                    || null == positiveButtonClickListener) {
                sure_layout.setVisibility(View.GONE);
            } else {
                sure_text.setText(positiveButtonText);
                sure_layout.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        dialog.dismiss();
                        positiveButtonClickListener.onClick(dialog,
                                DialogInterface.BUTTON_POSITIVE);
                    }
                });
            }

            // 设置消极按钮
            RelativeLayout quit_layout = (RelativeLayout) layout
                    .findViewById(R.id.quit_layout);
            TextView quit_text = (TextView) layout
                    .findViewById(R.id.quit_text);
            if (TextUtils.isEmpty(negativeButtonText)
                    || null == negativeButtonClickListener) {
                quit_layout.setVisibility(View.GONE);
            } else {
                quit_text.setText(negativeButtonText);
                quit_layout.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        dialog.dismiss();
                        negativeButtonClickListener.onClick(dialog,
                                DialogInterface.BUTTON_NEGATIVE);
                    }
                });
            }

            if (null != positiveButtonText && null == negativeButtonText) {
                if (null == positiveButtonClickListener) {
                    sure_layout.setVisibility(View.VISIBLE);

                    sure_layout.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            // TODO Auto-generated method stub
                            dialog.dismiss();
                        }
                    });
                }
            }

            // 设置对话框的视图
            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            dialog.setContentView(layout, params);
            return dialog;
        }
    }

//    private AlertDialog mDialog;
//

//
//    //自定义的弹窗（两个按钮的选择框）
//    public void notification2() {
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setTitle("两个按钮的选择框")
//                .setMessage("选择可以自定义布局样式，有两个按钮")
//                .setTopImage(R.drawable.icon_tanchuang_wenhao)
//                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        mDialog.dismiss();
//                    }
//                })
//                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        mDialog.dismiss();
//                    }
//                });
//        mDialog = builder.create();
//        mDialog.show();
//    }
//
//    //自定义的弹窗（一个按钮没有顶部图标）
//    public void notification3() {
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setTitle("没有提示信息，没有顶部图标")
//                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        mDialog.dismiss();
//                    }
//                })
//                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        mDialog.dismiss();
//                    }
//                });
//        mDialog = builder.create();
//        mDialog.show();
//    }
}

