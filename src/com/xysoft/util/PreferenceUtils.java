package com.xysoft.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

public class PreferenceUtils {
	
	public static boolean hasKey(Context context, String key) {
		return PreferenceManager.getDefaultSharedPreferences(context).contains(key);
	}
	
	public static String getString(Context context, String key, String defaultValue) {
		SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
		return settings.getString(key, defaultValue);
	}

	public static void setString(Context context, String key, String value) {
		SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
		settings.edit().putString(key, value).commit();
	}

	public static boolean getBoolean(Context context, String key, boolean defaultValue) {
		final SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
		return settings.getBoolean(key, defaultValue);
	}

	public static void setBoolean(Context context, String key, boolean value) {
		SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
		settings.edit().putBoolean(key, value).commit();
	}

	public static void setInt(Context context, String key, int value) {
		SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
		settings.edit().putInt(key, value).commit();
	}

	public static int getInt(Context context, String key, int defaultValue) {
		SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
		return settings.getInt(key, defaultValue);
	}

	public static void setFloat(Context context, String key, float value) {
		SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
		settings.edit().putFloat(key, value).commit();
	}

	public static float getFloat(Context context, String key, float defaultValue) {
		SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
		return settings.getFloat(key, defaultValue);
	}

	public static void setLong(Context context, String key, long value) {
		SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
		settings.edit().putLong(key, value).commit();
	}

	public static long getLong(Context context, String key, long defaultValue) {
		SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
		return settings.getLong(key, defaultValue);
	}

	public static void clearPreference(Context context,SharedPreferences p) {
		Editor editor = p.edit();
		editor.clear();
		editor.commit();
	}
}
