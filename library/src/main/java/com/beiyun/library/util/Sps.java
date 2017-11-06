package com.beiyun.library.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.annotation.NonNull;
import android.util.Log;

import com.beiyun.library.entity.SpMode;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Set;

/**
 * Created by beiyun on 2017/11/3.
 * 偏好类操作
 */
public class Sps {

    private static final String TAG = "Sps";
    private SharedPreferences mSp;
    private Class<?> mCls;
    private Object mObject;
    private String mSpName;
    private SpMode mSpMode;

    private Sps(){}

    public static Sps init(){
        return new Sps();
    }

    public Sps name(String spName){
        this.mSpName = spName;
        return this;
    }

    public Sps mode(SpMode spMode){
        this.mSpMode = spMode;
        return this;
    }

    public Sps object(Object o){
        this.mObject = o;
        return this;
    }

    public Sps cls(Class<?> cls){
        this.mCls = cls;
        return this;
    }

    public Sps build(){
        String spName = null;
        if(mSpName != null){
            spName = mSpName;
        }else if(mCls != null){
            spName = mCls.getName();
        }else if(mObject != null){
            spName = mObject.getClass().getName();
        }else{
            throw new NullPointerException("this spName cannot be null");
        }
        mSp = Apps.getSharedPreferences(spName,mSpMode == null? getSpMode(SpMode.MODE_PRIVATE):getSpMode(mSpMode));
        return this;
    }


    /**
     * 保存 object
     * @return true 保存成功  false 保存失败
     */
    public boolean save(){
        if(mObject == null){
            throw new NullPointerException("the object that you'll save is null");
        }
        return save(mObject,this);
    }

    /**
     * 取出object
     * @return Object
     */
    public Object get(){
        if(mCls != null){
            return get(mCls,this);
        }else if(mObject != null){
            return get(mObject.getClass(),this);
        }else {
            throw new NullPointerException("cls and object cannot both be null");
        }
    }


    //保存object中所有的值
    public static boolean save(@NonNull Object o){
        return save(o,SpMode.MODE_PRIVATE);
    }


    /**
     * 根据对应的class文件获取对应object中所有的值
     * @param cls 获取Object所对应的class文件
     * @return object
     */
    public static Object get(Class<?> cls){
        return get(cls,SpMode.MODE_PRIVATE);
    }

    /**
     *@param spMode SP的类型
     * @param o 将要保存的object
     * @return true 表示保存成功， false表示保存失败
     */
    public static boolean save(@NonNull Object o, SpMode spMode){
        Sps sps = Sps.init().object(o).mode(spMode).build();
        return save(o,sps);
    }

    /**
     * @param cls 对应实体类的class文件
     * @param spMode sp类型
     * @return object
     */
    //通过class文件找回保存的该class中所有保存的值
    public static Object get(Class<?> cls,SpMode spMode) {
        Sps sps = Sps.init().cls(cls).mode(spMode).build();
        return get(cls,sps);
    }


    /***
     *
     * @param spName 定义sp名字
     * @param o 保存的实体对象
     * @param spMode 保存类型
     * @return true 保存成功   false 保存失败
     */
    public static boolean save(String spName,@NonNull Object o, SpMode spMode){
        Sps sps = Sps.init().name(spName).object(o).mode(spMode).build();
        return save(o,sps);
    }


    /**
     *
     * @param spName sp名字
     * @param cls 对应接收类的class文件
     * @param spMode sp类型
     * @return Object 要获取的实体类对象
     */
    public static Object get(String spName,Class<?> cls,SpMode spMode){
        Sps sps = Sps.init().name(spName).cls(cls).mode(spMode).build();
        return get(cls,sps);
    }



    //保存Boolean值
    public void putBoolean(String key, boolean booleanValue) {
        mSp.edit().putBoolean(key,booleanValue).apply();
    }

    //保存int值
    public void putInt(String key, int intValue) {
        mSp.edit().putInt(key,intValue).apply();
    }

    //保存string字串
    public void putString(String key, String stringValue) {
        mSp.edit().putString(key,stringValue).apply();

    }


    public void putLong(String key, long longValue){
        mSp.edit().putLong(key,longValue).apply();
    }


