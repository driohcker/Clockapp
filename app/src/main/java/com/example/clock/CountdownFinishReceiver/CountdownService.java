package com.example.clock.CountdownFinishReceiver;

import android.app.Service;
import android.content.Intent;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

public class CountdownService extends Service {

    public static final String COUNTDOWN_FINISH_ACTION = "com.example.clock.COUNTDOWN_FINISH";
    public static final String COUNTDOWN_UPDATE_ACTION = "com.example.clock.COUNTDOWN_UPDATE";
    private CountDownTimer countDownTimer;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        long durationMillis = intent.getLongExtra("duration", 0);

        // 初始化倒计时器
        countDownTimer = new CountDownTimer(durationMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                // 发送剩余时间的更新广播
                //Log.e("onTick广播", "已更新");
                Intent updateIntent = new Intent(COUNTDOWN_UPDATE_ACTION);
                updateIntent.putExtra("time_remaining", millisUntilFinished);
                sendBroadcast(updateIntent);
            }

            @Override
            public void onFinish() {
                // 倒计时结束，发送广播
                Intent finishIntent = new Intent(COUNTDOWN_FINISH_ACTION);
                sendBroadcast(finishIntent);
            }
        };

        // 启动倒计时
        countDownTimer.start();
        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}


