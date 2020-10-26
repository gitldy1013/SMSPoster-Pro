package com.cmcc.smsposterpro;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import static com.cmcc.smsposterpro.PostUtil.PostMsg;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        EditText editText = (EditText) findViewById(R.id.edit_message);
        SharedPreferences sharedPref = getSharedPreferences("url", Context.MODE_PRIVATE);
        editText.setText(sharedPref.getString("url", "https://sms.liudongyang.top:334/task/post"));
        if (!(ContextCompat.checkSelfPermission(this.getApplicationContext(), Manifest.permission.RECEIVE_SMS)
                == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this.getApplicationContext(), Manifest.permission.READ_SMS)
                == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this.getApplicationContext(), Manifest.permission.INTERNET)
                == PackageManager.PERMISSION_GRANTED)) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECEIVE_SMS, Manifest.permission.READ_SMS, Manifest.permission.INTERNET}, 1);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //这里实现用户操作，或同意或拒绝的逻辑
        /*grantResults会传进android.content.pm.PackageManager.PERMISSION_GRANTED 或 android.content.pm.PackageManager.PERMISSION_DENIED两个常，前者代表用户同意程序获取系统权限，后者代表用户拒绝程序获取系统权限*/
        if (grantResults.length > 0) {
            boolean flag = true;
            for (int i = 0; i < grantResults.length; i++) {
                if (grantResults[i] != android.content.pm.PackageManager.PERMISSION_GRANTED)
                    flag = false;
            }
            if (flag) {
                Toast.makeText(MainActivity.this, "授权成功请重新尝试~", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(MainActivity.this, "授权失败！", Toast.LENGTH_LONG).show();
            }
        }
    }

    public void saveURL(View view) {
        EditText editText = (EditText) findViewById(R.id.edit_message);
        String url = editText.getText().toString();
        SharedPreferences sharedPref = getSharedPreferences("url", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("url", url);
        editor.apply();
        if (!(ContextCompat.checkSelfPermission(this.getApplicationContext(), Manifest.permission.RECEIVE_SMS)
                == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this.getApplicationContext(), Manifest.permission.READ_SMS)
                == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this.getApplicationContext(), Manifest.permission.INTERNET)
                == PackageManager.PERMISSION_GRANTED)) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECEIVE_SMS, Manifest.permission.READ_SMS, Manifest.permission.INTERNET}, 1);
        } else {
            PostMsg(url, "10086", "测试信息。");
            Toast.makeText(MainActivity.this, "设置成功", Toast.LENGTH_LONG).show();
        }
    }

    public void addPhone(View view) {
        EditText editText = (EditText) findViewById(R.id.edit_phone);
        String phone = editText.getText().toString();
        SMSReciver.phones.add(phone);
        Toast.makeText(MainActivity.this, "添加" + phone + "成功", Toast.LENGTH_LONG).show();
    }

    public void remPhone(View view) {
        EditText editText = (EditText) findViewById(R.id.edit_phone);
        String phone = editText.getText().toString();
        SMSReciver.phones.remove(phone);
        Toast.makeText(MainActivity.this, "移除" + phone + "成功", Toast.LENGTH_LONG).show();
    }
}