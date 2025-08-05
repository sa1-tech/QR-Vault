package com.qrvault.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import com.qrvault.R;
import com.qrvault.database.QRDatabaseHelper;
import com.qrvault.models.QRCode;
import com.qrvault.utils.BackupUtils;
import com.qrvault.utils.ExportUtils;
import com.qrvault.utils.PreferenceUtils;

import java.util.List;

public class SettingsActivity extends AppCompatActivity {

	private SwitchCompat switchDarkMode;
	private ActivityResultLauncher<Intent> restoreFilePickerLauncher;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// Apply theme before view inflation
		PreferenceUtils.applyTheme(this);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);

		initViews();
		setupListeners();
		initRestorePicker();
	}

	private void initViews() {
		switchDarkMode = findViewById(R.id.switchDarkMode);
		int themePref = PreferenceUtils.getThemePref(this);
		switchDarkMode.setChecked(themePref == PreferenceUtils.THEME_DARK);
	}

	private void setupListeners() {
		switchDarkMode.setOnCheckedChangeListener((buttonView, isChecked) -> {
			PreferenceUtils.setThemePref(this,
					isChecked ? PreferenceUtils.THEME_DARK : PreferenceUtils.THEME_LIGHT);
			recreate();
		});

		findViewById(R.id.btnExportCSV).setOnClickListener(v -> {
			QRDatabaseHelper dbHelper = new QRDatabaseHelper(this);
			List<QRCode> qrList = dbHelper.getAllQRCodes();
			ExportUtils.exportToCSV(this, qrList);
		});

		findViewById(R.id.btnExportPDF).setOnClickListener(v -> {
			QRDatabaseHelper dbHelper = new QRDatabaseHelper(this);
			List<QRCode> qrList = dbHelper.getAllQRCodes();
			ExportUtils.exportToPDF(this, qrList);
		});

		findViewById(R.id.btnBackup).setOnClickListener(v -> {
			QRDatabaseHelper dbHelper = new QRDatabaseHelper(this);
			List<QRCode> qrList = dbHelper.getAllQRCodes();
			BackupUtils.backupToJson(this, qrList);
		});


		findViewById(R.id.btnRestore).setOnClickListener(v ->
				BackupUtils.openRestoreFilePicker(this, restoreFilePickerLauncher));

		findViewById(R.id.btnAbout).setOnClickListener(v ->
				showAboutDialog());
	}

	private void initRestorePicker() {
		restoreFilePickerLauncher = registerForActivityResult(
				new ActivityResultContracts.StartActivityForResult(), result -> {
					if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
						BackupUtils.restoreFromJson(this, result.getData().getData());
					} else {
						showToast("Restore cancelled");
					}
				});
	}

	private void showToast(String msg) {
		Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
	}

	private void showAboutDialog() {
		String htmlContent = "<h2 style='text-align:center;'>📱 QRVault</h2>"
				+ "<p><b>Version:</b> 1.0.0</p>"
				+ "<p><b>Developer:</b> <i>Sa1</i> 👨‍💻</p>"
				+ "<p><b>Privacy Policy:</b><br/>"
				+ "We do <u>not</u> collect, store, or share any personal data.<br/>"
				+ "All QR codes are stored <b>locally</b> and securely on your device.</p>"
				+ "<p style='text-align:center;'>❤️ Built with passion for productivity.</p>";

		new AlertDialog.Builder(this)
				.setTitle("About")
				.setMessage(Html.fromHtml(htmlContent, Html.FROM_HTML_MODE_COMPACT))
				.setPositiveButton("Got it", null)
				.show();
	}
}
