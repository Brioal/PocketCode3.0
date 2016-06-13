package com.brioal.pocketcode30.entiy;

import cn.bmob.v3.BmobObject;

/**
 * 收藏列表实体类
 * Created by Brioal on 2016/5/31.
 */

public class CollectEntity extends BmobObject {
    private String mUserId; //用户id
    private String mMessageId; //文章的id

    public CollectEntity() {
    }

    public CollectEntity(String mUserId, String mMessageId) {
        this.mUserId = mUserId;
        this.mMessageId = mMessageId;
    }

    public String getUserId() {
        return mUserId;
    }

    public String getMessageId() {
        return mMessageId;
    }

}
