package com.qrvault.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.qrvault.R;
import com.qrvault.adapters.QRCodeAdapter;
import com.qrvault.database.QRDatabaseHelper;
import com.qrvault.models.QRCode;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

	private RecyclerView qrRecyclerView;
	private QRCodeAdapter adapter;
	private QRDatabaseHelper dbHelper;
	private SearchView searchView;
	private ImageButton btnFavorite, btnSettings;
	private boolean showingFavorites = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// Initialize UI components
		qrRecyclerView = findViewById(R.id.qrRecyclerView);
		searchView = findViewById(R.id.searchView);
		btnFavorite = findViewById(R.id.btnFavorite);
		btnSettings = findViewById(R.id.btnSettings);
		dbHelper = new QRDatabaseHelper(this);

		// Setup RecyclerView
		qrRecyclerView.setLayoutManager(new LinearLayoutManager(this));
		adapter = new QRCodeAdapter(this, new ArrayList<>(), qr -> {
			// Pass the QRCode object (must implement Serializable)
			Intent intent = new Intent(MainActivity.this, ViewQRCodeActivity.class);
			intent.putExtra("qr", qr);
			startActivity(intent);
		});
		qrRecyclerView.setAdapter(adapter);

		// Add new QR
		findViewById(R.id.btnAddQR).setOnClickListener(v -> {
			startActivity(new Intent(MainActivity.this, AddQRCodeActivity.class));
		});

		// Scan QR
		findViewById(R.id.btnScanQR).setOnClickListener(v -> {
			Intent intent = new Intent(MainActivity.this, ScanQRCodeActivity.class);
			startActivityForResult(intent, 1001);
		});

		// Open Settings
		btnSettings.setOnClickListener(v -> {
			startActivity(new Intent(MainActivity.this, SettingsActivity.class));
		});

		// SearchView logic
		searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
			@Override
			public boolean onQueryTextSubmit(String query) {
				return false; // Not used
			}

			@Override
			public boolean onQueryTextChange(String newText) {
				adapter.filter(newText);

				return true;
			}
		});


		// Toggle Favorites
		btnFavorite.setOnClickListener(v -> {
			showingFavorites = !showingFavorites;
			if (showingFavorites) {
				List<QRCode> favorites = dbHelper.getFavoriteQRCodes();
				adapter.updateData(favorites);
				Log.d("QR_DEBUG", "Showing favorites: " + favorites.size());
			} else {
				loadQRCodes();
				Log.d("QR_DEBUG", "Showing all QR codes");
			}
		});

		// Load all QR codes initially
		loadQRCodes();
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (!showingFavorites) {
			loadQRCodes();
		}
	}

	@Override
	public void onBackPressed() {
		if (showingFavorites) {
			showingFavorites = false;
			loadQRCodes();
		} else {
			super.onBackPressed();
		}
	}

	private void loadQRCodes() {
		List<QRCode> allQRCodes = dbHelper.getAllQRCodes();
		adapter.updateData(allQRCodes);
		Log.d("QR_DEBUG", "Loaded all QR codes: " + allQRCodes.size());
	}
}
