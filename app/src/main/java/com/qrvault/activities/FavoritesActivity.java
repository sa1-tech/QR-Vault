package com.qrvault.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.qrvault.R;
import com.qrvault.adapters.QRCodeAdapter;
import com.qrvault.database.QRDatabaseHelper;
import com.qrvault.models.QRCode;

import java.util.List;

public class FavoritesActivity extends AppCompatActivity implements QRCodeAdapter.OnQRClickListener {

	private RecyclerView recyclerView;
	private TextView emptyView;
	private QRCodeAdapter adapter;
	private QRDatabaseHelper dbHelper;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_favorites);

		recyclerView = findViewById(R.id.recyclerFavorites);
		emptyView = findViewById(R.id.emptyText);
		dbHelper = new QRDatabaseHelper(this);

		recyclerView.setLayoutManager(new LinearLayoutManager(this));

		if (getSupportActionBar() != null) {
			getSupportActionBar().setTitle("Favorite QR Codes");
			getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		loadFavorites();
	}

	private void loadFavorites() {
		List<QRCode> favorites = dbHelper.getFavoriteQRCodes();
		if (favorites == null || favorites.isEmpty()) {
			emptyView.setVisibility(View.VISIBLE);
			recyclerView.setVisibility(View.GONE);
		} else {
			emptyView.setVisibility(View.GONE);
			recyclerView.setVisibility(View.VISIBLE);
			if (adapter == null) {
				adapter = new QRCodeAdapter(this, favorites, this);
				recyclerView.setAdapter(adapter);
			} else {
				adapter.updateData(favorites);

			}
		}
	}

	@Override
	public void onQRClicked(QRCode qrCode) {
		Intent intent = new Intent(this, ViewQRCodeActivity.class);
		intent.putExtra("qr", qrCode); // QRCode should implement Serializable
		startActivity(intent);
	}

	@Override
	public boolean onSupportNavigateUp() {
		onBackPressed();
		return true;
	}
}
