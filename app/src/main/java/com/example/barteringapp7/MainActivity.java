package com.example.barteringapp7;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.barteringapp7.ui.UploadItems.UploadItemsViewModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private UploadItemsViewModel uploadItemsViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        EditText EmailEditText=findViewById(R.id.txtusername);
        EditText PasswordEditText=findViewById(R.id.txtpass);
        findViewById(R.id.btnLogin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String E=EmailEditText.getText().toString();
                String P=PasswordEditText.getText().toString();
                APIService apiService = RetrofitClient.getRetrofitInstance().create(APIService.class);


                Call<String> call = apiService.Login(E, P);
                call.enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        if (response.isSuccessful()) {
                            String responseData = response.body();
                            // Process responseData here based on the received response message
                            if (responseData.equals("Admin")) {
                                Toast.makeText(getApplicationContext(), "Admin logged in", Toast.LENGTH_SHORT).show();
                                GlobalVariables.getInstance().setEmail(E);


                            } else if (responseData.equals("Trusted")) {

                                Intent intent = new Intent(MainActivity.this, TrustedActivity.class);
                                GlobalVariables.getInstance().setEmail(E);

                                startActivity(intent);

                                Toast.makeText(getApplicationContext(), "Trusted logged in", Toast.LENGTH_SHORT).show();
                            } else if (responseData.equals("User")) {

                                GlobalVariables.getInstance().setEmail(E);

                                Intent intent = new Intent(MainActivity.this, NavigationActivity.class);

                                startActivity(intent);
                            } else {
                                // Handle Invalid Email and Password response
                            }
                        } else {
                            try {
                                String error = response.errorBody().string();
                                Log.e("Retrofit", "Error: " + error); // Log the error
                            } catch (IOException e) {
                                Log.e("Retrofit", "Error while getting error body: " + e.getMessage()); // Log the IOException
                                throw new RuntimeException(e);
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        String errorMessage = t.getMessage(); // Get the exception message
                        if (errorMessage != null) {
                            Toast.makeText(getApplicationContext(), "Failed"+errorMessage, Toast.LENGTH_SHORT).show();

                        } else {
                            Toast.makeText(getApplicationContext(), "Unknown error", Toast.LENGTH_SHORT).show();
                        }                    }
                });



            }
        });

        EditText txtpass = findViewById(R.id.txtpass);
        ImageButton imgPasswordToggle = findViewById(R.id.imgPasswordToggle);

        imgPasswordToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Toggle password visibility
                if (txtpass.getInputType() == InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD) {
                    // Hide password
                    txtpass.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                } else {
                    // Show password
                    txtpass.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);

                }

                // Move cursor to the end of the text
                txtpass.setSelection(txtpass.getText().length());
            }
        });


    }


}