package com.cmcc.smsposterpro;

import android.app.Application;

import com.cmcc.smsposterpro.service.AliveService;
import com.xdandroid.hellodaemon.DaemonEnv;

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        //需要在 Application 的 onCreate() 中调用一次 DaemonEnv.initialize()
        DaemonEnv.initialize(
                this,  //Application Context.
                AliveService.class, //刚才创建的 Service 对应的 Class 对象.
                DaemonEnv.DEFAULT_WAKE_UP_INTERVAL);  //定时唤醒的时间间隔(ms), 默认 6 分钟.
        AliveService.sShouldStopService = false;
        DaemonEnv.startServiceMayBind(AliveService.class);
    }
}
