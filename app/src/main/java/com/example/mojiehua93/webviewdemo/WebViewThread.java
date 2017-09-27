package com.example.mojiehua93.webviewdemo;

import android.os.Handler;
import android.webkit.WebView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by MOJIEHUA93 on 2017/9/3.
 */

public class WebViewThread extends Thread {
    private WebView webView;
    private String url;
    private Handler handler;

    public WebViewThread(WebView webView, String url, Handler handler){
        this.webView = webView;
        this.url = url;
        this.handler = handler;
    }

    @Override
    public void run() {
        URL httpUrl = null;
        try {
            httpUrl = new URL(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        HttpsURLConnection connection = null;
        try {
            connection = (HttpsURLConnection) httpUrl.openConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }
        connection.setReadTimeout(5000);
        try {
            connection.setRequestMethod("GET");
        } catch (ProtocolException e) {
            e.printStackTrace();
        }
        try {
            connection.connect();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            final StringBuffer buffer = new StringBuffer();
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String str = null;
            while ((str = reader.readLine()) != null){
                buffer.append(str);
            }
            handler.post(new Runnable() {
                @Override
                public void run() {
                    webView.loadData(buffer.toString(), "text/html;charset=utf-8", null);
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
