package com.example.splashscreen;

import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    Animation goku1,goku2;
    ImageView imgGoku1,imgGoku2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
       goku1= AnimationUtils.loadAnimation(this,R.anim.goku1_anim);
        goku2= AnimationUtils.loadAnimation(this,R.anim.goku2_anim);
        imgGoku1=findViewById(R.id.p1);
        imgGoku2=findViewById(R.id.p2);

        imgGoku1.startAnimation(goku1);
        imgGoku2.startAnimation(goku2);

        Handler handler=new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
               startActivity(new Intent(MainActivity.this , MainActivity2.class));
               finish();
            }
        },3000);
    }
}