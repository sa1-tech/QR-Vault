package com.qrvault.services;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.Nullable;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.qrvault.models.QRCode;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class BackupService extends IntentService {

	public static final String ACTION_BACKUP = "com.qrvault.BACKUP";
	public static final String EXTRA_QR_LIST = "extra_qr_list";

	public BackupService() {
		super("BackupService");
	}

	@Override
	protected void onHandleIntent(@Nullable Intent intent) {
		if (intent != null && ACTION_BACKUP.equals(intent.getAction())) {
			List<QRCode> qrCodes = (List<QRCode>) intent.getSerializableExtra(EXTRA_QR_LIST);
			if (qrCodes != null) {
				backupToFirebase(qrCodes);
			}
		}
	}

	private void backupToFirebase(List<QRCode> qrCodes) {
		DatabaseReference ref = FirebaseDatabase.getInstance().getReference("qr_backups");

		// Optional: Use current timestamp for backup group
		String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
		DatabaseReference backupGroupRef = ref.child("backup_" + timestamp);

		for (QRCode qr : qrCodes) {
			backupGroupRef.child(String.valueOf(qr.getId())).setValue(qr);
		}

		Log.d("BackupService", "Backup completed to Firebase under: backup_" + timestamp);
	}
}
