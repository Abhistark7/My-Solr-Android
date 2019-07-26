package com.whiteturtlestudio.mysolr;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.whiteturtlestudio.mysolr.databinding.ConductorPanelDetailsBinding;
import com.whiteturtlestudio.mysolr.model.Data;
import com.whiteturtlestudio.mysolr.model.RegisterUser;
import com.whiteturtlestudio.mysolr.model.Sensor;
import com.whiteturtlestudio.mysolr.model.TimestampValue;

import java.util.ArrayList;
import java.util.List;

public class PanelDetailForm extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

	private static final String ARG_NAME = "arg_name";
	private static final String ARG_EMAIL = "arg_email";
	private static final String ARG_MOBILE = "arg_mobile";
	private static final String ARG_PASSWORD = "arg_password";
	private ConductorPanelDetailsBinding binding;
	private String name;
	private String email;
	private String mobile;
	private String password;
	private String panelName;
	private String panelCapacity;
	private String selectedUnit;
	private String selectedAge;
	private String[] unit = {"Watt", "KiloWatt"};
	private String[] age = {"< 1 Year ", "1-2 Years", "2-3 Years", "3-4 Years", "4-5 Years", "> 5 Years"};
	private static final String USER_ID = "allusers";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		binding = DataBindingUtil.setContentView(this, R.layout.conductor_panel_details);
		init();
	}

	private void init() {
		handleBackButtonClick();
		extract();
		setupEditTexts();
		setupSpinners();
		handleSaveClick();
	}

	private void handleBackButtonClick() {
		binding.backButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				PanelDetailForm.this.finish();
			}
		});
	}

	private void extract() {
		name = getIntent().getExtras().getString(ARG_NAME);
		email = getIntent().getExtras().getString(ARG_EMAIL);
		mobile = getIntent().getExtras().getString(ARG_MOBILE);
		password = getIntent().getExtras().getString(ARG_PASSWORD);
	}

	private void setupEditTexts() {
		binding.enterName.editText.setHint(R.string.name);
		binding.enterCapacity.editText.setHint(R.string.capacity);
		binding.enterCapacity.editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(3)});
		binding.enterCapacity.editText.setInputType(InputType.TYPE_CLASS_NUMBER);
		binding.selectAge.editText.setHint(R.string.age);
		binding.selectUnit.editText.setHint(R.string.unit);
	}

	private void setupSpinners() {
		binding.selectAgeSpinner.setOnItemSelectedListener(this);
		binding.selectUnitSpinner.setOnItemSelectedListener(this);
	}

	private void handleSaveClick() {
		binding.saveButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				validate();
			}
		});
	}

	private void validate() {
		if(binding.enterName.editText.getText().toString().isEmpty()) {
			binding.enterName.editText.setError("Please enter panel name");
		} else {
			panelName = binding.enterName.editText.getText().toString();
		}
		if(binding.enterName.editText.getText().toString().isEmpty()) {
			binding.enterCapacity.editText.setError("Please enter capacity");
		} else {
			panelCapacity = binding.enterCapacity.editText.getText().toString();
		}
		if(binding.selectUnit.editText.getText().toString().isEmpty()) {
			binding.selectUnit.editText.setError("Please enter unit");
		} else {
			selectedUnit = binding.selectUnit.editText.getText().toString();
		}
		if(binding.selectAge.editText.getText().toString().isEmpty()) {
			binding.selectAge.editText.setError("Please enter panel age");
		} else {
			selectedAge = binding.selectAge.editText.getText().toString();
		}

		if(!panelName.isEmpty() && !panelCapacity.isEmpty() && !selectedUnit.isEmpty() && !selectedAge.isEmpty()) {
			registerUserToFirebase();
		} else {
			showToast("Please fill all the fields!");
		}
	}

	private void registerUserToFirebase() {
		Data data = new Data();
		Sensor current = new Sensor(new ArrayList<TimestampValue>());

		TimestampValue timestampValue1 = new TimestampValue("82848484884", "23");
		TimestampValue timestampValue2 = new TimestampValue("90809435908", "12");
		List<TimestampValue> timestampValueList = new ArrayList<>();
		timestampValueList.add(timestampValue1);
		timestampValueList.add(timestampValue2);
		current.setTimestampValueList(timestampValueList);

		Sensor humidity = new Sensor(new ArrayList<TimestampValue>());
		humidity.setTimestampValueList(timestampValueList);

		Sensor intensity = new Sensor(new ArrayList<TimestampValue>());
		intensity.setTimestampValueList(timestampValueList);

		Sensor temperature = new Sensor(new ArrayList<TimestampValue>());
		temperature.setTimestampValueList(timestampValueList);

		Sensor voltage = new Sensor(new ArrayList<TimestampValue>());
		voltage.setTimestampValueList(timestampValueList);

		data.setCurrent(current);
		data.setHumidity(humidity);
		data.setIntensity(intensity);
		data.setTemperature(temperature);
		data.setVoltage(voltage);

		RegisterUser registerUser = new RegisterUser();
		registerUser.setData(data);
		registerUser.setEmail(email);
		registerUser.setName(name);
		registerUser.setPanelName(panelName);
		registerUser.setPanelAge(selectedAge);
		registerUser.setPanelCapacity(panelCapacity);
		registerUser.setPanelUnit(selectedUnit);
		registerUser.setPassword(password);
		registerUser.setPhone(mobile);
		DatabaseReference databaseReference = FirebaseDatabase
				.getInstance()
				.getReference(USER_ID);
		databaseReference.push().setValue(registerUser);
		showToast("Registration Successful");
		openLoginScreen();
	}

	private void openLoginScreen() {
		Intent intent = new Intent(PanelDetailForm.this, LoginActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
		this.startActivity(intent);
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
		if(parent.getId() == R.id.select_unit_spinner) {
			selectedUnit = unit[position];
			showToast(selectedUnit);
		}
		if(parent.getId() == R.id.select_age_spinner) {
			selectedAge = age[position];
			showToast(selectedAge);
		}
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {
		showToast("Please select all the fields!");
	}

	private void showToast(String message) {
		Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
	}
}
