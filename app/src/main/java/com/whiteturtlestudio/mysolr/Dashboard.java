package com.whiteturtlestudio.mysolr;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.whiteturtlestudio.mysolr.databinding.ConductorDashboardBinding;
import com.whiteturtlestudio.mysolr.model.KeyValue;
import com.whiteturtlestudio.mysolr.model.TimestampValue;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Dashboard extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

	private static final String USER_ID = "allusers";
	private ConductorDashboardBinding binding;
	private DashboardAdapter adapter;
	private static final String EMAIL = "arg_email";
	private String email;
	private DatabaseReference databaseReference;
	List<TimestampValue> current = new ArrayList<>();
	List<TimestampValue> voltage = new ArrayList<>();
	List<TimestampValue> temperature = new ArrayList<>();
	List<TimestampValue> light = new ArrayList<>();
	List<TimestampValue> humidity = new ArrayList<>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		databaseReference = FirebaseDatabase.getInstance().getReference(USER_ID);
		binding = DataBindingUtil.setContentView(this, R.layout.conductor_dashboard);
		init();
	}

	private void init() {
		extract();
		fillHeaderCard();
		setupRecyclerView();
		handleFabClick();
		setupNavigationView();
		setupSwipeRefresh();
	}

	private void extract() {
		email = getIntent().getExtras().getString(EMAIL);
	}

	private void fillHeaderCard() {
		binding.cardHeader.headerText.setText("Your total savings for today");
		binding.cardHeader.subHeaderText.setText(getTodaysDate());
		binding.cardHeader.amountText.setText("₹ 35.07");
	}

	private void setupRecyclerView() {
		adapter = new DashboardAdapter();
		binding.recyclerView.setNestedScrollingEnabled(false);
		binding.recyclerView.setAdapter(adapter);
		binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
	}

	private String getTodaysDate() {
		SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy");
		Date date = new Date();
		return sdf.format(date);
	}

	private void handleFabClick() {
		binding.fab.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				binding.drawerLayout.openDrawer(Gravity.RIGHT);
			}
		});
	}

	private void setupNavigationView() {
		binding.navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
			@Override
			public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
				int id = menuItem.getItemId();
				switch (id) {
					case R.id.action_my_profile:
						openMyProfile();
						break;

					case R.id.action_my_panel:
						openMyPanel();
						break;

					case R.id.action_trends:
						openTrend();
						break;

					case R.id.action_savings:
						openSavings();
						break;

					case R.id.action_log_out:
						logout();
						break;
				}
				return false;
			}
		});
	}

	private void openMyPanel() {
		Intent intent = new Intent(Dashboard.this, MyPanel.class);
		Bundle bundle = new Bundle();
		bundle.putString(EMAIL, email);
		intent.putExtras(bundle);
		this.startActivity(intent);
	}

	private void openMyProfile() {
		Intent intent = new Intent(Dashboard.this, MyProfile.class);
		Bundle bundle = new Bundle();
		bundle.putString(EMAIL, email);
		intent.putExtras(bundle);
		this.startActivity(intent);
	}

	private void openSavings() {
		Intent intent = new Intent(Dashboard.this, Savings.class);
		Bundle bundle = new Bundle();
		bundle.putString(EMAIL, email);
		intent.putExtras(bundle);
		this.startActivity(intent);
	}


	private void logout() {
		Intent intent = new Intent(Dashboard.this, LoginActivity.class);
		this.startActivity(intent);
	}

	private void openTrend() {
		Intent intent = new Intent(Dashboard.this, Trend.class);
		Bundle bundle = new Bundle();
		bundle.putString(EMAIL, email);
		intent.putExtras(bundle);
		this.startActivity(intent);
	}

	private void setupSwipeRefresh() {
		binding.swipe.setOnRefreshListener(this);
		this.onRefresh();
	}

	@Override
	public void onBackPressed() {
		if (binding.drawerLayout.isDrawerOpen(Gravity.RIGHT)) {
			binding.drawerLayout.closeDrawer(Gravity.RIGHT);
		} else {
			super.onBackPressed();
		}
	}

	@Override
	public void onRefresh() {
		loadDataFromFirebase();
	}

	private void loadDataFromFirebase() {
		databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
			@Override
			public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
				for(DataSnapshot data : dataSnapshot.getChildren()) {
					if(data.child("email").getValue() != null) {
						if(data.child("email").getValue().equals(email)) {
							loadAllSensorData(data);
						}
					}
				}
			}

			@Override
			public void onCancelled(@NonNull DatabaseError databaseError) {
			}
		});
	}

	private void loadAllSensorData(DataSnapshot dataSnapshot) {
		resetPreviousData();
		DataSnapshot childData = dataSnapshot.child("data").child("current");
		for(DataSnapshot data : childData.getChildren()) {
			String timestamp = data.child("0").child("timestamp").getValue().toString();
			String value = data.child("0").child("value").getValue().toString();
			TimestampValue timestampValue = new TimestampValue(timestamp, value);
			current.add(timestampValue);
		}

		DataSnapshot voltageChildData = dataSnapshot.child("data").child("voltage");
		for(DataSnapshot data : voltageChildData.getChildren()) {
			String timestamp = data.child("0").child("timestamp").getValue().toString();
			String value = data.child("0").child("value").getValue().toString();
			TimestampValue timestampValue = new TimestampValue(timestamp, value);
			voltage.add(timestampValue);
		}

		DataSnapshot temperatureChildData = dataSnapshot.child("data").child("temperature");
		for(DataSnapshot data : temperatureChildData.getChildren()) {
			String timestamp = data.child("0").child("timestamp").getValue().toString();
			String value = data.child("0").child("value").getValue().toString();
			TimestampValue timestampValue = new TimestampValue(timestamp, value);
			temperature.add(timestampValue);
		}

		DataSnapshot lightChildData = dataSnapshot.child("data").child("intensity");
		for(DataSnapshot data : lightChildData.getChildren()) {
			String timestamp = data.child("0").child("timestamp").getValue().toString();
			String value = data.child("0").child("value").getValue().toString();
			TimestampValue timestampValue = new TimestampValue(timestamp, value);
			light.add(timestampValue);
		}

		DataSnapshot humidityChildData = dataSnapshot.child("data").child("humidity");
		for(DataSnapshot data : humidityChildData.getChildren()) {
			String timestamp = data.child("0").child("timestamp").getValue().toString();
			String value = data.child("0").child("value").getValue().toString();
			TimestampValue timestampValue = new TimestampValue(timestamp, value);
			humidity.add(timestampValue);
		}
		updateAdapter();
		binding.swipe.setRefreshing(false);
	}

	private void resetPreviousData() {
		current.clear();
		voltage.clear();
		temperature.clear();
		light.clear();
		humidity.clear();
	}

	private void updateAdapter() {
		String lastCurrentReading = current.get(current.size()-1).getValue();
		String lastVoltageReading = voltage.get(voltage.size()-1).getValue();
		String lastTemperatureReading = temperature.get(temperature.size()-1).getValue();
		String lastLightReading = light.get(light.size()-1).getValue();
		String lastHumidityReading = humidity.get(humidity.size()-1).getValue();

		List<KeyValue> keyValueList = new ArrayList<>();
		KeyValue keyValue1 = new KeyValue();
		KeyValue keyValue2 = new KeyValue();
		KeyValue keyValue3 = new KeyValue();
		KeyValue keyValue4 = new KeyValue();
		KeyValue keyValue5= new KeyValue();
		KeyValue keyValue6= new KeyValue();
		KeyValue keyValue7= new KeyValue();
		keyValueList.clear();

		keyValue1.setKey("Today's total energy production");
		keyValue1.setValue(calculateTodayEnergy()+"Wh");
		keyValueList.add(keyValue1);

		keyValue2.setKey("Current Panel Temperature");
		keyValue2.setValue(lastTemperatureReading+"°C");
		keyValueList.add(keyValue2);

		keyValue3.setKey("Current Sun Light Intensity");
		keyValue3.setValue(lastLightReading+"kΩ");
		keyValueList.add(keyValue3);

		keyValue4.setKey("Current Relative Humidity");
		keyValue4.setValue(lastHumidityReading+"%");
		keyValueList.add(keyValue4);

		keyValue5.setKey("Panel Health");
		keyValue5.setValue("83%");
		keyValueList.add(keyValue5);

		keyValue6.setKey("Voltage Reading");
		keyValue6.setValue(lastVoltageReading+"V");
		keyValueList.add(keyValue6);

		keyValue7.setKey("Current Reading");
		keyValue7.setValue(lastCurrentReading+"A");
		keyValueList.add(keyValue7);

		adapter.update(keyValueList);
		updateHeaderCard();
	}

	private String calculateTodayEnergy() {
		int n=0;
		float energy = 0;
		for(TimestampValue current1 : current) {
			for(TimestampValue voltage2: voltage) {
				energy = Float.valueOf(current1.getValue()) * Float.valueOf(voltage2.getValue());
				n++;
			}
		}
		float averageEnergy = energy/n;
		return String.format("%.2f",averageEnergy);
	}

	private void updateHeaderCard() {
		binding.cardHeader.amountText.setText(String.format("%s%.2f", "₹ ", Float.valueOf(calculateTodayEnergy()) * 3.5));
	}
}


