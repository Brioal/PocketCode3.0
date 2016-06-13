package com.brioal.pocketcode30.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.brioal.brioallib.bgabanner.BGABanner;
import com.brioal.brioallib.interfaces.OnLoaderMoreListener;
import com.brioal.brioallib.util.NetWorkUtil;
import com.brioal.brioallib.view.CircleImageView;
import com.brioal.pocketcode30.R;
import com.brioal.pocketcode30.activity.WebViewActivity;
import com.brioal.pocketcode30.entiy.BannerEntity;
import com.brioal.pocketcode30.entiy.MessageEntity;
import com.brioal.pocketcode30.entiy.User;
import com.brioal.pocketcode30.fragment.MainFragment;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;

/**
 * 文章的适配器
 * Created by Brioal on 2016/5/12.
 */
public class MessageAdapter extends RecyclerView.Adapter {
    private int TYPE_BANNER = 0; //轮播
    private int TYPE_LOAD_MORE = 1;//加载更多
    private int TYPE_NO_MORE = 2;//没有更多
    private int TYPE_CONTENT = 3;//内容
    private String TAG = "ContextAdapter";

    private Context mContext;
    private List<MessageEntity> mList;
    private List<BannerEntity> mBannerList;
    private OnLoaderMoreListener loaderMoreListener;


    public void setLoaderMoreListener(OnLoaderMoreListener loaderMoreListener) {
        this.loaderMoreListener = loaderMoreListener;
    }

    public MessageAdapter(Context mContext, List<MessageEntity> mList, List<BannerEntity> mBannerList) {
        this.mContext = mContext;
        this.mList = mList;
        this.mBannerList = mBannerList;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_BANNER) {
            View itemView = LayoutInflater.from(mContext).inflate(R.layout.item_banner, parent, false);
            return new BannerViewHolder(itemView);
        } else if (viewType == TYPE_CONTENT) {
            View itemView = LayoutInflater.from(mContext).inflate(R.layout.item_content, parent, false);
            return new ContentViewHolder(itemView);
        } else if (viewType == TYPE_LOAD_MORE) {
            View itemView = LayoutInflater.from(mContext).inflate(R.layout.item_loadmore, parent, false);
            return new LoadMoreViewHolder(itemView);
        } else if (viewType == TYPE_NO_MORE) {
            View itemView = LayoutInflater.from(mContext).inflate(R.layout.item_nomore, parent, false);
            return new NoMoreViewHolder(itemView);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof BannerViewHolder) {
            if (mBannerList.size() > 3) {
                List<View> views = new ArrayList<>();
                List<String> tips = new ArrayList<>();
                for (int i = 0; i < mBannerList.size(); i++) {
                    final BannerEntity model = mBannerList.get(i);
                    ImageView imageView = new ImageView(mContext);
                    imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                    Glide.with(mContext).load(model.getImageUrl(mContext)).into(imageView);
                    imageView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(mContext, WebViewActivity.class);
                            intent.putExtra("MessageId", model.getMessageId());
                            mContext.startActivity(intent);
                        }
                    });
                    views.add(imageView);
                    tips.add(model.getTip());
                }

                ((BannerViewHolder) holder).mBanner.setViews(views);
                ((BannerViewHolder) holder).mBanner.setTips(tips);
                ((BannerViewHolder) holder).mBanner.setPageChangeDuration(2000);
            }

        } else if (holder instanceof ContentViewHolder) {
            final MessageEntity model = mList.get(mBannerList == null ? position : position - 1);
            ((ContentViewHolder) holder).mTitle.setText(model.getTitle());
            ((ContentViewHolder) holder).mDesc.setText(model.getDesc());
            String mHeadUrl = model.getAuthorHeadUrl();
            if (mHeadUrl == null) {
                BmobQuery<User> query = new BmobQuery<User>();
                query.addWhereEqualTo("objectId", model.getAuthorId());
                query.findObjects(mContext, new FindListener<User>() {
                    @Override
                    public void onSuccess(List<User> object) {
                        Log.i(TAG, "onSuccess: 查询用户成功");
                        User user = object.get(0);
                        String mUrl = user.getHeadUrl(mContext);
                        model.setAuthorHeadUrl(mUrl);
                        Glide.with(mContext).load(mUrl).into(((ContentViewHolder) holder).mHead);
                    }

                    @Override
                    public void onError(int code, String msg) {
                        Log.i(TAG, "onError: 查询失败");
                    }
                });
            } else {
                Glide.with(mContext).load(mHeadUrl).into(((ContentViewHolder) holder).mHead);
            }

            ((ContentViewHolder) holder).mClassify.setText(model.getClassify());
            ((ContentViewHolder) holder).mParise.setText(model.getPraise() + "");
            ((ContentViewHolder) holder).mComment.setText(model.getComment() + "");
            ((ContentViewHolder) holder).mRead.setText(model.getRead() + "");
            ((ContentViewHolder) holder).mCollect.setText(model.getCollect() + "");
            ((ContentViewHolder) holder).itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, WebViewActivity.class);
                    intent.putExtra("Message", model);
                    mContext.startActivity(intent);
                }
            });
        } else if (holder instanceof LoadMoreViewHolder) {
            if (NetWorkUtil.isNetworkConnected(mContext)) {
                loaderMoreListener.loadMore();
            }
        }

    }

    @Override
    public int getItemCount() {
        int count = mList.size();
        if (mBannerList != null) { //banner存在,则多一个
            count += 1;
        }
        if (mList.size() >= 7) {//还有数据,或者数据大小合适,应该显示底部信息
            count += 1;
        }
        return count;
    }

    @Override
    public int getItemViewType(int position) {
        if (mBannerList != null && position == 0) {
            return TYPE_BANNER;
        } else if (position == getItemCount() - 1) {
            if (!NetWorkUtil.isNetworkConnected(mContext)) {
                return TYPE_NO_MORE; //没有更多布局
            } else {
                if (mList.size() % MainFragment.LOAD_LIMIT == 0) {
                    return TYPE_LOAD_MORE; //加载更多布局
                } else if (mList.size() >= 7) {
                    return TYPE_NO_MORE; //没有更多布局
                } else {
                    return TYPE_CONTENT; //内容布局
                }
            }
        }
        return TYPE_CONTENT;
    }

    //内容布局
    class ContentViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.item_content_title)
        TextView mTitle;
        @Bind(R.id.item_content_desc)
        TextView mDesc;
        @Bind(R.id.item_content_head)
        CircleImageView mHead;
        @Bind(R.id.item_content_classify)
        TextView mClassify;
        @Bind(R.id.item_content_parise)
        TextView mParise;
        @Bind(R.id.item_content_msg)
        TextView mComment;
        @Bind(R.id.item_content_read)
        TextView mRead;
        @Bind(R.id.item_content_collect)
        TextView mCollect;
        View itemView;

        public ContentViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            ButterKnife.bind(this, itemView);
        }
    }

    //轮播布局
    class BannerViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.main_banner)
        BGABanner mBanner;

        public BannerViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }
    }

    //加载更多布局
    private class LoadMoreViewHolder extends RecyclerView.ViewHolder {

        public LoadMoreViewHolder(View itemView) {
            super(itemView);
        }
    }

    //没有更多布局
    private class NoMoreViewHolder extends RecyclerView.ViewHolder {

        public NoMoreViewHolder(View itemView) {
            super(itemView);
        }
    }
}
