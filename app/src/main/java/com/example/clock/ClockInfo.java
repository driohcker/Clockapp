package com.example.clock;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.NumberPicker;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import java.lang.reflect.Field;

/**
 * 对应页面activity_clock_info
 */
public class ClockInfo extends AppCompatActivity implements NumberPicker.OnValueChangeListener {

    NumberPicker time_widePicker,hourPicker,minutePicker;
    LinearLayout repeat_time_unit;
    private boolean isExpanded = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_clock_info);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // 获取布局文件中的控件
        time_widePicker = findViewById(R.id.time_widePicker);
        hourPicker = findViewById(R.id.hourPicker);
        minutePicker = findViewById(R.id.minutePicker);
        repeat_time_unit = findViewById(R.id.repeat_time_unit);

        // 定义stringpicker要显示的文字
        String[] values = {"上午", "下午"};

        // 设置最小和最大值，数组的索引
        time_widePicker.setMinValue(0);
        time_widePicker.setMaxValue(values.length - 1);
        hourPicker.setMaxValue(11);
        hourPicker.setMinValue(0);
        minutePicker.setMaxValue(59);
        minutePicker.setMinValue(0);

        // 设置显示的文字数组
        time_widePicker.setDisplayedValues(values);

        // 禁用编辑框，防止用户手动输入
        disableEditText(time_widePicker);
        disableEditText(hourPicker);
        disableEditText(minutePicker);

        // 设置监听器，获取用户选择的文字
        time_widePicker.setOnValueChangedListener(this);
        hourPicker.setOnValueChangedListener(this);
        minutePicker.setOnValueChangedListener(this);
        repeat_time_unit.setOnClickListener(new repeat_time_OnClickListener());
    }

    // 禁用 NumberPicker 内部的 EditText
    private void disableEditText(NumberPicker picker) {
        try {
            Field[] pickerFields = NumberPicker.class.getDeclaredFields();
            for (Field field : pickerFields) {
                if (field.getType().equals(EditText.class)) {
                    field.setAccessible(true);
                    EditText editText = (EditText) field.get(picker);
                    if (editText != null) {
                        editText.setFocusable(false);      // 禁用焦点
                        editText.setFocusableInTouchMode(false); // 禁用触摸时的焦点
                        //editText.setEnabled(false);        // 禁用编辑功能
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 三个滑动栏公用valuechange方法，化简代码
    @Override
    public void onValueChange(NumberPicker numberPicker, int i, int i1) {
        if(numberPicker.getId() == R.id.time_widePicker){
            //被触发传入值
        }
        if(numberPicker.getId() == R.id.hourPicker){
            //被触发传入值
        }
        if(numberPicker.getId() == R.id.minutePicker){
            //被触发传入值
        }
    }

    //repeat_time_unit触发器
    class repeat_time_OnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            Fragment existingFragment = fragmentManager.findFragmentById(R.id.framelayout_repeat_time_select);

            if (isExpanded) {
                // 收起时隐藏 Fragment
                if (existingFragment != null) {
                    fragmentManager.beginTransaction()
                            .remove(existingFragment)
                            .commit();
                }
                collapse(repeat_time_unit,findViewById(R.id.framelayout_repeat_time_select));
            } else {
                // 展开时显示 Fragment
                if (existingFragment == null) {
                    findViewById(R.id.framelayout_repeat_time_select).setVisibility(View.VISIBLE);
                    expand(repeat_time_unit,findViewById(R.id.framelayout_repeat_time_select));
                    fragmentManager.beginTransaction()
                            .replace(R.id.framelayout_repeat_time_select, new RepeatTimeUnitSetting())
                            .commit();
                }
            }
            isExpanded = !isExpanded;
        }
    }
    //repeat_time_unit展开时的动画
    private void expand(LinearLayout repeat_time_unit, FrameLayout fragmentContainer) {
        int startHeight = 0;
        int endHeight = (int) getResources().getDimension(R.dimen.repeat_time_unit_height); // 设置你额外内容的高度

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
    //repeat_time_unit收起时的动画
    private void collapse(LinearLayout repeat_time_unit, FrameLayout fragmentContainer) {
        int startHeight = (int) getResources().getDimension(R.dimen.repeat_time_unit_height);
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