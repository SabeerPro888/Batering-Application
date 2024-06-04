package com.example.barteringapp7.ui.Profile;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.barteringapp7.APIService;
import com.example.barteringapp7.GlobalVariables;
import com.example.barteringapp7.RetrofitClient;
import com.example.barteringapp7.User;
import com.example.barteringapp7.databinding.FragmentProfileBinding;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileFragment extends Fragment {

    private FragmentProfileBinding binding;
    private EditText txtContact, txtAddress, txtOldPassword, txtNewPassword;
    private Button btnSave, btnSelectImage;
    private ImageView imageView;
    private Bitmap selectedBitmap;
    private String initialContact, initialAddress, initialOldPassword, initialNewPassword;
    String oldPassword;

    private ActivityResultLauncher<Void> cameraLauncher;
    private ActivityResultLauncher<String> galleryLauncher;
    private ActivityResultLauncher<String> permissionLauncher;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentProfileBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        getUserDetails();

        // Initialize views
        txtContact = binding.txtContact;
        txtOldPassword = binding.txtOldPassword;
        txtNewPassword = binding.txtNewPassword;
        btnSave = binding.btnSave;
        btnSelectImage = binding.btnSelectImage;
        imageView = binding.imageView7;

        setUpActivityResultLaunchers();

        // Set onClickListener for the Select Image button
        btnSelectImage.setOnClickListener(v -> checkPermissionAndDispatchIntent());

        // Set onClickListener for the Save button
        btnSave.setOnClickListener(v -> {
            if (validateInputs()) {
                saveChanges();
            }
        });

        return root;
    }

    private void setUpActivityResultLaunchers() {
        cameraLauncher = registerForActivityResult(new ActivityResultContracts.TakePicturePreview(), result -> {
            if (result != null) {
                setImageBitmap(result);
            }
        });

        galleryLauncher = registerForActivityResult(new ActivityResultContracts.GetContent(), result -> {
            if (result != null) {
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(requireContext().getContentResolver(), result);
                    setImageBitmap(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        permissionLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(),
                isGranted -> {
                    if (isGranted) {
                        showImageSourceDialog();
                    } else {
                        Toast.makeText(getContext(), "Permission Denied", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void dispatchGalleryIntent() {
        galleryLauncher.launch("image/*");
    }

    private void checkPermissionAndDispatchIntent() {
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            showImageSourceDialog();
        } else {
            permissionLauncher.launch(Manifest.permission.CAMERA);
        }
    }

    private void showImageSourceDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Choose an option")
                .setItems(new CharSequence[]{"Take Photo", "Choose from Gallery", "Cancel"}, (dialog, which) -> {
                    switch (which) {
                        case 0:
                            dispatchTakePictureIntent();
                            break;
                        case 1:
                            dispatchGalleryIntent();
                            break;
                        case 2:
                            dialog.dismiss();
                            break;
                    }
                });
        builder.show();
    }

    private void dispatchTakePictureIntent() {
        cameraLauncher.launch(null);
    }

    private void setImageBitmap(Bitmap bitmap) {
        imageView.setImageBitmap(bitmap);
        selectedBitmap = bitmap;
        imageView.setTag(true);
        imageView.setVisibility(View.VISIBLE);
        imageView.setOnClickListener(v -> showImageSourceDialog());
    }

    private boolean validateInputs() {
        String contact = txtContact.getText().toString().trim();


        if (TextUtils.isEmpty(contact)) {
            Toast.makeText(getContext(), "Please fill in all required fields", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void saveChanges() {
        String newContact = txtContact.getText().toString().trim();
        String newOldPassword = txtOldPassword.getText().toString().trim();
        String newNewPassword = txtNewPassword.getText().toString().trim();

        if (newOldPassword.equals(oldPassword)) {
            boolean isUpdated = false;

            if (!newContact.equals(initialContact)) {
                // Save the new contact number
                initialContact = newContact;
                isUpdated = true;
                Toast.makeText(getContext(), "Contact number updated", Toast.LENGTH_SHORT).show();
            }

            if (!newNewPassword.equals(initialNewPassword)) {
                // Save the new password
                initialNewPassword = newNewPassword;
                isUpdated = true;
                Toast.makeText(getContext(), "New password updated", Toast.LENGTH_SHORT).show();
            }

            if (selectedBitmap != null) {
                // If an image has been selected, save it
                File imageFile = ImageUtils.saveImageToStorage(getContext(), selectedBitmap);
                if (imageFile != null) {
                    isUpdated = true;
                    uploadImage(imageFile, newContact, newNewPassword);
                } else {
                    Toast.makeText(getContext(), "Failed to save image", Toast.LENGTH_SHORT).show();
                }
            }

            if (!isUpdated) {
                // No updates were made
                Toast.makeText(getContext(), "No changes to save", Toast.LENGTH_SHORT).show();
            }

        } else {
            Toast.makeText(getContext(), "Incorrect Old Password", Toast.LENGTH_SHORT).show();
        }

        // Optionally, reset the fields or perform other actions
    }

    private void uploadImage(File imageFile, String newContact, String newPassword) {
        APIService apiService = RetrofitClient.getRetrofitInstance().create(APIService.class);

        RequestBody requestFile = RequestBody.create(okhttp3.MediaType.parse("image/jpeg"), imageFile);
        MultipartBody.Part body = MultipartBody.Part.createFormData("image", imageFile.getName(), requestFile);
        RequestBody contactBody = RequestBody.create(okhttp3.MediaType.parse("text/plain"), newContact);
        RequestBody passwordBody = RequestBody.create(okhttp3.MediaType.parse("text/plain"), newPassword);
        String email=GlobalVariables.getInstance().getEmail();
        Call<String> call = apiService.uploadImage(email,body, contactBody, passwordBody);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getContext(), "Data Saved", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "Saving Failed", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(getContext(), "Saving Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getUserDetails() {
        APIService apiService = RetrofitClient.getRetrofitInstance().create(APIService.class);
        String email = GlobalVariables.getInstance().getEmail();
        Call<User> call = apiService.getUserDetails(email);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    User details = response.body();
                    if (details != null) {
                        Log.e("User email", details.getEmail() != null ? details.getEmail() : "N/A");
                        Log.e("User Password", details.getPassword() != null ? details.getPassword() : "N/A");
                        Log.e("User Gender", details.getGender() != null ? details.getGender() : "N/A");
                        Log.e("User Username", details.getUser_name() != null ? details.getUser_name() : "N/A");
                        Log.e("User ProfilePic", details.getProfile_Pic() != null ? details.getProfile_Pic() : "N/A");


                        // Set initial values to EditTexts
                        txtContact.setText(details.getContact() != null ? details.getContact() : "");
                        oldPassword=details.getPassword();
                        // Store initial values for comparison
                        initialContact = details.getContact();


                        String imagePath = RetrofitClient.BASE_URL + "BarteringAppAPI/Content/Images/" + details.getProfile_Pic();
                        Picasso.get().load(imagePath).into(imageView);
                    }
                } else {
                    Log.e("Error", "Response unsuccessful");
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.e("Error", "Failed to fetch user details", t);
            }
        });
    }
}
