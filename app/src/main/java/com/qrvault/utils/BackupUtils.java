package com.qrvault.utils;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.qrvault.database.QRDatabaseHelper;
import com.qrvault.models.QRCode;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.util.List;

public class BackupUtils {

	private static final String BACKUP_FILE_NAME = "qr_backup.json";

	// ✅ Export to MediaStore > Documents/QRVault/Backups
	public static void backupToJson(@NonNull Context context, @NonNull List<QRCode> qrList) {
		try {
			String fileName = "qr_backup_" + System.currentTimeMillis() + ".json";
			String relativePath = "Documents/QRVault/Backups";

			ContentValues values = new ContentValues();
			values.put(MediaStore.MediaColumns.DISPLAY_NAME, fileName);
			values.put(MediaStore.MediaColumns.MIME_TYPE, "application/json");
			values.put(MediaStore.MediaColumns.RELATIVE_PATH, relativePath);

			Uri uri = context.getContentResolver().insert(MediaStore.Files.getContentUri("external"), values);
			if (uri == null) {
				Toast.makeText(context, "❌ Failed to create file in MediaStore", Toast.LENGTH_SHORT).show();
				return;
			}

			OutputStream outputStream = context.getContentResolver().openOutputStream(uri);
			if (outputStream == null) {
				Toast.makeText(context, "❌ Unable to open output stream", Toast.LENGTH_SHORT).show();
				return;
			}

			String json = new Gson().toJson(qrList);
			outputStream.write(json.getBytes());
			outputStream.flush();
			outputStream.close();

			Toast.makeText(context, "✅ Backup saved to Documents/QRVault/Backups", Toast.LENGTH_LONG).show();

		} catch (Exception e) {
			Toast.makeText(context, "❌ Backup Failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
		}
	}

	// ✅ Restore from file picker (Uri)
	public static void restoreFromJson(@NonNull Context context, @NonNull Uri uri) {
		try {
			InputStream inputStream = context.getContentResolver().openInputStream(uri);
			if (inputStream == null) {
				Toast.makeText(context, "⚠️ Failed to open backup file", Toast.LENGTH_SHORT).show();
				return;
			}

			BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
			Type listType = new TypeToken<List<QRCode>>() {
			}.getType();
			List<QRCode> qrList = new Gson().fromJson(reader, listType);
			reader.close();

			QRDatabaseHelper db = new QRDatabaseHelper(context);
			db.clearAllQR(); // optional clear existing
			db.insertBulkQR(qrList);

			Toast.makeText(context, "✅ Backup Restored Successfully!", Toast.LENGTH_LONG).show();

		} catch (Exception e) {
			Toast.makeText(context, "❌ Restore Failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
		}
	}

	// ✅ Default restore from internal backup file
	public static void restoreFromJson(@NonNull Context context) {
		try {
			File dir = new File(context.getExternalFilesDir(null), "backups");
			File backupFile = new File(dir, BACKUP_FILE_NAME);

			if (!backupFile.exists()) {
				Toast.makeText(context, "⚠️ No Backup Found!", Toast.LENGTH_SHORT).show();
				return;
			}

			FileReader reader = new FileReader(backupFile);
			Type listType = new TypeToken<List<QRCode>>() {
			}.getType();
			List<QRCode> qrList = new Gson().fromJson(reader, listType);
			reader.close();

			QRDatabaseHelper db = new QRDatabaseHelper(context);
			db.clearAllQR(); // optional clear existing
			db.insertBulkQR(qrList);

			Toast.makeText(context, "✅ Backup Restored Successfully!", Toast.LENGTH_LONG).show();

		} catch (Exception e) {
			Toast.makeText(context, "❌ Restore Failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
		}
	}

	// ✅ Launch system file picker to choose a JSON file
	public static void openRestoreFilePicker(@NonNull Context context, @NonNull ActivityResultLauncher<Intent> launcher) {
		Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
		intent.setType("application/json");
		intent.addCategory(Intent.CATEGORY_OPENABLE);
		launcher.launch(intent);
	}
}
