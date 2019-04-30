package com.mimikko.mimikkoui.bottombar;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.aleaf.android.bottombar.BottomBarView;

public class MainActivity extends AppCompatActivity {

    BottomBarView bottomBarView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        bottomBarView = findViewById(R.id.bottomBarView);
        bottomBarView.setOnItemSelectedListener(new BottomBarView.OnItemSelectedListener() {
            @Override
            public boolean onItemSelected(int id) {

                return true;
            }
        });
    }
}
