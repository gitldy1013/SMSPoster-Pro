package com.cmcc.smsposterpro;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import static com.cmcc.smsposterpro.PostUtil.PostMsg;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        EditText editText = (EditText) findViewById(R.id.edit_message);
        SharedPreferences sharedPref = getSharedPreferences("url", Context.MODE_PRIVATE);
        editText.setText(sharedPref.getString("url", "http://leg.liudongyang.top:8080/task/post"));
    }

    public void saveURL(View view) {
        EditText editText = (EditText) findViewById(R.id.edit_message);
        String url = editText.getText().toString();
        SharedPreferences sharedPref = getSharedPreferences("url", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("url", url);
        editor.apply();
        PostMsg(url, "10086", "测试信息。");
        Toast.makeText(MainActivity.this, "设置成功", Toast.LENGTH_LONG).show();
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