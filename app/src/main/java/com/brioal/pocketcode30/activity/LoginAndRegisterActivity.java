package com.brioal.pocketcode30.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.brioal.brioallib.base.BaseActivity;
import com.brioal.brioallib.util.StatusBarUtils;
import com.brioal.brioallib.util.ThemeUtil;
import com.brioal.pocketcode30.R;
import com.brioal.pocketcode30.fragment.JoinFragment;
import com.brioal.pocketcode30.fragment.LoginFragment;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.ButterKnife;

public class LoginAndRegisterActivity extends BaseActivity {

    static {
        TAG = "LoginActInfo";
    }
    TabLayout mTab;
    ViewPager mContainer;
    private String[] mTitles = new String[]{
            "登录",
            "加入我们"
    };

    private ViewPagerAdapter mAdapter;

    @Override
    public void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        setContentView(R.layout.activity_login_and_register);
        ButterKnife.bind(this);
        initId();
        initTab();
    }

    @Override
    public void initTheme() {
        super.initTheme();
        String color = ThemeUtil.readThemeColor(mContext);
        StatusBarUtils.setColor(mContext, color);
    }

    private void initTab() {
        mAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        mContainer.setAdapter(mAdapter);
        mTab.setupWithViewPager(mContainer);
        mContainer.setCurrentItem(0);
        StatusBarUtils.setTranslucent(this);
    }

    private void initId() {
        mTab = (TabLayout) findViewById(R.id.login_tab);
        mContainer = (ViewPager) findViewById(R.id.login_container);
    }

    private class ViewPagerAdapter extends FragmentStatePagerAdapter {
        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return LoginFragment.getInstance();
                case 1:
                    return JoinFragment.getInstance();
            }
            return null;
        }

        @Override
        public int getCount() {
            return mTitles.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mTitles[position];
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0 && resultCode == RESULT_OK) {
            setResult(RESULT_OK);
            finish();
        }
    }

    //进入登陆界面
    public static void startLogin(final Activity activity, final View container) {
        Snackbar.make(container, "未登陆，即将跳转登陆界面", Snackbar.LENGTH_SHORT).show();
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Intent intent = new Intent(activity, LoginAndRegisterActivity.class);
                activity.startActivityForResult(intent, 0);
            }
        }, 1500);
    }
}
