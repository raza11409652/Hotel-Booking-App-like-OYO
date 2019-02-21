package com.flatondemand.user.fod;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.flatondemand.user.fod.app.Bookings;
import com.flatondemand.user.fod.app.BottomNavigationBehavior;

public class Dash extends AppCompatActivity {
    private ActionBar toolbar;
    private BottomNavigationView bottomNavigationView;
    private FrameLayout frameLayout;
    private Fragment homeFrag , bookingsFrag , profileFrag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash);
        toolbar = getSupportActionBar();

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.nav_bottom);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
// attaching bottom sheet behaviour - hide / show on scroll
        CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) navigation.getLayoutParams();
        layoutParams.setBehavior(new BottomNavigationBehavior());
        toolbar.setTitle("FOD");
        loadFragment(new HomeFrag());
    }

    @Override
    protected void onStart() {
        super.onStart();
     //   loadFragment(new HomeFrag());
    }

    @Override
    protected void onRestart() {
        super.onRestart();
       // loadFragment(new HomeFrag());
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            Fragment fragment;
            switch (menuItem.getItemId()) {
                case R.id.navigation_home:
                    toolbar.setTitle("FOD");
                    fragment= new HomeFrag();
                    loadFragment(fragment);
                    return true;
                case R.id.navigation_bookings:
                    toolbar.setTitle("Your Bookings");
                    fragment= new Bookings();
                    loadFragment(fragment);
                    return true;

                case R.id.navigation_profile:
                    fragment= new Profile();
                    toolbar.setTitle("Profile");
                    loadFragment(fragment);
                    return true;
              default:
                    fragment = new HomeFrag();
                    toolbar.setTitle("FOD");
                    loadFragment(fragment);
                    return true;


            }
           // return false;

        }
    };
    private void loadFragment(Fragment fragment) {
        // load fragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.mainFrame, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }




}
