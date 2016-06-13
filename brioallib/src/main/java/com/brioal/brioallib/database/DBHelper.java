package com.brioal.brioallib.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**数据库操作类
 * Created by Brioal on 2016/5/12.
 */
public class DBHelper extends SQLiteOpenHelper {
    private final String table_classify = "create table Classify ( _id integer primary key autoincrement , mId integer , mClassify )";
    private final String table_banner = "create table Banner ( mBannerTip , mMessageId ,   mMessageUrl , mBannerImageUrl )";
    private final String message_table = "create table Message ( mAuthorId , mMessageTitle,  mMessageDesc , mMessageClassify , mPraiseCount integer , mCommentCount integer ,  mCollectCount integer , mReadCount integer , mMessageUrl , mAuthorName ,mAuthorHeadUrl , mMessageId primary key)";
    private final String table_tag = "create table Tag ( _id integer primary key autoincrement , mTag )" ; //创建本地标签表

    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(table_classify);
        db.execSQL(table_banner);
        db.execSQL(message_table);
        db.execSQL(table_tag);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
