package com.cmcc.smsposterpro;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.widget.Toast;

import java.util.ArrayList;

public class SMSReciver extends BroadcastReceiver {

    public static ArrayList<String> phones = new ArrayList<>();

    static {
        phones.add("+8613691363167");
        phones.add("106581226500");
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        SmsMessage[] msg = null;
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            Object[] pdusObj = (Object[]) bundle.get("pdus");
            assert pdusObj != null;
            msg = new SmsMessage[pdusObj.length];
            String format = bundle.getString("format");
            for (int i = 0; i < pdusObj.length; i++) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    msg[i] = SmsMessage.createFromPdu((byte[]) pdusObj[i], format);
                } else {
                    msg[i] = SmsMessage.createFromPdu((byte[]) pdusObj[i]);
                }
            }
        }
        assert msg != null;
        for (SmsMessage smsMessage : msg) {
            if (smsMessage.getOriginatingAddress() != null && phones.contains(smsMessage.getOriginatingAddress())) {
                String msgTxt = smsMessage.getMessageBody();
                Toast.makeText(context, "收到了短信：" + msgTxt, Toast.LENGTH_LONG).show();
                SharedPreferences sharedPref = context.getSharedPreferences("url", Context.MODE_PRIVATE);
                String url = sharedPref.getString("url", "http://leg.liudongyang.top:8080/task/post");
                PostUtil.PostMsg(url, smsMessage.getOriginatingAddress(), msgTxt);
            }
        }
    }

}
