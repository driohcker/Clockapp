package com.example.clock.ClockInfo_pack;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.clock.MainActivity_pack.test;
import com.example.clock.R;
import com.example.clock.entity.ClockUnitView;
import com.example.clock.entity.myClock;

import java.lang.reflect.Field;

/**
 * 对应页面activity_clock_info
 */
@RequiresApi(api = Build.VERSION_CODES.O)
public class ClockInfo extends AppCompatActivity implements NumberPicker.OnValueChangeListener {

    NumberPicker time_widePicker,hourPicker,minutePicker;
    LinearLayout repeat_time_unit;
    Button confirm,cancel;
    TextView info,repeatTimes;

    myClock myclock;

    private boolean isExpanded = false;
    int hour, minute, time_wide;
    String infomation;

    String viewFrom;
    // 定义stringpicker要显示的文字
    String[] values = {"上午", "下午"};

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
        confirm = findViewById(R.id.confirm);
        cancel = findViewById(R.id.cancel);
        info = findViewById(R.id.info);
        repeatTimes = findViewById(R.id.repeat_times_view);

        // 设置最小和最大值，数组的索引
        time_widePicker.setMinValue(0);
        time_widePicker.setMaxValue(values.length - 1);
        hourPicker.setMaxValue(11);
        hourPicker.setMinValue(0);
        minutePicker.setMaxValue(59);
        minutePicker.setMinValue(0);

        // 设置显示的文字数组
        time_widePicker.setDisplayedValues(values);

        repeatTimes.setText("周 ");

        // 禁用编辑框，防止用户手动输入
        disableEditText(time_widePicker);
        disableEditText(hourPicker);
        disableEditText(minutePicker);

        // 设置监听器，获取用户选择的文字
        time_widePicker.setOnValueChangedListener(this);
        hourPicker.setOnValueChangedListener(this);
        minutePicker.setOnValueChangedListener(this);
        repeat_time_unit.setOnClickListener(new repeat_time_OnClickListener());
        confirm.setOnClickListener(new confirmOnClickListener());
        cancel.setOnClickListener(new cancelOnClickListener());

        //检查来自哪个页面并对来自该页面的数据进行传入
        checkViewFrom();
    }

    private void checkViewFrom(){
        // 检查 Intent 中的 "view" 参数是否存在
        viewFrom = getIntent().getStringExtra("viewfrom");
        if (viewFrom == null) {
            Log.e("ClockInfo", "viewfrom参数为null");
        } else {
            Log.d("ClockInfo", "viewfrom参数的值为: " + viewFrom);
        }
        // 通过 "view" 的值判断，并添加 null 检查以避免异常
        if ("ClockUnitView".equals(viewFrom)) {  // 使用字符串常量在前，避免空指针异常
            myclock = (myClock) getIntent().getSerializableExtra("myClock");

            if (myclock != null) {
                Log.d("ClockInfo", "已接收到 myClock 对象");

                // 初始化控件值
                time_widePicker.setValue(myclock.getTimeWide().equals(values[0]) ? 0 : 1);
                hourPicker.setValue(myclock.getTimeHour());
                minutePicker.setValue(myclock.getTimeMinute());
                info.setText(myclock.getInfo());
                repeatTimes.setText("周 " + myclock.getRepeatTimes_ToString());

                // 初始化值,避免两方数据不同步
                time_wide = time_widePicker.getValue();
                hour = hourPicker.getValue();
                minute = minutePicker.getValue();
                infomation = String.valueOf(info.getText());
            } else {
                Log.e("ClockInfo", "myClock对象为null，未能正确接收");
            }
        }
        else if ("main".equals(viewFrom)) {
            if (myclock == null){
                Log.e("ClockInfo", "未拥有myClock对象，将创建myClock对象" );
                myclock = new myClock();
            }
        }else {
            Log.e("ClockInfo", "view参数值不为 'ClockUnitView或main'，未能进行myClock初始化");
        }
    }

    // 三个滑动栏公用valuechange方法，化简代码
    @Override
    public void onValueChange(NumberPicker numberPicker, int i, int i1) {
        if(numberPicker.getId() == R.id.time_widePicker){
            //被触发传入值
            time_wide = time_widePicker.getValue();
        }
        if(numberPicker.getId() == R.id.hourPicker){
            //被触发传入值
            hour = hourPicker.getValue();
        }
        if(numberPicker.getId() == R.id.minutePicker){
            //被触发传入值
            minute = minutePicker.getValue();
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
                            .replace(R.id.framelayout_repeat_time_select, new RepeatTimeUnitSetting(ClockInfo.this))
                            .commit();
                }
            }
            isExpanded = !isExpanded;
        }
    }
    //确定按钮触发
    class confirmOnClickListener implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            if("main".equals(viewFrom)){
                Intent intent = new Intent();
                intent.putExtra("addClockUnit", true); // 可以根据需要传递其他数据
                intent.putExtra("myClock",getNewMyclock());
                setResult(RESULT_OK, intent);

                //这里编写调用myClock的uploadToDatabase方法将数据上传至数据库(已废弃，已改至ClockUnitView创建时自动创建)
            }
            if("ClockUnitView".equals(viewFrom)){
                Intent intent1 = new Intent(view.getContext(), test.class);
                intent1.putExtra("setClockUnit", true); // 可以根据需要传递其他数据
                intent1.putExtra("myClockID", myclock.getID());
                setResult(RESULT_OK, intent1);

                //这里编写调用myClock的updateToDatabase方法将数据库的数据进行更新
                myclock.setTime(hour,minute);
                myclock.setInfo(String.valueOf(info.getText()));
                myclock.setTimeWide(values[time_wide]);
                myclock.updateToDatabase();
            }
            finish();
        }
    }
    //取消按钮触发
    class cancelOnClickListener implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            Intent intent = new Intent();
            finish();
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
    //获取clock对象
    public myClock getMyclock(){
        return myclock;
    }
    //获取新clock对象，新clock对象更新为当前页面上的值
    public myClock getNewMyclock(){
        myclock.setTime(hour,minute);
        myclock.setTimeWide(values[time_wide]);
        //RepeatTime由RepeatTimeUnitSetting单独实时赋值
        infomation = String.valueOf(info.getText());
        myclock.setInfo(infomation);
        return myclock;
    }
}