package com.qrvault.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.qrvault.R;
import com.qrvault.database.QRDatabaseHelper;
import com.qrvault.models.QRCode;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class QRCodeAdapter extends RecyclerView.Adapter<QRCodeAdapter.ViewHolder> {
	private final List<QRCode> originalList;
	private final List<QRCode> filteredList;
	private final Context context;
	private final OnQRClickListener listener;
	private final QRDatabaseHelper dbHelper;

	public QRCodeAdapter(Context context, List<QRCode> qrCodes, OnQRClickListener listener) {
		this.context = context;
		this.originalList = new ArrayList<>(qrCodes);
		this.filteredList = new ArrayList<>(qrCodes);
		this.listener = listener;
		this.dbHelper = new QRDatabaseHelper(context);
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View view = LayoutInflater.from(context).inflate(R.layout.item_qrcode, parent, false);
		return new ViewHolder(view);
	}

	@Override
	public void onBindViewHolder(ViewHolder holder, int position) {
		QRCode qr = filteredList.get(position);

		holder.tvTitle.setText((qr.getTitle() == null || qr.getTitle().isEmpty()) ? "(Untitled)" : qr.getTitle());
		holder.tvContent.setText(qr.getContent());

		String formattedDate = (qr.getTimestamp() > 0)
				? new SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault()).format(qr.getTimestamp())
				: "No timestamp";
		holder.tvTimestamp.setText(formattedDate);

		holder.itemView.setOnClickListener(v -> {
			if (listener != null) listener.onQRClicked(qr);
		});

		holder.itemView.setOnLongClickListener(v -> {
			boolean newFavState = !qr.isFavorite();
			qr.setFavorite(newFavState);
			boolean success = dbHelper.toggleFavorite(qr.getId(), newFavState);
			if (success) {
				Toast.makeText(context, newFavState ? "Marked as Favorite" : "Removed from Favorites", Toast.LENGTH_SHORT).show();
				notifyItemChanged(holder.getAdapterPosition());
			} else {
				Toast.makeText(context, "Failed to update favorite", Toast.LENGTH_SHORT).show();
			}
			return true;
		});
	}

	@Override
	public int getItemCount() {
		return filteredList.size();
	}

	public void filter(String query) {
		filteredList.clear();
		if (query == null || query.trim().isEmpty()) {
			filteredList.addAll(originalList);
		} else {
			String lowerQuery = query.toLowerCase();
			for (QRCode qr : originalList) {
				if ((qr.getTitle() != null && qr.getTitle().toLowerCase().contains(lowerQuery)) ||
						(qr.getContent() != null && qr.getContent().toLowerCase().contains(lowerQuery))) {
					filteredList.add(qr);
				}
			}
		}
		notifyDataSetChanged();
	}

	// ✅ Renamed from updateList → updateData
	public void updateData(List<QRCode> newList) {
		originalList.clear();
		originalList.addAll(newList);
		filteredList.clear();
		filteredList.addAll(newList);
		notifyDataSetChanged();
	}

	public interface OnQRClickListener {
		void onQRClicked(QRCode qrCode);
	}

	public static class ViewHolder extends RecyclerView.ViewHolder {
		TextView tvTitle, tvContent, tvTimestamp;

		public ViewHolder(View view) {
			super(view);
			tvTitle = view.findViewById(R.id.tvTitle);
			tvContent = view.findViewById(R.id.tvContent);
			tvTimestamp = view.findViewById(R.id.tvTimestamp);
		}
	}
}
