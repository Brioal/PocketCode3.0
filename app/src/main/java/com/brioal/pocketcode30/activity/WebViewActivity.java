package com.brioal.pocketcode30.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.GeolocationPermissions;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.brioal.brioallib.interfaces.onCheckExitListener;
import com.brioal.brioallib.swipeback.app.SwipeBackActivity;
import com.brioal.brioallib.util.ClipboardUtil;
import com.brioal.brioallib.util.DataQuery;
import com.brioal.brioallib.util.StatusBarUtils;
import com.brioal.brioallib.util.ThemeUtil;
import com.brioal.brioallib.util.ToastUtils;
import com.brioal.brioallib.view.CircleImageView;
import com.brioal.brioallib.view.ProgressWebView;
import com.brioal.pocketcode30.R;
import com.brioal.pocketcode30.entiy.AttentionEntity;
import com.brioal.pocketcode30.entiy.CollectEntity;
import com.brioal.pocketcode30.entiy.MessageEntity;
import com.brioal.pocketcode30.entiy.PraiseEntity;
import com.brioal.pocketcode30.entiy.User;
import com.brioal.pocketcode30.util.Constants;
import com.bumptech.glide.Glide;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.DeleteListener;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.GetListener;
import cn.bmob.v3.listener.SaveListener;

/**
 * 显示文章内容
 * 传入ContentModel或者MessageId
 */
public class WebViewActivity extends SwipeBackActivity implements View.OnClickListener {


    static {
        TAG = "WebActInfo";
    }

    @Bind(R.id.web_toolbar)
    Toolbar mToolbar;
    @Bind(R.id.web_webView)
    ProgressWebView mWebView;
    @Bind(R.id.web_commit)
    EditText mCommentBtn;
    @Bind(R.id.web_collect)
    CheckBox mCollect;
    @Bind(R.id.web_parise)
    CheckBox mPraise;
    @Bind(R.id.web_main_container)
    CoordinatorLayout mainContainer;
    @Bind(R.id.web_bottomLayout)
    LinearLayout mBottomLayout;
    @Bind(R.id.web_author_head)
    CircleImageView mHead;
    @Bind(R.id.web_author_name)
    TextView mName;
    @Bind(R.id.web_author_btn_attention)
    Button mBtnAttention;
    @Bind(R.id.web_head_layout)
    LinearLayout webHeadLayout;

    private MessageEntity mModel;
    private String mContentId;
    private String mUrl;
    private String mTitle;
    private String mUserId; //头像的链接
    private int Praise;
    private int Collect;
    private int Read;
    private boolean isAttention = false; //是否关注
    private String attentionId; //如果已关注,存储关注的id

