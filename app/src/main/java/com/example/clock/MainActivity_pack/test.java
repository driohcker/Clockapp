package com.example.clock.MainActivity_pack;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
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
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.example.clock.entity.ClockUnitView;

public class test extends AppCompatActivity {

    Toolbar toolbar;
    MenuInflater inflater;
    FloatingActionButton addClock;
    LinearLayout clockUnitContainer;

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
    }

    class AddClockClickListener implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(test.this, ClockInfo.class);
            startActivityForResult(intent, 1);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            if (data.getBooleanExtra("addClockUnit", false)) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                new ClockUnitView(this, fragmentManager, clockUnitContainer);
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
