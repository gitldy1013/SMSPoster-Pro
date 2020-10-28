package com.cmcc.smsposterpro;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
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

import java.util.ArrayList;
import java.util.Map;

import static com.cmcc.smsposterpro.PostUtil.PostMsg;

public class MainActivity extends AppCompatActivity implements SmsServer {

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        EditText editText = findViewById(R.id.edit_message);
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
                && ContextCompat.checkSelfPermission(this.getApplicationContext(), Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this.getApplicationContext(), Manifest.permission.READ_PHONE_NUMBERS) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this.getApplicationContext(), Manifest.permission.INTERNET) == PackageManager.PERMISSION_GRANTED;
        if (!flag) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECEIVE_SMS, Manifest.permission.READ_SMS, Manifest.permission.READ_PHONE_STATE, Manifest.permission.READ_CONTACTS, Manifest.permission.READ_PHONE_NUMBERS, Manifest.permission.INTERNET}, 1);
        }
        return flag;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0) {
            boolean flag = true;
            int index = 0;
            for (int i = 0; i < grantResults.length; i++) {
                if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                    flag = false;
                    index = i;
                    break;
                }
            }
            if (flag) {
                Toast.makeText(MainActivity.this, "授权成功!", Toast.LENGTH_LONG).show();
                doView("授权成功!");
            } else {
                Toast.makeText(MainActivity.this, "授权失败！", Toast.LENGTH_LONG).show();
                doView("授权失败!");
                dealwithPermiss(MainActivity.this, permissions[index]);
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void saveURL(View view) {
        EditText editText = findViewById(R.id.edit_message);
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
        EditText editText = findViewById(R.id.edit_phone);
        String phone = editText.getText().toString();
        SMSReciver.phones.add(phone);
        Toast.makeText(MainActivity.this, "添加" + phone + "成功", Toast.LENGTH_LONG).show();
        doView("添加" + phone + "成功!");
    }

    public void remPhone(View view) {
        EditText editText = findViewById(R.id.edit_phone);
        String phone = editText.getText().toString();
        SMSReciver.phones.remove(phone);
        Toast.makeText(MainActivity.this, "移除" + phone + "成功", Toast.LENGTH_LONG).show();
        doView("移除" + phone + "成功!");
    }

    public void doView(String msg) {
        TextView textView = findViewById(R.id.out_message);
        textView.setEnabled(true);
        textView.setMovementMethod(ScrollingMovementMethod.getInstance());
        textView.append(System.getProperty("line.separator") + msg);
    }

    public void clean(View view) {
        TextView textView = findViewById(R.id.out_message);
        textView.setEnabled(true);
        textView.setText("");
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
        String phone = tm.getLine1Number().trim().length() == 0 ? tm.getSimOperatorName() : tm.getLine1Number();
        return phone;
    }

    public void dealwithPermiss(final Activity context, String permission) {
        if (!ActivityCompat.shouldShowRequestPermissionRationale(context, permission)) {
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setTitle("操作提示")
                    .setMessage("注意：当前缺少必要权限！\n请点击“设置”-“权限”-打开所需权限\n最后点击两次后退按钮，即可返回")
                    .setPositiveButton("去授权", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                            Uri uri = Uri.fromParts("package", context.getApplicationContext().getPackageName(), null);
                            intent.setData(uri);
                            context.startActivity(intent);
                        }
                    })
                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(MainActivity.this, "取消操作", Toast.LENGTH_SHORT).show();
                        }
                    }).show();

        }
    }

    public void showContacts(View view) {
        ArrayList<MyContacts> contacts = ContactUtils.getAllContacts(MainActivity.this);
        doView(contacts.toString());
    }
}