package com.whiteturtlestudio.mysolr;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.whiteturtlestudio.mysolr.databinding.ConductorTrendsBinding;
import com.whiteturtlestudio.mysolr.model.KeyValue;

public class Trend extends AppCompatActivity {

	private static final String ARG_TREND_TYPE = "arg_trend_type";
	private static final String EMAIL = "arg_email";
	private ConductorTrendsBinding binding;
	private String email;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		binding = DataBindingUtil.setContentView(this, R.layout.conductor_trends);
		init();
	}

	private void init() {
		extract();
		initCards();
		setCardClickListener();
		handleBackPress();
	}

	private void extract() {
		email = getIntent().getExtras().getString(EMAIL);
	}

	private void initCards() {
		KeyValue current = new KeyValue();
		current.setKey("Current Trend");
		binding.currentTrend.setSensor(current);

		KeyValue voltage = new KeyValue();
		voltage.setKey("Voltage Trend");
		binding.voltageTrend.setSensor(voltage);

		KeyValue temperature = new KeyValue();
		temperature.setKey("Temperature Trend");
		binding.temperatureTrend.setSensor(temperature);

		KeyValue light = new KeyValue();
		light.setKey("Light Intensity Trend");
		binding.lightIntensityTrend.setSensor(light);

		KeyValue humidity = new KeyValue();
		humidity.setKey("Humidity Trend");
		binding.humidityTrend.setSensor(humidity);
	}

	private void setCardClickListener() {
		binding.currentTrend.getRoot().setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				openTrendDetail("current");
			}
		});
		binding.voltageTrend.getRoot().setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				openTrendDetail("voltage");
			}
		});
		binding.temperatureTrend.getRoot().setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				openTrendDetail("temperature");
			}
		});
		binding.humidityTrend.getRoot().setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				openTrendDetail("humidity");
			}
		});
		binding.lightIntensityTrend.getRoot().setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				openTrendDetail("intensity");
			}
		});

	}

	private void openTrendDetail(String trendType) {
		Intent intent = new Intent(Trend.this, TrendDetail.class);
		Bundle bundle = new Bundle();
		bundle.putString(EMAIL, email);
		bundle.putString(ARG_TREND_TYPE, trendType);
		intent.putExtras(bundle);
		this.startActivity(intent);
	}

	private void handleBackPress() {
		binding.backButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				onBackPressed();
			}
		});
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
	}
}
