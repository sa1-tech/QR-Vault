package com.qrvault.utils;

import android.app.Activity;
import android.view.Window;
import android.view.WindowManager;

public class SecurityUtil {

	public static void enableSecureMode(Activity activity) {
		if (activity != null && activity.getWindow() != null) {
			Window window = activity.getWindow();
			window.setFlags(
					WindowManager.LayoutParams.FLAG_SECURE,
					WindowManager.LayoutParams.FLAG_SECURE
			);
		}
	}

	public static void disableSecureMode(Activity activity) {
		if (activity != null && activity.getWindow() != null) {
			activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_SECURE);
		}
	}
}
