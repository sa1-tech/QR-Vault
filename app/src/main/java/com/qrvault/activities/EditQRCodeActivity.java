package com.qrvault.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.qrvault.R;
import com.qrvault.database.QRDatabaseHelper;
import com.qrvault.models.QRCode;

public class EditQRCodeActivity extends AppCompatActivity {

	private EditText inputTitle, inputData;
	private Button btnUpdate;
	private QRCode qrCode;
	private QRDatabaseHelper dbHelper;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_qrcode);

		// Initialize views
		inputTitle = findViewById(R.id.inputTitle);
		inputData = findViewById(R.id.inputData);
		btnUpdate = findViewById(R.id.btnUpdate);
		dbHelper = new QRDatabaseHelper(this);

		// Get QR object from Intent
		qrCode = (QRCode) getIntent().getSerializableExtra("qr");

		if (qrCode == null) {
			Toast.makeText(this, "Invalid QR Code", Toast.LENGTH_SHORT).show();
			finish();
			return;
		}

		// Set existing values
		inputTitle.setText(qrCode.getTitle());
		inputData.setText(qrCode.getContent());

		// Update button logic
		btnUpdate.setOnClickListener(v -> {
			String updatedTitle = inputTitle.getText().toString().trim();
			String updatedData = inputData.getText().toString().trim();

			if (updatedTitle.isEmpty() || updatedData.isEmpty()) {
				Toast.makeText(this, "Title and data cannot be empty", Toast.LENGTH_SHORT).show();
				return;
			}

			// Update model
			qrCode.setTitle(updatedTitle);
			qrCode.setContent(updatedData);
			qrCode.setTimestamp(System.currentTimeMillis());

			// Update in database
			boolean isUpdated = dbHelper.updateQRCode(qrCode);
			if (isUpdated) {
				Toast.makeText(this, "QR Code updated successfully", Toast.LENGTH_SHORT).show();

				// Redirect to MainActivity
				Intent intent = new Intent(EditQRCodeActivity.this, MainActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(intent);
				finish(); // Finish current activity to avoid backstack
			} else {
				Toast.makeText(this, "Update failed. Please try again.", Toast.LENGTH_SHORT).show();
			}
		});

	}
}
