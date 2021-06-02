package com.example.bitcointrader.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.bitcointrader.R;
import com.example.bitcointrader.Request.IRequestCallBack;
import com.example.bitcointrader.Request.RequestRetriever;
import com.example.bitcointrader.util.Urls;

import org.json.JSONException;
import org.json.JSONObject;

public class RegisterActivity extends AppCompatActivity {

    private RequestRetriever requestRetriever = new RequestRetriever();
    private EditText email;
    private EditText registerUsername;
    private EditText registerPassword;
    private EditText registerConfirmPassword;
    private LinearLayout backToLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        email = (EditText) findViewById(R.id.email);
        registerUsername = (EditText) findViewById(R.id.registerUsername);
        registerPassword = (EditText) findViewById(R.id.registerPassword);
        registerConfirmPassword = (EditText) findViewById(R.id.registerConfirmPassword);
        backToLayout = (LinearLayout) findViewById(R.id.backToLogin);

        Button register = (Button) findViewById(R.id.registerRegister);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validate()) {
                    JSONObject body = new JSONObject();
                    try {
                        body.put("username", registerUsername.getText().toString());
                        body.put("email", email.getText().toString());
                        body.put("password", registerPassword.getText().toString());

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    requestRetriever.register(Urls.REGISTER, getApplicationContext(), new IRequestCallBack() {
                        @Override
                        public void onSuccess(Object response) {
                            String data = response.toString();
                            if (data.equals("error"))
                                Toast.makeText(getApplicationContext(), "Wrong register credetentials", Toast.LENGTH_SHORT).show();
                            else {
                                Toast.makeText(getApplicationContext(), "Activate your account via email", Toast.LENGTH_LONG).show();
                                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                            }
                        }
                    }, body);
                } else
                    Toast.makeText(RegisterActivity.this, "Invalid input", Toast.LENGTH_SHORT).show();
            }
        });

        backToLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    public boolean validate() {
        return validateEmail() && validatePassword() && validateUsername();
    }

    private boolean validatePassword() {
        String password = registerPassword.getText().toString();
        String confirmPassword = registerConfirmPassword.getText().toString();
        if (password.length() != confirmPassword.length())
            return false;
        if (password.length() < 6 || password.length() > 30)
            return false;
        return password.equals(confirmPassword);
    }

    private boolean validateUsername() {
        String username = registerUsername.getText().toString();
        return username.length() >= 6 && username.length() <= 30;
    }

    private boolean validateEmail() {
        String emailText = email.getText().toString();
        return emailText.matches("[a-zA-Z0-9]{3,}@[a-zA-Z][a-zA-Z0-9]{2,}\\.[a-z]{2,}");
    }
}