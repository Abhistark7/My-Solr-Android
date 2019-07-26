package com.whiteturtlestudio.mysolr;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;
import android.view.View;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.whiteturtlestudio.mysolr.databinding.ConductorMyPanelBinding;
import com.whiteturtlestudio.mysolr.model.KeyValue;

public class MyPanel extends AppCompatActivity {

	private static final String USER_ID = "allusers";
	private static final String EMAIL = "arg_email";
	private ConductorMyPanelBinding binding;
	private DatabaseReference databaseReference;
	private String email;
	private String panelName;
	private String panelCapacity;
	private String panelAge;
	private String panelUnit;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		databaseReference = FirebaseDatabase.getInstance().getReference(USER_ID);
		binding = DataBindingUtil.setContentView(this, R.layout.conductor_my_panel);
		init();
		extract();
	}

	private void init() {
		handleBackPress();
		loadMyPanelDetailFromFirebase();
	}

	private void extract() {
		email = getIntent().getExtras().getString(EMAIL);
	}

	private void loadMyPanelDetailFromFirebase() {
		databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
			@Override
			public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
				for(DataSnapshot data : dataSnapshot.getChildren()) {
					if(data.child("name").getValue() != null && data.child("phone").getValue() != null) {
						if(data.child("email").getValue().equals(email)) {
							panelName = data.child("panelName").getValue().toString();
							panelCapacity = data.child("panelCapacity").getValue().toString();
							panelUnit = data.child("panelUnit").getValue().toString();
							panelAge = data.child("panelAge").getValue().toString();
							setupEditTexts();
						}
					}
				}
			}

			@Override
			public void onCancelled(@NonNull DatabaseError databaseError) {
			}
		});
	}

	private void setupEditTexts() {
		binding.cardHeader.headerText.setText(panelName);
		binding.cardHeader.subHeaderText.setText(String.format("%s%s%s", panelCapacity, " ", panelUnit ));
		binding.cardHeader.subSubHeaderText.setText(panelAge);

		KeyValue keyValue1 = new KeyValue();
		keyValue1.setKey("Panel Name");
		keyValue1.setValue(panelName);
		binding.panelName.setSensor(keyValue1);

		KeyValue keyValue2 = new KeyValue();
		keyValue2.setKey("Capacity");
		keyValue2.setValue(String.format("%s%s%s", panelCapacity, " ", panelUnit));
		binding.capacity.setSensor(keyValue2);

		KeyValue keyValue3 = new KeyValue();
		keyValue3.setKey("Age");
		keyValue3.setValue(panelAge);
		binding.age.setSensor(keyValue3);
	}

	private void handleBackPress() {
		binding.backButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				onBackPressed();
			}
		});
	}
}
