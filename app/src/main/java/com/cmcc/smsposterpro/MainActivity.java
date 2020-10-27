package com.cmcc.smsposterpro;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.Map;

import static com.cmcc.smsposterpro.PostUtil.PostMsg;

public class MainActivity extends AppCompatActivity implements SmsServer {

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        EditText editText = (EditText) findViewById(R.id.edit_message);
        SharedPreferences sharedPref = getSharedPreferences("url", Context.MODE_PRIVATE);
        editText.setText(sharedPref.getString("url", SMSReciver.SMSURL));
        getPermission();
        ObservableSMS.getInstance().addObserver(this);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private boolean getPermission() {
        boolean flag = ContextCompat.checkSelfPermission(this.getApplicationContext(), Manifest.permission.RECEIVE_SMS) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this.getApplicationContext(), Manifest.permission.READ_SMS) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this.getApplicationContext(), Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this.getApplicationContext(), Manifest.permission.READ_PHONE_NUMBERS) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this.getApplicationContext(), Manifest.permission.INTERNET) == PackageManager.PERMISSION_GRANTED;
        if (!flag) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECEIVE_SMS, Manifest.permission.READ_SMS, Manifest.permission.READ_PHONE_STATE, Manifest.permission.READ_PHONE_NUMBERS, Manifest.permission.INTERNET}, 1);
        }
        return flag;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0) {
            boolean flag = true;
            for (int grantResult : grantResults) {
                if (grantResult != PackageManager.PERMISSION_GRANTED) {
                    flag = false;
                    break;
                }
            }
            if (flag) {
                Toast.makeText(MainActivity.this, "授权成功!", Toast.LENGTH_LONG).show();
                doView("授权成功!");
            } else {
                Toast.makeText(MainActivity.this, "授权失败！", Toast.LENGTH_LONG).show();
                doView("授权失败!");
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void saveURL(View view) {
        EditText editText = (EditText) findViewById(R.id.edit_message);
        String url = editText.getText().toString();
        SharedPreferences sharedPref = getSharedPreferences("url", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("url", url);
        editor.apply();
        if (getPermission()) {
            PostMsg(url, "10086", "测试信息。", this);
            Toast.makeText(MainActivity.this, "设置成功", Toast.LENGTH_LONG).show();
            doView("设置成功!已发送测试信息：10086：测试信息。");
        } else {
            Toast.makeText(MainActivity.this, "需要先授权", Toast.LENGTH_LONG).show();
            doView("缺少必要权限！");
        }
    }

    public void addPhone(View view) {
        EditText editText = (EditText) findViewById(R.id.edit_phone);
        String phone = editText.getText().toString();
        SMSReciver.phones.add(phone);
        Toast.makeText(MainActivity.this, "添加" + phone + "成功", Toast.LENGTH_LONG).show();
        doView("添加" + phone + "成功!");
    }

    public void remPhone(View view) {
        EditText editText = (EditText) findViewById(R.id.edit_phone);
        String phone = editText.getText().toString();
        SMSReciver.phones.remove(phone);
        Toast.makeText(MainActivity.this, "移除" + phone + "成功", Toast.LENGTH_LONG).show();
        doView("移除" + phone + "成功!");
    }

    public void doView(String msg) {
        TextView textView = (TextView) findViewById(R.id.out_message);
        textView.setEnabled(true);
        textView.setMovementMethod(ScrollingMovementMethod.getInstance());
        textView.append(System.getProperty("line.separator") + msg);
    }

    public void clean(View view) {
        TextView textView = (TextView) findViewById(R.id.out_message);
        textView.setEnabled(true);
        textView.clearComposingText();
    }

    @Override
    public void update(ObservableSMS o, Map<String, String> values) {
        String url = values.get("url");
        String addr = values.get("addr");
        String msgTxt = values.get("msg");
        assert msgTxt != null;
        PostUtil.PostMsg(url, addr, msgTxt, this);
        doView("收到来自" + addr + "的信息：" + msgTxt);
    }

    @SuppressLint({"MissingPermission", "HardwareIds"})
    public String GetPhoneNum() {
        TelephonyManager tm = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
        return tm.getLine1Number().trim().length() == 0 ? tm.getSimOperatorName() : tm.getLine1Number();
    }

}