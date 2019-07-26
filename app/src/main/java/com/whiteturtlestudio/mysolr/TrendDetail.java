package com.whiteturtlestudio.mysolr;

import android.graphics.Color;
import android.os.Bundle;

import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.whiteturtlestudio.mysolr.databinding.ConductorTrendsDetailBinding;
import com.whiteturtlestudio.mysolr.model.TimestampValue;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

public class TrendDetail extends AppCompatActivity {

	private ConductorTrendsDetailBinding binding;
	private String trendType;
	private String email;
	private static final String USER_ID = "allusers";
	private DatabaseReference databaseReference;
	private static final String ARG_TREND_TYPE = "arg_trend_type";
	private static final String EMAIL = "arg_email";
	private List<TimestampValue> timestampValueList = new ArrayList<>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		binding = DataBindingUtil.setContentView(this, R.layout.conductor_trends_detail);
		databaseReference = FirebaseDatabase.getInstance().getReference(USER_ID);
		init();
	}

	private void init() {
		binding.chart1.setTouchEnabled(true);
		extract();
		loadDataFromFirebase();
	}

	private void extract() {
		email = getIntent().getExtras().getString(EMAIL);
		trendType = getIntent().getExtras().getString(ARG_TREND_TYPE);
	}

	private void loadDataFromFirebase() {
		databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
			@Override
			public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
				for (DataSnapshot data : dataSnapshot.getChildren()) {
					if (data.child("email").getValue() != null) {
						if (data.child("email").getValue().equals(email)) {
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
		binding.progressCircular.setEnabled(true);
		DataSnapshot childData = dataSnapshot.child("data").child(trendType);
		timestampValueList.clear();
		for (DataSnapshot data : childData.getChildren()) {
			String timestamp = data.child("0").child("timestamp").getValue().toString();
			String value = data.child("0").child("value").getValue().toString();
			TimestampValue timestampValue = new TimestampValue(timestamp, value);
			timestampValueList.add(timestampValue);
		}
		createGraph();
	}

	private void createGraph() {
		ArrayList<Entry> values = new ArrayList<>();
		for (int i = 0; i < timestampValueList.size(); i++) {
			Entry entry = new Entry();
			entry.setX(Float.valueOf(timestampValueList.get(i).getTimestamp()));
			entry.setY(Float.valueOf(timestampValueList.get(i).getValue()));
			values.add(entry);
		}

//

		LineDataSet set1;
		set1 = new LineDataSet(values, "DataSet 1");

		set1.setAxisDependency(YAxis.AxisDependency.LEFT);
		set1.setColor(ColorTemplate.getHoloBlue());
		set1.setCircleColor(Color.BLACK);
		set1.setLineWidth(2f);
		set1.setCircleRadius(3f);
		set1.setFillAlpha(65);
		set1.setFillColor(ColorTemplate.getHoloBlue());
		set1.setHighLightColor(Color.rgb(244, 117, 117));
		set1.setDrawCircleHole(false);

		// create a data object with the data sets
		LineData data = new LineData(set1);
		data.setValueTextColor(Color.WHITE);
		data.setValueTextSize(9f);

		// set data
		binding.chart1.setData(data);
		binding.chart1.performClick();XAxis xAxis = binding.chart1.getXAxis();
//		xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
//		xAxis.setValueFormatter(new MyXAxisValueFormatter());

	}


}
