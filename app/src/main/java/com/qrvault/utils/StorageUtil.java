package com.qrvault.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;

public class StorageUtil {
	public static File saveBitmapToStorage(Context context, Bitmap bitmap, String fileName) {
		File dir = new File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "QRVault");
		if (!dir.exists()) dir.mkdirs();

		File file = new File(dir, fileName + ".png");
		try (FileOutputStream out = new FileOutputStream(file)) {
			bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
			return file;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
