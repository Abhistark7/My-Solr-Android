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
import com.whiteturtlestudio.mysolr.databinding.ConductorMyProfileBinding;
import com.whiteturtlestudio.mysolr.model.KeyValue;

public class MyProfile extends AppCompatActivity {

	private static final String USER_ID = "allusers";
	private DatabaseReference databaseReference;
	private ConductorMyProfileBinding binding;
	private static final String EMAIL = "arg_email";
	private String email;
	private String name;
	private String phone;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		databaseReference = FirebaseDatabase.getInstance().getReference(USER_ID);
		binding = DataBindingUtil.setContentView(this, R.layout.conductor_my_profile);
		init();
		extract();
	}

	private void init() {
		handleBackPress();
	}

	private void extract() {
		email = getIntent().getExtras().getString(EMAIL);
		getUserData();
	}

	private void getUserData() {
		databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
			@Override
			public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
				for(DataSnapshot data : dataSnapshot.getChildren()) {
					if(data.child("name").getValue() != null && data.child("phone").getValue() != null) {
						if(data.child("email").getValue().equals(email)) {
							name = data.child("name").getValue().toString();
							phone = data.child("phone").getValue().toString();
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
		KeyValue keyValue1 = new KeyValue();
		keyValue1.setKey("Name");
		keyValue1.setValue(name);
		binding.name.setSensor(keyValue1);

		KeyValue keyValue2 = new KeyValue();
		keyValue2.setKey("Email");
		keyValue2.setValue(email);
		binding.email.setSensor(keyValue2);

		KeyValue keyValue3 = new KeyValue();
		keyValue3.setKey("Phone");
		keyValue3.setValue(phone);
		binding.phone.setSensor(keyValue3);
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
