package com.brioal.pocketcode30.fragment;

import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.brioal.brioallib.base.BaseFragment;
import com.brioal.brioallib.interfaces.OnLoaderMoreListener;
import com.brioal.brioallib.util.NetWorkUtil;
import com.brioal.pocketcode30.R;
import com.brioal.pocketcode30.adapter.AttentionAdapter;
import com.brioal.pocketcode30.entiy.AttentionEntity;
import com.brioal.pocketcode30.util.Constants;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;


/**
 * Created by Brioal on 2016/5/31.
 */

public class AttentionFragment extends BaseFragment implements OnLoaderMoreListener {
    static {
        TAG = "AttentionInfo";
    }

    public static AttentionFragment mFragment;
    private AttentionAdapter mAdapter;
    private List<AttentionEntity> mList;
    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mRefreshLayout;
    private int mCount = 0;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0) {
                setView();
            } else if (msg.what == 1) {
                mAdapter.notifyItemRangeChanged(mCount, mList.size());
            }
        }
    };

    public static AttentionFragment getInstance() {
        if (mFragment == null) {
            mFragment = new AttentionFragment();
        }
        return mFragment;
    }

    @Override
    public void initData() {
    }

    @Override
    public void initView() {
        mRootView = inflater.inflate(R.layout.fragment_attention, container, false);
        mRecyclerView = (RecyclerView) mRootView.findViewById(R.id.fragment_attention_recyclerView);
        mRefreshLayout = (SwipeRefreshLayout) mRootView.findViewById(R.id.fragment_attention_refreshLayout);
        mRefreshLayout.setColorSchemeColors(Color.BLUE, Color.GREEN, Color.RED);
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadDataNet();
            }
        });
    }

    @Override
    public void loadDataNet() {
        super.loadDataNet();
        if (NetWorkUtil.isNetworkConnected(mContext)) {
            Constants.getDataUtil(mContext).getAttentionDataNet(Constants.getUser(mContext).getObjectId(), new FindListener<AttentionEntity>() {
                @Override
                public void onSuccess(List<AttentionEntity> list) {
                    Log.i(TAG, "onSuccess: 获取关注数据成功");
                    mList = list;
                        mHandler.sendEmptyMessage(0);
                }

                @Override
                public void onError(int i, String s) {
                    Log.i(TAG, "onError: 加载关注数据失败" + s);
                }
            });
        }
    }

    @Override
    public void setView() {
        if (mList.size() == 0) {
            mRecyclerView.setVisibility(View.GONE);
        } else {
            mRecyclerView.setVisibility(View.VISIBLE);
            mAdapter = new AttentionAdapter(mContext, mList, AttentionAdapter.TYPE_ATTENTION);
            mAdapter.setLoaderMoreListener(this);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
            mRecyclerView.setAdapter(mAdapter);
            if (mRefreshLayout.isRefreshing()) {
                mRefreshLayout.setRefreshing(false);
            }
        }

    }

    @Override
    public void loadMore() {
        mCount = mList.size();
        BmobQuery<AttentionEntity> queryContent = new BmobQuery<>();
        queryContent.order("-createdAt");
        queryContent.setSkip(mCount);
        queryContent.setLimit(15);
        queryContent.findObjects(mContext, new FindListener<AttentionEntity>() {
            @Override
            public void onSuccess(List<AttentionEntity> list) {
                Log.i(TAG, "onSuccess: 加载成功" + list.size() + "条内容");
                for (int i = 0; i < list.size(); i++) {
                    mList.add(list.get(i));
                }
                mHandler.sendEmptyMessage(1);
            }

            @Override
            public void onError(int i, String s) {
                Log.i(TAG, "onError: 加载失败" + s);
            }
        });
    }
}
