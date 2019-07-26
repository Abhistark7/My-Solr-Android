package com.whiteturtlestudio.mysolr;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
import android.view.View;
import android.widget.Adapter;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.whiteturtlestudio.mysolr.databinding.ConductorRegisterBinding;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

public class Register extends AppCompatActivity {

	private static final String ARG_NAME = "arg_name";
	private static final String ARG_EMAIL = "arg_email";
	private static final String ARG_MOBILE = "arg_mobile";
	private static final String ARG_PASSWORD = "arg_password";
	private ConductorRegisterBinding binding;
	private String name;
	private String email;
	private String mobile;
	private String password;
	private String confirmPassword;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		binding = DataBindingUtil.setContentView(this, R.layout.conductor_register);
		init();
		initializeVariables();
	}

	private void init() {
		setupEditText();
		handleBackButtonClick();
		handleRegisterButtonClick();
	}

	public void initializeVariables() {
		name = "";
		email = "";
		mobile = "";
		password = "";
		confirmPassword = "";
	}

	private void setupEditText() {
		binding.enterName.editText.setHint(R.string.name);
		binding.enterEmail.editText.setHint(R.string.email);
		binding.enterPhone.editText.setHint(R.string.phone_number);
		binding.enterPhone.editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(10)});
		binding.enterPhone.editText.setInputType(InputType.TYPE_CLASS_NUMBER);
		binding.enterPassword.editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
		binding.enterPassword.editText.setTypeface(Typeface.create("product_sans_regular", Typeface.NORMAL));
		binding.enterConfirmPassword.editText.setTypeface(Typeface.create("product_sans_regular", Typeface.NORMAL));
		binding.enterConfirmPassword.editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
		binding.enterPassword.editText.setHint(R.string.password);
		binding.enterConfirmPassword.editText.setHint(R.string.confirm_password);
	}

	private void handleBackButtonClick() {
		binding.backButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Register.this.finish();
			}
		});
	}

	private void handleRegisterButtonClick() {
		binding.registerButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				validateInput();
			}
		});
	}

	private void validateInput() {
		if (binding.enterName.editText.getText() != null && !binding.enterName.editText.getText().toString().isEmpty()) {
			name = binding.enterName.editText.getText().toString();
		} else {
			binding.enterName.editText.setError(String.format("%s%s", getString(R.string.please_enter), getString(R.string.name)));
		}
		if (binding.enterEmail.editText.getText() != null && !binding.enterEmail.editText.getText().toString().isEmpty()) {
			email = binding.enterEmail.editText.getText().toString();
		} else {
			binding.enterEmail.editText.setError(String.format("%s%s", getString(R.string.please_enter), getString(R.string.email)));
		}
		if (binding.enterPhone.editText.getText() != null && !binding.enterPhone.editText.getText().toString().isEmpty()) {
			mobile = binding.enterPhone.editText.getText().toString();
		} else {
			binding.enterPhone.editText.setError(String.format("%s%s", getString(R.string.please_enter), getString(R.string.phone_number)));
		}
		if (binding.enterPassword.editText.getText() != null && !binding.enterPassword.editText.getText().toString().isEmpty()) {
			password = binding.enterPassword.editText.getText().toString();
		} else {
			binding.enterPassword.editText.setError(String.format("%s%s", getString(R.string.please_enter), getString(R.string.password)));
		}
		if (binding.enterConfirmPassword.editText.getText() != null && !binding.enterConfirmPassword.editText.getText().toString().isEmpty()) {
			confirmPassword = binding.enterConfirmPassword.editText.getText().toString();
		} else {
			binding.enterConfirmPassword.editText.setError(String.format("%s%s", getString(R.string.please_enter), getString(R.string.password)));
		}

		validateForm();
	}

	private void validateForm() {
		if (!name.isEmpty() && !email.isEmpty() && !mobile.isEmpty() && !confirmPassword.isEmpty() && !password.isEmpty()) {
			if (confirmPassword.equals(password)) {
				openPanelDetailsActivity();
			} else {
				showToast("Password doesn't match!");
			}
		} else {
			showToast("Please enter all the fields!");
		}
	}

	private void openPanelDetailsActivity() {
		Intent intent = new Intent(Register.this, PanelDetailForm.class);
		Bundle bundle = new Bundle();
		bundle.putString(ARG_NAME, name);
		bundle.putString(ARG_EMAIL, email);
		bundle.putString(ARG_MOBILE, mobile);
		bundle.putString(ARG_PASSWORD, password);
		intent.putExtras(bundle);
		this.startActivity(intent);
	}

	private void showToast(String message) {
		Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
	}

}
