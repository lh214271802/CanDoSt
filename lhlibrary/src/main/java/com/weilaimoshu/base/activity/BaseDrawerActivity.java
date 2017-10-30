package com.weilaimoshu.base.activity;

import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.weilaimoshu.R;
import com.weilaimoshu.base.permission.PermissionBaseActivity;

/**
 * Created by liaohui on 2017/9/19.
 */

public abstract class BaseDrawerActivity extends PermissionBaseActivity {
    protected DrawerLayout drawerLayout;
    protected FrameLayout flActivityContainer;

    NavigationView.OnNavigationItemSelectedListener mListener = new NavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.feedback:
                    break;
                case R.id.apply:
                    break;
                case R.id.about:
                    break;
                case R.id.update:
                    break;
                case R.id.clear:
                    break;
                case R.id.logout:
                    break;
            }
            return false;
        }
    };

    @Override
    protected void setBaseView(@LayoutRes int layoutId) {
        contentView = LayoutInflater.from(this).inflate(R.layout.activity_drawer, null);
        setContentView(contentView);
        drawerLayout = (DrawerLayout) findViewById(R.id.root_layout);
        flActivityContainer = (FrameLayout) findViewById(R.id.activity_container);
        flActivityContainer.addView(LayoutInflater.from(this).inflate(layoutId, flActivityContainer, false));
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(mListener);
        navigationView.setItemIconTintList(null);
        navigationView.setItemTextColor(null);
    }
}
