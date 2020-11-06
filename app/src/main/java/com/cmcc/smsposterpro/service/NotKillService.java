package com.cmcc.smsposterpro.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.cmcc.smsposterpro.MainActivity;
import com.cmcc.smsposterpro.R;

public class NotKillService extends Service {

    public static final String CHANNEL_ID = "cmcc_id";
    public static final String CHANNEL_NAME = "cmcc_name";

    @Override
    public void onCreate() {
        super.onCreate();
        registerNotificationChannel();
        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                notificationIntent, 0);
        int notifyId = (int) System.currentTimeMillis();
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, CHANNEL_ID);
        mBuilder.setSmallIcon(R.mipmap.ic_launcher).setContentTitle("短信转发服务")
                .setContentText("短信转发服务运行中...").setContentIntent(pendingIntent);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            mBuilder.setContentTitle(getResources().getString(R.string.app_name));
        }
        startForeground(notifyId, mBuilder.build());
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new Binder();
    }

    /**
     * 注册通知通道
     */
    private void registerNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            NotificationChannel notificationChannel = mNotificationManager.getNotificationChannel(CHANNEL_ID);
            if (notificationChannel == null) {
                NotificationChannel channel = new NotificationChannel(CHANNEL_ID,
                        CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH);
                //是否在桌面icon右上角展示小红点
                channel.enableLights(true);
                //小红点颜色
                channel.setLightColor(Color.RED);
                //通知显示
                channel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
                //是否在久按桌面图标时显示此渠道的通知
                channel.setShowBadge(true);
                mNotificationManager.createNotificationChannel(channel);
            }
        }
    }

}
