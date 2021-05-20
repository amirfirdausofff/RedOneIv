package com.amir.redoneiv.Activity.Main;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;

import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.amir.redoneiv.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.amir.redoneiv.Activity.Main.DashboardFragment.swipe_container;

public class MainActivity extends AppCompatActivity {

    private static long back_pressed;
    public static Fragment fragment1;
    public static Fragment fragment2;
    public static FragmentManager fm;
    public static Fragment active;
    @BindView(R.id.frame_layout) FrameLayout frame_layout;
    @BindView(R.id.navigation) BottomNavigationView navigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        fm = getSupportFragmentManager();
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        fragment1 = new DashboardFragment();
        fragment2 = new RewardFragment();
        active = fragment1;

        fm.beginTransaction().add(R.id.frame_layout, fragment2, "2").hide(fragment2).commit();
        fm.beginTransaction().add(R.id.frame_layout,fragment1, "1").commit();
    }


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment;
            switch (item.getItemId()) {
                case R.id.action_item1:
                    if(active == fragment1){
                    }else{
                        fm.beginTransaction().show(fragment1).hide(active).commit();
                        DashboardFragment.getProfile();
                        DashboardFragment.getUsage();
                    }

                    active = fragment1;
                    return true;
                case R.id.action_item2:
                    if(active == fragment2){
                    }else{
                        fm.beginTransaction().show(fragment2).hide(active).commit();
                    }
                    active = fragment2;
                    return true;
            }

            return false;
        }
    };


    @Override
    public void onBackPressed() {
        if (back_pressed + 2000 > System.currentTimeMillis())  moveTaskToBack(true);
        else Toast.makeText(getBaseContext(), "Press once again to exit!", Toast.LENGTH_SHORT).show();
        back_pressed = System.currentTimeMillis();
    }
}
