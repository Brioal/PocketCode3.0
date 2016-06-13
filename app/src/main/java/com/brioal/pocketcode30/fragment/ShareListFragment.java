package com.brioal.pocketcode30.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.brioal.brioallib.base.BaseFragment;
import com.brioal.brioallib.util.DataQuery;
import com.brioal.brioallib.util.NetWorkUtil;
import com.brioal.pocketcode30.R;
import com.brioal.pocketcode30.adapter.ShareListAdapter;
import com.brioal.pocketcode30.entiy.MessageEntity;

import java.util.List;

import cn.bmob.v3.listener.FindListener;

/**
 * 分享列表展示
 * Created by Brioal on 2016/5/31.
 */

public class ShareListFragment extends BaseFragment {
    static {
        TAG = "ShareListFragmentInfo";
    }

    private SwipeRefreshLayout mRefreshLayout;
    private RecyclerView mRecyclerView;
    public static ShareListFragment mFragment;
    private String mUserID;
    private List<MessageEntity> mList; //分享列表数据源
    private ShareListAdapter mAdapter; //分享列表适配器

    public static ShareListFragment getInstance(String mUserID) {
        if (mFragment == null) {
            mFragment = new ShareListFragment();
        }
        Bundle bundle = new Bundle();
        bundle.putString("UserId", mUserID);
        mFragment.setArguments(bundle);
        return mFragment;
    }


    @Override
    public void initData() {
        mUserID = getArguments().getString("UserId");
    }


    @Override
    public void initView() {
        super.initView();
        mRootView = inflater.inflate(R.layout.fragment_share_list, container, false);
        mRecyclerView = (RecyclerView) mRootView.findViewById(R.id.fragment_share_recyclerView);
        mRefreshLayout = (SwipeRefreshLayout) mRootView.findViewById(R.id.fragment_share_refreshLayout);
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
            DataQuery<MessageEntity> query = new DataQuery<>();
            query.getDatas(mContext, 100, 0, "-updatedAt", 0, "mAuthorId", mUserID, new FindListener<MessageEntity>() {
                @Override
                public void onSuccess(List<MessageEntity> list) {
                    Log.i(TAG, "onSuccess: 加载成功" + list.size() + "条发布内容");
                    mList = list;
                    mHandler.sendEmptyMessage(0);
                }

                @Override
                public void onError(int i, String s) {
                    Log.i(TAG, "onError: 加载失败" + s);
                }
            });
        }
    }

    @Override
    public void setView() {
        super.setView();
        if (mList.size() == 0) {
            mRecyclerView.setVisibility(View.GONE);
        } else {
            mRecyclerView.setVisibility(View.VISIBLE);
            mAdapter = new ShareListAdapter(mContext, mList);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
            mRecyclerView.setAdapter(mAdapter);
            if (mRefreshLayout.isRefreshing()) {
                mRefreshLayout.setRefreshing(false);
            }
        }

    }


}
