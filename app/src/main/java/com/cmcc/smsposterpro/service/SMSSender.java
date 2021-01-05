package com.cmcc.smsposterpro.service;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsManager;
import android.widget.Toast;

import com.cmcc.smsposterpro.activity.OldMainActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SMSSender {
    public static Map<String, String> destPhones = new HashMap<>();

    static {
        //从key接收的信息会发送到value号码
        destPhones.put("+8613691363167", "+8613691363167");
        destPhones.put("10693943799667658888", "+8615541674236");
    }

    /**
     * 发送短信
     */
    public static void sendSMS(String phone, String scPhone, String content, final OldMainActivity oldMainActivity) {
        SmsManager manager = SmsManager.getDefault();
        oldMainActivity.getIntent();
        PendingIntent sentIntent = PendingIntent.getBroadcast(oldMainActivity.getApplication().getApplicationContext(), 0,
                new Intent(), 0);
        if (content.length() > 70) {
            ArrayList<String> msgs = manager.divideMessage(content);
            ArrayList<PendingIntent> sentIntents = new ArrayList<PendingIntent>();
            for (int i = 0; i < msgs.size(); i++) {
                sentIntents.add(sentIntent);
            }
            manager.sendMultipartTextMessage(phone, null, msgs, sentIntents, null);
        } else {
            manager.sendTextMessage(phone, null, content, null, null);
        }
        Toast.makeText(oldMainActivity, "短信发送完成", Toast.LENGTH_SHORT).show();
        oldMainActivity.doView("短信发送完成: 从" + scPhone + "发送至" + phone + " 短信内容为：" + content);
    }

    public static void sendSMS(String phone, String scPhone, String content, Context context) {
        sendSMS(phone, content, context);
        Toast.makeText(context, "短信发送完成", Toast.LENGTH_SHORT).show();
    }

    private static void sendSMS(String phone, String content, Context context) {
        SmsManager manager = SmsManager.getDefault();
        PendingIntent sentIntent = PendingIntent.getBroadcast(context, 0,
                new Intent(), 0);
        if (content.length() > 70) {
            ArrayList<String> msgs = manager.divideMessage(content);
            ArrayList<PendingIntent> sentIntents = new ArrayList<PendingIntent>();
            for (int i = 0; i < msgs.size(); i++) {
                sentIntents.add(sentIntent);
            }
            manager.sendMultipartTextMessage(phone, null, msgs, sentIntents, null);
        } else {
            manager.sendTextMessage(phone, null, content, null, null);
        }
    }
}
