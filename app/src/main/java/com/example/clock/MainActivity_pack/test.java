package com.example.clock.MainActivity_pack;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
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
import com.example.clock.R;
import com.example.clock.Settings.Settings;
import com.example.clock.entity.myClock;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.example.clock.entity.ClockUnitView;

import java.util.ArrayList;
import java.util.List;

@RequiresApi(api = Build.VERSION_CODES.O)
public class test extends AppCompatActivity {

    Toolbar toolbar;
    MenuInflater inflater;
    FloatingActionButton addClock;
    LinearLayout clockUnitContainer;

    // 定义一个 ArrayList 用于存储 ClockUnitView 实例
    private ArrayList<ClockUnitView> clockUnitViews = new ArrayList<>();


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

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        addClock.setOnClickListener(new AddClockClickListener());

        getClockUnitViews();
    }

    private void getClockUnitViews() {
        //获取数据库的所有数据行并转换为ClockUnitView
    }

    public void delClockUnitView(ClockUnitView clockUnitView){
        //删除ClockUnitView(遍历每个并查询其特征码，根据特征码删除)
    }

    class AddClockClickListener implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(test.this, ClockInfo.class);
            intent.putExtra("viewfrom", "main");
            startActivityForResult(intent, 1);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (data.getBooleanExtra("addClockUnit", false)) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                clockUnitViews.add(new ClockUnitView(this, fragmentManager, clockUnitContainer, this, (myClock) data.getSerializableExtra("myClock")));
            }
            if (data.getBooleanExtra("setClockUnit", false)) {
                //这里执行数组的每个成员执行自己的update方法
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
