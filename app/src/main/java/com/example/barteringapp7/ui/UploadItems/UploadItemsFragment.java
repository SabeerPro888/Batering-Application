package com.example.barteringapp7.ui.UploadItems;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;



import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.TooltipCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.barteringapp7.APIService;
import com.example.barteringapp7.CategoriesActivity;
import com.example.barteringapp7.Category;
import com.example.barteringapp7.GlobalVariables;
import com.example.barteringapp7.ItemUploadedActivity;
import com.example.barteringapp7.MyApplication;
import com.example.barteringapp7.R;
import com.example.barteringapp7.RetrofitClient;
import com.example.barteringapp7.SubCategory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UploadItemsFragment extends Fragment {

    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_IMAGE_GALLERY = 2;
    private static final int REQUEST_CAMERA_PERMISSION = 1001;

    private List<ImageView> imageViews;
    private Button btnUpload;

    private ActivityResultLauncher<Void> cameraLauncher;
    private ActivityResultLauncher<String> galleryLauncher;
    private ActivityResultLauncher<String> permissionLauncher;
    private Spinner spinnerCategory;
    private Spinner spinnerSubCategory;
    List<Bitmap> listOfbitmaps;
    String userEmail;
    EditText description,barterfor,price,title;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_upload_items, container, false);



        spinnerCategory = root.findViewById(R.id.spinnercategory);
        spinnerSubCategory = root.findViewById(R.id.spinnersubcategory);
         listOfbitmaps = new ArrayList<>();

        ImageButton infoIcon = root.findViewById(R.id.info_icon);


        imageViews = new ArrayList<>();
        imageViews.add((ImageView) root.findViewById(R.id.Imageview1));
        imageViews.add((ImageView) root.findViewById(R.id.Imageview2));
        imageViews.add((ImageView) root.findViewById(R.id.Imageview3));
        imageViews.add((ImageView) root.findViewById(R.id.Imageview4));
        imageViews.add((ImageView) root.findViewById(R.id.Imageview5));
        description=root.findViewById(R.id.txtDescription);
        title=root.findViewById(R.id.txtTitle);
        price=root.findViewById(R.id.txtPrice);
        barterfor=root.findViewById(R.id.txtBarterFor);


        infoIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                TooltipCompat.setTooltipText(infoIcon, "Items over 5000 price can only be verified");
            }
        });



        getCategoriesFromApi();
        spinnerCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = parent.getItemAtPosition(position).toString();
                getSubCategories(selectedItem);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btnUpload = root.findViewById(R.id.btnUpload);

        setUpActivityResultLaunchers();

        for (ImageView imageView : imageViews) {
            imageView.setOnClickListener(v -> checkPermissionAndDispatchIntent());
        }


String email=GlobalVariables.getInstance().getEmail();


        // Define an implementation of the ItemUploadCallback interface
        ItemUploadCallback callback = new ItemUploadCallback() {
            @Override
            public void onItemUploaded(int itemId) {
                // Handle the uploaded item ID here
                if (itemId != -1) {
                    Log.d(TAG, "Item uploaded successfully. Item ID: " + itemId);
                    sendVerificationRequest(String.valueOf(itemId));
                    // Do something with the item ID, like navigating to another screen or updating UI
                } else {
                    Log.e(TAG, "Failed to upload item.");
                    // Handle the upload failure
                }
            }
        };

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File[] files=convertBitmapListToFileArray(listOfbitmaps);
                uploadItem(email,title.getText().toString(),spinnerCategory.getSelectedItem().toString(),description.getText().toString(),
                        barterfor.getText().toString(),Integer.parseInt(price.getText().toString()),files);
                clearEditTextFields(title,description,price,barterfor);

            }
        });

        root.findViewById(R.id.btnVerify).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(Integer.parseInt(price.getText().toString())!=0){
                    if(Integer.parseInt(price.getText().toString())>5000){
                        File[] files=convertBitmapListToFileArray(listOfbitmaps);
                        verfifyItemandUpload(email,title.getText().toString(),spinnerCategory.getSelectedItem().toString(),description.getText().toString(),
                                barterfor.getText().toString(),Integer.parseInt(price.getText().toString()),files, callback);
                        clearEditTextFields(title,description,price,barterfor);
                    }else{
                        showDialog("Price Alert", "The price must be greater than 5000.");

                    }
                }else{

                }


            }
        });

