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
import androidx.core.content.ContextCompat;
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

public class NavigationActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityNavigationBinding binding;
    TextView badge;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNavigationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarNavigation.toolbar);

        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;

        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_View_Items, R.id.nav_Upload_Items, R.id.nav_View_Uploads, R.id.nav_View_Requests, R.id.nav_Profile, R.id.nav_Notifications, R.id.nav_history,R.id.nav_RecommendedItems)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_navigation);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        navController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
            @Override
            public void onDestinationChanged(@NonNull NavController controller, @NonNull NavDestination destination, Bundle arguments) {
                getSupportActionBar().setTitle(destination.getLabel());
            }
        });

        // Set the navigation item selected listener
        navigationView.setNavigationItemSelectedListener(this);

        Menu menu = navigationView.getMenu();
        MenuItem menuItem = menu.findItem(R.id.nav_View_Requests);
        View actionView = menuItem.getActionView();
        if (actionView == null) {
            actionView = getLayoutInflater().inflate(R.layout.menu_item_with_badge, null);
            menuItem.setActionView(actionView);
        }

        badge = actionView.findViewById(R.id.badge);
        getRequestsNotificationCount();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getRequestsNotificationCount();
    }

    public void getRequestsNotificationCount() {
        APIService apiService = RetrofitClient.getRetrofitInstance().create(APIService.class);
        String email = GlobalVariables.getInstance().getEmail();
        Call<Integer> call = apiService.getRequestsCount(email);

        call.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                if (response.isSuccessful()) {
                    int notificationCount = response.body();
                    if (notificationCount > 0) {
                        badge.setVisibility(View.VISIBLE);
                        badge.setText(String.valueOf(notificationCount));
                    } else {
                        badge.setVisibility(View.GONE);
                    }
                } else {
                    Log.e("API Call Response Check", "Response Not Successful");
                }
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {
                Log.e("API Call Response Check", "On Failure");
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);

        MenuItem menuItem = menu.findItem(R.id.action_notification);
        View actionView = MenuItemCompat.getActionView(menuItem);
        ImageView notificationIcon = actionView.findViewById(R.id.notification_icon);
        TextView notificationBadge = actionView.findViewById(R.id.notification_badge);
        APIService apiService = RetrofitClient.getRetrofitInstance().create(APIService.class);
        String email = GlobalVariables.getInstance().getEmail();
        Call<Integer> call = apiService.getNotificationsCount(email);
        call.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                if (response.isSuccessful()) {
                    int notificationCount = response.body();
                    if (notificationCount > 0) {
                        notificationBadge.setVisibility(View.VISIBLE);
                        notificationBadge.setText(String.valueOf(notificationCount));
                    } else {
                        notificationBadge.setVisibility(View.GONE);
                    }
                    notificationIcon.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(NavigationActivity.this, Activity_Notification.class);
                            startActivity(intent);
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {
                // Handle the failure
            }
        });

        notificationBadge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NavigationActivity.this, Activity_Notification.class);
                startActivity(intent);
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_Logout) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_navigation);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration) || super.onSupportNavigateUp();
    }

    @Override
    public void onBackPressed() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_navigation);
        if (!navController.popBackStack()) {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_logout) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
            return true;
        }

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_navigation);
        return NavigationUI.onNavDestinationSelected(item, navController) || super.onOptionsItemSelected(item);
    }
}