    public void putFloat(String key,float floatValue){
        mSp.edit().putFloat(key,floatValue).apply();
    }

    public void putStringSet(String key, @NonNull Set<String> stringSet){
        mSp.edit().putStringSet(key,stringSet).apply();
    }

    //获取Boolean值
    public boolean getBoolean(String key){
        return mSp.getBoolean(key,false);
    }

    //获取int值
    public int getInt(String key){
        return mSp.getInt(key,-1);
    }

    //获取string值
    public String getString(String key){
        return mSp.getString(key,null);
    }


    public long getLong(String key){
        return mSp.getLong(key,-1);
    }

    public float getFloat(String key){
        return mSp.getFloat(key,-1);
    }

    public Set<String> getStringSet(String key){
        return mSp.getStringSet(key,null);
    }


    //获取sp类型
    private int getSpMode(SpMode spMode) {

        int mode = 0;
        switch (spMode){
            case MODE_APPEND:
                mode = Context.MODE_APPEND;
                break;
            case MODE_PRIVATE:
                mode = Context.MODE_PRIVATE;
                break;
            case MODE_NO_LOCALIZED_COLLATORS:
                if (Build.VERSION.SDK_INT >= 24) {
                    mode = Context.MODE_NO_LOCALIZED_COLLATORS;
                }else{
                    mode = Context.MODE_PRIVATE;
                }
                break;
            case MODE_ENABLE_WRITE_AHEAD_LOGGING:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    mode = Context.MODE_ENABLE_WRITE_AHEAD_LOGGING;
                }else {
                    mode = Context.MODE_PRIVATE;
                }
                break;
        }

        return mode;
    }


    private static boolean save(Object o,Sps sps){
        Field[] fields = o.getClass().getDeclaredFields();
        for (Field f:fields) {
            try {
                f.setAccessible(true);
                String fieldType = f.getGenericType().toString();
                if("class java.lang.String".equals(fieldType)){
                    sps.putString(f.getName(), String.valueOf(f.get(o)));
                }else if("int".equals(fieldType)){
                    sps.putInt(f.getName(),f.getInt(o));
                }else if("boolean".equals(fieldType)){
                    sps.putBoolean(f.getName(),f.getBoolean(o));
                }else if("float".equals(fieldType)){
                    sps.putFloat(f.getName(),f.getFloat(o));
                }else if("long".equals(fieldType)){
                    sps.putLong(f.getName(),f.getLong(o));
                }else if("java.util.Set<java.lang.String>".equals(fieldType)){
                    Set<String> stringSet = (Set<String>) f.get(o);
                    sps.putStringSet(f.getName(), stringSet);
                }
            } catch (IllegalAccessException e) {
                Log.e(TAG, "save: "+e.getMessage() );
                return false;
            }
        }

        Log.e(TAG, "save: success" );
        return true;
    }


    private static Object get(Class<?> cls,Sps sps){
        try {
            Constructor<?> constructor = cls.getConstructor();
            Object o = constructor.newInstance();
            Field[] fields = cls.getDeclaredFields();
            for (Field f:fields) {
                f.setAccessible(true);
                String fieldType = f.getGenericType().toString();
                if("class java.lang.String".equals(fieldType)){
                    String stringValue = sps.getString(f.getName());
                    f.set(o,stringValue);
                }else if("int".equals(fieldType)){
                    int intValue = sps.getInt(f.getName());
                    f.setInt(o,intValue);
                }else if("boolean".equals(fieldType)){
                    boolean booleanValue = sps.getBoolean(f.getName());
                    f.setBoolean(o,booleanValue);
                }else if("float".equals(fieldType)){
                    float floatValue = sps.getFloat(f.getName());
                    f.setFloat(o,floatValue);
                }else if("long".equals(fieldType)){
                    long longValue = sps.getLong(f.getName());
                    f.setLong(o,longValue);
                }else if("java.util.Set<java.lang.String>".equals(fieldType)){
                    Set<String> stringSet = sps.getStringSet(f.getName());
                    f.set(o,stringSet);
                }
            }

            Log.e(TAG, "get: "+o);
            return o;
        } catch (Exception e) {
            Log.e(TAG, "get: 没有默认的构造函数"+e.getMessage() );
            return null;
        }
    }


}