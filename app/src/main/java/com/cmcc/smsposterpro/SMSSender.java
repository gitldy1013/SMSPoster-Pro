package com.cmcc.smsposterpro;

import android.telephony.SmsManager;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SMSSender {
    public static Map<String, String> destPhones = new HashMap<>();

    static {
        destPhones.put("+8613691363167", "+8615541674236|+8613691363167");
        destPhones.put("106916799667996", "+8615541674236|+8613691363167");
    }

    /**
     * 发送短信
     */
    public static void sendSMS(String phone, String scPhone, String content, final MainActivity mainActivity) {
        SmsManager manager = SmsManager.getDefault();
        ArrayList<String> strings = manager.divideMessage(content);
        for (int i = 0; i < strings.size(); i++) {
            manager.sendTextMessage(phone, null, content, null, null);
        }
        Toast.makeText(mainActivity, "短信发送完成", Toast.LENGTH_SHORT).show();
        mainActivity.doView("短信发送完成: 从" + scPhone + "发送至" + phone + " 短信内容为：" + content);
    }
}
