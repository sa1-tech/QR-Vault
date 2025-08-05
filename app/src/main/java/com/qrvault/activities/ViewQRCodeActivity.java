package com.qrvault.activities;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.journeyapps.barcodescanner.BarcodeEncoder;
import com.qrvault.R;
import com.qrvault.database.QRDatabaseHelper;
import com.qrvault.models.QRCode;

import java.io.File;
import java.io.FileOutputStream;

public class ViewQRCodeActivity extends AppCompatActivity {

	private TextView tvTitle, tvData;
	private ImageView qrImage, btnShare, btnCopy, btnFavorite;
	private QRCode qrCode;
	private QRDatabaseHelper dbHelper;
	private boolean isFavorite;
	private Button btnEditQR;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_qrcode);

		tvTitle = findViewById(R.id.tvTitle);
		tvData = findViewById(R.id.tvData);
		qrImage = findViewById(R.id.qrImage);
		btnShare = findViewById(R.id.btnShare);
		btnCopy = findViewById(R.id.btnCopy);
		btnFavorite = findViewById(R.id.btnFavorite);
		btnEditQR = findViewById(R.id.btnEditQR);

		dbHelper = new QRDatabaseHelper(this);

		qrCode = (QRCode) getIntent().getSerializableExtra("qr");

		if (qrCode == null) {
			finish();
			return;
		}

		tvTitle.setText(qrCode.getTitle());
		tvData.setText(qrCode.getContent());
		generateQR(qrCode.getContent());

		isFavorite = qrCode.isFavorite();
		updateFavoriteIcon();

		btnCopy.setOnClickListener(v -> {
			ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
			clipboard.setPrimaryClip(ClipData.newPlainText("QR Content", qrCode.getContent()));
			Toast.makeText(this, "Copied to clipboard", Toast.LENGTH_SHORT).show();
		});

//		btnShare.setOnClickListener(v -> {
//			Intent shareIntent = new Intent(Intent.ACTION_SEND);
//			shareIntent.setType("text/plain");
//			shareIntent.putExtra(Intent.EXTRA_TEXT, qrCode.getContent());
//			startActivity(Intent.createChooser(shareIntent, "Share QR Code"));
//		});
		btnShare.setOnClickListener(v -> {
			try {
				BarcodeEncoder encoder = new BarcodeEncoder();
				Bitmap qrBitmap = encoder.encodeBitmap(qrCode.getContent(), com.google.zxing.BarcodeFormat.QR_CODE, 500, 500);

				// Save bitmap to cache directory
				File cachePath = new File(getCacheDir(), "images");
				cachePath.mkdirs();
				File file = new File(cachePath, "qr_share.png");
				FileOutputStream stream = new FileOutputStream(file);
				qrBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
				stream.close();

				// Get URI using FileProvider
				Uri contentUri = FileProvider.getUriForFile(this, getPackageName() + ".provider", file);

				// Create share intent
				Intent shareIntent = new Intent(Intent.ACTION_SEND);
				shareIntent.setType("image/png");
				shareIntent.putExtra(Intent.EXTRA_STREAM, contentUri);
				shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
				startActivity(Intent.createChooser(shareIntent, "Share QR Code"));

			} catch (Exception e) {
				Toast.makeText(this, "Error sharing QR image", Toast.LENGTH_SHORT).show();
				e.printStackTrace();
			}
		});


		btnFavorite.setOnClickListener(v -> {
			isFavorite = !isFavorite;
			qrCode.setFavorite(isFavorite);
			dbHelper.updateQRCode(qrCode);
			updateFavoriteIcon();
			Toast.makeText(this, isFavorite ? "Added to favorites" : "Removed from favorites", Toast.LENGTH_SHORT).show();
		});

		btnEditQR.setOnClickListener(v -> {
			Intent intent = new Intent(ViewQRCodeActivity.this, EditQRCodeActivity.class);
			intent.putExtra("qr", qrCode);
			startActivity(intent);
		});
	}

	private void generateQR(String data) {
		try {
			BarcodeEncoder encoder = new BarcodeEncoder();
			Bitmap bitmap = encoder.encodeBitmap(data, com.google.zxing.BarcodeFormat.QR_CODE, 500, 500);
			qrImage.setImageBitmap(bitmap);
		} catch (Exception e) {
			Toast.makeText(this, "Error generating QR code", Toast.LENGTH_SHORT).show();
		}
	}

	private void updateFavoriteIcon() {
		// Change these drawable names if you use star_filled and star_border
		btnFavorite.setImageResource(isFavorite ? R.drawable.s2 : R.drawable.s);
	}
}
