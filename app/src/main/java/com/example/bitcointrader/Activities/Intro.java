package com.example.bitcointrader.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.transition.Explode;
import android.transition.Fade;
import android.transition.Slide;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.LinearLayout;

import com.example.bitcointrader.R;

public class Intro extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setAnimation();
        setContentView(R.layout.activity_intro);


        LinearLayout bitcoin = findViewById(R.id.bitcoin);
        bitcoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                ActivityOptions options =
//                        ActivityOptions.makeSceneTransitionAnimation(Intro.this);
                startActivity(new Intent(Intro.this, BitcoinActivity.class));
            }
        });

        LinearLayout ethereum = findViewById(R.id.ethereum);
        ethereum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Intro.this, EthereumActivity.class));
            }
        });

        LinearLayout elrond = findViewById(R.id.elrond);
        elrond.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Intro.this, ElrondActivity.class));
            }
        });

        LinearLayout litecoin = findViewById(R.id.litecoin);
        litecoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Intro.this, LitecoinActivity.class));
            }
        });

        LinearLayout dash = findViewById(R.id.dash);
        dash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Intro.this, DashActivity.class));
            }
        });

        LinearLayout dogecoin = findViewById(R.id.dogecoin);
        dogecoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Intro.this, DogecoinActivity.class));
            }
        });
    }

    public void setAnimation() {
        if (Build.VERSION.SDK_INT > 20) {

            Fade fade = new Fade();
            fade.setDuration(400);
            fade.setInterpolator(new DecelerateInterpolator());
            getWindow().setEnterTransition(null);
            getWindow().setExitTransition(null);
        }
    }
}