package com.example.bitcointrader.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.transition.Fade;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.LinearLayout;

import com.example.bitcointrader.R;
import com.example.bitcointrader.util.Urls;

public class CoinListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setAnimation();
        setContentView(R.layout.activity_coinlist);


        LinearLayout bitcoin = findViewById(R.id.bitcoin);
        bitcoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                ActivityOptions options =
//                        ActivityOptions.makeSceneTransitionAnimation(Intro.this);
                startActivity(new Intent(CoinListActivity.this, BitcoinActivity.class));
            }
        });

        LinearLayout ethereum = findViewById(R.id.ethereum);
        ethereum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(CoinListActivity.this, EthereumActivity.class));
            }
        });

        LinearLayout elrond = findViewById(R.id.elrond);
        elrond.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(CoinListActivity.this, ElrondActivity.class));
            }
        });

        LinearLayout litecoin = findViewById(R.id.litecoin);
        litecoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(CoinListActivity.this, LitecoinActivity.class));
            }
        });

        LinearLayout dash = findViewById(R.id.dash);
        dash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(CoinListActivity.this, DashActivity.class));
            }
        });

        LinearLayout dogecoin = findViewById(R.id.dogecoin);
        dogecoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(CoinListActivity.this, DogecoinActivity.class));
            }
        });

        LinearLayout binancecoin = findViewById(R.id.binancecoin);
        binancecoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CoinListActivity.this, CoinActivity.class);
                intent.putExtra("url", Urls.BINANCECOIN);
                startActivity(intent);
            }
        });

        LinearLayout bitcoincash = findViewById(R.id.bitcoincash);
        bitcoincash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CoinListActivity.this, CoinActivity.class);
                intent.putExtra("url", Urls.BITCOINCASH);
                startActivity(intent);
            }
        });

        LinearLayout filecoin = findViewById(R.id.filecoin);
        filecoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CoinListActivity.this, CoinActivity.class);
                intent.putExtra("url", Urls.FILECOIN);
                startActivity(intent);
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