package com.example.barteringapp7;

import static android.view.View.inflate;
import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.TooltipCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.barteringapp7.ui.UploadItems.UploadItemsFragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UploadActivity extends AppCompatActivity {
    private List<ImageView> imageViews;
    private Button btnUpload;
    private Button btnVerify;

    private int TotalPriceGrains;

    private ActivityResultLauncher<Void> cameraLauncher;
    private ActivityResultLauncher<String> galleryLauncher;
    private ActivityResultLauncher<String> permissionLauncher;

    List<Bitmap> listOfbitmaps;
    EditText description, barterfor, price, title;
    TextView textViewPrice;
    int WeightValue;
    LinearLayout modelSpinnerContainer;

    LinearLayout AttributeSpinnerContainer;

    LinearLayout brandcontainer;
    private String selectedBrand;
    private String selectedModel;
    String categoryName;
    String subcategoryName;
    Spinner modelSpinner;
    Spinner BrandSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);
        setUpActivityResultLaunchers();

        textViewPrice=findViewById(R.id.textViewPrice);
        title = findViewById(R.id.txtTitle);
        price = findViewById(R.id.txtPrice);
        description = findViewById(R.id.txtDescription);
        barterfor = findViewById(R.id.txtBarterFor);


        Intent intent = getIntent();
        listOfbitmaps = new ArrayList<>();

        if (intent != null && intent.hasExtra("subcategoryName") && intent.hasExtra("CategoryName")) {
            subcategoryName = intent.getStringExtra("subcategoryName");
            categoryName = intent.getStringExtra("CategoryName");
            modelSpinnerContainer = findViewById(R.id.modelSpinnerContainer);
            AttributeSpinnerContainer = findViewById(R.id.AttributeSpinnerContainer);
            brandcontainer = findViewById(R.id.brandcontainer);

            ImageButton infoIcon = findViewById(R.id.info_icon);
            ImageView imageView1 = findViewById(R.id.Imageview1);

            if("Grains".equals(categoryName)){
                textViewPrice.setText("Price/Kg");
            }

            infoIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TooltipCompat.setTooltipText(infoIcon, "Items over 5000 price can only be verified");
                }
            });

            fetchbrand(subcategoryName);

            btnUpload = findViewById(R.id.btnUpload);
            btnVerify = findViewById(R.id.btnVerify);
            imageViews = new ArrayList<>();
            imageViews.add(findViewById(R.id.Imageview1));
            imageViews.add(findViewById(R.id.Imageview2));
            imageViews.add(findViewById(R.id.Imageview3));
            imageViews.add(findViewById(R.id.Imageview4));
            imageViews.add(findViewById(R.id.Imageview5));

            for (int i = 1; i < imageViews.size(); i++) {
                imageViews.get(i).setVisibility(View.GONE);
            }

            imageView1.setOnClickListener(v -> showOptionsDialog());

            imageView1.setOnClickListener(v -> checkPermissionAndDispatchIntent());

            String email = GlobalVariables.getInstance().getEmail();
            ItemUploadCallback callback = new ItemUploadCallback() {
                @Override
                public void onItemUploaded(int itemId) {
                    if (itemId != -1) {
                        sendVerificationRequest(String.valueOf(itemId));
                    } else {
                        // Handle the upload failure
                    }
                }
            };

            btnVerify.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String jsonattributes = convertAttributesToJson();
                    if("Grains".equals(categoryName)){

                        TotalPriceGrains=WeightValue*Integer.parseInt(price.getText().toString());
                        Log.e("TotalPriceGrains",String.valueOf(TotalPriceGrains));

                        if (TotalPriceGrains >= 5000) {
                            File[] files = convertBitmapListToFileArray(listOfbitmaps);
                            selectedBrand = BrandSpinner != null ? (String) BrandSpinner.getSelectedItem() : "";
                            selectedModel = modelSpinner != null ? (String) modelSpinner.getSelectedItem() : "";
                            if (selectedBrand != null && selectedModel != null) {
                                verfifyItemandUpload(email, title.getText().toString(), subcategoryName, categoryName, description.getText().toString(),
                                        barterfor.getText().toString(), Integer.parseInt(price.getText().toString()), files,
                                        selectedBrand, selectedModel, jsonattributes, callback);
                            } else {
                                Toast.makeText(UploadActivity.this, "Please select both brand and model", Toast.LENGTH_SHORT).show();
                            }
                        }else{
                            Toast.makeText(UploadActivity.this, "Total PRice is not greater than 5000", Toast.LENGTH_SHORT).show();

                        }
                    }else{
                        if (Integer.parseInt(price.getText().toString()) >= 5000) {
                            File[] files = convertBitmapListToFileArray(listOfbitmaps);
                            selectedBrand = BrandSpinner != null ? (String) BrandSpinner.getSelectedItem() : "";
                            selectedModel = modelSpinner != null ? (String) modelSpinner.getSelectedItem() : "";
                            if (selectedBrand != null && selectedModel != null) {
                                verfifyItemandUpload(email, title.getText().toString(), subcategoryName, categoryName, description.getText().toString(),
                                        barterfor.getText().toString(), Integer.parseInt(price.getText().toString()), files,
                                        selectedBrand, selectedModel, jsonattributes, callback);
                            } else {
                                Toast.makeText(UploadActivity.this, "Please select both brand and model", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                }
            });

            btnUpload.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String   jsonattributes = convertAttributesToJson();
                    File[] files = convertBitmapListToFileArray(listOfbitmaps);
                    // Ensure BrandSpinner and modelSpinner are not null before accessing their selected items
                    selectedBrand = BrandSpinner != null ? (String) BrandSpinner.getSelectedItem() : "";
                    selectedModel = modelSpinner != null ? (String) modelSpinner.getSelectedItem() : "";

                    if (selectedBrand != null && selectedModel != null) {
                        uploadItem(email, title.getText().toString(), subcategoryName, categoryName, description.getText().toString(),
                                barterfor.getText().toString(), Integer.parseInt(price.getText().toString()), files,
                                selectedBrand, selectedModel, jsonattributes);
                        gotoRecommendation();
                    } else {
                        Toast.makeText(UploadActivity.this, "Please select both brand and model", Toast.LENGTH_SHORT).show();
                    }
                }
            });


            ImageButton backButton = findViewById(R.id.back_button);
            backButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });

        }
        TextView toolbarTitle = findViewById(R.id.toolbar_title);
        toolbarTitle.setText("Upload Item");
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void setUpActivityResultLaunchers() {
        cameraLauncher = registerForActivityResult(new ActivityResultContracts.TakePicturePreview(), result -> {
            if (result != null) {
                setImageBitmap(result);
            }
        });

        galleryLauncher = registerForActivityResult(new ActivityResultContracts.GetMultipleContents(), results -> {
            if (results != null && !results.isEmpty()) {
                List<Bitmap> bitmaps = new ArrayList<>();
                for (Uri result : results) {
                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), result);
                        bitmaps.add(bitmap);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                setImageBitmaps(bitmaps);
            }
        });

        permissionLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(),
                isGranted -> {
                    if (isGranted) {
                        showOptionsDialog();
                    } else {
                        Toast.makeText(getApplicationContext(), "Permission Denied", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void dispatchGalleryIntent() {
        galleryLauncher.launch("image/*");
    }

    private void setImageBitmaps(List<Bitmap> bitmaps) {
        int numImageViews = imageViews.size();
        int numImages = bitmaps.size();
        int assignedImages = 0;
        int bitmapIndex = 0; // Index of the current bitmap being assigned

        // Count the number of image views that already have images assigned to them
        for (int i = 0; i < numImageViews; i++) {
            ImageView imageView = imageViews.get(i);
            if (imageView.getTag() != null) {
                assignedImages++;
            }
        }

        // Update the ImageViews that haven't been assigned images yet
        for (int i = assignedImages; i < numImageViews && bitmapIndex < numImages; i++) {
            ImageView imageView = imageViews.get(i);
            if (imageView.getTag() == null) { // Check if the ImageView hasn't been assigned an image yet
                Bitmap bitmap = bitmaps.get(bitmapIndex); // Get the bitmap at the current index
                imageView.setImageBitmap(bitmap);
                imageView.setTag(true);
                imageView.setVisibility(View.VISIBLE);
                listOfbitmaps.add(bitmap);
                bitmapIndex++; // Move to the next bitmap index
            }
        }
    }


    private void checkPermissionAndDispatchIntent() {
        if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            showOptionsDialog();
        } else {
            permissionLauncher.launch(Manifest.permission.CAMERA);
        }
    }

    private void showOptionsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(UploadActivity.this);
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

    private void setImageBitmap(Bitmap bitmap) {
        for (ImageView imageView : imageViews) {
            if (imageView.getTag() == null) {
                imageView.setImageBitmap(bitmap);
                imageView.setTag(true);
                imageView.setVisibility(View.VISIBLE);
                listOfbitmaps.add(bitmap);
                imageView.setOnClickListener(v -> showOptionsDialog());
                return;
            }
        }
    }

    public void fetchbrand(String subcategory) {
        APIService apiService = RetrofitClient.getRetrofitInstance().create(APIService.class);
        Call<List<String>> call = apiService.getSubCategoriesBrand(subcategory);
        Log.e("Subcategory",subcategory);
        call.enqueue(new Callback<List<String>>() {
            @Override
            public void onResponse(Call<List<String>> call, Response<List<String>> response) {
                if (response.isSuccessful()) {
                    List<String> subcategoriesbrands = response.body();
                    for (String s : subcategoriesbrands) {
                        Log.e("brand check", s);
                        Log.e("brand check", subcategory);

                    }
                    if (subcategoriesbrands != null) {
                        populatebrand(subcategoriesbrands, subcategory);
                    }


                } else {
                    Log.e("brand check", "unsucessfull message");
                }
            }

            @Override
            public void onFailure(Call<List<String>> call, Throwable t) {
                Log.e("API check", "On Failure - fetchBrands function");

            }
        });

    }

    public void populatebrand(List<String> brands, String subcategory) {
        if (brands != null) {
            if ("Smartphones".equals(subcategory) || "Laptops".equals(subcategory)) {
                // Create a new Spinner instance for brands
                BrandSpinner = new Spinner(this);
                // Set up the TextView for the label
                TextView brandLabel = new TextView(this);
                brandLabel.setTextSize(16);
                brandLabel.setText("Brand");
                brandLabel.setPadding(8, 8, 8, 8);

                // Add the TextView to the container
                brandcontainer.addView(brandLabel);
                BrandSpinner.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                // Create an ArrayAdapter for the brands
                ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, brands);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                BrandSpinner.setAdapter(adapter);

                // Add the Spinner to the container
                brandcontainer.addView(BrandSpinner);

                // Set up an OnItemSelectedListener for the brandSpinner
                BrandSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        String selectedBrand = (String) parent.getItemAtPosition(position);
                        getModelsbyBrand(selectedBrand, subcategory);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                        // Handle when nothing is selected
                    }
                });
            } else {
                // If the subcategory is not "Smartphones" or "Laptops", proceed with fetching subcategory attributes
                getsubcategoryattributes(subcategory);
            }
        }

    }

    public void getModelsbyBrand(String brand, String subcategory) {
        APIService apiService = RetrofitClient.getRetrofitInstance().create(APIService.class);
        Call<List<String>> call = apiService.getModelsByBrand(brand);

        call.enqueue(new Callback<List<String>>() {
            @Override
            public void onResponse(Call<List<String>> call, Response<List<String>> response) {
                if (response.isSuccessful()) {
                    List<String> Models = response.body();

                    createModelSpinner(Models, subcategory);
                } else {

                }
            }

            @Override
            public void onFailure(Call<List<String>> call, Throwable t) {

            }
        });
    }

