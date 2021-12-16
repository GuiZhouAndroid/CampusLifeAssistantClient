package work.lpssfxy.www.campuslifeassistantclient.view.activity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

import butterknife.BindView;
import butterknife.OnClick;
import work.lpssfxy.www.campuslifeassistantclient.R;
import work.lpssfxy.www.campuslifeassistantclient.R2;
import work.lpssfxy.www.campuslifeassistantclient.base.dialog.AlertDialog;
import work.lpssfxy.www.campuslifeassistantclient.view.BaseActivity;

/**
 * created by on 2021/12/16
 * 描述：浏览器进入官网
 *
 * @author ZSAndroid
 * @create 2021-12-16-12:25
 */

@SuppressLint("NonConstantResourceId")
public class WebViewActivity extends BaseActivity {

    /** 返回按钮 */
    @BindView(R2.id.iv_web_view_back) ImageView mIvWebView;
    /** Toolbar */
    @BindView(R2.id.toolbar_web_view) Toolbar mToolbarWebView;
    /** 网站标题 */
    @BindView(R2.id.tv_web_view_title) TextView mTvWebViewTitle;
    /** 进度条 */
    @BindView(R2.id.progress_web_view) ProgressBar mProgressWebView;
    /** 浏览器容器 */
    @BindView(R2.id.web_view) WebView mWebView;
    /** 浏览器设置 */
    private WebSettings mWebSettings;
    /** 返回对话框 */
    private AlertDialog mDialog;

    @Override
    protected Boolean isSetSwipeBackLayout() {
        return false;
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
        return R.layout.activity_webview;
    }

    @Override
    protected void prepareData() {
        mWebSettings = mWebView.getSettings();
        mWebView.loadUrl("https://www.lpssy.edu.cn/");
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
        //设置不用系统浏览器打开,直接显示在当前Webview
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });

        //设置WebChromeClient类
        mWebView.setWebChromeClient(new WebChromeClient() {

            //获取网站标题
            @SuppressLint("SetTextI18n")
            @Override
            public void onReceivedTitle(WebView view, String title) {
                if (title.length()>15){
                    mTvWebViewTitle.setText(title.substring(0,10)+"...");
                }
            }

            //获取加载进度
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress < 100) {
                    mProgressWebView.setProgress(newProgress);
                } else if (newProgress == 100) {
                    mProgressWebView.setProgress(newProgress);
                }
            }
        });


        //设置WebViewClient类
        mWebView.setWebViewClient(new WebViewClient() {
            //设置加载前的函数
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                mProgressWebView.setVisibility(View.VISIBLE);
            }

            //设置结束加载函数
            @Override
            public void onPageFinished(WebView view, String url) {
                mProgressWebView.setVisibility(View.GONE);
            }
        });
    }

    /**
     * 使用 ButterKnife注解式监听单击事件
     *
     * @param view 控件Id
     */
    @OnClick({R2.id.iv_web_view_back})
    public void onWebViewViewClick(View view) {
        switch (view.getId()) {
            case R.id.iv_web_view_back://点击返回
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("温馨提示")
                        .setMessage("确定直接返回主页吗？")
                        .setTopImage(R.drawable.icon_tanchuang_wenhao)
                        .setNegativeButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                WebViewActivity.this.finish();
                            }
                        })
                        .setPositiveButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mDialog.dismiss();
                            }
                        });
                mDialog = builder.create();
                mDialog.show();
                break;
        }
    }

    //点击返回上一页面而不是退出浏览器
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && mWebView.canGoBack()) {
            mWebView.goBack();
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

    //销毁Webview
    @Override
    protected void onDestroy() {
        if (mWebView != null) {
            mWebView.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
            mWebView.clearHistory();

            ((ViewGroup) mWebView.getParent()).removeView(mWebView);
            mWebView.destroy();
            mWebView = null;
        }
        super.onDestroy();
    }
}
