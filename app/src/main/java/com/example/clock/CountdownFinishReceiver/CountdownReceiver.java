package com.example.clock.CountdownFinishReceiver;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

//广播接收器
public class CountdownReceiver extends BroadcastReceiver {

    private static CountdownReceiver instance;
    String timeFormatted;
    private final ArrayList<TextView> timeRemainsViews = new ArrayList<>();

    private CountdownReceiver() {
        // 私有构造函数，防止外部实例化
    }

    public static CountdownReceiver getInstance() {
        if (instance == null) {
            instance = new CountdownReceiver();
        }
        return instance;
    }

    @SuppressLint("DefaultLocale")
    @Override
    public void onReceive(Context context, Intent intent) {
        if (CountdownService.COUNTDOWN_FINISH_ACTION.equals(intent.getAction())) {
            // 倒计时结束，启动新的 Activity
            Intent newIntent = new Intent(context, CountdownFinished.class);
            newIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(newIntent);
        }
        if (CountdownService.COUNTDOWN_UPDATE_ACTION.equals(intent.getAction())) {
            long timeRemaining = intent.getLongExtra("time_remaining", 0);

            timeFormatted = String.format("%02d小时%02d分钟%02d秒",
                    TimeUnit.MILLISECONDS.toHours(timeRemaining),
                    TimeUnit.MILLISECONDS.toMinutes(timeRemaining) % 60,
                    TimeUnit.MILLISECONDS.toSeconds(timeRemaining) % 60);
            // 更新所有TextView中的时间显示
            updateAllTimeRemains();
        }
    }

    // 添加TextView到数组
    public void addTimeRemainsView(TextView textView) {
        if (!timeRemainsViews.contains(textView)) {
            timeRemainsViews.add(textView);
        }
    }

    // 从数组中移除TextView
    public void removeTimeRemainsView(TextView textView) {
        timeRemainsViews.remove(textView);
    }

    // 更新所有TextView的时间显示
    private void updateAllTimeRemains() {
        for (TextView timeRemainsView : timeRemainsViews) {
            timeRemainsView.setText(timeFormatted);
        }
    }

    public void unregister(Context context) {
        context.unregisterReceiver(this);
    }
}