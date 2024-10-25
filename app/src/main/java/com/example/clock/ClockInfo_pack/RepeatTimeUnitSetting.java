package com.example.clock.ClockInfo_pack;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.example.clock.R;
import com.example.clock.entity.myClock;

/**
 * 对应页面activity_repeat_time_unit_setting
 */
public class RepeatTimeUnitSetting extends Fragment implements CompoundButton.OnCheckedChangeListener {

    CheckBox monday, tuseday, wednesday, thursday, friday, saturday, sunday;
    //闹钟数据对象
    myClock myClock;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_repeat_time_unit_setting, container, false);
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);

        monday = view.findViewById(R.id.checkBox1);
        tuseday = view.findViewById(R.id.checkBox2);
        wednesday = view.findViewById(R.id.checkBox3);
        thursday = view.findViewById(R.id.checkBox4);
        friday = view.findViewById(R.id.checkBox5);
        saturday = view.findViewById(R.id.checkBox6);
        sunday = view.findViewById(R.id.checkBox7);

        monday.setOnCheckedChangeListener(this);
        tuseday.setOnCheckedChangeListener(this);
        wednesday.setOnCheckedChangeListener(this);
        thursday.setOnCheckedChangeListener(this);
        friday.setOnCheckedChangeListener(this);
        saturday.setOnCheckedChangeListener(this);
        sunday.setOnCheckedChangeListener(this);

    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        if(compoundButton.getId() == R.id.checkBox1){
            if(b){
                Log.d("checkbox1","开启了");
            }else{
                Log.d("checkbox1","关闭了");
            }
        }
        if(compoundButton.getId() == R.id.checkBox2){
            if(b){
                Log.d("checkbox2","开启了");
            }else{
                Log.d("checkbox2","关闭了");
            }
        }
        if(compoundButton.getId() == R.id.checkBox3){
            if(b){
                Log.d("checkbox2","开启了");
            }else{
                Log.d("checkbox2","关闭了");
            }
        }
        if(compoundButton.getId() == R.id.checkBox4){
            if(b){
                Log.d("checkbox2","开启了");
            }else{
                Log.d("checkbox2","关闭了");
            }
        }
        if(compoundButton.getId() == R.id.checkBox5){
            if(b){
                Log.d("checkbox2","开启了");
            }else{
                Log.d("checkbox2","关闭了");
            }
        }
        if(compoundButton.getId() == R.id.checkBox6){
            if(b){
                Log.d("checkbox2","开启了");
            }else{
                Log.d("checkbox2","关闭了");
            }
        }
        if(compoundButton.getId() == R.id.checkBox7){
            if(b){
                Log.d("checkbox2","开启了");
            }else{
                Log.d("checkbox2","关闭了");
            }
        }
    }
}