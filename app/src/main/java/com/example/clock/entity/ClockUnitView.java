package com.example.clock.entity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.LayoutTransition;
import android.animation.ValueAnimator;
import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.clock.MainActivity_pack.ClockExtraSetting;
import com.example.clock.R;

import java.io.Serializable;

public class ClockUnitView extends Fragment {

    private final View view;
    private final LinearLayout clockUnit;
    private final FrameLayout fragmentContainer;
    private final FragmentManager fragmentManager;
    private TextView time,time_wide,times,time_remains;

    private static final String TAG = "ClockUnitView";
    private boolean isExpanded = false;

    private myClock myclock;


    @RequiresApi(api = Build.VERSION_CODES.O)
    public ClockUnitView(Context context, FragmentManager fragmentManager, ViewGroup parent) {
        this.view = LayoutInflater.from(context).inflate(R.layout.activity_clock_unit, parent, false);
        this.fragmentContainer = new FrameLayout(context);
        this.fragmentContainer.setId(View.generateViewId());

        //这里对新建闹钟ClockInfo传来的值对myclock对象进行初始化
        //模拟值传入
        myclock = new myClock(11,31,"上午");

        this.clockUnit = view.findViewById(R.id.clockUnit);
        time = view.findViewById(R.id.time);
        time_wide = view.findViewById(R.id.time_wide);
        times = view.findViewById(R.id.times);
        time_remains = view.findViewById(R.id.time_remains);

        ((LinearLayout) view.findViewById(R.id.clockUnit)).addView(fragmentContainer);

        parent.addView(view);
        this.fragmentManager = fragmentManager;

        // 设置父容器的 LayoutTransition，以启用删除时的动画效果
        if (parent instanceof LinearLayout) {
            ((LinearLayout) parent).setLayoutTransition(new LayoutTransition());
        }

        setupClickListener();//clockunit点击展开收缩

        update();
    }

    public void update(){
        //设置一个线程对他进行每秒更新
        this.time.setText(myclock.getTime().toString());
        //this.times.setText(myclock.getRepeatTimes().toString());
        this.time_wide.setText(myclock.getTimeWide());
        //time_remains没写
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
            Log.d(TAG, "Updated container height: " + animatedValue);
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

    public void deleteClockUnit(){
        // 确保展开视图收起
        collapseClockUnit();

        // 从父布局移除 view
        if (view.getParent() instanceof ViewGroup) {
            ((ViewGroup) view.getParent()).removeView(view);
        }

        // 清空引用
        myclock = null;
    }
}
