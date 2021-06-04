package com.example.bitcointrader.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.bitcointrader.R;
import com.example.bitcointrader.Request.IRequestCallBack;
import com.example.bitcointrader.Request.RequestRetriever;
import com.example.bitcointrader.util.Urls;

import org.json.JSONException;
import org.json.JSONObject;

public class ChangePasswordActivity extends AppCompatActivity {

    private RequestRetriever requestRetriever = new RequestRetriever();
    private EditText oldPassword, newPassword, confirmPassword;
    private Button change;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        oldPassword = findViewById(R.id.changePassword);
        newPassword = findViewById(R.id.changeNewPassword);
        confirmPassword = findViewById(R.id.changeConfirmPassword);
        change = findViewById(R.id.changeChange);

        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validatePassword()) {
                    JSONObject body = new JSONObject();
                    try {
                        body.put("oldPassword", oldPassword.getText().toString());
                        body.put("newPassword", newPassword.getText().toString());

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    requestRetriever.transaction(Urls.CHANGE, getApplicationContext(), new IRequestCallBack() {
                        @Override
                        public void onSuccess(Object response) {
                            String data = response.toString();

                            if (data.equals("error"))
                                Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
                            else {
                                Toast.makeText(getApplicationContext(), "Password Changed Succesfully", Toast.LENGTH_LONG).show();
                                finish();
                            }
                        }
                    }, body);
                } else
                    Toast.makeText(getApplicationContext(), "Invalid input", Toast.LENGTH_SHORT).show();
            }
        });


    }

    private boolean validatePassword() {
        String password = oldPassword.getText().toString();
        String newPassw = newPassword.getText().toString();
        String confPassword = confirmPassword.getText().toString();
        if (newPassw.length() != confPassword.length())
            return false;
        if (password.length() < 6 || password.length() > 30)
            return false;
        if (newPassw.length() < 6 || newPassw.length() > 30)
            return false;
        return newPassw.equals(confPassword);
    }
}