package com.brioal.pocketcode30.entiy;

import android.content.Context;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;

/**用户实体类
 * Created by Brioal on 2016/5/14.
 */
public class User extends BmobUser {
    private BmobFile mHead; //头像
    private String mDesc; //个人签名
    private String mInterest; //兴趣
    private String mBlog; //博客地址
    private String mGitHub; //Github地址
    private String mQQ; //QQ

    private String mHeadUrl;  //头像链接
    public String getHeadUrl(Context mContext) {
        return mHeadUrl == null ? mHead==null?null:mHead.getFileUrl(mContext) : mHeadUrl;
    }

    public void setHead(BmobFile mHead) {
        this.mHead = mHead;
    }


    public String getInterest() {
        return mInterest;
    }

    public void setInterest(String mInterest) {
        this.mInterest = mInterest;
    }

    public String getBlog() {
        return mBlog;
    }

    public void setBlog(String mBlog) {
        this.mBlog = mBlog;
    }

    public String getGithub() {
        return mGitHub;
    }

    public void setGithub(String mGitHub) {
        this.mGitHub = mGitHub;
    }

    public String getQQ() {
        return mQQ;
    }

    public void setQQ(String mQQ) {
        this.mQQ = mQQ;
    }


    public String getDesc() {
        return mDesc;
    }

    public void setDesc(String mDesc) {
        this.mDesc = mDesc;
    }


    public void setHeadUrl(String mHeadUrl) {
        this.mHeadUrl = mHeadUrl;
    }
}
