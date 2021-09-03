package work.lpssfxy.www.campuslifeassistantclient.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.util.Base64;

import com.google.gson.Gson;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.lang.reflect.Type;

import work.lpssfxy.www.campuslifeassistantclient.App;

/**
 * created by on 2021/8/23
 * 描述：
 *
 * @author ZSAndroid
 * @create 2021-08-23-14:24
 */
public class SharePreferenceUtil {

    private static SharedPreferences sSp;
    private static SharedPreferences.Editor sEditor;

    public static final String FILE_NAME = "ZSAndroid";

    public static SharePreferenceUtil getInstance() {
        return InstanceHolder.sInstance;
    }

    public SharePreferenceUtil() {
        sSp = App.getInstance().getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        sEditor = sSp.edit();
    }



    static class InstanceHolder {
        private static final SharePreferenceUtil sInstance = new SharePreferenceUtil();
    }


    public int getInt(String key, int value) {
        return sSp.getInt(key, value);
    }

    public boolean getBoolean(String key, boolean value) {
        return sSp.getBoolean(key, value);
    }

    public String optString(String key) {
        return getString(key, "");
    }

    public Long optLong(String key) {
        return getLong(key, 0);
    }

    public Boolean optBoolean(String key) {
        return getBoolean(key, false);
    }

    public int optInt(String key) {
        return getInt(key, 0);
    }

    public String getString(String key, String value) {
        return sSp.getString(key, value);
    }

    public long getLong(String key, long value) {
        return sSp.getLong(key, value);
    }


    public boolean putInt(String key, int params) {
        try {
            sEditor.putInt(key, params);
            sEditor.commit();
            return true;
        } catch (Exception e) {

        }
        return false;
    }

    public boolean putString(String key, String params) {
        try {
            sEditor.putString(key, params);
            sEditor.commit();
            return true;
        } catch (Exception e) {

        }
        return false;

    }

    public boolean putBoolean(String key, boolean params) {
        try {
            sEditor.putBoolean(key, params);
            sEditor.commit();
            return true;
        } catch (Exception e) {

        }
        return false;
    }

    public boolean putLong(String key, long params) {
        try {
            sEditor.putLong(key, params);
            sEditor.commit();
            return true;
        } catch (Exception e) {

        }
        return false;
    }

    public void remove(String key) {
        remove(FILE_NAME, key);
    }

    public void remove(String spName, String key) {
        sEditor.remove(key);
        sEditor.apply();
    }

    public void clear() {
        clear(FILE_NAME);
    }

    public void clear(String fileName) {
        sEditor.clear();
        sEditor.apply();
    }

    public boolean contains(String key) {
        return sSp.contains(key);
    }

