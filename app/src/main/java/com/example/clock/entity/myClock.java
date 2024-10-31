package com.example.clock.entity;

import android.content.Context;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.clock.Sql.ClockDatabaseHelper;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;


/**
 *  闹钟类，负责记录闹钟的相关信息
 */
@RequiresApi(api = Build.VERSION_CODES.O)
public class myClock implements Serializable {
    /**
     *  ID 特征码
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
    private String ID;
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

    /**
     * 这里注意对每个需要用到的变量进行初始化，避免在传入值时出现空指针异常
     */
    public myClock(){
        this.ifuse = true;
        this.info = "";
        this.time_hour = 0;
        this.time_minute = 0;
        this.time_wide = "上午";
        this.repeat_times = new ArrayList<>();
        repeat_times.add(LocalDateTime.now().getDayOfWeek());
        this.time = LocalTime.of(time_hour, time_minute);
        this.ID = getNewID();
    }
    public myClock(int time_hour, int time_minute, String time_wide){
        this.time_hour = time_hour;     //小时数字
        this.time_minute = time_minute; //分钟数字
        this.time_wide = time_wide;     //上午还是下午
        this.repeat_times = new ArrayList<>();
        this.info = "";
        this.ifuse = true;
        time = LocalTime.of(time_hour, time_minute);
    }
    public myClock(String ID, int time_hour, int time_minute, String time_wide, String times, int ifuse, String info){
        /**
         *  初始化闹钟属性
         */
        this.ID = ID;
        this.time_hour = time_hour;     //小时数字
        this.time_minute = time_minute; //分钟数字
        this.time_wide = time_wide;     //上午还是下午
        this.repeat_times = getRepeatTimes_FromString(times);      //频率
        if (repeat_times.isEmpty()){
            repeat_times.add(LocalDateTime.now().getDayOfWeek());
        }
        this.ifuse = (ifuse == 1);              //是否使用，初始化为是
        this.info = info;               //备注
        time = LocalTime.of(time_hour, time_minute);

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
    public void setTimeWide(String time_wide){
        this.time_wide = time_wide;
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
            Collections.sort(repeat_times); // 保持排序
        }
    }
    public void setRepeatTimes(List<DayOfWeek> repeat_times){
        this.repeat_times = repeat_times;
    }
    public List<DayOfWeek> getRepeatTimes(){
        return this.repeat_times;
    }
    public List<DayOfWeek> getRepeatTimes_FromString(String repeat_times){
        List<DayOfWeek> daysOfWeek = new ArrayList<>();
        String[] dayNumbers = repeat_times.split(","); // 按逗号分割字符串

        for (String dayNumber : dayNumbers) {
            try {
                int dayIndex = Integer.parseInt(dayNumber.trim()); // 转换为整数并去除多余空格
                DayOfWeek dayOfWeek = DayOfWeek.of(dayIndex); // 将数字映射为 DayOfWeek 对象
                daysOfWeek.add(dayOfWeek);
            } catch (IllegalArgumentException e) {
                System.err.println("Invalid day number: " + dayNumber); // 捕获错误
            }
        }
        return daysOfWeek;
    }
    public String getRepeatTimes_ToString() {
        StringBuilder serialized = new StringBuilder();
        for (DayOfWeek day : repeat_times) {
            switch (day){
                case MONDAY:
                    serialized.append("1");
                    break;
                case TUESDAY:
                    serialized.append("2");
                    break;
                case WEDNESDAY:
                    serialized.append("3");
                    break;
                case THURSDAY:
                    serialized.append("4");
                    break;
                case FRIDAY:
                    serialized.append("5");
                    break;
                case SATURDAY:
                    serialized.append("6");
                    break;
                case SUNDAY:
                    serialized.append("7");
                    break;
                default:
                    break;
            }
            serialized.append(","); // 将每个日期以逗号分隔
        }
        if (serialized.length() > 0) {
            serialized.setLength(serialized.length() - 1); // 去掉最后一个逗号
        }
        return serialized.toString();
    }
    public void delRepeatTimes(DayOfWeek repeat_times){
        this.repeat_times.remove(repeat_times);
    }
    //显示优化
    public String showRepeatTimes(){
        // 检查是否包含所有星期
        if (repeat_times.containsAll(Arrays.asList(DayOfWeek.values()))) {
            return "每天";
        }
        return "周 " + getRepeatTimes_ToString();
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
    public void calculateTimeClock(){
        if(this.time_wide.equals("上午")){
            this.time_clock = this.time;
        }else if(this.time_wide.equals("下午")){
            this.time_clock = this.time.plusHours(12);
        }else {
            throw new IllegalArgumentException("time_wide 的值无效");
        }
    }
    public String getimeRemains_ToString() {
        now = LocalDateTime.now(); // 获取当前的时间和日期
        nextAlarm = null;
        calculateTimeClock();

        // 如果 repeat_times 为空，设置为今天的星期
        if (repeat_times.isEmpty()) {
            repeat_times.add(now.getDayOfWeek());
        }

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

        // 获取剩余的小时、分钟和秒
        long hours = time_remains.toHours();
        long minutes = time_remains.toMinutes() % 60; // 取余，得到当前小时的分钟
        long seconds = time_remains.getSeconds() % 60; // 取余，得到当前分钟的秒

        // 构造返回的字符串
        return String.format("%d小时%d分钟%d秒", hours, minutes, seconds);
    }
    public Duration getTimeRemains(){
        now = LocalDateTime.now(); // 获取当前的时间和日期
        nextAlarm = null;
        calculateTimeClock();

        // 如果 repeat_times 为空，设置为今天的星期
        if (repeat_times.isEmpty()) {
            repeat_times.add(now.getDayOfWeek());
        }

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

    //数据库操作
    public void setClockDatabaseHelper(Context context){
        clockDatabaseHelper = ClockDatabaseHelper.getInstance(context);
    }
    public void updateFromDatabse(){
        //这里编写将clock更新为数据库的数据
        myClock newClock = clockDatabaseHelper.getMyClock(ID);
        this.time_hour = newClock.getTimeHour();
        this.time_minute = newClock.getTimeMinute();
        this.time = LocalTime.of(time_hour, time_minute);
        this.time_wide = newClock.getTimeWide();
        this.repeat_times = newClock.getRepeatTimes();
        this.info = newClock.getInfo();
        this.ifuse = newClock.getIfuse();

    }
    public void updateToDatabase(){
        //这里编写将clock的属性更新至数据库的操作
        clockDatabaseHelper.updateClock(this, ID);
    }
    public void updateIfUseToDatabase(){
        //这里编写将clock的属性更新至数据库的操作
        clockDatabaseHelper.updateClockIfuse(ID, ifuse);
    }
    public void uploadToDatabase(){
        //这里编写将clock的属性上传至数据库的操作
        clockDatabaseHelper.insertClock(this);
    }
    public void delFromDatabase(){
        //这里编写将clock的属性从数据库移除的操作
        clockDatabaseHelper.deleteClock(ID);
    }

    //根据当地时间生成特征码
    private String getNewID(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        String id = dateFormat.format(new Date());
        return id;
    }
    //获取自身ID
    public String getID(){
        return ID;
    }

}
