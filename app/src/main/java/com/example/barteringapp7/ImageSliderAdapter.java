package com.example.barteringapp7;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ImageSliderAdapter extends PagerAdapter {
    private Context context;
    private LayoutInflater layoutInflater;

    private boolean showVerificationTick;

    public Context getContext() {
        return context;

    }

    public void setContext(Context context) {
        this.context = context;
    }

    public List<String> getImagePaths() {
        return imagePaths;
    }

    public void setImagePaths(List<String> imagePaths) {
        this.imagePaths = imagePaths;
        notifyDataSetChanged();

    }

    public ImageSliderAdapter(Context context) {
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);

    }


    private List<String> imagePaths;
    @Override
    public int getCount() {
        return imagePaths != null ? imagePaths.size() : 0;

    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;

    }
    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_image_slider, container, false);
        ImageView imageView = view.findViewById(R.id.PagerimageView);

        // Load image from path into ImageView using Picasso
        String imageUrl = imagePaths.get(position);
        Picasso.get().load(imageUrl).into(imageView);
        ImageView verificationTickImageView = view.findViewById(R.id.verificationTickImageView);
        verificationTickImageView.setVisibility(showVerificationTick ? View.VISIBLE : View.GONE);

        // Add the inflated view to the ViewPager container
        container.addView(view);

        // Return the inflated view
        return view;
    }
    public void setShowVerificationTick(boolean show) {
        this.showVerificationTick = show;
        notifyDataSetChanged();
    }



    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
//    private void addVerificationTickOverImage(ViewGroup container, int position) {
//        // Get the current item view from the ViewPager
//        View itemView = container.getChildAt(position);
//        if (itemView != null) {
//            // Create an ImageView for the verification tick
//            ImageView verificationTick = new ImageView(context);
//            verificationTick.setImageResource(R.drawable.baseline_check_circle_outline_24);
//            verificationTick.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
//
//            // Set layout parameters for the verification tick
//            FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(
//                    FrameLayout.LayoutParams.WRAP_CONTENT,
//                    FrameLayout.LayoutParams.WRAP_CONTENT
//            );
//            layoutParams.gravity = Gravity.BOTTOM | Gravity.END; // Bottom right corner
//            layoutParams.bottomMargin = dpToPx(8); // Adjust bottom margin
//            layoutParams.rightMargin = dpToPx(8); // Adjust right margin
//            verificationTick.setLayoutParams(layoutParams);
//
//            // Add the verification tick ImageView to the current item view
//            FrameLayout frameLayout = itemView.findViewById(R.id.FrameLayout);
//            if (frameLayout != null) {
//                frameLayout.addView(verificationTick);
//            } else {
//                Log.e("Verification Tick", "FrameLayout is null at position: " + position);
//            }
//        }
//    }
//
//    // Convert dp to pixels
//    private int dpToPx(int dp) {
//        float density = context.getResources().getDisplayMetrics().density;
//        return Math.round(dp * density);
//    }


}
