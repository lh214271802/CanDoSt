/**
 * Copyright 2016 JustWayward Team
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.lh.util;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.v4.content.SharedPreferencesCompat;
import android.util.Base64;

import com.blankj.utilcode.util.CloseUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
import java.util.Map;
import java.util.Set;

/**
 * Created by lfh on 2016/8/13.
 */
public class SharedPreferencesUtil {

    private static SharedPreferencesUtil prefsUtil;
    public Context context;
    public SharedPreferences prefs;
    public SharedPreferences.Editor editor;

    public synchronized static SharedPreferencesUtil getInstance() {
        return prefsUtil;
    }

    public static void init(Context context, String prefsname, int mode) {
        prefsUtil = new SharedPreferencesUtil();
        prefsUtil.context = context;
        prefsUtil.prefs = prefsUtil.context.getSharedPreferences(prefsname, mode);
        prefsUtil.editor = prefsUtil.prefs.edit();
    }

    private SharedPreferencesUtil() {
    }


    public boolean getBoolean(String key, boolean defaultVal) {
        return this.prefs.getBoolean(key, defaultVal);
    }

    public boolean getBoolean(String key) {
        return this.prefs.getBoolean(key, false);
    }


    public String getString(String key, String defaultVal) {
        return this.prefs.getString(key, defaultVal);
    }

    public String getString(String key) {
        return this.prefs.getString(key, null);
    }

    public int getInt(String key, int defaultVal) {
        return this.prefs.getInt(key, defaultVal);
    }

    public int getInt(String key) {
        return this.prefs.getInt(key, 0);
    }


    public float getFloat(String key, float defaultVal) {
        return this.prefs.getFloat(key, defaultVal);
    }

    public float getFloat(String key) {
        return this.prefs.getFloat(key, 0f);
    }

    public long getLong(String key, long defaultVal) {
        return this.prefs.getLong(key, defaultVal);
    }

    public long getLong(String key) {
        return this.prefs.getLong(key, 0l);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public Set<String> getStringSet(String key, Set<String> defaultVal) {
        return this.prefs.getStringSet(key, defaultVal);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public Set<String> getStringSet(String key) {
        return this.prefs.getStringSet(key, null);
    }

    public Map<String, ?> getAll() {
        return this.prefs.getAll();
    }

    public boolean exists(String key) {
        return prefs.contains(key);
    }


    public SharedPreferencesUtil putString(String key, String value) {
        editor.putString(key, value);
        editor.commit();
        return this;
    }

    public SharedPreferencesUtil putInt(String key, int value) {
        editor.putInt(key, value);
        editor.commit();
        return this;
    }

    public SharedPreferencesUtil putFloat(String key, float value) {
        editor.putFloat(key, value);
        editor.commit();
        return this;
    }

    public SharedPreferencesUtil putLong(String key, long value) {
        editor.putLong(key, value);
        editor.commit();
        return this;
    }

    public SharedPreferencesUtil putBoolean(String key, boolean value) {
        editor.putBoolean(key, value);
        editor.commit();
        return this;
    }

    public void commit() {
        editor.commit();
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public SharedPreferencesUtil putStringSet(String key, Set<String> value) {
        editor.putStringSet(key, value);
        editor.commit();
        return this;
    }

    public boolean putObject(String key, Object object) {
        boolean result = false;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream out = null;
        try {
            out = new ObjectOutputStream(baos);
            out.writeObject(object);
            String objectVal = new String(Base64.encode(baos.toByteArray(), Base64.DEFAULT));
            editor.putString(key, objectVal);
            result = editor.commit();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            CloseUtils.closeIO(baos, out);
            return result;
        }
    }

    public <T> T getObject(String key, Class<T> clazz) {
        if (prefs.contains(key)) {
            String objectVal = prefs.getString(key, null);
            byte[] buffer = Base64.decode(objectVal, Base64.DEFAULT);
            ByteArrayInputStream bais = new ByteArrayInputStream(buffer);
            ObjectInputStream ois = null;
            try {
                ois = new ObjectInputStream(bais);
                T t = (T) ois.readObject();
                return t;
            } catch (StreamCorruptedException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (bais != null) {
                        bais.close();
                    }
                    if (ois != null) {
                        ois.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    public SharedPreferencesUtil remove(String key) {
        editor.remove(key);
        editor.commit();
        return this;
    }

    public SharedPreferencesUtil removeAll() {
        editor.clear();
        editor.commit();
        return this;
    }

    private static final String WRITABLE_FLAG = "writable";

    public boolean getCachedWritableFlag() {
        return null == prefs || prefs.getBoolean(WRITABLE_FLAG, true);
    }


    private static final String SOFT_KEYBOARD_HEIGHT = "SoftKeyboardHeight";

    public void setCachedKeyboardHeight(int height) {
        if (null != prefs) {
            prefs.edit().putInt(SOFT_KEYBOARD_HEIGHT, height).apply();
        }
    }

    public int getCachedKeyboardHeight() {
        if (null != prefs) {
            return prefs.getInt(SOFT_KEYBOARD_HEIGHT, 0);
        }
        return 0;
    }

    public void setCachedWritableFlag(boolean value) {
        if (null != prefs) {
            prefs.edit().putBoolean(WRITABLE_FLAG, value).apply();
        }
    }

    private static final String KEY_SOFT_KEYBOARD_HEIGHT = "softKeyboardHeight";

    public void updateSoftKeyboardHeight(int height) {
        SharedPreferences.Editor editor = prefs.edit().putInt(KEY_SOFT_KEYBOARD_HEIGHT, height);
        SharedPreferencesCompat.EditorCompat.getInstance().apply(editor);
    }


    public int getSoftKeyboardHeight() {
        return prefs.getInt(KEY_SOFT_KEYBOARD_HEIGHT, 0);
    }
}
