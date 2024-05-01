package com.example.barteringapp7;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuItemCompat;

public class Activity_App_Bar extends AppCompatActivity {


        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.app_bar_navigation);

            // Your other initialization code here

        }

        @Override
        public boolean onCreateOptionsMenu(Menu menu) {
            getMenuInflater().inflate(R.menu.toolbar_menu, menu);

            // Get the MenuItem for the notification
            MenuItem menuItem = menu.findItem(R.id.action_notification);

            // Get the custom layout view for the notification icon
            View actionView = MenuItemCompat.getActionView(menuItem);
            ImageView notificationIcon = actionView.findViewById(R.id.notification_icon);
            TextView notificationBadge = actionView.findViewById(R.id.notification_badge);

            // Set up your notification count logic here
            int notificationCount = 10; // Example count, replace with your actual logic

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

            return true;
        }


}
