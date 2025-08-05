package com.qrvault.utils;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BackupManagerUtil {

	private static final String DB_NAME = "qrvault.db";
	private static final String BACKUP_FOLDER = "QRVaultBackup";
	private static final ExecutorService executor = Executors.newSingleThreadExecutor();
	private static final Handler mainHandler = new Handler(Looper.getMainLooper());
	private final Context context;
	private final Mode mode;
	private final BackupCallback callback;

	public BackupManagerUtil(Context context, Mode mode, BackupCallback callback) {
		this.context = context.getApplicationContext();
		this.mode = mode;
		this.callback = callback;
	}

	public void execute() {
		executor.execute(() -> {
			boolean result = (mode == Mode.BACKUP)
					? performBackup()
					: performRestore();

			mainHandler.post(() -> {
				if (callback != null) callback.onComplete(result);
			});
		});
	}

	private boolean performBackup() {
		File dbFile = context.getDatabasePath(DB_NAME);
		File backupDir = new File(context.getExternalFilesDir(null), BACKUP_FOLDER);
		if (!backupDir.exists() && !backupDir.mkdirs()) return false;

		File backupFile = new File(backupDir, DB_NAME);

		try (FileChannel src = new FileInputStream(dbFile).getChannel();
		     FileChannel dst = new FileOutputStream(backupFile).getChannel()) {

			dst.transferFrom(src, 0, src.size());
			return true;

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	private boolean performRestore() {
		File dbFile = context.getDatabasePath(DB_NAME);
		File backupFile = new File(context.getExternalFilesDir(null), BACKUP_FOLDER + "/" + DB_NAME);

		if (!backupFile.exists()) return false;

		try (FileChannel src = new FileInputStream(backupFile).getChannel();
		     FileChannel dst = new FileOutputStream(dbFile).getChannel()) {

			dst.transferFrom(src, 0, src.size());
			return true;

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public enum Mode {BACKUP, RESTORE}

	public interface BackupCallback {
		void onComplete(boolean success);
	}
}