// Call the uploadItem function with the callback



        return root;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Initialize ViewModel
        UploadItemsViewModel uploadItemsViewModel = new ViewModelProvider(requireActivity()).get(UploadItemsViewModel.class);
    }
    private void showDialog(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
    private void setUpActivityResultLaunchers() {
        cameraLauncher = registerForActivityResult(new ActivityResultContracts.TakePicturePreview(), result -> {
            if (result != null) {
                setImageBitmap(result);
            }
        });

        galleryLauncher = registerForActivityResult(new ActivityResultContracts.GetContent(),
                result -> {
                    if (result != null) {
                        try {
                            Bitmap bitmap = MediaStore.Images.Media.getBitmap(requireActivity().getContentResolver(), result);
                            setImageBitmap(bitmap);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });

        permissionLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(),
                isGranted -> {
                    if (isGranted) {
                        showOptionsDialog();
                    } else {
                        Toast.makeText(requireContext(), "Permission Denied", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void checkPermissionAndDispatchIntent() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            showOptionsDialog();
        } else {
            permissionLauncher.launch(Manifest.permission.CAMERA);
        }
    }

    private void showOptionsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Choose an option")
                .setItems(new CharSequence[]{"Take Photo", "Choose from Gallery", "Cancel"}, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
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
                    }
                });
        builder.show();
    }

    private void dispatchTakePictureIntent() {
        cameraLauncher.launch(null);
    }

    private void dispatchGalleryIntent() {
        galleryLauncher.launch("image/*");
    }

    private void setImageBitmap(Bitmap bitmap) {
        for (ImageView imageView : imageViews) {
            if (imageView.getTag() == null) {
                imageView.setImageBitmap(bitmap);
                imageView.setTag(true);
                listOfbitmaps.add(bitmap); // Adding bitmap to the list
                return;
            }
        }
        Toast.makeText(requireContext(), "No more ImageViews available", Toast.LENGTH_SHORT).show();
    }

    private void getCategoriesFromApi() {
        // Implement your API call here to retrieve categories
        // This is a placeholder method, replace it with your actual API call
        APIService apiService = RetrofitClient.getRetrofitInstance().create(APIService.class);
        Call<List<Category>> call = apiService.getCategories();

        call.enqueue(new Callback<List<Category>>() {
            @Override
            public void onResponse(Call<List<Category>> call, Response<List<Category>> response) {
                List<Category> categories = response.body();
                if (categories != null) {
                    List<String> categoryNames = new ArrayList<>();
                    for (Category category : categories) {
                        categoryNames.add(category.getCategory_name());
                    }

                    // Populate spinnerCategory with retrieved categories
                    ArrayAdapter<String> categoryAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, categoryNames);
                    categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerCategory.setAdapter(categoryAdapter);

                }
            }

            @Override
            public void onFailure(Call<List<Category>> call, Throwable t) {

            }
        });

    }
    public void getSubCategories(String ccategory){
        APIService apiService = RetrofitClient.getRetrofitInstance().create(APIService.class);
        Call<List<SubCategory>> call = apiService.getSubCategories(ccategory);
        call.enqueue(new Callback<List<SubCategory>>() {
            @Override
            public void onResponse(Call<List<SubCategory>> call, Response<List<SubCategory>> response) {
                if (response.isSuccessful()) {
                    List<SubCategory> subcategories = response.body();
                    if (subcategories != null) {
                        // Extract category names from the list
                        List<String> categoryNames = new ArrayList<>();
                        for (SubCategory subCategory : subcategories) {
                            String name = subCategory.getSubcategory_name();
                            if (name != null) {
                                categoryNames.add(name);
                            } else {
                                Log.e("SubCategory", "Name is null for SubCategory: " + subCategory.toString());
                                Log.e("SubCategory", "Name is null for SubCategory: " + ccategory.toString());
                                // Handle null name (e.g., skip or provide a default value)
                            }
                        }
                        // Populate spinnerSubCategory with retrieved categories
                        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, categoryNames);
                        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinnerSubCategory.setAdapter(categoryAdapter);
                    } else {
                        Log.e("Response", "Response body is null");
                    }
                } else {
                    Log.e("Response", "Unsuccessful response code: " + response.code());
                }
            }
            @Override
            public void onFailure(Call<List<SubCategory>> call, Throwable t) {
                Log.e("Some error","OnFailureError",t);
            }
            });
        }
    public interface ItemUploadCallback {
        void onItemUploaded(int itemId);
    }
    public void verfifyItemandUpload(String email, String itemName, String category, String description,
                           String barterFor, int price, File[] images, final ItemUploadCallback callback) {

        // Create Retrofit instance
        APIService apiService = RetrofitClient.getRetrofitInstance().create(APIService.class);

        // Prepare request body parts
        RequestBody emailBody = RequestBody.create(MediaType.parse("text/plain"), email);
        RequestBody itemNameBody = RequestBody.create(MediaType.parse("text/plain"), itemName);
        RequestBody categoryBody = RequestBody.create(MediaType.parse("text/plain"), category);
        RequestBody descriptionBody = RequestBody.create(MediaType.parse("text/plain"), description);
        RequestBody barterForBody = RequestBody.create(MediaType.parse("text/plain"), barterFor);
        RequestBody priceBody = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(price));

        MultipartBody.Part[] imageParts = new MultipartBody.Part[5];
        for (int i = 0; i < images.length; i++) {
            if (images[i] != null) {
                RequestBody imageBody = RequestBody.create(MediaType.parse("image/*"), images[i]);
                imageParts[i] = MultipartBody.Part.createFormData("Image_0" + (i + 1), images[i].getName(), imageBody);
            }
        }

        // Make the upload request
        Call<Integer> call = apiService.uploadItem(emailBody, itemNameBody, categoryBody,
                descriptionBody, barterForBody, priceBody, imageParts);

        call.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                if (response.isSuccessful()) {
                    int itemId = response.body();
                    callback.onItemUploaded(itemId);
                } else {
                    Log.e(TAG, "Upload failed: " + response.message());
                    callback.onItemUploaded(-1); // Indicate failure with a negative item ID or handle it as needed
                }
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {
                Log.e(TAG, "Upload failed: " + t.getMessage(), t);
                callback.onItemUploaded(-1); // Indicate failure with a negative item ID or handle it as needed
            }
        });
    }
    private File[] convertBitmapListToFileArray(List<Bitmap> bitmaps) {
        File[] files = new File[bitmaps.size()];

        for (int i = 0; i < bitmaps.size(); i++) {
            Bitmap bitmap = bitmaps.get(i);
            if (bitmap != null) {
                File file = saveBitmapToFile(bitmap, "image_" + i + ".png");
                files[i] = file;
            } else {
                Log.e(TAG, "Bitmap at index " + i + " is null");
            }
        }

        return files;
    }

    private File saveBitmapToFile(Bitmap bitmap, String filename) {
        File file = new File(requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES), filename);

        try {
            FileOutputStream fos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return file;
    }


    private void clearEditTextFields(EditText title,EditText description,EditText price,EditText barterfor) {

        // Clear text for all EditText fields
        description.getText().clear();
        title.getText().clear();
        price.getText().clear();
        barterfor.getText().clear();
    }
    public void uploadItem(String email, String itemName, String category, String description,
                          String barterFor, int price, File[] images) {


        // Create Retrofit instance
        final APIService[] apiService = {RetrofitClient.getRetrofitInstance().create(APIService.class)};

        // Prepare request body parts
        RequestBody emailBody = RequestBody.create(MediaType.parse("text/plain"), email);
        RequestBody itemNameBody = RequestBody.create(MediaType.parse("text/plain"), itemName);
        RequestBody categoryBody = RequestBody.create(MediaType.parse("text/plain"), category);
        RequestBody descriptionBody = RequestBody.create(MediaType.parse("text/plain"), description);
        RequestBody barterForBody = RequestBody.create(MediaType.parse("text/plain"), barterFor);
        RequestBody priceBody = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(price));

        MultipartBody.Part[] imageParts = new MultipartBody.Part[5];
        for (int i = 0; i < images.length; i++) {
            if (images[i] != null) {
                RequestBody imageBody = RequestBody.create(MediaType.parse("image/*"), images[i]);
                imageParts[i] = MultipartBody.Part.createFormData("Image_0" + (i + 1), images[i].getName(), imageBody);
            }
        }

        // Make the upload request
        Call<Integer> call = apiService[0].uploadItem(emailBody, itemNameBody, categoryBody,
                descriptionBody, barterForBody, priceBody, imageParts);
        call.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {

                if (response.isSuccessful()) {
                    Log.d(TAG, "Upload Sucessfully");
                } else {
                    Log.e(TAG, "Upload failed: " + response.message());
                }

            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {
                Log.e(TAG, "Upload failed: " + t.getMessage(), t);
            }
        });


    }

    public void sendVerificationRequest(String itemid){
        final APIService apiService = RetrofitClient.getRetrofitInstance().create(APIService.class);

        Call<Void> call = apiService.sendVerificationRequest(itemid);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    // Handle successful response
                    Log.d(TAG, "Verification request sent successfully.");
                    Intent intent=new Intent(getContext(), ItemUploadedActivity.class);
                    startActivity(intent);
                } else {
                    // Handle unsuccessful response
                    Log.e(TAG, "Failed to send verification request. Response code: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                // Handle network errors
                Log.e(TAG, "Failed to send verification request: " + t.getMessage(), t);
            }
        });





    }

}







