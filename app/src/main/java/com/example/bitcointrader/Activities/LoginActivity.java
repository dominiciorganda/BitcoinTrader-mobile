package com.example.bitcointrader.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bitcointrader.Entities.CommonUtils;
import com.example.bitcointrader.R;
import com.example.bitcointrader.Request.IRequestCallBack;
import com.example.bitcointrader.Request.RequestRetriever;
import com.example.bitcointrader.Request.RequestSingleton;
import com.example.bitcointrader.util.Urls;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {

    private RequestRetriever requestRetriever = new RequestRetriever();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        EditText username = (EditText) findViewById(R.id.username);
        EditText password = (EditText) findViewById(R.id.password);

        Button login = (Button) findViewById(R.id.login);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                System.out.println(username.getText().toString());
//                System.out.println(password.getText().toString());
                JSONObject body = new JSONObject();
                try {
                    body.put("username", username.getText().toString());
                    body.put("password", password.getText().toString());

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                requestRetriever.login(Urls.LOGIN, getApplicationContext(), new IRequestCallBack() {
                    @Override
                    public void onSuccess(Object response) {
                        String data = response.toString();
                        if (data.equals("error"))
                            Toast.makeText(getApplicationContext(), "Wrong or inactive login credetentials", Toast.LENGTH_SHORT).show();
                        else {
                            startActivity(new Intent(LoginActivity.this, Intro.class));
                        }
                    }
                }, body);
            }
        });

        TextView register = (TextView) findViewById(R.id.register);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        logout();
    }

    @Override
    protected void onResume(){
        super.onResume();
        logout();
    }

    public void logout() {
        CommonUtils.logout(this.getApplicationContext());
    }
}