package com.example.clock.entity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.LayoutTransition;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.clock.CountdownFinishReceiver.CountdownReceiver;
import com.example.clock.CountdownFinishReceiver.CountdownService;
import com.example.clock.MainActivity_pack.ClockExtraSetting;
import com.example.clock.MainActivity_pack.test;
import com.example.clock.R;

import java.time.Duration;

@RequiresApi(api = Build.VERSION_CODES.O)
public class ClockUnitView extends Fragment implements CompoundButton.OnCheckedChangeListener{

    private final View view;
    private Context context;
    private final LinearLayout clockUnit;
    private final FrameLayout fragmentContainer;
    private final FragmentManager fragmentManager;
    private final test parentActivity;
    private TextView time,time_wide,times,time_remains;
    private Switch ifuse;

    private static final String TAG = "ClockUnitView";
    private boolean isExpanded = false;

    private myClock myclock;
    private String ID;

    private CountdownReceiver countdownReceiver;

    public void onDestroy() {
        // 注销广播接收器
        if (countdownReceiver != null) {
            getContext().unregisterReceiver(countdownReceiver);
        }
        super.onDestroy();
    }

    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    public ClockUnitView(Context context, FragmentManager fragmentManager, ViewGroup parent, test parentActivity, myClock myclock) {
        this.parentActivity = parentActivity;
        this.view = LayoutInflater.from(context).inflate(R.layout.activity_clock_unit, parent, false);
        this.fragmentContainer = new FrameLayout(context);
        this.fragmentContainer.setId(View.generateViewId());
        this.context = context;
        //这里对新建闹钟ClockInfo传来的值对myclock对象进行初始化
        this.myclock = myclock;
        ID = myclock.getID();
        //将数据上传至数据库
        this.myclock.setClockDatabaseHelper(context);
        this.myclock.uploadToDatabase();

        //获取组件
        this.clockUnit = view.findViewById(R.id.clockUnit);
        time = view.findViewById(R.id.time);
        time_wide = view.findViewById(R.id.time_wide);
        times = view.findViewById(R.id.times);
        time_remains = view.findViewById(R.id.time_remains);
        ifuse = view.findViewById(R.id.ifuse);


        ((LinearLayout) view.findViewById(R.id.clockUnit)).addView(fragmentContainer);

        parent.addView(view);
        this.fragmentManager = fragmentManager;

        // 设置父容器的 LayoutTransition，以启用删除时的动画效果
        if (parent instanceof LinearLayout) {
            ((LinearLayout) parent).setLayoutTransition(new LayoutTransition());
        }

        //clockunit点击展开收缩
        this.setupClickListener();
        ifuse.setOnCheckedChangeListener(this);

        updateShow();
        countdownReceiver.addTimeRemainsView(time_remains);
    }

    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    private void setCountdownService() {
        // 获取倒计时剩余时间（以毫秒为单位）
        long durationMillis = myclock.getTimeRemains().toMillis();

        // 启动倒计时服务
        Intent countdownIntent = new Intent(context, CountdownService.class);
        countdownIntent.putExtra("duration", durationMillis);
        context.startService(countdownIntent);

        // 注册广播接收器
        countdownReceiver = countdownReceiver.getInstance();
        IntentFilter filter = new IntentFilter();
        filter.addAction(CountdownService.COUNTDOWN_FINISH_ACTION);
        filter.addAction(CountdownService.COUNTDOWN_UPDATE_ACTION);
        context.registerReceiver(countdownReceiver, filter, Context.RECEIVER_EXPORTED);
    }

    //将自身的myclock数据上传至数据库
    public void updateToDatabase(){
        this.myclock.updateToDatabase();
    }
    //把自身的myclock数据更新为数据库的数据（暂时没用）
    public void updateFromDatabase(){
        this.myclock.updateFromDatabse();
    }
    //更新显示信息
    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    public void updateShow(){
        this.time.setText(myclock.getTime().toString());
        this.times.setText(myclock.showRepeatTimes());
        this.time_wide.setText(myclock.getTimeWide());
        this.ifuse.setChecked(myclock.getIfuse());

        setCountdownService();
    }

    private void setupClickListener() {
        clockUnit.setOnClickListener(v -> {
            Log.d(TAG, "Clock unit clicked, isExpanded: " + isExpanded);
            Fragment fragment = fragmentManager.findFragmentById(fragmentContainer.getId());
            if (isExpanded) {
                if(fragment != null){
                    fragmentManager.beginTransaction().remove(fragment).commit();
                }
                collapse();
            } else {
                if (fragment == null){
                    expand();
                    fragmentManager.beginTransaction().replace(fragmentContainer.getId(),new ClockExtraSetting(this)).commit();
                }

            }
        });
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        myclock.setIfuse(b);
        myclock.updateIfUseToDatabase();
    }

    private void expand() {
        Log.d(TAG, "Expanding ClockUnitView");
        isExpanded = true;
        fragmentContainer.setVisibility(View.VISIBLE);
        animateHeight(fragmentContainer, 0, (int) view.getResources().getDimension(R.dimen.time_unit_extra_content_height));
    }

    private void collapse() {

        Log.d(TAG, "Collapsing ClockUnitView");
        isExpanded = false;
        animateHeight(fragmentContainer, (int) view.getResources().getDimension(R.dimen.time_unit_extra_content_height), 0);
    }

    private void animateHeight(final FrameLayout container, int startHeight, int endHeight) {
        Log.d(TAG, "Animating height from " + startHeight + " to " + endHeight);
        if (container == null) {
            Log.e(TAG, "Container is null!");
            return;
        }

        ValueAnimator animator = ValueAnimator.ofInt(startHeight, endHeight);
        animator.addUpdateListener(animation -> {
            int animatedValue = (int) animation.getAnimatedValue();
            ViewGroup.LayoutParams layoutParams = container.getLayoutParams();
            if (layoutParams == null) {
                Log.e(TAG, "LayoutParams is null for container!");
                return;
            }
            layoutParams.height = animatedValue;
            container.setLayoutParams(layoutParams);
            //Log.d(TAG, "Updated container height: " + animatedValue);
        });

        animator.setDuration(300);
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                if (!isExpanded) {
                    container.setVisibility(View.GONE);
                    Log.d(TAG, "Container hidden after collapse");
                }
            }
        });
        animator.start();
    }

    public View getView() {
        return view;
    }

    public myClock getMyclock(){
        return myclock;
    }

    public void collapseClockUnit (){
        Fragment fragment = fragmentManager.findFragmentById(fragmentContainer.getId());
        if (isExpanded) {
            if (fragment != null) {
                fragmentManager.beginTransaction().remove(fragment).commit();
            }
            collapse();
        }
    }

    //删除ClockUnitView
    public void deleteClockUnit(){
        // 确保展开视图收起
        collapseClockUnit();

        // 从父布局移除 view
        if (view.getParent() instanceof ViewGroup) {
            ((ViewGroup) view.getParent()).removeView(view);
        }

        this.myclock.delFromDatabase();

        // 清空引用
        myclock = null;

        countdownReceiver.removeTimeRemainsView(time_remains);

        parentActivity.delClockUnitView(this);
    }

    public String getID(){
        return ID;
    }

}
