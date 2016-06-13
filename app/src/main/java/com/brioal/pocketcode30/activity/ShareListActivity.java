package com.brioal.pocketcode30.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.brioal.brioallib.interfaces.ActivityFormat;
import com.brioal.brioallib.swipeback.app.SwipeBackActivity;
import com.brioal.brioallib.util.StatusBarUtils;
import com.brioal.brioallib.util.ThemeUtil;
import com.brioal.pocketcode30.R;
import com.brioal.pocketcode30.fragment.ShareListFragment;
import com.brioal.pocketcode30.util.Constants;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 查看分享的文章
 * 传入用户的ObjectId
 * 从本地读取和从网路加载
 * 提供添加分享功能 ,删除分享功能 , 返回分享的数据数量
 */
public class ShareListActivity extends SwipeBackActivity implements ActivityFormat {

    static {
        TAG = "ShareListInfo";
    }

    @Bind(R.id.toolBar)
    Toolbar mToolBar;

    @Override
    public void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_share_list);
        ButterKnife.bind(this);
    }


    //标题栏设置
    public void initBar() {
        mToolBar.setTitle("我的分享");
        setSupportActionBar(mToolBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //显示返回按钮
    }

    @Override
    public void initTheme() {
        String color = ThemeUtil.readThemeColor(mContext);
        mToolBar.setBackgroundColor(Color.parseColor(color));
        StatusBarUtils.setColor(this, color);
    }


    @Override
    public void loadDataNet() {
        mHandler.sendEmptyMessage(0);
    }

    @Override
    public void setView() {
        ShareListFragment fragment = new ShareListFragment();
        Bundle bundle = new Bundle();
        bundle.putString("UserId", Constants.getDataUtil(mContext).getUserLocal().getObjectId());
        fragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().add(R.id.share_container, fragment).commit();
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
}
