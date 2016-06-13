package com.brioal.pocketcode30.entiy;

import cn.bmob.v3.BmobObject;

/**
 * 分类实体类
 * Created by Brioal on 2016/5/12.
 */
public class ClassifyEntity extends BmobObject {
    private int mId;
    private String mClassify;

    public ClassifyEntity(int mId, String mClassify) {
        this.mId = mId;
        this.mClassify = mClassify;
    }

    public int getmId() {
        return mId;
    }

    public String getmClassify() {
        return mClassify;
    }

}
