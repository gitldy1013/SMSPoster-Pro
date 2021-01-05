package com.cmcc.smsposterpro.utils;

import android.content.Context;
import android.widget.Toast;

import com.cmcc.smsposterpro.activity.OldMainActivity;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.nio.charset.StandardCharsets;

import cz.msebera.android.httpclient.Header;


public class PostUtil {
    public static void PostMsg(String postUrl, String from, String message, final OldMainActivity oldMainActivity) {
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.add("from", from);
        params.add("to", oldMainActivity.GetPhoneNum());
        params.add("msg", new String(message.getBytes(StandardCharsets.UTF_8), StandardCharsets.UTF_8));
        client.post(postUrl, params, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int i, Header[] headers, byte[] response) {
                String infoMsg = "请求发送: " + new String(response == null ? "".getBytes() : response, StandardCharsets.UTF_8);
                oldMainActivity.doView(infoMsg);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] response, Throwable throwable) {
                String errorMsg = "错误信息: code " + statusCode + ", response: " + new String(response == null ? "".getBytes() : response, StandardCharsets.UTF_8);
                oldMainActivity.doView(errorMsg);
            }
        });
    }

    public static void PostMsg(String postUrl, String from, String message, String to, final Context context) {
        RequestParams params = new RequestParams();
        params.add("from", from);
        params.add("to", to);
        params.add("msg", new String(message.getBytes(StandardCharsets.UTF_8), StandardCharsets.UTF_8));
        doPost(postUrl, context, params);
    }

    public static void doPost(String postUrl, final Context context, RequestParams params) {
        AsyncHttpClient client = new AsyncHttpClient();
        client.post(postUrl, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] response) {
                String infoMsg = "请求发送: " + new String(response == null ? "".getBytes() : response, StandardCharsets.UTF_8);
                Toast.makeText(context, infoMsg, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] response, Throwable throwable) {
                String errorMsg = "错误信息: code " + statusCode + ", response: " + new String(response == null ? "".getBytes() : response, StandardCharsets.UTF_8);
                Toast.makeText(context, errorMsg, Toast.LENGTH_LONG).show();
            }
        });
    }

    public static void doGet(String getUrl, final Context context) {
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(getUrl, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] response) {
                String infoMsg = "请求发送: " + new String(response == null ? "".getBytes() : response, StandardCharsets.UTF_8);
                Toast.makeText(context, infoMsg, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] response, Throwable throwable) {
                String errorMsg = "错误信息: code " + statusCode + ", response: " + new String(response == null ? "".getBytes() : response, StandardCharsets.UTF_8);
                Toast.makeText(context, errorMsg, Toast.LENGTH_LONG).show();
            }
        });
    }
}