    /**
     * 将图片保存到SharedPreferences
     * @param context
     * @param preference
     * @param key
     * @param bitmap
     */
    public static void saveBitmapToSharedPreferences(Context context, String preference, String key, Bitmap bitmap) {
        //第一步:将Bitmap压缩至字节数组输出流ByteArrayOutputStream
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 80, byteArrayOutputStream);
        //第二步:利用Base64将字节数组输出流中的数据转换成字符串String
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        String imageString = new String(Base64.encodeToString(byteArray, Base64.DEFAULT));
        //第三步:将String保持至SharedPreferences
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                preference, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, imageString);
        editor.commit();
    }

    /**
     * 从SharedPreferences取出图片
     * @param context
     * @param preference
     * @param key
     * @param defaultValue
     * @return
     */
    public static Bitmap getBitmapFromSharedPreferences(Context context, String preference, String key, String defaultValue) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                preference, Context.MODE_PRIVATE);
        //第一步:取出字符串形式的Bitmap
        String imageString = sharedPreferences.getString(key, defaultValue);
        if (imageString != null) {
            //第二步:利用Base64将字符串转换为ByteArrayInputStream
            byte[] byteArray = Base64.decode(imageString, Base64.DEFAULT);
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArray);
            //第三步:利用ByteArrayInputStream生成Bitmap
            return BitmapFactory.decodeStream(byteArrayInputStream);
        } else {
            return null;
        }
    }


    /**
     * 设置字符
     *
     * @param context
     * @param preference
     * @param key
     * @param value
     */
    public static void setStringPreferences(Context context, String preference,
                                            String key, String value) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                preference, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.commit();
    }

    /**
     * 获取字符
     *
     * @param context
     * @param preference
     * @param key
     * @param defaultValue
     * @return
     */
    public static String getStringPreference(Context context,
                                             String preference, String key, String defaultValue) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                preference, Context.MODE_PRIVATE);
        return sharedPreferences.getString(key, defaultValue);
    }

    /**
     * 设置长整型
     *
     * @param context
     * @param preference
     * @param key
     * @param value
     */
    public static void setLongPreference(Context context, String preference,
                                         String key, long value) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                preference, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putLong(key, value);
        editor.commit();
    }

    /**
     * 获取长整型
     *
     * @param context
     * @param preference
     * @param key
     * @param defaultValue
     * @return
     */
    public static long getLongPreference(Context context, String preference,
                                         String key, long defaultValue) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                preference, Context.MODE_PRIVATE);
        return sharedPreferences.getLong(key, defaultValue);
    }

    /**
     * 设置boolean类型
     *
     * @param context
     * @param preference
     * @param key
     * @param value
     */
    public static void setBooleanPreferences(Context context,
                                             String preference, String key, boolean value) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                preference, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    /**
     * 获取长整型
     *
     * @param context
     * @param preference
     * @param key
     * @param defaultValue
     * @return
     */
    public static boolean getBooleanPreference(Context context,
                                               String preference, String key, boolean defaultValue) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                preference, Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(key, defaultValue);
    }

    /**
     * 设置int
     *
     * @param context
     * @param preference
     * @param key
     * @param value
     */
    public static void setIntPreferences(Context context, String preference,
                                         String key, int value) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                preference, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(key, value);
        editor.commit();
    }

    /**
     * 获取int
     *
     * @param context
     * @param preference
     * @param key
     * @param defaultValue
     * @return
     */
    public static int getIntPreference(Context context, String preference,
                                       String key, int defaultValue) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                preference, Context.MODE_PRIVATE);
        return sharedPreferences.getInt(key, defaultValue);
    }

    /**
     * 删除一个属性
     *
     * @param context
     * @param preference
     * @param key
     */
    public static void deletePrefereceKey(Context context, String preference,
                                          String key) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                preference, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(key);
        editor.commit();
    }

    // 通过类名字去获取一个对象
    public static <T> T getObject(Context context, Class<T> clazz) {
        String key = getKey(clazz);
        String json = getString(context, key, null);
        if (TextUtils.isEmpty(json)) {
            return null;
        }
        try {
            Gson gson = new Gson();
            return gson.fromJson(json, clazz);
        } catch (Exception e) {
            return null;
        }
    }

    // 通过Type去获取一个泛型对象
    public static <T> T getObject(Context context, Type type) {
        String key = getKey(type);
        String json = getString(context, key, null);
        if (TextUtils.isEmpty(json)) {
            return null;
        }
        try {
            Gson gson = new Gson();
            return gson.fromJson(json, type);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 保存一个对象，object必须是普通类，而不是泛型，如果是泛型,请使用 {@link (Context, Object, Type)}
     *
     * @param context
     * @param object
     */
    public static void putObject(Context context, Object object) {
        String key = getKey(object.getClass());
        Gson gson = new Gson();
        String json = gson.toJson(object);
        putString(context, key, json);
    }

    /**
     * 保存一个泛型对象
     *
     * @param context
     * @param object
     * @param type    如果你要保存 List<Person> 这个类, type应该 传入 new TypeToken<List<Person>>() {}.getType()
     */
    public static void putObject(Context context, Object object, Type type) {
        String key = getKey(type);
        Gson gson = new Gson();
        String json = gson.toJson(object);
        putString(context, key, json);
    }

    public static void removeObject(Context context, Class<?> clazz) {
        remove(context, getKey(clazz));
    }

    public static void removeObject(Context context, Type type) {
        remove(context, getKey(type));
    }

    public static String getKey(Class<?> clazz) {
        return clazz.getName();
    }

    public static String getKey(Type type) {
        return type.toString();
    }

    public static void remove(Context context, String key) {
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sp.edit();
        edit.remove(key);
        edit.commit();
    }

    public static void putString(Context context, String key, String value) {
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sp.edit();
        edit.putString(key, value);
        edit.commit();
    }

    public static String getString(Context context, String key, String defValue) {
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        return sp.getString(key, defValue);
    }
}

