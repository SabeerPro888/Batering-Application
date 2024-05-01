package com.example.barteringapp7;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ItemUploadedActivity extends AppCompatActivity {

    Button btnCon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_uploaded);
        btnCon=findViewById(R.id.btnContinue);

        btnCon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(ItemUploadedActivity.this,NavigationActivity.class);
                startActivity(intent);
            }
        });



    }
}