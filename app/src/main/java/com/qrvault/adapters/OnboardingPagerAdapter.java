package com.qrvault.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.qrvault.R;
import com.qrvault.models.OnboardingItem;

import java.util.List;

public class OnboardingPagerAdapter extends RecyclerView.Adapter<OnboardingPagerAdapter.OnboardingViewHolder> {

	private final List<OnboardingItem> onboardingItems;

	public OnboardingPagerAdapter(List<OnboardingItem> items) {
		this.onboardingItems = items;
	}

	@NonNull
	@Override
	public OnboardingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		View view = LayoutInflater.from(parent.getContext())
				.inflate(R.layout.item_onboarding, parent, false);
		return new OnboardingViewHolder(view);
	}

	@Override
	public void onBindViewHolder(@NonNull OnboardingViewHolder holder, int position) {
		holder.bind(onboardingItems.get(position));
	}

	@Override
	public int getItemCount() {
		return onboardingItems.size();
	}

	public static class OnboardingViewHolder extends RecyclerView.ViewHolder {
		private final ImageView imageIcon;
		private final TextView title, description;

		public OnboardingViewHolder(@NonNull View itemView) {
			super(itemView);
			imageIcon = itemView.findViewById(R.id.onboardingImage);
			title = itemView.findViewById(R.id.onboardingTitle);
			description = itemView.findViewById(R.id.onboardingDesc);
		}

		public void bind(OnboardingItem item) {
			imageIcon.setImageResource(item.getIconResId());
			title.setText(item.getTitle());
			description.setText(item.getDescription());
		}
	}
}
