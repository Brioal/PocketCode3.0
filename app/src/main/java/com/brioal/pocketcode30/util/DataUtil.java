package com.brioal.pocketcode30.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.brioal.brioallib.database.DBHelper;
import com.brioal.brioallib.interfaces.onCheckExitListener;
import com.brioal.brioallib.util.DataQuery;
import com.brioal.pocketcode30.entiy.AttentionEntity;
import com.brioal.pocketcode30.entiy.BannerEntity;
import com.brioal.pocketcode30.entiy.ClassifyEntity;
import com.brioal.pocketcode30.entiy.CollectEntity;
import com.brioal.pocketcode30.entiy.CommentEntity;
import com.brioal.pocketcode30.entiy.MessageEntity;
import com.brioal.pocketcode30.entiy.PraiseEntity;
import com.brioal.pocketcode30.entiy.User;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;

/**
 * 数据操作工具类
 * Created by Brioal on 2016/6/2.
 */

public class DataUtil {
    private DBHelper mHelper;
    private Context mContext;
    private String TAG = "DataUtilInfo";
    private SQLiteDatabase mDb;


    public DataUtil(Context mContext) {
        this.mContext = mContext;
        mHelper = new DBHelper(mContext, "PocketCode.db3", null, 1);
        mDb = mHelper.getReadableDatabase();
    }

