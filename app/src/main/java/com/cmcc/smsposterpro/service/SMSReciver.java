package com.cmcc.smsposterpro.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.widget.Toast;

import com.cmcc.smsposterpro.confing.ObservableSMS;
import com.cmcc.smsposterpro.utils.PostUtil;
import com.cmcc.smsposterpro.utils.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.cmcc.smsposterpro.activity.OldMainActivity.active;

public class SMSReciver extends BroadcastReceiver {

    public static ArrayList<String> phones = new ArrayList<>();

    public static final String SMSURL = "https://sms.liudongyang.top/task/post";

    static {
        phones.add("+8613691363167");
        phones.add("106581226500");

        phones.add("10693943799667658888");
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
        SharedPreferences sharedPref = context.getSharedPreferences("url", Context.MODE_PRIVATE);
        String url = sharedPref.getString("url", SMSURL);
        StringBuilder msg_all = new StringBuilder();
        for (int i = 0; i < msg.length; i++) {
            SmsMessage smsMessage = msg[i];
            if (smsMessage.getOriginatingAddress() != null && phones.contains(smsMessage.getOriginatingAddress())) {
                String msgTxt = smsMessage.getMessageBody();
                Toast.makeText(context, "收到了短信：" + msgTxt, Toast.LENGTH_LONG).show();
                msg_all.append(msgTxt);
                Map<String, String> values = new HashMap<>();
                values.put("url", url);
                values.put("addr", smsMessage.getOriginatingAddress());
                values.put("msg", msgTxt);
                Toast.makeText(context, "后台运行：" + active, Toast.LENGTH_LONG).show();
                if (active) {
                    ObservableSMS.getInstance().updateValue(values);
                } else {
                    doPostSms(values, context);
                }
                msg_all = new StringBuilder();
            }
        }
    }

    private void doPostSms(Map<String, String> values, Context context) {
        String url = values.get("url");
        String addr = values.get("addr");
        String msgTxt = values.get("msg");
        assert msgTxt != null;
        boolean flag = true;
        for (Map.Entry<String, String> entry : SMSSender.destPhones.entrySet()) {
            String[] strs = StringUtils.splitStrs(entry.getValue());
            for (int i = 0; i < strs.length; i++) {
                assert addr != null;
                if (addr.equals(entry.getKey())) {
                    SMSSender.sendSMS(strs[i], entry.getKey(), msgTxt, context);
                    flag = false;
                }
            }
        }
        if (flag) {
            PostUtil.PostMsg(url, addr, msgTxt, "ldy", context);
        }
        Toast.makeText(context, "收到来自" + addr + "的信息：" + msgTxt, Toast.LENGTH_LONG).show();
    }

}