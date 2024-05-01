package com.example.barteringapp7;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.squareup.picasso.Picasso;

import java.util.List;

public class ImageSliderAdapter extends PagerAdapter {
    private Context context;

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

        // Load image from path into Bitmap
//        Bitmap bitmap = BitmapFactory.decodeFile(imagePaths.get(position));
        String imageUrl = imagePaths.get(position);

        Picasso.get().load(imageUrl).into(imageView);

        container.addView(view);

        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

}
