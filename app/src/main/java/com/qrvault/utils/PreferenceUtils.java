package com.qrvault.utils;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.appcompat.app.AppCompatActivity;

import com.qrvault.R;

public class PreferenceUtils {

	// --- Theme Constants ---
	public static final int THEME_LIGHT = 0;
	public static final int THEME_DARK = 1;
	public static final int THEME_SYSTEM = 2;

	private static final String PREFS_NAME = "qr_preferences";

	// Preference Keys
	private static final String KEY_THEME_MODE = "theme_mode";
	private static final String KEY_APP_PIN = "app_pin";

	// --- Theme Preference Methods ---
	public static void setThemePref(Context context, int themeMode) {
		getPrefs(context).edit().putInt(KEY_THEME_MODE, themeMode).apply();
	}

	public static int getThemePref(Context context) {
		return getPrefs(context).getInt(KEY_THEME_MODE, THEME_SYSTEM);
	}

	public static void applyTheme(AppCompatActivity activity) {
		switch (getThemePref(activity)) {
			case THEME_LIGHT:
				activity.setTheme(R.style.AppTheme_Light);
				break;
			case THEME_DARK:
				activity.setTheme(R.style.AppTheme_Dark);
				break;
			case THEME_SYSTEM:
			default:
				activity.setTheme(R.style.AppTheme_System); // Ensure this exists in styles.xml
				break;
		}
	}

	// --- App Lock PIN Methods ---
	public static void setAppPin(Context context, String pin) {
		getPrefs(context).edit().putString(KEY_APP_PIN, pin).apply();
	}

	public static String getAppPin(Context context) {
		return getPrefs(context).getString(KEY_APP_PIN, null);
	}

	public static void clearAppPin(Context context) {
		getPrefs(context).edit().remove(KEY_APP_PIN).apply();
	}

	public static boolean isPinSet(Context context) {
		return getAppPin(context) != null;
	}

	// --- Private SharedPreferences Accessor ---
	private static SharedPreferences getPrefs(Context context) {
		return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
	}
}
