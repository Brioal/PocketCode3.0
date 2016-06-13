package com.brioal.pocketcode30.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.brioal.brioallib.view.CircleImageView;
import com.brioal.pocketcode30.R;
import com.brioal.pocketcode30.activity.WebViewActivity;
import com.brioal.pocketcode30.entiy.CollectEntity;
import com.brioal.pocketcode30.entiy.MessageEntity;
import com.brioal.pocketcode30.entiy.User;
import com.bumptech.glide.Glide;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.GetListener;

/**
 * 收藏列表的适配器
 * Created by Brioal on 2016/5/31.
 */

public class CollectAdapter extends RecyclerView.Adapter<CollectAdapter.CollectViewHolder> {

    private Context mContext;
    private List<CollectEntity> mList;
    private String TAG = "CollectAdapterInfo";

    public CollectAdapter(Context mContext, List<CollectEntity> mList) {
        this.mContext = mContext;
        this.mList = mList;
    }

    @Override
    public CollectViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.item_content, parent, false);
        return new CollectViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final CollectViewHolder holder, int position) {
        CollectEntity enity = mList.get(position);
        BmobQuery<MessageEntity> messageQuery = new BmobQuery<>();
        messageQuery.getObject(mContext, enity.getMessageId(), new GetListener<MessageEntity>() {
            @Override
            public void onSuccess(final MessageEntity messageEntity) {
                Log.i(TAG, "onSuccess:加载文章成功 ");
                holder.mTitle.setText(messageEntity.getTitle());
                holder.mDesc.setText(messageEntity.getDesc());
                BmobQuery<User> query = new BmobQuery<User>();
                query.addWhereEqualTo("objectId", messageEntity.getAuthorId());
                query.findObjects(mContext, new FindListener<User>() {
                    @Override
                    public void onSuccess(List<User> object) {
                        Log.i(TAG, "onSuccess: 查询用户成功");
                        User user = object.get(0);
                        String mUrl = user.getHeadUrl(mContext);
                        Glide.with(mContext).load(mUrl).into((holder).mHead);
                        messageEntity.setAuthorHeadUrl(mUrl);
                    }

                    @Override
                    public void onError(int code, String msg) {
                        Log.i(TAG, "onError: 查询失败");
                    }
                });
                holder.mClassify.setText(messageEntity.getClassify());
                holder.mPraise.setText(messageEntity.getPraise() + "");
                holder.mMsg.setText(messageEntity.getComment() + "");
                holder.mRead.setText(messageEntity.getRead() + "");
                holder.mCollect.setText(messageEntity.getCollect() + "");
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(mContext, WebViewActivity.class);
                        intent.putExtra("MessageId", messageEntity.getObjectId());
                        mContext.startActivity(intent);
                    }
                });
            }


            @Override
            public void onFailure(int i, String s) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class CollectViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.item_content_title)
        TextView mTitle;
        @Bind(R.id.item_content_desc)
        TextView mDesc;
        @Bind(R.id.item_content_head)
        CircleImageView mHead;
        @Bind(R.id.item_content_classify)
        TextView mClassify;
        @Bind(R.id.item_content_parise)
        TextView mPraise;
        @Bind(R.id.item_content_msg)
        TextView mMsg;
        @Bind(R.id.item_content_collect)
        TextView mCollect;
        @Bind(R.id.item_content_read)
        TextView mRead;
        View itemView;

        public CollectViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            this.itemView = itemView;
        }
    }
}
