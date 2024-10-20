package com.example.clock;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;


/**
 *  闹钟类，负责记录闹钟的相关信息
 */
public class Clocks {
    /**
     *  time 记录时间点（12小时制）
     *  time_clock 记录闹钟时间点（24小时制）
     *  time_wide 记录是上午还是下午
     *  repeat_times 记录每周哪一天触发闹钟
     *  time_remains 记录距离闹钟时间还有多少时间
     *  ifuse 记录是否使用该闹钟
     *  info 备注
     *  #ring 铃声（未实现）
     *  #vibration 是否震动（未实现）
     */
    private LocalTime time;
    private LocalTime time_clock;
    private String time_wide;
    private List<DayOfWeek> repeat_times;
    private Duration time_remains;
    private boolean ifuse;
    private String info;

    private static LocalDateTime now;
    private static LocalDateTime nextAlarm = null;

    public Clocks(){}
    public Clocks(LocalTime time, String time_wide, List<DayOfWeek> times, String info){
        /**
         *  初始化闹钟属性
         */
        this.time = time;               //几点
        this.time_wide = time_wide;     //上午还是下午
        this.repeat_times = times;      //频率
        this.ifuse = true;              //是否使用，初始化为是
        this.info = info;               //备注
    }

    /**
     *  设置此些方法的目的是为了在闹钟扩展界面方便的修改数值
     *  set设置闹钟时间（12小时制）
     *  get获取时间
     */
    public void setTime(LocalTime time, String time_wide){
        this.time = time;
        this.time_wide = time_wide;
    }
    public LocalTime getTime(){
        return this.time;
    }
    public String getTime_wide(){
        return this.time_wide;
    }

    /**
     *  add方法添加星期,导入哪一天
     *  set方法重设哪些星期,直接导入星期数组
     *  get方法获取这些值
     */
    public void addRepeat_times(DayOfWeek day){
        if (!repeat_times.contains(day)) {
            repeat_times.add(day);
        }
    }
    public void setRepeat_times(List<DayOfWeek> repeat_times){
        this.repeat_times = repeat_times;
    }
    public List<DayOfWeek> getRepeat_times(){
        return this.repeat_times;
    }

    /**
     *  set设置是否使用闹钟
     *  get获取闹钟使用状态
     */
    public void setIfuse(boolean ifuse) {
        this.ifuse = ifuse;
    }
    public boolean getIfuse(){
        return  ifuse;
    }

    /**
     *  calculate_time_clock 计算time_clock的值，将12小时制转换成24制，并作为实际存储数据
     *  caculate_remaining_time 计算距离最近的闹钟时间还有多久
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void calculate_time_clock(){
        if(this.time_wide.equals("上午")){
            this.time_clock = this.time;
        }else if(this.time_wide.equals("下午")){
            this.time_clock = this.time.plusHours(12);
        }else {
            throw new IllegalArgumentException("time_wide 的值无效");
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    public Duration calculate_remaining_time() {
        now = LocalDateTime.now(); // 获取当前的时间和日期
        nextAlarm = null;

        // 遍历设置的闹钟星期
        for (DayOfWeek day : repeat_times) {
            // 计算下一个闹钟触发的日期
            LocalDateTime alarmDateTime = LocalDateTime.now()
                    .with(day)
                    .withHour(time_clock.getHour())
                    .withMinute(time_clock.getMinute())
                    .withSecond(0)
                    .withNano(0);

            // 如果闹钟时间比当前时间早，计算到下一周的时间
            if (alarmDateTime.isBefore(now)) {
                alarmDateTime = alarmDateTime.plusWeeks(1);
            }

            // 找到距离当前时间最近的闹钟时间
            if (nextAlarm == null || alarmDateTime.isBefore(nextAlarm)) {
                nextAlarm = alarmDateTime;
            }
        }

        // 计算剩余时间
        time_remains = Duration.between(now, nextAlarm);
        return time_remains;
    }

    /**
     *  备注的设置和获取
     */
    public String getInfo() {
        return info;
    }
    public void setInfo(String info) {
        this.info = info;
    }
}
