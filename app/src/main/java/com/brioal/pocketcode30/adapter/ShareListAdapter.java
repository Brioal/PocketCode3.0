package com.brioal.pocketcode30.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.brioal.pocketcode30.R;
import com.brioal.pocketcode30.activity.WebViewActivity;
import com.brioal.pocketcode30.entiy.MessageEntity;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**分享列表的适配器
 * Created by Brioal on 2016/5/18.
 */
public class ShareListAdapter extends RecyclerView.Adapter<ShareListAdapter.ShareListViewHolder> {

    private Context mContext;
    private List<MessageEntity> mList;


    public ShareListAdapter(Context mContext, List<MessageEntity> mList) {
        this.mContext = mContext;
        this.mList = mList;
    }

    @Override
    public ShareListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.item_share_list, parent, false);
        return new ShareListViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ShareListViewHolder holder, int position) {
        final MessageEntity model = mList.get(position);
        holder.mShareTitle.setText(model.getTitle());
        holder.mShareDesc.setText(model.getDesc());
        holder.mShareClassify.setText(model.getClassify());
        holder.mShareParise.setText(model.getPraise() + "");
        holder.mShareRead.setText(model.getRead()+"");
        holder.mShareMsg.setText(model.getComment()+"");

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, WebViewActivity.class);
                intent.putExtra("MessageId", model.getObjectId());
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }


    class ShareListViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.item_share_title)
        TextView mShareTitle;
        @Bind(R.id.item_share_desc)
        TextView mShareDesc;
        @Bind(R.id.item_share_classify)
        TextView mShareClassify;
        @Bind(R.id.item_share_time)
        TextView mShareTime;
        @Bind(R.id.item_share_parise)
        TextView mShareParise;
        @Bind(R.id.item_share_msg)
        TextView mShareMsg;
        @Bind(R.id.item_share_read)
        TextView mShareRead;

        View itemView;

        public ShareListViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            ButterKnife.bind(this, itemView);
        }
    }


}
