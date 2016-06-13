package com.brioal.pocketcode30.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.brioal.brioallib.base.BaseActivity;
import com.brioal.brioallib.util.ClipboardUtil;
import com.brioal.brioallib.util.NetWorkUtil;
import com.brioal.brioallib.util.StatusBarUtils;
import com.brioal.brioallib.util.ThemeUtil;
import com.brioal.brioallib.util.ToastUtils;
import com.brioal.brioallib.view.AdaptGridView;
import com.brioal.pocketcode30.R;
import com.brioal.pocketcode30.adapter.TagAdapter;
import com.brioal.pocketcode30.entiy.MessageEntity;
import com.brioal.pocketcode30.entiy.User;
import com.brioal.pocketcode30.util.Constants;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.bmob.v3.listener.SaveListener;

public class AddMessageActivity extends BaseActivity implements View.OnClickListener {


    static {
        TAG = "AddMessageInfo";
    }

    @Bind(R.id.add_toolBar)
    Toolbar mToolBar;
    @Bind(R.id.add_url)
    EditText mEtUrl;
    @Bind(R.id.add_title)
    EditText mEtTitle;
    @Bind(R.id.add_desc)
    EditText mEtDesc;
    @Bind(R.id.add_tag)
    TextView addTag;
    @Bind(R.id.add_tag_layout)
    AdaptGridView addTagLayout;
    private User user;
    private List<String> mTags;
    private String title;
    private String desc;
    private String url;
    private ProgressDialog dialog;
    private AlertDialog.Builder builder;



    @Override
    public void initBar() {
        mToolBar.setTitle("分享文章");
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
    public void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_add_content);
        ButterKnife.bind(this);
        dialog = new ProgressDialog(mContext);
        initActions();
        mEtUrl.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!s.toString().contains("http") && !s.toString().contains("www")) {
                    //获取标题
                    if (NetWorkUtil.isNetworkConnected(mContext)) {
                        showProgressDialog("请稍等", "正在获取标题");
                        MessageAsy task = new MessageAsy();
                        task.execute(s.toString());
                    } else {
                        ToastUtils.showToast(mContext, "网络不可用,无法获取标题");
                    }
                }

            }
        });
    }

    public void showProgressDialog(String title, String message) {

        dialog.setTitle(title);
        dialog.setMessage(message);
        dialog.setCancelable(false);
    }

    public void showNoticeDialog(String title, String message) {
        builder = new AlertDialog.Builder(mContext);
        builder.setTitle(title).setMessage(message);
        builder.setCancelable(false);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }


    public void cleadTags() {
        mTags.clear();
        addTags();
        mTags.clear();
    }

    private TagAdapter mTagAdapter;

    public void addTags() {
        mTagAdapter = new TagAdapter(mContext, mTags);
        addTagLayout.setAdapter(mTagAdapter);
    }

    @Override
    public void setView() {
        addTags();
    }

    private void initActions() {
        addTag.setOnClickListener(this);
    }

    //获取当前属性
    public void initData() {
        mTags = Constants.getDataUtil(mContext).getLocalTag();
    }


    //添加文章
    private void addContent() {
        user = Constants.getDataUtil(mContext).getUserLocal();
        if (user == null) {
            ToastUtils.showToast(mContext, "未登陆,请登陆后再操作~");
            startActivity(new Intent(mContext, LoginAndRegisterActivity.class));
            return;
        }
        if (NetWorkUtil.isNetworkConnected(mContext)) {
            showProgressDialog("请稍等", "正在发表,请稍等....");
            title = mEtTitle.getText().toString();
            desc = mEtDesc.getText().toString();
            url = mEtUrl.getText().toString();
            //用户自己输入标题
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < mTags.size(); i++) {
                if (i == mTags.size() - 1) {
                    sb.append(mTags.get(i));
                } else {
                    sb.append(mTags.get(i) + ",");
                }
            }
            String tag = sb.toString();
            MessageEntity model = new MessageEntity(user.getObjectId(), title, desc, tag,  0, 0, 0, 0, url, user.getUsername(),user.getHeadUrl(mContext));
            model.save(mContext, new SaveListener() {
                @Override
                public void onSuccess() {
                    Log.i(TAG, "onSuccess: 保存数据到网络成功");
                    if (dialog.isShowing()) {
                        dialog.dismiss();
                    }
                    setResult(RESULT_OK);
                    finish();
                    overridePendingTransition(R.anim.anim_pop_in, R.anim.anim_pop_out);
                }

                @Override
                public void onFailure(int i, String s) {
                    showNoticeDialog("错误", s);
                }
            });

        } else {
            showNoticeDialog("错误", "当前网络不可用,请等网络可用时重试");
        }
    }

    //判断是否有内容，有就保存
    public void judgeContent() {
        if (mEtUrl.getText().toString().isEmpty()) {
            finish();
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
            builder.setTitle("提示").setMessage("存在正在编辑的内容，现在退出将会丢失所有内容，是否退出").setNegativeButton("退出", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();

                }
            }).setPositiveButton("不退出", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            }).create().show();
        }
    }


    protected void onResume() {
        super.onResume();
        String content = ClipboardUtil.getContent(mContext);
        if (content != null) {
            SharedPreferences preferences = getSharedPreferences("Url", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            String lastContent = "";
            if (content.contains("http") || content.contains("www")) {
                lastContent = preferences.getString("lastContent", "");
                if (!lastContent.equals(content)) {
                    mEtUrl.setText(content);
                    MessageAsy asy = new MessageAsy();
                    asy.execute(content);
                    editor.putString("lastContent", content);
                    editor.apply();
                }
            }
        }
        initTheme();
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
                judgeContent();
                break;
            case R.id.action_add:
                if (!mEtUrl.getText().toString().isEmpty()) {
                    addContent();
                } else {
                    mEtUrl.setError("链接不能为空");
                    mEtUrl.requestFocus();
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_tag:
                cleadTags();
                startActivityForResult(new Intent(mContext, TagChooseActivity.class), 0);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 0) {
            Log.i(TAG, "onActivityResult: ");
            mTags = (List<String>) data.getSerializableExtra("Tags");
            addTags();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        Constants.getDataUtil(mContext).saveTagLocal(mTags);
    }


    private class MessageAsy extends AsyncTask<String, Integer, String> {
        public String doInBackground(String... params) {
            String url = params[0];
            String title = null;
            try {
                Document localDocument = Jsoup.connect(url).get();
                title = localDocument.title();
            } catch (IOException localIOException) {
                localIOException.printStackTrace();
            }

            return title;
        }


        public void onPostExecute(String params) {
            Log.i(TAG, "onPostExecute: " + params);
            title = params;
            mEtTitle.setText(title);
            dialog.dismiss();
        }
    }
}
