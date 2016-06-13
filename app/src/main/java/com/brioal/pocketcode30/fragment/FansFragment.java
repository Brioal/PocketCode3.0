package com.brioal.pocketcode30.fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.brioal.brioallib.base.BaseFragment;
import com.brioal.brioallib.util.NetWorkUtil;
import com.brioal.pocketcode30.R;
import com.brioal.pocketcode30.adapter.AttentionAdapter;
import com.brioal.pocketcode30.entiy.AttentionEntity;
import com.brioal.pocketcode30.entiy.User;
import com.brioal.pocketcode30.util.Constants;

import java.util.List;

import cn.bmob.v3.listener.FindListener;

/**
 * 粉丝显示界面
 * Created by Brioal on 2016/6/1.
 */

public class FansFragment extends BaseFragment {
    public static FansFragment mFragment;
    private final String TAG = "FansFragmentInfo";
    private User user;

    public static FansFragment getInstance() {
        if (mFragment == null) {
            mFragment = new FansFragment();
        }
        return mFragment;
    }

    private RecyclerView mRecyclerView;
    private List<AttentionEntity> mList;
    private AttentionAdapter mAdapter;


    @Override
    public void initData() {
        super.initData();
        user = Constants.getDataUtil(mContext).getUserLocal();
    }

    @Override
    public void loadDataNet() {

        if (NetWorkUtil.isNetworkConnected(mContext)) {
            Constants.getDataUtil(mContext).getFansDataNet(Constants.getUser(mContext).getObjectId(), new FindListener<AttentionEntity>() {
                @Override
                public void onSuccess(List<AttentionEntity> list) {
                    Log.i(TAG, "onSuccess: 加载粉丝成功" + list.size());
                    mList = list;
                    mHandler.sendEmptyMessage(0);
                }

                @Override
                public void onError(int i, String s) {
                    Log.i(TAG, "onError: 加载粉丝失败" + s);
                }
            });
        }
    }

    @Override
    public void initView() {
        mRootView = inflater.inflate(R.layout.fragment_fans, container, false);
        mRecyclerView = (RecyclerView) mRootView.findViewById(R.id.fragment_fans_recyclerView);
    }

    @Override
    public void setView() {
        if (mList.size() == 0) {
            mRecyclerView.setVisibility(View.GONE);
        } else {
            mRecyclerView.setVisibility(View.VISIBLE);
            mAdapter = new AttentionAdapter(mContext, mList, AttentionAdapter.TYPE_FANS);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
            mRecyclerView.setAdapter(mAdapter);
        }

    }

}
