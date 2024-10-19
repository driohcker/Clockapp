package com.example.clock;

import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.NumberPicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.lang.reflect.Field;

public class ClockExtraSetting extends Fragment {

    NumberPicker stringPicker,hourPicker,minutePicker;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.clock_extra_setting, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // 获取布局文件中的 NumberPicker 控件
         stringPicker = view.findViewById(R.id.time_widePicker);
         hourPicker = view.findViewById(R.id.hourPicker);
         minutePicker = view.findViewById(R.id.minutePicker);

        // 定义要显示的文字
        String[] values = {"上午", "下午"};

        // 设置最小和最大值，数组的索引
        stringPicker.setMinValue(0);
        stringPicker.setMaxValue(values.length - 1);
        hourPicker.setMaxValue(11);
        hourPicker.setMinValue(0);
        minutePicker.setMaxValue(59);
        minutePicker.setMinValue(0);

        // 设置显示的文字数组
        stringPicker.setDisplayedValues(values);

        // 禁用编辑框，防止用户手动输入
        disableEditText(stringPicker);
        disableEditText(hourPicker);
        disableEditText(minutePicker);

        // 设置监听器，获取用户选择的文字
        stringPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {

                // 获取当前选择的文字
            }
        });
        hourPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                // 获取当前数值
            }
        });
        minutePicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                // 获取当前数值
            }
        });

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


}
