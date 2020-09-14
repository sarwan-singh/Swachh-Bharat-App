package com.app.swachhbharat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.RelativeLayout;

import com.airbnb.lottie.LottieAnimationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.InputStream;

public class MainActivity extends AppCompatActivity {


    private LottieAnimationView cleanLoading,bottom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final RelativeLayout main_layout = findViewById(R.id.main_layout);
        main_layout.setVisibility(View.GONE);
        cleanLoading = findViewById(R.id.loading_animation);
        cleanLoading.setVisibility(View.VISIBLE);
        cleanLoading.playAnimation();
        final Handler handler2 = new Handler();
        handler2.postDelayed(new Runnable() {
            @Override
            public void run() {

                cleanLoading.pauseAnimation();
                cleanLoading.setVisibility(View.GONE);
                main_layout.setVisibility(View.VISIBLE);
            }
        },8000);

// Write a message to the database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("message");

        myRef.setValue("Hello, World!");



    }

    public void upload(View view){
        Intent intent = new Intent(MainActivity.this,Upload.class);
        startActivity(intent);
    }
}
