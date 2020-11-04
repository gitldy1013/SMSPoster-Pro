package com.cmcc.smsposterpro;

import android.app.PendingIntent;
import android.content.Intent;
import android.telephony.SmsManager;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

public class SMSSender {
    //获取 SMSManger管理器
    static final SmsManager smsManager = SmsManager.getDefault();

    public static Map<String, String> destPhones = new HashMap<>();

    {
        destPhones.put("8613691363167", "+8615541674236|+8613691363167");
        destPhones.put("106916799667996", "+8615541674236|+8613691363167");
    }

    /**
     * 发送短信
     */
    public static void sendSMS(String phone, String scPhone, String content, final MainActivity mainActivity) {
        //创建一个 android.app.PendingIntent 对象
        PendingIntent pi = PendingIntent.getActivity(mainActivity, 0, new Intent(), 0);
        //发送短信
        smsManager.sendTextMessage(phone, scPhone, content, pi, null);
        //提示短信发送完成
        Toast.makeText(mainActivity, "短信发送完成", Toast.LENGTH_SHORT).show();
    }
}
