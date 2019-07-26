package com.whiteturtlestudio.mysolr;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.whiteturtlestudio.mysolr.databinding.ItemSensorBinding;
import com.whiteturtlestudio.mysolr.model.KeyValue;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

/**
 * @author Abhishek Sahu, created at 12/05/19
 **/
public class DashboardAdapter extends RecyclerView.Adapter<DashboardAdapter.DashboardViewHolder> {

	private List<KeyValue> keyValueList;

	public DashboardAdapter() {
		keyValueList = new ArrayList<>();
	}

	@NonNull
	@Override
	public DashboardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		ItemSensorBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_sensor, parent, false);
		return new DashboardViewHolder(binding);
	}

	public void update(List<KeyValue> newList) {
		this.keyValueList.clear();
		this.keyValueList.addAll(newList);
		this.notifyDataSetChanged();
	}

	@Override
	public void onBindViewHolder(@NonNull DashboardViewHolder holder, int position) {
		holder.bind(keyValueList.get(position));
	}

	@Override
	public int getItemCount() {
		return keyValueList.size();
	}

	class DashboardViewHolder extends RecyclerView.ViewHolder {

		private ItemSensorBinding binding;

		public DashboardViewHolder(ItemSensorBinding binding) {
			super(binding.getRoot());
			this.binding = binding;
		}

		void bind(KeyValue keyValue) {
			binding.setSensor(keyValue);
		}
	}
}
