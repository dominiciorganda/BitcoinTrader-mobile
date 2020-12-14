package com.example.bitcointrader;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    private RequestQueue requestQueue;
    private String url = "http://192.168.56.1:8081";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        requestQueue = Volley.newRequestQueue(this);
        refreshActualValue();
        getValues();

    }

    public void refreshActualValue(){
        final Handler handler = new Handler();
        Timer timer = new Timer();
        TimerTask doAsynchronousTask = new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    public void run() {
                        try {
                            getActualValue();
                        } catch (Exception e) {
                        }
                    }
                });
            }
        };
        timer.schedule(doAsynchronousTask, 0, 30000);

    }

    private void getActualValue() {
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url+"/getActual", null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Gson gson = new Gson();
                            String json = response.toString();
                            Bitcoin actual = gson.fromJson(json, Bitcoin.class);
                            TextView actualPrice = (TextView) findViewById(R.id.actualPrice);
                            actualPrice.setText(actual.showPrice());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, (Response.ErrorListener) Throwable::printStackTrace);
        requestQueue.add(request);
    }

    private void getValues() {
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url+"/getMax", null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Gson gson = new Gson();
                            String json = response.toString();
                            Bitcoin max = gson.fromJson(json, Bitcoin.class);
                            TextView maxPrice = (TextView) findViewById(R.id.maxPrice);
                            maxPrice.setText(max.showPrice());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, (Response.ErrorListener) Throwable::printStackTrace);
        requestQueue.add(request);
        request = new JsonObjectRequest(Request.Method.GET, url+"/getAnualMax", null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Gson gson = new Gson();
                            String json = response.toString();
                            Bitcoin anualmax = gson.fromJson(json, Bitcoin.class);
                            TextView anualMaxPrice = (TextView) findViewById(R.id.anualMax);
                            anualMaxPrice.setText(anualmax.showPrice());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, (Response.ErrorListener) Throwable::printStackTrace);
        requestQueue.add(request);
        request = new JsonObjectRequest(Request.Method.GET, url+"/getAnualMin", null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Gson gson = new Gson();
                            String json = response.toString();
                            Bitcoin anualmin= gson.fromJson(json, Bitcoin.class);
                            TextView anualMinPrice = (TextView) findViewById(R.id.anualMin);
                            anualMinPrice.setText(anualmin.showPrice());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, (Response.ErrorListener) Throwable::printStackTrace);
        requestQueue.add(request);
    }
}