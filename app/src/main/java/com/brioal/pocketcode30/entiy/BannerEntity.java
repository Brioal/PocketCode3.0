package com.brioal.pocketcode30.entiy;

import android.content.Context;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;

/**首页轮播实体类
 * Created by Brioal on 2016/5/12.
 */
public class BannerEntity extends BmobObject {
    private BmobFile mBannerImage; //图片
    private String mBannerTip;//提示
    private String mMessageId; //文章的id
    private String mMessageUrl; //链接

    private String mImageUrl ;

    public BannerEntity( String mTip,String mContentId , String mUrl, String mImageUrl) {
        this.mBannerTip = mTip;
        this.mMessageId = mContentId;
        this.mMessageUrl = mUrl;
        this.mImageUrl = mImageUrl;
    }

    public String getMessageId() {
        return mMessageId;
    }


    public String getImageUrl(Context mContext) {
        return mImageUrl==null? mBannerImage.getFileUrl(mContext):mImageUrl;
    }


    public String getTip() {
        return mBannerTip;
    }


    public String getUrl() {
        return mMessageUrl;
    }

}
