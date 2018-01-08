package com.renny.simplebrowser.business.sp;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.renny.simplebrowser.App;
import com.renny.simplebrowser.business.json.JsonHelper;

import java.lang.reflect.Type;
import java.util.Map;


public enum Sps {
    defaults("renny_default"), h5("renny_h5");
    private SharedPreferences sharedPreferences;

    Sps(String spFile) {
        sharedPreferences = App.getContext().getSharedPreferences(spFile, Context.MODE_PRIVATE);
    }

    public SharedPreferences getPrefs() {
        return sharedPreferences;
    }

    public <T> void put(String key, T value) {
        if (value == null) {
            return;
        }
        SharedPreferences.Editor editor = getPrefs().edit();
        if (value instanceof String) {
            editor.putString(key, (String) value);
        } else if (value instanceof Long) {
            editor.putLong(key, (Long) value);
        } else if (value instanceof Integer) {
            editor.putInt(key, (Integer) value);
        } else if (value instanceof Boolean) {
            editor.putBoolean(key, (Boolean) value);
        } else if (value instanceof Float) {
            editor.putFloat(key, (Float) value);
        }
        editor.apply();
    }

    public Object get(String key) {
        Map<String, ?> values = Sps.h5.getAll();
        Object result = null;
        if (values != null && values.containsKey(key)) {
            result = values.get(key);
        }
        return result;
    }

    public String getString(String key) {
        return getPrefs().getString(key, "");
    }

    public void putString(String key, String value) {
        getPrefs().edit().putString(key, value).apply();
    }

    public int getInt(String key) {
        return getPrefs().getInt(key, 0);
    }

    public void putInt(String key, int value) {
        getPrefs().edit().putInt(key, value).apply();
    }

    public long getLong(String key, long defaultLong) {
        return getPrefs().getLong(key, defaultLong);
    }

    public void putLong(String key, long value) {
        getPrefs().edit().putLong(key, value).apply();
    }

    public boolean getBoolean(String key, boolean defaultValue) {
        return getPrefs().getBoolean(key, defaultValue);
    }

    public void putBoolean(String key, boolean value) {
        getPrefs().edit().putBoolean(key, value).apply();
    }

    public <T> T getBean(String key, Type type) {
        String json = getPrefs().getString(key, null);
        if (TextUtils.isEmpty(json)) {
            return null;
        }
        return JsonHelper.fromJson(json, type);
    }

    public void putBean(String key, Object bean) {
        getPrefs().edit().putString(key, JsonHelper.toJSONString(bean)).apply();
    }

    public void remove(String key) {
        getPrefs().edit().remove(key).apply();
    }

    public Map<String, ?> getAll() {
        return getPrefs().getAll();
    }

    public void removeAll() {
        getPrefs().edit().clear().apply();
    }
}
