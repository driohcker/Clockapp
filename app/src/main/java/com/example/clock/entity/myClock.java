package com.example.clock.entity;

import android.content.Context;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.clock.Sql.ClockDatabaseHelper;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;


/**
 *  闹钟类，负责记录闹钟的相关信息
 */
public class myClock {
    /**
     *  time 记录时间点（12小时制）
     *  time_remains 记录距离闹钟时间还有多少时间
     *  <<利用数据库保存>>
     *  time_hour 时钟
     *  time_minute 分钟
     *  time_clock 记录闹钟时间点（24小时制）
     *  time_wide 记录是上午还是下午
     *  repeat_times 记录每周哪一天触发闹钟
     *  ifuse 记录是否使用该闹钟
     *  info 备注
     *
     *  <<利用sharedpreferences保存>>
     *  #ring 铃声（未实现）
     *  #vibration 是否震动（未实现）
     */


    private int time_hour,time_minute;
    private LocalTime time_clock;
    private String time_wide;
    private List<DayOfWeek> repeat_times;
    private Duration time_remains;
    private boolean ifuse;
    private String info;

    private static LocalDateTime now;
    private static LocalDateTime nextAlarm = null;
    private LocalTime time;

    private static ClockDatabaseHelper clockDatabaseHelper;

    public myClock(){}
    @RequiresApi(api = Build.VERSION_CODES.O)
    public myClock(int time_hour, int time_minute, String time_wide, List<DayOfWeek> times, String info, Context context){
        /**
         *  初始化闹钟属性
         */
        this.time_hour = time_hour;
        this.time_minute = time_minute;
        this.time_wide = time_wide;     //上午还是下午
        this.repeat_times = times;      //频率
        this.ifuse = true;              //是否使用，初始化为是
        this.info = info;               //备注
        time = LocalTime.of(time_hour, time_minute);
        clockDatabaseHelper = new ClockDatabaseHelper(context);
    }

    /**
     *  设置此些方法的目的是为了在闹钟扩展界面方便的修改数值
     *  set设置闹钟时间（12小时制）
     *  get获取时间
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void setTime(int time_hour, int time_minute){
        this.time_hour = time_hour;
        this.time_minute = time_minute;
        time = LocalTime.of(time_hour,time_minute);
    }
    public LocalTime getTime(){
        return this.time;
    }
    public String getTimeWide(){
        return this.time_wide;
    }
    public int getTimeHour(){
        return time_hour;
    }
    public int getTimeMinute(){
        return time_minute;
    }
    /**
     *  add方法添加星期,导入哪一天
     *  set方法重设哪些星期,直接导入星期数组
     *  get方法获取这些值
     */
    public void addRepeatTimes(DayOfWeek day){
        if (!repeat_times.contains(day)) {
            repeat_times.add(day);
        }
    }
    public void setRepeatTimes(List<DayOfWeek> repeat_times){
        this.repeat_times = repeat_times;
    }
    public List<DayOfWeek> getRepeatTimes(){
        return this.repeat_times;
    }
    public String getRepeatTimes_String() {
        StringBuilder serialized = new StringBuilder();
        for (DayOfWeek day : repeat_times) {
            serialized.append(day.name()).append(","); // 将每个日期以逗号分隔
        }
        if (serialized.length() > 0) {
            serialized.setLength(serialized.length() - 1); // 去掉最后一个逗号
        }
        return serialized.toString();
    }
    public void delRepeatTimes(DayOfWeek repeat_times){
        this.repeat_times.remove(repeat_times);
    }
    /**
     *  set设置是否使用闹钟
     *  get获取闹钟使用状态
     */
    public void setIfuse(boolean ifuse) {
        this.ifuse = ifuse;
    }
    public boolean getIfuse(){
        return ifuse;
    }

    /**
     *  calculate_time_clock 计算time_clock的值，将12小时制转换成24制，并作为实际存储数据
     *  caculate_remaining_time 计算距离最近的闹钟时间还有多久
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void calculateTimeClock(){
        if(this.time_wide.equals("上午")){
            this.time_clock = this.time;
        }else if(this.time_wide.equals("下午")){
            this.time_clock = this.time.plusHours(12);
        }else {
            throw new IllegalArgumentException("time_wide 的值无效");
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    public Duration calculateRemainingTime() {
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