    public void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_webview);
        ButterKnife.bind(this);
    }

    @Override
    public void loadDataLocal() {
        super.loadDataLocal();
        mModel = (MessageEntity) getIntent().getSerializableExtra("Message");
        if (mModel != null) {
            mHandler.sendEmptyMessage(0);
        } else {
            DataQuery<MessageEntity> query = new DataQuery<>();
            query.getData(mContext, getIntent().getStringExtra("MessageId"), new GetListener<MessageEntity>() {
                @Override
                public void onSuccess(MessageEntity messageEntity) {
                    mModel = messageEntity;
                    Log.i(TAG, "onSuccess: 查询从Banner来的数据成功");
                    mHandler.sendEmptyMessage(0);
                }

                @Override
                public void onFailure(int i, String s) {
                    Log.i(TAG, "onFailure: 查询从Banner来的数据失败" + s);
                    ToastUtils.showToast(mContext, "onFailure: 查询从Banner来的数据失败" + s);
                }
            });
        }
    }

    public void initBar() {
        mToolbar.setTitle(mTitle);
        mToolbar.setTitleTextColor(getResources().getColor(R.color.colorWhite));
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void initTheme() {
        String color = ThemeUtil.readThemeColor(mContext);
        mToolbar.setBackgroundColor(Color.parseColor(color));
        StatusBarUtils.setColor(this, color);
    }


    @Override
    public void setView() {
        super.setView();
        mContentId = mModel.getObjectId();
        mUrl = mModel.getUrl();
        mTitle = mModel.getTitle();
        mUserId = mModel.getAuthorId();
        initBottomLayout();
        initTopLayout();
        initWebView();

    }

    //初始化头部Layout
    public void initTopLayout() {
        BmobQuery<User> query1 = new BmobQuery<>();
        query1.getObject(mContext, mUserId, new GetListener<User>() {
            @Override
            public void onSuccess(final User user) {
                Log.i(TAG, "onSuccess: 加载作者数据成功");
                Glide.with(getApplicationContext()).load(user.getHeadUrl(mContext)).into(mHead);
                mName.setText(user.getUsername());
                mHead.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(mContext, UserInfoActivity.class);
                        Log.i(TAG, "onClick: " + user.getUsername());
                        Log.i(TAG, "onClick: " + user.getObjectId());
                        intent.putExtra("UserId", user.getObjectId());
                        intent.putExtra("IsAttention", isAttention);
                        if (attentionId != null) {
                            intent.putExtra("AttentionId", attentionId);
                        }
                        startActivityForResult(intent, 0); //启动作者信息界面
                    }
                });

            }

            @Override
            public void onFailure(int i, String s) {
                Log.i(TAG, "onFailure: 加载作者数据失败" + s);
            }
        });
        User user = Constants.getDataUtil(mContext).getUserLocal();
        if (user == null) { // 未登陆
            mBtnAttention.setVisibility(View.VISIBLE);
        } else { //已登录 ,判断是否关注了
            DataQuery<AttentionEntity> query = new DataQuery<>();
            query.getDatas(mContext, 10, 0, null, -1, null, null, new FindListener<AttentionEntity>() {
                @Override
                public void onSuccess(List<AttentionEntity> list) {
                    Log.i(TAG, "onSuccess: 查找关注记录成功");
                    if (list.size() == 0) { //未关注
                        isAttention = false;
                        mBtnAttention.setVisibility(View.VISIBLE); //按钮可见
                        mBtnAttention.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Log.i(TAG, "onClick: 点击关注按钮");
                                AttentionEntity enity = new AttentionEntity(Constants.getDataUtil(mContext).getUserLocal().getObjectId(), mUserId);
                                enity.save(mContext, new SaveListener() {
                                    @Override
                                    public void onSuccess() {
                                        Log.i(TAG, "onSuccess: 添加关注成功");
                                        ToastUtils.showToast(mContext, "添加关注成功");
                                        mBtnAttention.setVisibility(View.GONE);
                                    }

                                    @Override
                                    public void onFailure(int i, String s) {
                                        Log.i(TAG, "onFailure: 添加关注失败");
                                        ToastUtils.showToast(mContext, "添加关注失败" + s);
                                    }
                                });

                            }
                        });
                    } else {
                        //已关注
                        isAttention = true;
                        attentionId = list.get(0).getObjectId();
                        mBtnAttention.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onError(int i, String s) {

                }
            });


        }
    }

    //初始化底部layout
    public void initBottomLayout() {
        Constants.getDataUtil(mContext).isParise(mUserId, mModel.getObjectId(), new onCheckExitListener() {
            @Override
            public void exit(String attentionId) {
                mPraise.setChecked(true);
            }

            @Override
            public void noExit() {
                mPraise.setChecked(false);
            }
        });
        Constants.getDataUtil(mContext).isCollect(mUserId, mModel.getObjectId(), new onCheckExitListener() {
            @Override
            public void exit(String attentionId) {
                mCollect.setChecked(true);
            }

            @Override
            public void noExit() {
                mCollect.setChecked(false);
            }
        });

        mPraise.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mModel.increment("mPraise");
                    PraiseEntity entity = new PraiseEntity(mUserId, mModel.getObjectId());
                    entity.save(mContext, new SaveListener() {
                        @Override
                        public void onSuccess() {
                            Log.i(TAG, "onSuccess: 点赞记录保存成功");
                        }

                        @Override
                        public void onFailure(int i, String s) {
                            ToastUtils.showToast(mContext, s);
                        }
                    });
                    Praise++;
                } else {
                    mModel.increment("mPraise", -1);
                    Praise--;
                    Constants.getDataUtil(mContext).isParise(mUserId, mModel.getObjectId(), new onCheckExitListener() {
                        @Override
                        public void exit(String attentionId) {
                            Log.i(TAG, "exit: 存在点赞记录");
                            PraiseEntity entity = new PraiseEntity();
                            entity.delete(mContext, attentionId, new DeleteListener() {
                                @Override
                                public void onSuccess() {
                                    Log.i(TAG, "onSuccess: 删除成功");
                                }

                                @Override
                                public void onFailure(int i, String s) {

                                }
                            });
                        }

                        @Override
                        public void noExit() {

                        }
                    });
                }

            }
        });
        Log.i(TAG, "initBottomLayout: " + mCollect.isChecked());
        mCollect.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (Constants.getDataUtil(mContext).getUserLocal() == null) {
                    LoginAndRegisterActivity.startLogin(WebViewActivity.this, mainContainer);
                } else {
                    if (isChecked) {
                        mModel.increment("mCollect");
                        CollectEntity entity = new CollectEntity(Constants.getUser(mContext).getObjectId(), mModel.getObjectId());
                        entity.save(mContext, new SaveListener() {
                            @Override
                            public void onSuccess() {
                                Log.i(TAG, "onSuccess: 文章收藏成功");
                            }

                            @Override
                            public void onFailure(int i, String s) {

                            }
                        });
                        Collect++;
                    } else {
                        mModel.increment("mCollect", -1);
                        Collect--;
                        Constants.getDataUtil(mContext).isCollect(mUserId, mModel.getObjectId(), new onCheckExitListener() {
                            @Override
                            public void exit(String attentionId) {
                                CollectEntity entity = new CollectEntity();
                                entity.delete(mContext, attentionId, new DeleteListener() {
                                    @Override
                                    public void onSuccess() {
                                        Log.i(TAG, "onSuccess: 取消收藏成功");
                                        ToastUtils.showToast(mContext, "取消收藏成功");
                                    }

                                    @Override
                                    public void onFailure(int i, String s) {

                                    }
                                });
                            }

                            @Override
                            public void noExit() {

                            }
                        });
                    }
                }

            }
        });
        mCommentBtn.setText("查看" + mModel.getComment() + "条评论");
        mCommentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, CommentActivity.class);
                intent.putExtra("MessageId", mContentId);
                startActivity(intent);
            }
        });
    }

    public void initWebView() {
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });

        WebSettings settings = mWebView.getSettings();
        settings.setJavaScriptEnabled(true);  //支持js
        settings.setSupportZoom(true);  //支持缩放，默认为true。是下面那个的前提。
        settings.setBuiltInZoomControls(true); //设置内置的缩放控件。
        settings.setDisplayZoomControls(false); //隐藏原生的缩放控件
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN); //支持内容重新布局
        settings.supportMultipleWindows();  //多窗口
        settings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);  //关闭webview中缓存
        settings.setAllowFileAccess(true);  //设置可以访问文件
        settings.setNeedInitialFocus(true); //当webview调用requestFocus时为webview设置节点
        settings.setLoadsImagesAutomatically(true);  //支持自动加载图片
        settings.setDefaultTextEncodingName("utf-8");//设置编码格式
        WebSettings mWebSettings = mWebView.getSettings();
        mWebSettings.setSupportZoom(true);
        mWebSettings.setLoadWithOverviewMode(true);
        mWebSettings.setUseWideViewPort(true);
        mWebSettings.setDefaultTextEncodingName("utf-8");
        mWebSettings.setLoadsImagesAutomatically(true);
        //调用JS方法.安卓版本大于17,加上注解 @JavascriptInterface
        saveData(mWebSettings);
        mWebView.setWebChromeClient(webChromeClient);
        mWebView.setWebViewClient(webViewClient);
        mWebView.loadUrl(mUrl);
    }


    private void saveData(WebSettings mWebSettings) {
        mWebSettings.setDomStorageEnabled(true);
        mWebSettings.setDatabaseEnabled(true);
        mWebSettings.setAppCacheEnabled(true);
        String appCachePath = getApplicationContext().getCacheDir().getAbsolutePath();
        mWebSettings.setAppCachePath(appCachePath);
    }

    WebViewClient webViewClient = new WebViewClient() {

        /**
         * 多页面在同一个WebView中打开，就是不新建activity或者调用系统浏览器打开
         */
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

    };

    WebChromeClient webChromeClient = new WebChromeClient() {

        @Override
        public void onReceivedIcon(WebView view, Bitmap icon) {
            super.onReceivedIcon(view, icon);
        }

        @Override
        public void onGeolocationPermissionsHidePrompt() {
            super.onGeolocationPermissionsHidePrompt();
        }

        @Override
        public void onGeolocationPermissionsShowPrompt(final String origin, final GeolocationPermissions.Callback callback) {
            callback.invoke(origin, true, false);//注意个函数，第二个参数就是是否同意定位权限，第三个是是否希望内核记住
            super.onGeolocationPermissionsShowPrompt(origin, callback);
        }

        @Override
        public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);
            if (title != null) {
                getSupportActionBar().setTitle(title);
            }
        }

        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);

        }
    };


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && mWebView.canGoBack()) {
            mWebView.goBack();
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        if (mWebView != null) {
            mWebView.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
            mWebView.clearHistory();

            ((ViewGroup) mWebView.getParent()).removeView(mWebView);
            mWebView.destroy();
            mWebView = null;
        }
        super.onDestroy();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflator = new MenuInflater(this);
        //装填R.menu.my_menu对应的菜单,并添加到menu中
        inflator.inflate(R.menu.menu_web, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.action_refresh:
                mWebView.loadUrl(mUrl);
                break;
            case R.id.action_up:
                mWebView.scrollTo(0, 0);
                break;
            case R.id.action_copy:
                ClipboardUtil.setContent(mContext, mUrl);

                break;

            case R.id.action_share:
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, mUrl);
                sendIntent.setType("text/plain");
                startActivity(Intent.createChooser(sendIntent, "选择要分享的方式"));
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.web_author_head:
                Intent intent = new Intent(mContext, UserInfoActivity.class);
                intent.putExtra("UserId", mUserId);
                startActivityForResult(intent, 0);
                break;
            case R.id.web_author_name:
                Intent intent1 = new Intent(mContext, UserInfoActivity.class);
                intent1.putExtra("UserId", mUserId);
                startActivityForResult(intent1, 0);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            mBtnAttention.setVisibility(View.GONE);
            isAttention = true;
        } else if (resultCode == RESULT_CANCELED) {
            mBtnAttention.setVisibility(View.VISIBLE);
            isAttention = false;
        }
    }
}
