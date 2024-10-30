package com.example.clock.ClockInfo_pack;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.example.clock.R;
import com.example.clock.entity.myClock;

import java.time.DayOfWeek;
import java.util.List;

/**
 * 对应页面activity_repeat_time_unit_setting
 */
@RequiresApi(api = Build.VERSION_CODES.O)
public class RepeatTimeUnitSetting extends Fragment implements CompoundButton.OnCheckedChangeListener {

    CheckBox monday, tuseday, wednesday, thursday, friday, saturday, sunday;
    TextView repeatTimes;
    //父页面
    ClockInfo clockInfo;
    //闹钟数据对象(已取消，改采用直接对象引用)
    //myClock myClock;

    public RepeatTimeUnitSetting(ClockInfo clockInfo){
        this.clockInfo = clockInfo;
    }

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
        repeatTimes = clockInfo.findViewById(R.id.repeat_times_view);

        monday.setOnCheckedChangeListener(this);
        tuseday.setOnCheckedChangeListener(this);
        wednesday.setOnCheckedChangeListener(this);
        thursday.setOnCheckedChangeListener(this);
        friday.setOnCheckedChangeListener(this);
        saturday.setOnCheckedChangeListener(this);
        sunday.setOnCheckedChangeListener(this);

        initCheckBox();
    }

    private void initCheckBox(){
        if(clockInfo != null && clockInfo.getMyclock() != null){
            List<DayOfWeek> repeat_times = clockInfo.getMyclock().getRepeatTimes();
            if(repeat_times == null){
                Log.e("RepeatTimeUnitSetting","repeat_times对象不存在，无法进行初始化");
                return;
            }else{
                if(repeat_times.contains(DayOfWeek.MONDAY)){
                    monday.setChecked(true);
                }
                if(repeat_times.contains(DayOfWeek.TUESDAY)){
                    tuseday.setChecked(true);
                }
                if(repeat_times.contains(DayOfWeek.WEDNESDAY)){
                    wednesday.setChecked(true);
                }
                if(repeat_times.contains(DayOfWeek.THURSDAY)){
                    thursday.setChecked(true);
                }
                if(repeat_times.contains(DayOfWeek.FRIDAY)){
                    friday.setChecked(true);
                }
                if(repeat_times.contains(DayOfWeek.SATURDAY)){
                    saturday.setChecked(true);
                }
                if(repeat_times.contains(DayOfWeek.SUNDAY)){
                    sunday.setChecked(true);
                }
                if(monday.isChecked() && tuseday.isChecked() && wednesday.isChecked() && thursday.isChecked() && friday.isChecked() && saturday.isChecked() && sunday.isChecked()){
                    repeatTimes.setText("每天");
                }else{
                    repeatTimes.setText("周 " + clockInfo.getMyclock().getRepeatTimes_ToString());
                }
            }
        }
    }
    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        if(compoundButton.getId() == R.id.checkBox1){
            if(b){
                Log.d("checkbox1","开启了");
                clockInfo.getMyclock().addRepeatTimes(DayOfWeek.MONDAY);
            }else{
                Log.d("checkbox1","关闭了");
                clockInfo.getMyclock().delRepeatTimes(DayOfWeek.MONDAY);
            }
        }
        if(compoundButton.getId() == R.id.checkBox2){
            if(b){
                Log.d("checkbox2","开启了");
                clockInfo.getMyclock().addRepeatTimes(DayOfWeek.TUESDAY);
            }else{
                Log.d("checkbox2","关闭了");
                clockInfo.getMyclock().delRepeatTimes(DayOfWeek.TUESDAY);
            }
        }
        if(compoundButton.getId() == R.id.checkBox3){
            if(b){
                Log.d("checkbox3","开启了");
                clockInfo.getMyclock().addRepeatTimes(DayOfWeek.WEDNESDAY);
            }else{
                Log.d("checkbox3","关闭了");
                clockInfo.getMyclock().delRepeatTimes(DayOfWeek.WEDNESDAY);

            }
        }
        if(compoundButton.getId() == R.id.checkBox4){
            if(b){
                Log.d("checkbox4","开启了");
                clockInfo.getMyclock().addRepeatTimes(DayOfWeek.THURSDAY);
            }else{
                Log.d("checkbox4","关闭了");
                clockInfo.getMyclock().delRepeatTimes(DayOfWeek.THURSDAY);
            }
        }
        if(compoundButton.getId() == R.id.checkBox5){
            if(b){
                Log.d("checkbox5","开启了");
                clockInfo.getMyclock().addRepeatTimes(DayOfWeek.FRIDAY);
            }else{
                Log.d("checkbox5","关闭了");
                clockInfo.getMyclock().delRepeatTimes(DayOfWeek.FRIDAY);
            }
        }
        if(compoundButton.getId() == R.id.checkBox6){
            if(b){
                Log.d("checkbox6","开启了");
                clockInfo.getMyclock().addRepeatTimes(DayOfWeek.SATURDAY);
            }else{
                Log.d("checkbox6","关闭了");
                clockInfo.getMyclock().delRepeatTimes(DayOfWeek.SATURDAY);
            }
        }
        if(compoundButton.getId() == R.id.checkBox7){
            if(b){
                Log.d("checkbox7","开启了");
                clockInfo.getMyclock().addRepeatTimes(DayOfWeek.SUNDAY);
            }else{
                Log.d("checkbox7","关闭了");
                clockInfo.getMyclock().delRepeatTimes(DayOfWeek.SUNDAY);
            }
        }
        if(monday.isChecked() && tuseday.isChecked() && wednesday.isChecked() && thursday.isChecked() && friday.isChecked() && saturday.isChecked() && sunday.isChecked()){
            repeatTimes.setText("每天");
        }else{
            repeatTimes.setText("周 " + clockInfo.getMyclock().getRepeatTimes_ToString());
        }

    }
}