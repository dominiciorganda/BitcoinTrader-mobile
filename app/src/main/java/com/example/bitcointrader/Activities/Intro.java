package com.example.bitcointrader.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.bitcointrader.Entities.CommonUtils;
import com.example.bitcointrader.R;

public class Intro extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        TextView welcome = findViewById(R.id.welcome);
        welcome.setText("Welcome @" + CommonUtils.getPrefString(this, "username"));

        LinearLayout introButton = findViewById(R.id.introButton);
        introButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Intro.this, CoinListActivity.class));
            }
        });

        LinearLayout walletButton = findViewById(R.id.walletButton);
        walletButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Intro.this, WalletActivity.class));
            }
        });

        LinearLayout logoutButton = findViewById(R.id.logout);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logout();
                finish();
            }
        });

        LinearLayout accountButton = findViewById(R.id.accountButton);
        accountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Intro.this, AccountActivity.class));
            }
        });
    }

    public void logout() {
        CommonUtils.logout(this.getApplicationContext());
    }
}