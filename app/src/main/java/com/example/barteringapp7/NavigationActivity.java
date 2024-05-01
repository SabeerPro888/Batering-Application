package com.example.barteringapp7;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.barteringapp7.ui.Notifications.NotificationsFragment;
import com.example.barteringapp7.ui.UploadItems.UploadItemsFragment;
import com.example.barteringapp7.ui.ViewRequests.ViewRequestsFragment;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.example.barteringapp7.databinding.ActivityNavigationBinding;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NavigationActivity extends AppCompatActivity{

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityNavigationBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNavigationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        setSupportActionBar(binding.appBarNavigation.toolbar);


        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.

        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_View_Items, R.id.nav_Upload_Items, R.id.nav_View_Uploads, R.id.nav_View_Requests, R.id.nav_Profile,R.id.nav_Notifications)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_navigation);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);


    }





        // Inflate the menu; this adds items to the action bar if it is present.
        @Override
        public boolean onCreateOptionsMenu(Menu menu) {
            getMenuInflater().inflate(R.menu.toolbar_menu, menu);

            // Get the MenuItem for the notification
            MenuItem menuItem = menu.findItem(R.id.action_notification);

            // Get the custom layout view for the notification icon
            View actionView = MenuItemCompat.getActionView(menuItem);
            ImageView notificationIcon = actionView.findViewById(R.id.notification_icon);
            TextView notificationBadge = actionView.findViewById(R.id.notification_badge);
            APIService apiService = RetrofitClient.getRetrofitInstance().create(APIService.class);
            String email= GlobalVariables.getInstance().getEmail();
            Call<Integer> call = apiService.getNotificationsCount(email);
            call.enqueue(new Callback<Integer>() {
                @Override
                public void onResponse(Call<Integer> call, Response<Integer> response) {
                    if(response.isSuccessful()){
                        int notificationCount = response.body(); // Example count, replace with your actual logic

                        if (notificationCount > 0) {
                            notificationBadge.setVisibility(View.VISIBLE);
                            notificationBadge.setText(String.valueOf(notificationCount));
                        } else {
                            notificationBadge.setVisibility(View.GONE);
                        }

                        // Set click listener for the notification icon if needed
                        notificationIcon.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                // Handle click event for the notification icon
                            }
                        });

                    }else{

                    }
                }

                @Override
                public void onFailure(Call<Integer> call, Throwable t) {

                }
            });
            notificationBadge.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Handle click event for the notification icon
                    // Navigate to the NotificationsFragment when the icon is clicked
                   Intent intent=new Intent(NavigationActivity.this,Activity_Notification.class);
                   startActivity(intent);
                }
            });






            // Set up your notification count logic here

            return true;
        }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_Logout) {
            // Perform logout action here
            // For example, navigate to the sign-in page
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish(); // Optional: Finish the current activity to prevent the user from coming back to it via the back button
            return true;
        }

        return super.onOptionsItemSelected(item);
    }



    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_navigation);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }



}