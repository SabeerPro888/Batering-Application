package com.example.barteringapp7;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class Activity_SignUp extends AppCompatActivity {

    private EditText txtFirstName;
    private EditText txtLastName;
    private EditText txtAddress;
    private EditText txtEmail;
    private EditText txtPassword;
    private EditText txtConfirmPassword;
    private Button btnSignUp;
    TextView signin;

    RadioGroup rgGender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        txtFirstName = findViewById(R.id.txtFirstName);
        txtLastName = findViewById(R.id.txtLastName);
        txtAddress = findViewById(R.id.txtAddress);
        txtEmail = findViewById(R.id.txtEmail);
        txtPassword = findViewById(R.id.txtPassword);
        txtConfirmPassword = findViewById(R.id.txtConfirmPassword);
        btnSignUp = findViewById(R.id.btnSignUp);
        rgGender=findViewById(R.id.rgGender);
        signin=findViewById(R.id.txtSignIn);

        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(Activity_SignUp.this,MainActivity.class);
                startActivity(intent);
            }
        });

        btnSignUp.setOnClickListener(v -> {
            if (validateInput()) {
                // Proceed with sign-up logic

                signUpUser();
            }
        });
    }



    private boolean validateInput() {
        String firstName = txtFirstName.getText().toString().trim();
        String lastName = txtLastName.getText().toString().trim();
        String address = txtAddress.getText().toString().trim();
        String email = txtEmail.getText().toString().trim();
        String password = txtPassword.getText().toString().trim();
        String confirmPassword = txtConfirmPassword.getText().toString().trim();

        if (TextUtils.isEmpty(firstName)) {
            txtFirstName.setError("First Name is required");
            txtFirstName.requestFocus();
            return false;
        }

        if (TextUtils.isEmpty(lastName)) {
            txtLastName.setError("Last Name is required");
            txtLastName.requestFocus();
            return false;
        }

        if (TextUtils.isEmpty(address)) {
            txtAddress.setError("Address is required");
            txtAddress.requestFocus();
            return false;
        }

        if (TextUtils.isEmpty(email)) {
            txtEmail.setError("Email Address is required");
            txtEmail.requestFocus();
            return false;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            txtEmail.setError("Enter a valid Email Address");
            txtEmail.requestFocus();
            return false;
        }

        if (TextUtils.isEmpty(password)) {
            txtPassword.setError("Password is required");
            txtPassword.requestFocus();
            return false;
        }

        if (password.length() < 6) {
            txtPassword.setError("Password should be at least 6 characters");
            txtPassword.requestFocus();
            return false;
        }

        if (TextUtils.isEmpty(confirmPassword)) {
            txtConfirmPassword.setError("Password Confirmation is required");
            txtConfirmPassword.requestFocus();
            return false;
        }

        if (!password.equals(confirmPassword)) {
            txtConfirmPassword.setError("Passwords do not match");
            txtConfirmPassword.requestFocus();
            return false;
        }

        if (rgGender.getCheckedRadioButtonId() == -1) {
            Toast.makeText(this, "Please select your gender", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }


    private void signUpUser() {
        String firstName = txtFirstName.getText().toString().trim();
        String lastName = txtLastName.getText().toString().trim();
        String address = txtAddress.getText().toString().trim();
        String email = txtEmail.getText().toString().trim();
        String password = txtPassword.getText().toString().trim();
        String gender = "";

        int selectedGenderId = rgGender.getCheckedRadioButtonId();
        if (selectedGenderId == R.id.rbMale) {
            gender = "Male";
        } else if (selectedGenderId == R.id.rbFemale) {
            gender = "Female";
        }
        Log.e("Gender",gender);
        Log.e("email",email);
        Log.e("password",password);
        Log.e("firstName",firstName);



        APIService apiService = RetrofitClient.getRetrofitInstance().create(APIService.class);
        Call<String> call = apiService.signUp(firstName+lastName,password,"909",email,address,gender);

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(Activity_SignUp.this, response.body(), Toast.LENGTH_SHORT).show();
                    Intent intent =new Intent(Activity_SignUp.this,MainActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(Activity_SignUp.this, "Sign Up Failed", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(Activity_SignUp.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}

