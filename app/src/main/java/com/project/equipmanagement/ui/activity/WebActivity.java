package com.project.equipmanagement.ui.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.DownloadListener;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.project.equipmanagement.R;
import com.project.equipmanagement.constant.Constants;
import com.project.equipmanagement.ui.view.TopBarView;
import com.project.equipmanagement.utils.NetUtils;

import butterknife.Bind;
import butterknife.ButterKnife;

public class WebActivity extends AppCompatActivity implements TopBarView.onTitleBarClickListener {

    @Bind(R.id.top_bar_web_page)
    TopBarView topBarWebPage;
    @Bind(R.id.progressbar)
    ProgressBar progressbar;
    @Bind(R.id.webView)
    WebView webView;
    @Bind(R.id.rootView)
    LinearLayout rootView;

    //标题
    private String titleContent;
    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        ButterKnife.bind(this);

        initIntent();
        initTitle();
        initWebview();

        topBarWebPage.setClickListener(this);
    }

    private void initIntent() {
        Intent intent = getIntent();
        titleContent = intent.getStringExtra(Constants.INTENT_TITLE_WEB_PAGE);
        url = intent.getStringExtra(Constants.INTENT_URL_WEB_PAGE);
    }

    private void initTitle() {
        topBarWebPage.setTitle(titleContent);
    }

    @JavascriptInterface
    private void initWebview() {
        {
            //设置背景色
            webView.setBackgroundColor(0);
            //设置WebView属性，能够执行Javascript脚本
            webView.getSettings().setJavaScriptEnabled(true);
            webView.getSettings().setAllowFileAccess(true);
            // 开启DOM storage API 功能
            webView.getSettings().setDomStorageEnabled(true);
            // 开启database storage API功能
            webView.getSettings().setDatabaseEnabled(true);
            webView.getSettings().setDefaultTextEncodingName("UTF-8");
            //自适应屏幕
            webView.getSettings().setUseWideViewPort(true);
            webView.getSettings().setLoadWithOverviewMode(true);
            // 设置可以支持缩放
            webView.getSettings().setSupportZoom(true);
            webView.getSettings().setBuiltInZoomControls(true);
            //不显示webview缩放按钮
            webView.getSettings().setDisplayZoomControls(false);
            //设置缩放比例：最小25
            webView.setInitialScale(100);
            // 建议缓存策略为，判断是否有网络，有的话，使用LOAD_DEFAULT,无网络时，使用LOAD_CACHE_ELSE_NETWORK
            if (NetUtils.hasNetWorkConection(this)) {
                webView.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);   // 根据cache-control决定是否从网络上取数据。
            } else {
                webView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);   //优先加载缓存
            }

            //////////////////////////////
            webView.loadUrl(url);
            //设置了默认在本应用打开，不设置会用浏览器打开的
            webView.setWebViewClient(new WebViewClient() {
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    //返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
                    view.loadUrl(url);
                    return true;
                }

                @Override
                public void onPageFinished(WebView view, String url) {
                    super.onPageFinished(view, url);
                    //设置webView
                }

                @Override
                public void onPageStarted(WebView view, String url, Bitmap favicon) {
                    super.onPageStarted(view, url, favicon);
                    webView.setVisibility(View.VISIBLE);
                }

                @Override
                public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                    super.onReceivedError(view, request, error);
                }
            });

            webView.setWebChromeClient(new WebChromeClient() {
                @Override
                public void onProgressChanged(WebView view, int newProgress) {
                    if (newProgress == 100) {
                        // 网页加载完成
                        progressbar.setVisibility(View.GONE);
                    } else {
                        // 加载中
                        if (progressbar.getVisibility() == View.GONE) {
                            progressbar.setVisibility(View.VISIBLE);
                        }
                        progressbar.setProgress(newProgress);
                    }
                }
            });

            webView.setDownloadListener(new MyWebViewDownLoadListener());
        }
    }

    private class MyWebViewDownLoadListener implements DownloadListener {
        @Override
        public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype,
                                    long contentLength) {
            Uri uri = Uri.parse(url);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        }

    }

    @Override
    public void onBackClick() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            this.finish();
        }
    }

    @Override
    public void onRightClick() {

    }

    @Override
    public void onCloseClick() {
        this.finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        webView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        webView.onPause();
        //保证了webView退出后不再有声音
        webView.reload();
    }


    @Override
    protected void onDestroy() {
        if (webView != null) {
            webView.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
            rootView.removeView(webView);
            webView.destroy();
        }
        super.onDestroy();
    }
}
