package com.qrvault.utils;

import android.app.Activity;
import android.content.SharedPreferences;

import com.qrvault.R;

public class ThemeUtil {

	// Theme constants
	public static final int THEME_LIGHT = 0;
	public static final int THEME_DARK = 1;
	public static final int THEME_SYSTEM = 2;
	private static final String PREFS_NAME = "settings";
	private static final String KEY_THEME_MODE = "theme_mode";

	/**
	 * Apply selected theme before setContentView() is called in Activity.
	 */
	public static void applyTheme(Activity activity) {
		SharedPreferences prefs = activity.getSharedPreferences(PREFS_NAME, Activity.MODE_PRIVATE);
		int theme = prefs.getInt(KEY_THEME_MODE, THEME_SYSTEM); // Default = system

		switch (theme) {
			case THEME_LIGHT:
				activity.setTheme(R.style.AppTheme_Light);
				break;
			case THEME_DARK:
				activity.setTheme(R.style.AppTheme_Dark);
				break;
			case THEME_SYSTEM:
			default:
				activity.setTheme(R.style.AppTheme_System); // Should follow system default
				break;
		}
	}

	/**
	 * Save selected theme mode into SharedPreferences.
	 */
	public static void setThemeMode(Activity activity, int themeMode) {
		activity.getSharedPreferences(PREFS_NAME, Activity.MODE_PRIVATE)
				.edit()
				.putInt(KEY_THEME_MODE, themeMode)
				.apply();
	}

	/**
	 * Get current saved theme mode.
	 */
	public static int getThemeMode(Activity activity) {
		return activity.getSharedPreferences(PREFS_NAME, Activity.MODE_PRIVATE)
				.getInt(KEY_THEME_MODE, THEME_SYSTEM);
	}
}
