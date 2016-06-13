package com.brioal.pocketcode30.entiy;

import cn.bmob.v3.BmobObject;

/**
 * 文章实体类
 * Created by Brioal on 2016/5/12.
 */
public class MessageEntity extends BmobObject {
    private String mAuthorId;//作者ID
    private String mTitle; //标题
    private String mDesc; //描述
    private String mClassify; //分类
    private int mPraise; //点赞数
    private int mComment; //评论数
    private int mCollect; //收藏数量
    private int mRead; //阅读量
    private String mUrl; //链接

    private String mAuthorUserName ; //作者的名称
    private String mAuthorHeadUrl; //作者头像地址


    public MessageEntity() {
    }

    public MessageEntity(String mAuthorId, String mTitle, String mDesc, String mClassify, int mPraise, int mComment, int mCollect, int mRead, String mUrl, String mAuthorUserName, String mAuthorHeadUrl) {
        this.mAuthorId = mAuthorId;
        this.mTitle = mTitle;
        this.mDesc = mDesc;
        this.mClassify = mClassify;
        this.mPraise = mPraise;
        this.mComment = mComment;
        this.mCollect = mCollect;
        this.mRead = mRead;
        this.mUrl = mUrl;
        this.mAuthorUserName = mAuthorUserName;
        this.mAuthorHeadUrl = mAuthorHeadUrl;
    }

    public String getAuthorId() {
        return mAuthorId;
    }

    public void setAuthorId(String mAuthorId) {
        this.mAuthorId = mAuthorId;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public String getDesc() {
        return mDesc;
    }

    public void setDesc(String mDesc) {
        this.mDesc = mDesc;
    }

    public String getClassify() {
        return mClassify;
    }

    public void setClassify(String mClassify) {
        this.mClassify = mClassify;
    }

    public int getPraise() {
        return mPraise;
    }

    public void setPraise(int mPraise) {
        this.mPraise = mPraise;
    }

    public int getComment() {
        return mComment;
    }

    public void setComment(int mComment) {
        this.mComment = mComment;
    }

    public int getCollect() {
        return mCollect;
    }

    public void setCollect(int mCollect) {
        this.mCollect = mCollect;
    }

    public int getRead() {
        return mRead;
    }

    public void setRead(int mRead) {
        this.mRead = mRead;
    }

    public String getUrl() {
        return mUrl;
    }

    public void setUrl(String mUrl) {
        this.mUrl = mUrl;
    }

    public String getAuthorUserName() {
        return mAuthorUserName;
    }

    public void setAuthorUserName(String mAuthorUserName) {
        this.mAuthorUserName = mAuthorUserName;
    }

    public String getAuthorHeadUrl() {
        return mAuthorHeadUrl;
    }

    public void setAuthorHeadUrl(String mAuthorHeadUrl) {
        this.mAuthorHeadUrl = mAuthorHeadUrl;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MessageEntity that = (MessageEntity) o;

        if (mPraise != that.mPraise) return false;
        if (mComment != that.mComment) return false;
        if (mCollect != that.mCollect) return false;
        if (mRead != that.mRead) return false;
        if (mAuthorId != null ? !mAuthorId.equals(that.mAuthorId) : that.mAuthorId != null)
            return false;
        if (mTitle != null ? !mTitle.equals(that.mTitle) : that.mTitle != null) return false;
        if (mDesc != null ? !mDesc.equals(that.mDesc) : that.mDesc != null) return false;
        if (mClassify != null ? !mClassify.equals(that.mClassify) : that.mClassify != null)
            return false;
        if (mUrl != null ? !mUrl.equals(that.mUrl) : that.mUrl != null) return false;
        if (mAuthorUserName != null ? !mAuthorUserName.equals(that.mAuthorUserName) : that.mAuthorUserName != null)
            return false;
        return mAuthorHeadUrl != null ? mAuthorHeadUrl.equals(that.mAuthorHeadUrl) : that.mAuthorHeadUrl == null;

    }

    @Override
    public int hashCode() {
        int result = mAuthorId != null ? mAuthorId.hashCode() : 0;
        result = 31 * result + (mTitle != null ? mTitle.hashCode() : 0);
        result = 31 * result + (mDesc != null ? mDesc.hashCode() : 0);
        result = 31 * result + (mClassify != null ? mClassify.hashCode() : 0);
        result = 31 * result + mPraise;
        result = 31 * result + mComment;
        result = 31 * result + mCollect;
        result = 31 * result + mRead;
        result = 31 * result + (mUrl != null ? mUrl.hashCode() : 0);
        result = 31 * result + (mAuthorUserName != null ? mAuthorUserName.hashCode() : 0);
        result = 31 * result + (mAuthorHeadUrl != null ? mAuthorHeadUrl.hashCode() : 0);
        return result;
    }
}
