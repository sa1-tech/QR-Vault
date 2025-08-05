package com.qrvault.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.qrvault.database.QRDatabaseHelper;
import com.qrvault.models.QRCode;

public class ScanQRCodeActivity extends AppCompatActivity {
	private IntentIntegrator qrScanner;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		qrScanner = new IntentIntegrator(this);
		qrScanner.setOrientationLocked(false);
		qrScanner.setPrompt("Scan a QR Code");
		qrScanner.setBeepEnabled(true);
		qrScanner.initiateScan();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
		if (result != null) {
			if (result.getContents() != null) {
				String scannedData = result.getContents();

				// Construct QRCode object with proper constructor arguments
				QRCode scannedQR = new QRCode(
						0,                          // id
						"Scanned QR",              // title
						scannedData,               // content
						"",                        // imagePath
						System.currentTimeMillis(),// timestamp
						false,                     // isFavorite
						"Text",                    // type
						""                         // tags
				);

				QRDatabaseHelper dbHelper = new QRDatabaseHelper(this);
				boolean success = dbHelper.insertQRCode(scannedQR);

				Toast.makeText(
						this,
						success ? "QR Code saved to vault" : "Failed to save QR Code",
						Toast.LENGTH_SHORT
				).show();

				// Navigate back to MainActivity
				Intent returnIntent = new Intent(this, MainActivity.class);
				returnIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
				startActivity(returnIntent);
				finish();

			} else {
				Toast.makeText(this, "Scan cancelled", Toast.LENGTH_SHORT).show();
				finish();
			}
		} else {
			super.onActivityResult(requestCode, resultCode, data);
		}
	}
}
