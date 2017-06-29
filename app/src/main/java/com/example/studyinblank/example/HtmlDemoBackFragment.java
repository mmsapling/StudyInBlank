package com.example.studyinblank.example;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebBackForwardList;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

import com.example.studyinblank.R;

import framework.app.BaseFragment;
import framework.utils.LogUtils;

/**
 * Created by cxw on 2017/5/11.
 *
 * @创建者: xuanwen
 * @创建日期: 2017/5/11
 * @描述: TODO
 */
public class HtmlDemoBackFragment extends BaseFragment{
    private WebView mWebView;
    private Button mBtnJavaCallHtml;
    public HtmlDemoBackFragment(){
        initTitleBar("HTML",true,false);
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = View.inflate(getActivity(), R.layout.fra_html_demo, null);
        mWebView = (WebView) view.findViewById(R.id.webview_test);
        mBtnJavaCallHtml = (Button) view.findViewById(R.id.btn_js);
        mBtnJavaCallHtml.setVisibility(View.GONE);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mWebView.getSettings().setJavaScriptEnabled(true);
        Client client = new Client();

        mWebView.setWebViewClient(client);
        //js调用本地代码
       mWebView.addJavascriptInterface(new DemoObject(),"jsObj");
        mWebView.loadUrl("file:///android_asset/a.html");

    }
   class Client extends WebViewClient {
       @Override
       public boolean shouldOverrideUrlLoading(WebView view, String url) {
           view.loadUrl(url);
          return true;// return super.shouldOverrideUrlLoading(view, url);
       }
   }
    class DemoObject extends Object {
        @JavascriptInterface
        public void goBack() {
            onBackPressed();
        }
        @JavascriptInterface
        public void showHistoryUrl() {
            showHistoryUrl1();
        }
    }

    public void onBackPressed() {

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(mWebView.canGoBack()){
                    mWebView.goBack();
                }
//                if(mWebView.canGoBack()){
//                    //获取历史列表
//                    WebBackForwardList webBackForwardList = mWebView.copyBackForwardList();
//                    //判断当前历史列表是否最顶端，其实canGoBack已经判断过
//                    if(webBackForwardList.getCurrentIndex() > 0){
//                        //获取历史列表
//                        String historyUrl = webBackForwardList.getItemAtIndex(webBackForwardList.getCurrentIndex() - 1).getUrl();
//                        LogUtils.debug("url = " + historyUrl);
//                        //按照自己的规则检查是否为可跳转地址
//                        mWebView.goBack();
//                    }else{
//                        ToastUtils.showToast("hahah, success!");
//                    }
//                }else{
//                    ToastUtils.showToast("hahah, 11111!");
//                }
            }
        });

    }
    public void showHistoryUrl1(){
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                WebBackForwardList webBackForwardList1 = mWebView.copyBackForwardList();
                int size = webBackForwardList1.getSize();
                int temp = size;
                while(temp > 0){
                    temp--;
                    LogUtils.debug(webBackForwardList1.getItemAtIndex(temp).getUrl());
                }
            }
        });

    }
}
