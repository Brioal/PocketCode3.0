package com.brioal.pocketcode30.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.brioal.brioallib.interfaces.ActivityFormat;
import com.brioal.brioallib.swipeback.app.SwipeBackActivity;
import com.brioal.brioallib.util.StatusBarUtils;
import com.brioal.brioallib.util.ThemeUtil;
import com.brioal.pocketcode30.R;
import com.brioal.pocketcode30.fragment.AttentionFragment;

import butterknife.Bind;
import butterknife.ButterKnife;

public class AttentionActivity extends SwipeBackActivity implements ActivityFormat {

    static {
        TAG = "AttentionInfo";
    }


    @Bind(R.id.toolBar)
    Toolbar mToolBar;

    @Override
    public void initData() {

    }

    @Override
    public void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_attention);
        ButterKnife.bind(this);
    }

    @Override
    public void initBar() {
        mToolBar.setTitle("我的关注");
        setSupportActionBar(mToolBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void initTheme() {
        String color = ThemeUtil.readThemeColor(mContext);
        mToolBar.setBackgroundColor(Color.parseColor(color));
        StatusBarUtils.setColor(this, color);
    }

    @Override
    public void loadDataLocal() {
        super.loadDataLocal();
        mHandler.sendEmptyMessage(0);
    }

    @Override
    public void setView() {
        getSupportFragmentManager().beginTransaction().add(R.id.attention_container, new AttentionFragment()).commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        loadDataNet();
    }
}
