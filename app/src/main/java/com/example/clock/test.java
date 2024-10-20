package com.example.clock;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import androidx.appcompat.widget.Toolbar;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

/**
 * 对应页面activity_test
 */
public class test extends AppCompatActivity {

    Toolbar toolbar;
    MenuInflater inflater;
    FloatingActionButton addClock;
    LinearLayout clockUnit;
    private boolean isExpanded = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_test);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        toolbar = findViewById(R.id.toolbar);
        addClock = findViewById(R.id.fab);
        clockUnit = findViewById(R.id.clockUnit);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        addClock.setOnClickListener(new myButtonListener());
        clockUnit.setOnClickListener(new clockUnitListener());

    }

    //实现添加闹钟
    class myButtonListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent();
            intent.setClass(test.this, ClockInfo.class);
            startActivity(intent);
        }
    }
    //点击clockunit时的触发器
    class clockUnitListener implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            Fragment existingFragment = fragmentManager.findFragmentById(R.id.fragment_container);

            if (isExpanded) {
                // 收起时隐藏 Fragment
                if (existingFragment != null) {
                    fragmentManager.beginTransaction()
                            .remove(existingFragment)
                            .commit();
                }
                collapse(clockUnit,findViewById(R.id.fragment_container));
            } else {
                // 展开时显示 Fragment
                if (existingFragment == null) {
                    findViewById(R.id.fragment_container).setVisibility(View.VISIBLE);
                    expand(clockUnit,findViewById(R.id.fragment_container));
                    fragmentManager.beginTransaction()
                            .replace(R.id.fragment_container, new ClockExtraSetting())
                            .commit();
                }
            }
            isExpanded = !isExpanded;
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }
    //clockunit展开时的动画
    private void expand(LinearLayout clockUnit, FrameLayout fragmentContainer) {
        int startHeight = 0;
        int endHeight = (int) getResources().getDimension(R.dimen.time_unit_extra_content_height); // 设置你额外内容的高度

        ValueAnimator animator = ValueAnimator.ofInt(startHeight, endHeight);
        animator.addUpdateListener(animation -> {
            int animatedValue = (int) animation.getAnimatedValue();
            ViewGroup.LayoutParams layoutParams = fragmentContainer.getLayoutParams();
            layoutParams.height = animatedValue;
            fragmentContainer.setLayoutParams(layoutParams);
        });
        animator.setDuration(300); // 动画持续时间
        animator.start();
    }
    //clockunit收起时的动画
    private void collapse(LinearLayout clockUnit, FrameLayout fragmentContainer) {
        int startHeight = (int) getResources().getDimension(R.dimen.time_unit_extra_content_height);
        int endHeight = 0;

        ValueAnimator animator = ValueAnimator.ofInt(startHeight, endHeight);
        animator.addUpdateListener(animation -> {
            int animatedValue = (int) animation.getAnimatedValue();
            ViewGroup.LayoutParams layoutParams = fragmentContainer.getLayoutParams();
            layoutParams.height = animatedValue;
            fragmentContainer.setLayoutParams(layoutParams);
        });
        animator.setDuration(300); // 动画持续时间
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                fragmentContainer.setVisibility(View.GONE); // 隐藏额外内容
            }
        });
        animator.start();
    }
}
