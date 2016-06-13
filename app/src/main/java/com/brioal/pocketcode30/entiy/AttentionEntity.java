package com.brioal.pocketcode30.entiy;

import cn.bmob.v3.BmobObject;

/**
 * Created by Brioal on 2016/5/31.
 */

public class AttentionEntity extends BmobObject {
    private String mUserId ; //"我"的的ID
    private String mAuthorId ; //别人的ID

    public AttentionEntity() {
    }

    public AttentionEntity(String mUserId, String mAuthorId) {
        this.mUserId = mUserId;
        this.mAuthorId = mAuthorId;
    }



    public String getmUserId() {
        return mUserId;
    }

    public void setmUserId(String mUserId) {
        this.mUserId = mUserId;
    }

    public String getmAuthorId() {
        return mAuthorId;
    }

    public void setmAuthorId(String mAuthorId) {
        this.mAuthorId = mAuthorId;
    }
}