//    public void createModelSpinner(List<String> models,String subcategory) {
//
//        modelSpinnerContainer.removeAllViews();
//
//        // Create a new Spinner instance for models
//        Spinner modelSpinner = new Spinner(this);
//        TextView brandTextView = new TextView(this);
//        brandTextView.setText("Model");
//        brandTextView.setTextSize(16);
//        brandTextView.setPadding(8, 8, 8, 8);
//
//        // Add the TextView to the container
//        modelSpinnerContainer.addView(brandTextView);
//        // Create an ArrayAdapter for the models
//        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, models);
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        modelSpinner.setAdapter(adapter);
//
//        modelSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                 selectedModel = (String) parent.getItemAtPosition(position);
//                getsubcategoryattributes(subcategory);
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//                // Handle when nothing is selected
//            }
//        });
//        modelSpinner.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
//
//
//        // Add the Spinner to the modelSpinnerContainer
//        modelSpinnerContainer.addView(modelSpinner);
//        // Create a new Spinner instance
//
//    }

    public void getsubcategoryattributes(String subcategory) {
        APIService apiService = RetrofitClient.getRetrofitInstance().create(APIService.class);
        Call<List<String>> call = apiService.getsubcategoryattributes(subcategory);

        call.enqueue(new Callback<List<String>>() {
            @Override
            public void onResponse(Call<List<String>> call, Response<List<String>> response) {
                if (response.isSuccessful()) {
                    List<String> attributes = response.body();

                    setsubcategoryattributes(attributes, subcategory);

                } else {

                }
            }

            @Override
            public void onFailure(Call<List<String>> call, Throwable t) {

            }
        });

    }

    public void setsubcategoryattributes(List<String> attributes, String subcat) {

        if (categoryName.equals("Electronic Devices")) {
            AttributeSpinnerContainer.removeAllViews();
            for (String attribute : attributes) {
                // Inflate the layout for each attribute
                View attributeLayout = getLayoutInflater().inflate(R.layout.spinner_layout, null);

                // Find TextView and Spinner in the inflated layout
                TextView attributeTextView = attributeLayout.findViewById(R.id.textViewAttributeName);
                Spinner attributeValueSpinner = attributeLayout.findViewById(R.id.spinnerAttributeValue);

                // Set attribute name as TextView text
                attributeTextView.setText(attribute);

                // Call API to get attribute values for the current attribute
                APIService apiService = RetrofitClient.getRetrofitInstance().create(APIService.class);
                Call<List<String>> call = apiService.getttributevalues(subcat, attribute);

                call.enqueue(new Callback<List<String>>() {
                    @Override
                    public void onResponse(Call<List<String>> call, Response<List<String>> response) {
                        if (response.isSuccessful()) {
                            List<String> values = response.body();
                            if (values != null) {
                                // Create ArrayAdapter for attribute values
                                ArrayAdapter<String> adapter = new ArrayAdapter<>(UploadActivity.this, android.R.layout.simple_spinner_item, values);
                                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                attributeValueSpinner.setAdapter(adapter);

                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<List<String>> call, Throwable t) {
                        Log.e("API Call", "Failed: " + t.getMessage());
                    }
                });

                // Detach the inflated layout from its parent before adding it to AttributeSpinnerContainer
                ViewGroup parent = (ViewGroup) attributeLayout.getParent();
                if (parent != null) {
                    parent.removeView(attributeLayout);
                }

                // Add the inflated layout to the AttributeSpinnerContainer
                AttributeSpinnerContainer.addView(attributeLayout);
            }
        } else if ("Grains".equals(categoryName)) {
            setGrainsAttributes(attributes);
        } else {
            Log.e("CategoryNAme", categoryName);


            setOtherCategoryAttributes(attributes, subcat);
        }

    }


    public void uploadItem(String email, String itemName, String subcategory, String category, String description,
                           String barterFor, int price, File[] images, String brand, String model, String attributesJson) {


        // Create Retrofit instance
        final APIService[] apiService = {RetrofitClient.getRetrofitInstance().create(APIService.class)};

        // Prepare request body parts
        RequestBody emailBody = RequestBody.create(MediaType.parse("text/plain"), email);
        RequestBody itemNameBody = RequestBody.create(MediaType.parse("text/plain"), itemName);
        RequestBody subcategoryBody = RequestBody.create(MediaType.parse("text/plain"), subcategory);
        RequestBody categoryBody = RequestBody.create(MediaType.parse("text/plain"), category);

        RequestBody descriptionBody = RequestBody.create(MediaType.parse("text/plain"), description);
        RequestBody barterForBody = RequestBody.create(MediaType.parse("text/plain"), barterFor);
        RequestBody priceBody = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(price));
        RequestBody attributesJsonBody = RequestBody.create(MediaType.parse("text/plain"), attributesJson);
        RequestBody model1 = RequestBody.create(MediaType.parse("text/plain"), model);
        RequestBody brand1 = RequestBody.create(MediaType.parse("text/plain"), brand);

        MultipartBody.Part[] imageParts = new MultipartBody.Part[5];
        for (int i = 0; i < images.length; i++) {
            if (images[i] != null) {
                RequestBody imageBody = RequestBody.create(MediaType.parse("image/*"), images[i]);
                imageParts[i] = MultipartBody.Part.createFormData("Image_0" + (i + 1), images[i].getName(), imageBody);
            }
        }
        Log.e("Json string", attributesJson);
        Log.e("Model string", selectedModel);
        Log.e("Brand string", selectedBrand);
        Log.e("category string", category);

        // Make the upload request
        Call<Integer> call = apiService[0].uploadItem2(emailBody, itemNameBody, subcategoryBody, categoryBody,
                descriptionBody, barterForBody, priceBody, attributesJsonBody, imageParts, brand1, model1);
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


    public void sendVerificationRequest(String itemid) {
        final APIService apiService = RetrofitClient.getRetrofitInstance().create(APIService.class);

        Call<Void> call = apiService.sendVerificationRequest(itemid);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    // Handle successful response
                    Log.d(TAG, "Verification request sent successfully.");
                    Intent intent = new Intent(UploadActivity.this, ItemUploadedActivity.class);
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


    public interface ItemUploadCallback {
        void onItemUploaded(int itemId);
    }

    public void verfifyItemandUpload(String email, String itemName, String subcategory, String category, String description,
                                     String barterFor, int price, File[] images, String brand, String model, String attributesJson,
                                     UploadActivity.ItemUploadCallback callback) {

        // Create Retrofit instance
        final APIService[] apiService = {RetrofitClient.getRetrofitInstance().create(APIService.class)};

        // Prepare request body parts
        RequestBody emailBody = RequestBody.create(MediaType.parse("text/plain"), email);
        RequestBody itemNameBody = RequestBody.create(MediaType.parse("text/plain"), itemName);
        RequestBody subcategoryBody = RequestBody.create(MediaType.parse("text/plain"), subcategory);
        RequestBody categoryBody = RequestBody.create(MediaType.parse("text/plain"), category);
        RequestBody descriptionBody = RequestBody.create(MediaType.parse("text/plain"), description);
        RequestBody barterForBody = RequestBody.create(MediaType.parse("text/plain"), barterFor);
        RequestBody priceBody = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(price));
        RequestBody attributesJsonBody = RequestBody.create(MediaType.parse("text/plain"), attributesJson);
        RequestBody model1 = RequestBody.create(MediaType.parse("text/plain"), model);
        RequestBody brand1 = RequestBody.create(MediaType.parse("text/plain"), brand);

        MultipartBody.Part[] imageParts = new MultipartBody.Part[5];
        for (int i = 0; i < images.length; i++) {
            if (images[i] != null) {
                RequestBody imageBody = RequestBody.create(MediaType.parse("image/*"), images[i]);
                imageParts[i] = MultipartBody.Part.createFormData("Image_0" + (i + 1), images[i].getName(), imageBody);
            }
        }

        // Make the upload request
        Call<Integer> call = apiService[0].uploadItem2(emailBody, itemNameBody, subcategoryBody, categoryBody,
                descriptionBody, barterForBody, priceBody, attributesJsonBody, imageParts, brand1, model1);
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
        File file = new File(getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES), filename);

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


    @NonNull
    private String convertAttributesToJson() {
        JSONObject attributesJson = new JSONObject();

        // Iterate over each attribute layout in the AttributeSpinnerContainer
        for (int i = 0; i < AttributeSpinnerContainer.getChildCount(); i++) {
            View attributeLayout = AttributeSpinnerContainer.getChildAt(i);

            // Find TextView and Spinner in the attribute layout
            TextView attributeTextView = attributeLayout.findViewById(R.id.textViewAttributeName);
            Spinner attributeValueSpinner = attributeLayout.findViewById(R.id.spinnerAttributeValue);
            EditText attributeValueEditText = attributeLayout.findViewById(R.id.editTextAttributeValue);

            // Get attribute name
            String attributeName = attributeTextView.getText().toString();

            // Check if the view is a Spinner
            if (attributeValueSpinner != null && attributeValueSpinner.getSelectedItem() != null) {
                String attributeValue = (String) attributeValueSpinner.getSelectedItem();
                try {
                    // Add attribute name and value to JSON object
                    attributesJson.put(attributeName, attributeValue);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else if (attributeValueEditText != null) {
                // If the view is an EditText, get its value explicitly
                String attributeValue = attributeValueEditText.getText().toString().trim(); // trim to remove leading/trailing spaces

                if (!attributeValue.isEmpty()) {
                    try {
                        // Add attribute name and value to JSON object
                        attributesJson.put(attributeName, attributeValue);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            }
        }

        Log.e("Json atributes",attributesJson.toString());
        try {
           String Weight= (String) attributesJson.get("Weight (KG)");
           Log.e("Weight value",Weight);
           WeightValue=Integer.parseInt(Weight);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        // Convert JSON object to string and return
        return attributesJson.toString();
    }


    public void createModelSpinner(List<String> models, String subcategory) {
        modelSpinnerContainer.removeAllViews();

        // Create a new Spinner instance for models
        modelSpinner = new Spinner(this);
        TextView modelTextView = new TextView(this);
        modelTextView.setText("Model");
        modelTextView.setTextSize(16);
        modelTextView.setPadding(8, 8, 8, 8);

        // Add the TextView to the container
        modelSpinnerContainer.addView(modelTextView);

        // Create an ArrayAdapter for the models
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, models);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        modelSpinner.setAdapter(adapter);
        modelSpinner.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        getsubcategoryattributes(subcategory);

        // Set listener for model spinner
        modelSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedModel = (String) parent.getItemAtPosition(position);
                getElectronicsAverage(selectedModel);


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Handle when nothing is selected
            }
        });

        // Add the Spinner to the modelSpinnerContainer
        modelSpinnerContainer.addView(modelSpinner);
    }


    public void getElectronicsAverage(String model) {
        APIService apiService = RetrofitClient.getRetrofitInstance().create(APIService.class);

        Call<Double> call = apiService.getElectronicsAverage(model);
        call.enqueue(new Callback<Double>() {
            @Override
            public void onResponse(Call<Double> call, Response<Double> response) {
                if (response.isSuccessful()) {
                    Double electronicsAverage = response.body();
                    Log.e("Average", model + String.valueOf(electronicsAverage));
                    Log.e("Average", categoryName);

                    if(categoryName.equals("Grains")){
                        TextView recommendedPrice = findViewById(R.id.txtRecommendedPrice);
                        recommendedPrice.setText("Recommended Price: " + electronicsAverage+"/Kg");
                    }else{
                        TextView recommendedPrice = findViewById(R.id.txtRecommendedPrice);
                        recommendedPrice.setText("Recommended Price: " + electronicsAverage);
                    }
                    // Update UI with the average value

                }
            }

            @Override
            public void onFailure(Call<Double> call, Throwable t) {
                // Handle failure
            }
        });
    }

    public void gotoRecommendation() {
        Intent i = new Intent(UploadActivity.this, RecommendedPosts.class);
        startActivity(i);
    }

    @NonNull
    private String getFirstAttributeValue(JSONObject attributesJson) {
        String firstAttributeValue = null;

        // Get the keys of the JSON object
        Iterator<String> keys = attributesJson.keys();

        // Check if there is at least one key
        if (keys.hasNext()) {
            // Get the first key
            String firstKey = keys.next();

            // Get the value corresponding to the first key
            firstAttributeValue = attributesJson.optString(firstKey);
        }

        return firstAttributeValue;
    }

    public void setOtherCategoryAttributes(List<String> attributes, String subcat) {
        AttributeSpinnerContainer.removeAllViews();

        // Iterate over each attribute
        for (int i = 0; i < attributes.size(); i++) {
            String attribute = attributes.get(i);

            // Create a final copy of the index for use within the inner class
            final int spinnerIndex = i;

            // Inflate the layout for each attribute
            View attributeLayout = getLayoutInflater().inflate(R.layout.spinner_layout, null);
            TextView attributeTextView = attributeLayout.findViewById(R.id.textViewAttributeName);
            Spinner attributeValueSpinner = attributeLayout.findViewById(R.id.spinnerAttributeValue);
            attributeTextView.setText(attribute);

            // Make API call to get values for each attribute
            APIService apiService = RetrofitClient.getRetrofitInstance().create(APIService.class);
            Call<List<String>> attributeValuesCall = apiService.getttributevalues(subcat, attribute);
            attributeValuesCall.enqueue(new Callback<List<String>>() {
                @Override
                public void onResponse(Call<List<String>> call, Response<List<String>> response) {
                    if (response.isSuccessful()) {
                        List<String> values = response.body();
                        if (values != null) {
                            ArrayAdapter<String> adapter = new ArrayAdapter<>(UploadActivity.this, android.R.layout.simple_spinner_item, values);
                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            attributeValueSpinner.setAdapter(adapter);

                            // Check if the current spinner is the first spinner
                            if (spinnerIndex == 0) {
                                // Set listener only for the first spinner
                                attributeValueSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                        String selectedValue = (String) parent.getItemAtPosition(position);
                                        updateRecommendedPrice(selectedValue);
                                    }

                                    @Override
                                    public void onNothingSelected(AdapterView<?> parent) {
                                        // Handle when nothing is selected
                                    }
                                });
                            }
                        }
                    }
                }

                @Override
                public void onFailure(Call<List<String>> call, Throwable t) {
                    Log.e("API Call", "Failed: " + t.getMessage());
                }
            });
            AttributeSpinnerContainer.addView(attributeLayout);

        }// Add the attribute layout to the container
    }


    private void updateRecommendedPrice(String selectedValue) {
        // Here you can update the recommended price based on the selected value from the first spinner
        getElectronicsAverage(selectedValue);
    }


    private void setGrainsAttributes(List<String> attributes) {
        AttributeSpinnerContainer.removeAllViews();


        for (int i = 0; i < attributes.size(); i++) {
            String attribute = attributes.get(i);

            // Create a final copy of the index for use within the inner class
            final int spinnerIndex = i;

            // Inflate the layout for each attribute
            View attributeLayout = getLayoutInflater().inflate(R.layout.spinner_layout, null);
            TextView attributeTextView = attributeLayout.findViewById(R.id.textViewAttributeName);
            Spinner attributeValueSpinner = attributeLayout.findViewById(R.id.spinnerAttributeValue);
            attributeTextView.setText(attribute);

            // Make API call to get values for each attribute
            APIService apiService = RetrofitClient.getRetrofitInstance().create(APIService.class);
            Call<List<String>> attributeValuesCall = apiService.getttributevalues(subcategoryName, attribute);
            attributeValuesCall.enqueue(new Callback<List<String>>() {
                @Override
                public void onResponse(Call<List<String>> call, Response<List<String>> response) {
                    if (response.isSuccessful()) {
                        List<String> values = response.body();
                        if (values != null) {
                            ArrayAdapter<String> adapter = new ArrayAdapter<>(UploadActivity.this, android.R.layout.simple_spinner_item, values);
                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            attributeValueSpinner.setAdapter(adapter);

                            // Check if the current spinner is the first spinner
                            if (spinnerIndex == 0) {
                                // Set listener only for the first spinner
                                attributeValueSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                        String selectedValue = (String) parent.getItemAtPosition(position);
                                        updateRecommendedPrice(selectedValue);
                                    }

                                    @Override
                                    public void onNothingSelected(AdapterView<?> parent) {
                                        // Handle when nothing is selected
                                    }
                                });
                            }
                        }
                    }
                }

                @Override
                public void onFailure(Call<List<String>> call, Throwable t) {
                    Log.e("API Call", "Failed: " + t.getMessage());
                }
            });
            AttributeSpinnerContainer.addView(attributeLayout);

        }

        View weightLayout = getLayoutInflater().inflate(R.layout.text_input_layout, null);
        TextView weightTextView = weightLayout.findViewById(R.id.textViewAttributeName);
        EditText weightEditText = weightLayout.findViewById(R.id.editTextAttributeValue);
        weightTextView.setText("Weight (KG)");

        // Add the views to the container
        AttributeSpinnerContainer.addView(weightLayout);



        // Add any additional attributes from the list


    }






}


