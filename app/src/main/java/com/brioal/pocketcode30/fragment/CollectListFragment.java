package com.brioal.pocketcode30.fragment;

import android.graphics.Color;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.brioal.brioallib.base.BaseFragment;
import com.brioal.brioallib.util.NetWorkUtil;
import com.brioal.pocketcode30.R;
import com.brioal.pocketcode30.adapter.CollectAdapter;
import com.brioal.pocketcode30.entiy.CollectEntity;
import com.brioal.pocketcode30.util.Constants;

import java.util.List;

import cn.bmob.v3.listener.FindListener;

/**
 * 收藏列表的
 * Created by Brioal on 2016/5/31.
 */

public class CollectListFragment extends BaseFragment {
    static {
        TAG = "CollectFragmentInfo";
    }

    public static CollectListFragment mFragment;
    private List<CollectEntity> mList;
    private CollectAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mRefreshLayout;

    public static CollectListFragment getInstance() {
        if (mFragment == null) {
            mFragment = new CollectListFragment();
        }
        return mFragment;
    }


    @Override
    public void initView() {
        mRootView = inflater.inflate(R.layout.fragment_collect, container, false);
        mRecyclerView = (RecyclerView) mRootView.findViewById(R.id.fragment_collect_recyclerView);
        mRefreshLayout = (SwipeRefreshLayout) mRootView.findViewById(R.id.fragment_collect_refreshLayout);
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
        if (NetWorkUtil.isNetworkConnected(mContext)) {
            Constants.getDataUtil(mContext).getCollectDataNet(Constants.getUser(mContext).getObjectId(), new FindListener<CollectEntity>() {
                @Override
                public void onSuccess(List<CollectEntity> list) {
                    mList = list;
                    Log.i(TAG, "onSuccess: 加载收藏成功" + list.size());
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
        if (mList.size() == 0) {
            mRecyclerView.setVisibility(View.GONE);
        } else {
            mRecyclerView.setVisibility(View.VISIBLE);
            mAdapter = new CollectAdapter(mContext, mList);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
            mRecyclerView.setAdapter(mAdapter);
            if (mRefreshLayout.isRefreshing()) {
                mRefreshLayout.setRefreshing(false);
            }
        }

    }
}
