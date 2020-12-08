package com.example.bitcointrader;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    private RequestQueue mQueue;
    private String url = "http://192.168.56.1:8081";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mQueue = Volley.newRequestQueue(this);
        TextView tv1 = (TextView) findViewById(R.id.actualPrice);
        jsonParse();
    }

    private void jsonParse() {
        System.out.println("jk");
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url+"/getActual", null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String json = response.toString();
                            //mTextViewResult.append(json);

                            JSONObject jObject = new JSONObject(json);
                            System.out.println(json);

                            //Keyword - Value Pairs
                            String idValue = jObject.getString("price");
                            TextView tv1 = (TextView) findViewById(R.id.actualPrice);
                            tv1.setText(idValue);


                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, (Response.ErrorListener) error -> {
                    System.out.println("----------------------");
                    error.printStackTrace();
                });
        mQueue.add(request);
    }
}