package com.example.clock.MainActivity_pack;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentManager;

import com.example.clock.ClockInfo_pack.ClockInfo;
import com.example.clock.CountdownFinishReceiver.CountdownReceiver;
import com.example.clock.R;
import com.example.clock.Settings.Settings;
import com.example.clock.Sql.ClockDatabaseHelper;
import com.example.clock.entity.myClock;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.example.clock.entity.ClockUnitView;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@RequiresApi(api = Build.VERSION_CODES.O)
public class test extends AppCompatActivity {

    Toolbar toolbar;
    MenuInflater inflater;
    FloatingActionButton addClock;
    LinearLayout clockUnitContainer;

    private String TAG = "test";
    // 定义一个 ArrayList 用于存储 ClockUnitView 实例
    private ArrayList<ClockUnitView> clockUnitViews = new ArrayList<>();

    ClockDatabaseHelper clockDatabaseHelper;
    FragmentManager fragmentManager;
    private Handler handler;
    private Runnable runnable;

    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
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
        clockUnitContainer = findViewById(R.id.clockContainer);

        clockDatabaseHelper = ClockDatabaseHelper.getInstance(this);
        fragmentManager = getSupportFragmentManager();

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        addClock.setOnClickListener(new AddClockClickListener());

        getClockUnitViews();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 停止调用
        handler.removeCallbacks(runnable);
        // 注销广播接收器
        CountdownReceiver.getInstance().unregister(this);
    }
    

    //获取数据库的所有数据行并转换为ClockUnitView
    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    private void getClockUnitViews() {
        // 从数据库获取所有的 myClock 对象
        List<myClock> clockList = clockDatabaseHelper.getAllClocksAsList();

        // 遍历每个 myClock 对象并将其转换为 ClockUnitView
        for (myClock clock : clockList) {
            clockUnitViews.add(new ClockUnitView(this, fragmentManager, clockUnitContainer, this, clock));
        }
    }

    public void delClockUnitView(ClockUnitView clockUnitView){
        //删除ClockUnitView(遍历每个并查询其特征码，根据特征码删除)
        Iterator<ClockUnitView> iterator = clockUnitViews.iterator();
        while (iterator.hasNext()) {
            ClockUnitView clockUnitView_ = iterator.next();
            if (clockUnitView_.getID().equals(clockUnitView.getID())) {
                iterator.remove();  // 安全删除当前元素
                Log.e(TAG,"ClockUnitView:"+clockUnitView_.getID()+"已经被删除");
            }
        }
    }

    class AddClockClickListener implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(test.this, ClockInfo.class);
            intent.putExtra("viewfrom", "main");
            startActivityForResult(intent, 1);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (data.getBooleanExtra("addClockUnit", false)) {
                clockUnitViews.add(new ClockUnitView(this, fragmentManager, clockUnitContainer, this, (myClock) data.getSerializableExtra("myClock")));
            }
            if (data.getBooleanExtra("setClockUnit", false)) {
                /*
                //这里执行数组的每个成员执行自己的updateshow方法
                Iterator<ClockUnitView> iterator = clockUnitViews.iterator();
                while (iterator.hasNext()) {
                    iterator.next().updateShow();
                }
                 */
                //改为执行指定传来的成员进行单独更新，执行自己的updateshow方法
                Iterator<ClockUnitView> iterator = clockUnitViews.iterator();
                String updateID = data.getStringExtra("myClockID");
                while (iterator.hasNext()) {
                    ClockUnitView clockUnitView = iterator.next();
                    // 检查当前对象的 ID 是否与传入的 updateID 匹配
                    if (clockUnitView.getID().equals(updateID)) {
                        clockUnitView.updateFromDatabase();
                        clockUnitView.updateShow();
                        break; // 找到后可以退出循环
                    }
                }
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        menu.findItem(R.id.action_settings).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(@NonNull MenuItem menuItem) {
                Intent intent = new Intent();
                intent.setClass(test.this, Settings.class);
                startActivity(intent);
                return false;
            }
        });

        return true;
    }


}
