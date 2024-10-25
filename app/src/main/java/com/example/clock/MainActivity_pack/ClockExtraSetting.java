package com.example.clock.MainActivity_pack;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.clock.ClockInfo_pack.ClockInfo;
import com.example.clock.R;

import java.lang.reflect.Field;

/**
 * 此类用来对闹钟的简要信息进行修改并记录
 * 对应页面clock_extra_setting
 */
public class ClockExtraSetting extends Fragment implements NumberPicker.OnValueChangeListener, View.OnClickListener {

    NumberPicker time_widePicker,hourPicker,minutePicker;
    Button settings,confirm;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_clock_extra_setting, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // 获取布局文件中的 NumberPicker 控件
         time_widePicker = view.findViewById(R.id.time_widePicker);
         hourPicker = view.findViewById(R.id.hourPicker);
         minutePicker = view.findViewById(R.id.minutePicker);
         settings = view.findViewById(R.id.settings_button);

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
        settings.setOnClickListener(this);
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

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.settings_button){
            //跳转页面
            Intent intent = new Intent();
            intent.setClass(this.getContext(), ClockInfo.class);
            startActivity(intent);
        }
        if(view.getId() == R.id.confirm_button){
            //记录数值
        }
    }
}
