package com.android;

/**
 * Created by Ngoc Vu on 12/18/2017.
 */
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import com.android.Effect.Typewriter;

import java.util.ArrayList;
import java.util.Random;

public class SplashScreenActivity extends AppCompatActivity {

    ArrayList<Integer> arrayImage;
    private RelativeLayout relativeLayout;
    private static int SPLASH_TIME_OUT = 1500;
    private Typewriter typewriterAppname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash_screen);
        typewriterAppname = (Typewriter) findViewById(R.id.typewriterAppname);
        relativeLayout = (RelativeLayout) findViewById(R.id.relativeLayout);

        arrayImage = new ArrayList<>();
        //arrayImage.add(R.drawable.bg_splashscreen1);
        //arrayImage.add(R.drawable.bg_splashscreen2);
        arrayImage.add(R.drawable.bg_splashscreen3);
        Random random = new Random();
        int p = random.nextInt(arrayImage.size());

        relativeLayout.setBackgroundResource(arrayImage.get(p));

        String appname = getString(R.string.app_name);
        typewriterAppname.setCharacterDelay(100);
        typewriterAppname.animateText(appname);

        // intuser();
         //initDataInFireBase();
    }

    @Override
    protected void onResume() {
        super.onResume();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashScreenActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        }, SPLASH_TIME_OUT);
    }
}

