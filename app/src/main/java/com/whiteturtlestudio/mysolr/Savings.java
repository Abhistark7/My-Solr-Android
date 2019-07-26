package com.whiteturtlestudio.mysolr;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;
import android.view.View;

import com.google.firebase.database.FirebaseDatabase;
import com.whiteturtlestudio.mysolr.databinding.ConductorSavingsBinding;

public class Savings extends AppCompatActivity {

	private ConductorSavingsBinding binding;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		binding = DataBindingUtil.setContentView(this, R.layout.conductor_savings);
		init();
	}

	private void init() {
		handleBack();
		setupTextViews();
	}

	private void handleBack() {
		binding.backButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				onBackPressed();
			}
		});
	}

	private void setupTextViews() {
		binding.cardHeader.headerText.setText("Your total savings");
		binding.cardHeader.subHeaderText.setText("1 day");
		binding.cardHeader.amountText.setText("₹ 0.0");
	}
}
