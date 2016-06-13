package com.brioal.pocketcode30.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.brioal.brioallib.base.BaseActivity;
import com.brioal.brioallib.util.StatusBarUtils;
import com.brioal.brioallib.util.ThemeUtil;
import com.brioal.pocketcode30.R;
import com.brioal.pocketcode30.adapter.TagChooseAdapter;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

public class TagChooseActivity extends BaseActivity {


    static {
        TAG = "TagChooseActInfo";
    }
    @Bind(R.id.toolBar)
    Toolbar mToolBar;
    @Bind(R.id.tag_choose_recyclerView)
    RecyclerView mRecyclerView;
    private TagChooseAdapter mAdapter;

    @Override
    public void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_tag_choose);
        ButterKnife.bind(this);
    }

    @Override
    public void initBar() {
        mToolBar.setTitle("选择标签");
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
    public void loadDataNet() {
        super.loadDataNet();
        mHandler.sendEmptyMessage(0);
    }

    @Override
    public void setView() {
        if (mAdapter == null) {
            mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
            mAdapter = new TagChooseAdapter(mContext);
            mRecyclerView.setAdapter(mAdapter);
        } else {
            mAdapter.notifyDataSetChanged();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_content, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.action_add:
                ArrayList<String> tag = mAdapter.getmSelectTags();
                Intent intent = new Intent();
                intent.putExtra("Tags", tag);
                setResult(RESULT_OK, intent);
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
