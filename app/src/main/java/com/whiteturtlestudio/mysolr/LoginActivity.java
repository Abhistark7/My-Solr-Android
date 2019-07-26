package com.whiteturtlestudio.mysolr;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.whiteturtlestudio.mysolr.databinding.ConductorLoginBinding;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

public class LoginActivity extends AppCompatActivity {

	private static final String USER_ID = "allusers";
	private static final String EMAIL = "arg_email";
	private ConductorLoginBinding binding;
	private boolean isPasswordVisible;
	private String username = "";
	private String password = "";
	private DatabaseReference databaseReference;
	private boolean isLoginSuccess;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		binding = DataBindingUtil.setContentView(this, R.layout.conductor_login);
		databaseReference = FirebaseDatabase.getInstance().getReference(USER_ID);
		init();
	}

	private void init() {
		binding.enterPassword.editText.setHint(R.string.password);
		binding.enterPassword.eyeIcon.setVisibility(View.VISIBLE);
		hidePassword();
		handleEyeButton();
		loginButtonClick();
		handleRegisterClick();
	}

	private void handleEyeButton() {
		binding.enterPassword.eyeIcon.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (isPasswordVisible) {
					hidePassword();
				} else {
					showPassword();
				}
			}
		});
	}

	private void showPassword() {
		isPasswordVisible = true;
		binding.enterPassword.eyeIcon.setColorFilter(ContextCompat.getColor(this, R.color.blue));
		binding.enterPassword.editText.setInputType(InputType.TYPE_CLASS_TEXT);
	}

	private void hidePassword() {
		isPasswordVisible = false;
		binding.enterPassword.eyeIcon.setColorFilter(ContextCompat.getColor(this, R.color.grey_light));
		binding.enterPassword.editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
		binding.enterPassword.editText.setTypeface(Typeface.create("product_sans_regular", Typeface.NORMAL));
	}

	private void loginButtonClick() {
		binding.loginButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				handleLoginClick();
			}
		});
	}

	private void handleLoginClick() {
		if (binding.enterUsername.editText.getText().toString().isEmpty()) {
			binding.enterUsername.editText.setError("Enter username");
		} else {
			username = binding.enterUsername.editText.getText().toString();
		}
		if (binding.enterPassword.editText.getText().toString().isEmpty()) {
			binding.enterPassword.editText.setError("Enter password");
		} else {
			password = binding.enterPassword.editText.getText().toString();
		}
		if (!username.isEmpty() && !password.isEmpty()) {
			binding.progressCircular.setVisibility(View.VISIBLE);
			binding.loginButton.setText("");
			loginWithFirebase();
		}
	}

	private void loginWithFirebase() {
		databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
			@Override
			public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
				for (DataSnapshot data : dataSnapshot.getChildren()) {
					if (data.child("email").getValue() != null && data.child("password").getValue() != null) {
						if (data.child("email").getValue().equals(username) && data.child("password").getValue().equals(password)) {
							showToast("Login Successful");
							isLoginSuccess = true;
							openDashboard();
							binding.progressCircular.setVisibility(View.GONE);
						}
					}
				}
				if (!isLoginSuccess) {
					binding.progressCircular.setVisibility(View.GONE);
					binding.loginButton.setText("Login");
					showToast("Invalid username and password");
				}
			}

			@Override
			public void onCancelled(@NonNull DatabaseError databaseError) {
			}
		});
	}

	private void openDashboard() {
		Intent intent = new Intent(LoginActivity.this, Dashboard.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
		Bundle bundle = new Bundle();
		bundle.putString(EMAIL, username);
		intent.putExtras(bundle);
		this.startActivity(intent);
	}

	private void handleRegisterClick() {
		binding.registerView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				openRegisterActivity();
			}
		});
	}

	private void openRegisterActivity() {
		Intent intent = new Intent(this, Register.class);
		this.startActivity(intent);
	}

	private void showToast(String message) {
		Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
	}

}
