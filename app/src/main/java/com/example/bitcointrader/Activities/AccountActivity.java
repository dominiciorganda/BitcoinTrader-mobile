package com.example.bitcointrader.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.bitcointrader.Entities.CommonUtils;
import com.example.bitcointrader.R;

public class AccountActivity extends AppCompatActivity {

    private TextView username, email, money;
    private LinearLayout changePassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        username = findViewById(R.id.username);
        email = findViewById(R.id.email);
        money = findViewById(R.id.money);
        changePassword = findViewById(R.id.changePassword);

        username.setText(CommonUtils.getPrefString(this, "username"));
        email.setText(CommonUtils.getPrefString(this, "email"));
        money.setText(CommonUtils.getPrefString(this, "money") + " $");
        changePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AccountActivity.this, ChangePasswordActivity.class));
            }
        });
    }
}