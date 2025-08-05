package com.qrvault.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.qrvault.R;

import java.util.List;

public class BackupListAdapter extends RecyclerView.Adapter<BackupListAdapter.Holder> {

	private final List<String> backups;
	private final OnBackupClickListener listener;

	public BackupListAdapter(List<String> backups, OnBackupClickListener listener) {
		this.backups = backups;
		this.listener = listener;
	}

	@NonNull
	@Override
	public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		View view = LayoutInflater.from(parent.getContext())
				.inflate(R.layout.item_backup, parent, false);
		return new Holder(view);
	}

	@Override
	public void onBindViewHolder(@NonNull Holder holder, int position) {
		String backupKey = backups.get(position);
		holder.tvBackupTime.setText(formatBackupLabel(backupKey));
		holder.itemView.setOnClickListener(v -> {
			if (listener != null) {
				listener.onBackupSelected(backupKey);
			}
		});
	}

	@Override
	public int getItemCount() {
		return backups.size();
	}

	// Optional: Pretty format for backup key like "backup_20240626_2345"
	private String formatBackupLabel(String key) {
		if (key.startsWith("backup_")) {
			String raw = key.replace("backup_", "");
			if (raw.matches("\\d{8}_\\d{4}")) {
				String date = raw.substring(0, 8);
				String time = raw.substring(9);
				return String.format("Backup: %s/%s/%s %s:%s",
						date.substring(6), date.substring(4, 6), date.substring(0, 4),
						time.substring(0, 2), time.substring(2));
			}
		}
		return key; // fallback
	}

	public interface OnBackupClickListener {
		void onBackupSelected(String backupKey);
	}

	static class Holder extends RecyclerView.ViewHolder {
		TextView tvBackupTime;

		Holder(View itemView) {
			super(itemView);
			tvBackupTime = itemView.findViewById(R.id.tvBackupTime);
		}
	}
}
