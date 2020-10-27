package com.cmcc.smsposterpro;

import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.nio.charset.StandardCharsets;

import cz.msebera.android.httpclient.Header;


public class PostUtil {
    public static void PostMsg(String postUrl, String from, String message, final MainActivity mainActivity) {
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.add("from", from);
        params.add("to", mainActivity.GetPhoneNum());
        params.add("msg", new String(message.getBytes(StandardCharsets.UTF_8), StandardCharsets.UTF_8));
        client.post(postUrl, params, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int i, Header[] headers, byte[] response) {
                String infoMsg = "Request sent, response: " + new String(response == null ? "".getBytes() : response, StandardCharsets.UTF_8);
                Log.i("HTTP_REQUEST_OK", infoMsg);
                mainActivity.doView("HTTP_REQUEST_OK:" + infoMsg);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] response, Throwable throwable) {
                String errorMsg = "Request error : code " + statusCode + ", response: " + new String(response == null ? "".getBytes() : response, StandardCharsets.UTF_8);
                Log.e("HTTP_REQUEST_ERROR", errorMsg);
                mainActivity.doView("HTTP_REQUEST_ERROR:" + errorMsg);
            }
        });
    }
}