    //根据分类返回文章内容
    public List<MessageEntity> getContentModels(String classify) {
        List<MessageEntity> list = new ArrayList<>();
        Cursor cursor = null;
        try {
            mDb = mHelper.getReadableDatabase();
            if (classify.equals("精选")) {
                cursor = mDb.rawQuery("select * from Message", null);
            } else {
                cursor = mDb.rawQuery("select * from Message where mMessageClassify like '" + classify + "%'", null);
            }
            while (cursor.moveToNext()) {
                MessageEntity model = new MessageEntity(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getInt(4), cursor.getInt(5), cursor.getInt(6), cursor.getInt(7), cursor.getString(8), cursor.getString(9), cursor.getString(10));
                model.setObjectId(cursor.getString(11));
                list.add(model);
            }
        } catch (Exception e) {
            Log.i(TAG, "读取文章数据失败" + e.toString());
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return list;
    }

    //获取网络的分类数据
    public void getContentNet(String classify, int limit, FindListener<MessageEntity> listener) {
        DataQuery<MessageEntity> query = new DataQuery<>();
        query.getDatas(mContext, limit, 0, "-createdAt", 1, "mClassify", classify, listener);
    }

    //保存文章数据
    public void saveMessageDataLocal(List<MessageEntity> list) {
        try {
            mDb.execSQL("truncate table Message");
            for (int i = 0; i < list.size(); i++) {
                MessageEntity model = list.get(i);
                mDb.execSQL("insert into Message values ( ? , ? , ? , ? , ? , ? , ? , ? , ? ,? ,? , ?  )", new Object[]{
                        model.getAuthorId(),
                        model.getTitle(),
                        model.getDesc(),
                        model.getClassify(),
                        model.getPraise(),
                        model.getComment(),
                        model.getCollect(),
                        model.getRead(),
                        model.getUrl(),
                        model.getAuthorUserName(),
                        model.getAuthorHeadUrl(),
                        model.getObjectId()
                });
            }
        } catch (Exception e) {
            Log.i(TAG, "保存文章数据出错" + e.toString());
            e.printStackTrace();
        }

    }

    //保存Banner数据到本地
    public void saveBannerDataLocal(List<BannerEntity> list) {
        Log.i(TAG, "保存Banner数据");
        mDb.execSQL("truncate table Banner");
        try {
            for (int i = 0; i < list.size(); i++) {
                BannerEntity model = list.get(i);
                mDb.execSQL("insert into Banner values ( ? , ? , ? , ? )", new Object[]{
                        model.getTip(),
                        model.getMessageId(),
                        model.getUrl(),
                        model.getImageUrl(mContext)
                });
            }
        } catch (Exception e) {
            Log.i(TAG, "保存Banner数据出错" + e.toString());
            e.printStackTrace();
        }
    }


    //获取首页Banner
    public List<BannerEntity> getBanners() {
        List<BannerEntity> banners = new ArrayList<>();
        Cursor cursor = null;
        try {
            cursor = mDb.rawQuery("select * from Banner", null);
            while (cursor.moveToNext()) {
                BannerEntity model = new BannerEntity(cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4));
                banners.add(model);
            }
        } catch (Exception e) {
            Log.i(TAG, "getBanners: 读取Banner数据出错" + e.toString());
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return banners;
    }

    //获取收藏的列表
    public void getCollectDataNet(String mUserId, FindListener<CollectEntity> listener) {
        DataQuery<CollectEntity> query = new DataQuery<>();
        query.getDatas(mContext, 100, 0, "-createdAt", 0, "mUserId", mUserId, listener);
    }

    //获取我的关注列表
    public void getAttentionDataNet(String userId, FindListener<AttentionEntity> listener) {
        DataQuery<AttentionEntity> query = new DataQuery<>();
        query.getDatas(mContext, 100, 0, "-createdAt", 0, "mUserId", userId, listener);
    }

    //获取我的粉丝列表
    public void getFansDataNet(String userId, FindListener<AttentionEntity> listener) {
        DataQuery<AttentionEntity> query = new DataQuery<>();
        query.getDatas(mContext, 100, 0, "-createdAt", 0, "mAuthorId", userId, listener);
    }


    //获取本地保存的Tag
    public List<String> getLocalTag() {
        List<String> strings = new ArrayList<>();
        Cursor cursor = null;
        try {
            cursor = mDb.rawQuery("select * from Tag", null);
            while (cursor.moveToNext()) {
                strings.add(cursor.getString(1));
            }
        } catch (Exception e) {
            Log.i(TAG, "getLocalTag: 查询本地TAG失败");
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return strings;
    }

    //获取标签选择的大分类
    public List<String> getGroups() {
        //10个大分类
        String[] heads = new String[]{
                "移动开发",
                "编程语言",
                "游戏&图像",
                "系统&安全",
                "数据库",
                "研发工具",
        };
        return Arrays.asList(heads);
    }

    //获取tag数据
    public List<List<String>> getChilds() {
        String[][] childs = new String[][]{

                {
                        "移动开发",
                        "Android",
                        "IOS",
                        "Swift",
                        "微信开发",
                },
                {
                        "编程语言",
                        "算法",
                        "JAVA",
                        "Python开发",
                        "Go",
                        "C#",
                        ".NET",
                        "PHP",
                        "C/C++",
                },
                {
                        "移动游戏",
                        "电脑游戏",
                        "图像处理",
                        "图片制作",
                },
                {
                        "系统",
                        "安全",
                        "Linux/UNIX",
                        "Windows",
                        "网络基础",
                        "Tomcat",
                },
                {
                        "数据库",
                        "PrestoDB",
                        "MySql",
                        "PostgreSQL",
                        "MongoDB",
                        "Redis",
                },
                {
                        "Eclipse",
                        "Idea",
                        "Android Studio",
                },

        };

        List<List<String>> result = new ArrayList<>();
        for (int i = 0; i < childs.length; i++) {
            result.add(Arrays.asList(childs[i]));
        }
        return result;
    }


    //读取本地的分类数据
    public List<ClassifyEntity> getClassifyLocal() {
        List<ClassifyEntity> list = new ArrayList<>();
        Cursor cursor = null;
        try {
            cursor = mDb.rawQuery("select * from Classify", null);
            while (cursor.moveToNext()) {
                ClassifyEntity model = new ClassifyEntity(cursor.getInt(1), cursor.getString(2));
                list.add(model);
            }
        } catch (Exception e) {
            Log.i(TAG, "getClassifyLocal: 查询本地分类失败" + e.toString());
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return list;
    }

    //获取网络的分类数据
    public void getClassifyNet(FindListener<ClassifyEntity> listener) {
        DataQuery<ClassifyEntity> query = new DataQuery<>();
        query.getDatas(mContext, 20, 0, "", -1, "", null, listener);
    }

    //保存分类数据到本地
    public void saveClassifyLocal(List<ClassifyEntity> list) {
        SQLiteDatabase db = mHelper.getReadableDatabase();
        db.execSQL("delete from Classify where _id > 0"); //清空
        for (int i = 0; i < list.size(); i++) {
            ClassifyEntity model = list.get(i);
            db.execSQL("insert into Classify values ( null , ? , ? )", new Object[]{
                    model.getmId(),
                    model.getmClassify()
            });
        }
    }

    //从网络获取评论数据
    public void getCommentNet(String messageId, FindListener<CommentEntity> listener) {
        DataQuery<CommentEntity> query = new DataQuery<>();
        query.getDatas(mContext, 100, 0, "-createdAt", 0, "mMessageId", messageId, listener);
    }


    //保存用户信息到本地
    public void saveUserLocal(User user) {
        SharedPreferences preferences = mContext.getSharedPreferences("PocketCode", Context.MODE_APPEND);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("Id", user.getObjectId());
        editor.putString("UserName", user.getUsername());
        editor.putString("HeadUrl", user.getHeadUrl(mContext));
        editor.putString("Desc", user.getDesc());
        editor.putString("Favorite", user.getInterest());
        editor.putString("Blog", user.getBlog());
        editor.putString("GitHub", user.getGithub());
        editor.putString("QQ", user.getQQ());

        editor.apply();
    }

    //获取本地用户信息
    public User getUserLocal() {
        SharedPreferences preferences = mContext.getSharedPreferences("PocketCode", Context.MODE_APPEND);
        String mObjectId = preferences.getString("Id", "");
        String mUserName = preferences.getString("UserName", "");
        String mHeadUrl = preferences.getString("HeadUrl", "");
        String mDesc = preferences.getString("Desc", "");
        String mFavorite = preferences.getString("Favorite", "");
        String mBlog = preferences.getString("Blog", "");
        String mGithub = preferences.getString("GitHub", "");
        String mQQ = preferences.getString("QQ", "");
        if (mUserName.isEmpty()) {
            return null;
        } else {
            User user = new User();
            user.setObjectId(mObjectId);
            user.setUsername(mUserName);
            user.setHeadUrl(mHeadUrl);
            user.setInterest(mFavorite);
            user.setDesc(mDesc);
            user.setBlog(mBlog);
            user.setGithub(mGithub);
            user.setQQ(mQQ);
            return user;
        }
    }

    //注销登录
    public void deleteUserLocal() {
        SharedPreferences preferences = mContext.getSharedPreferences("PocketCode", Context.MODE_APPEND);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.apply();
    }

    //获取是否存在这样一条关注 我的id  , 别人的id
    public void isAttentionExit(String mineId, String otherId, final onCheckExitListener listener) {
        BmobQuery<AttentionEntity> query = new BmobQuery<>();
        query.addWhereEqualTo("mUserId", mineId);
        query.addWhereEqualTo("mAuthorId", otherId);
        query.findObjects(mContext, new FindListener<AttentionEntity>() {
            @Override
            public void onSuccess(List<AttentionEntity> list) {
                Log.i(TAG, "onSuccess: 查询关注数据成功");
                if (list.size() == 0) {
                    listener.noExit();
                } else {
                    listener.exit(list.get(0).getObjectId());
                }
            }

            @Override
            public void onError(int i, String s) {
                Log.i(TAG, "onError: 查询关注数据失败" + s);
                listener.noExit();
            }
        });
    }

    //查询是否收藏了当前的文章
    public void isCollect(String userId, String messageId, final onCheckExitListener listerner) {
        BmobQuery<CollectEntity> query = new BmobQuery<>();
        query.addWhereEqualTo("mUserId", userId);
        query.addWhereEqualTo("mMessageId", messageId);
        query.findObjects(mContext, new FindListener<CollectEntity>() {
            @Override
            public void onSuccess(List<CollectEntity> list) {
                Log.i(TAG, "onSuccess: 查询是否收藏成功" + list.size());
                if (list.size() == 0) {
                    listerner.noExit();
                } else {
                    listerner.exit(list.get(0).getObjectId());
                }
            }

            @Override
            public void onError(int i, String s) {
                Log.i(TAG, "onError: 加载失败" + s);
                listerner.noExit();
            }
        });
    }

    //查询是否给当前文章点赞
    public void isParise(String userId, String messageId, final onCheckExitListener listener) {
        BmobQuery<PraiseEntity> query = new BmobQuery<>();
        query.addWhereEqualTo("mUserId", userId);
        query.addWhereEqualTo("mMessageId", messageId);
        query.findObjects(mContext, new FindListener<PraiseEntity>() {
            @Override
            public void onSuccess(List<PraiseEntity> list) {
                Log.i(TAG, "onSuccess: 查询点赞信息失败");
                if (list.size() == 0) {
                    listener.noExit();
                } else {
                    listener.exit(list.get(0).getObjectId());
                }
            }

            @Override
            public void onError(int i, String s) {
                Log.i(TAG, "onError: 查询点赞信息失败" + s);
            }
        });
    }

    //保存使用过得TAG
    public void saveTagLocal(List<String> list) {

        try {
            mDb.execSQL("delete from Tag where _id > 0");
            for (int i = 0; i < list.size(); i++) {
                mDb.execSQL("insert into Tag values ( null , ? )", new Object[]{list.get(i)});
            }
        } catch (Exception e) {
            Log.i(TAG, "saveTagLocal: " + e.toString());
            e.printStackTrace();
        }
    }


}
