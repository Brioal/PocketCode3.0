package com.brioal.pocketcode30.util;

import android.content.Context;

import com.brioal.pocketcode30.entiy.User;

/**
 * Created by Brioal on 2016/5/8.
 */
public class Constants {
    public static final String APPID = "ef191dfb7fbeb86a15d6e307607c1c59";
    private static DataUtil mDataUtil; // 数据获取类
    private static User mUser;
    //返回数据加载工具的实例
    public static DataUtil getDataUtil(Context context) {
        if (mDataUtil == null) {
            mDataUtil = new DataUtil(context);
        }
        return mDataUtil;
    }

    public static User getUser(Context context) {
        if (mUser == null) {
            mUser = getDataUtil(context).getUserLocal();
        }
        return mUser;
    }
}
