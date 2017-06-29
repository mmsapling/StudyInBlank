package com.example.studyinblank.example;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.Button;

import com.example.studyinblank.R;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

import framework.app.BaseFragment;
import framework.utils.LogUtils;
import framework.utils.ToastUtils;

/**
 * Created by cxw on 2017/5/11.
 *
 * @创建者: xuanwen
 * @创建日期: 2017/5/11
 * @描述: TODO
 */
public class HtmlDemoFragment extends BaseFragment{
    private WebView mWebView;
    private Button mBtnJavaCallHtml;
    public HtmlDemoFragment(){
        initTitleBar("HTML",true,false);
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = View.inflate(getActivity(), R.layout.fra_html_demo, null);
        mWebView = (WebView) view.findViewById(R.id.webview_test);
        mBtnJavaCallHtml = (Button) view.findViewById(R.id.btn_js);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mWebView.getSettings().setJavaScriptEnabled(true);

        mWebView.setWebChromeClient(mClient);
        //js调用本地代码
       mWebView.addJavascriptInterface(new DemoObject(),"jsObj");
        mWebView.loadUrl("file:///android_asset/demo.html");
        final Map  map = new HashMap();
        map.put("test1","1");
        map.put("test2","2");
        Gson gson = new Gson();
        final String s = gson.toJson(map);
        mBtnJavaCallHtml.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //mWebView.loadUrl("javascript: JavaCallHtml(' "+s+" ')");
                mWebView.loadUrl("javascript: JavaCallHtml(' "+s+" ')");
            }
        });
    }

    class DemoObject extends Object{
        @JavascriptInterface
        public void javaCallHtml(String map){
            test(map);
           // show(map.get("test1").toString());
        }
    }

    private void test(String map) {
        LogUtils.debug("map aaa" + map.toString());
    }

    private void show(String n){
        ToastUtils.showToast(n);
    }
    private void show(){
        ToastUtils.showToast("");
    }
    private WebChromeClient  mClient = new WebChromeClient(){
        @Override
        public boolean onJsAlert(WebView view, String url, String message, JsResult result) {

            return super.onJsAlert(view, url, message, result);
        }
    };
}
