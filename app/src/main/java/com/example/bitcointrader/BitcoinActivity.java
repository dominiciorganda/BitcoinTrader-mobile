package com.example.bitcointrader;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


public class BitcoinActivity extends AppCompatActivity {

    private RequestQueue requestQueue;
    private String url = "http://192.168.56.1:8081/CoinTrader/bitcoin";
    private List<Coin> chartCoins = new ArrayList<>();
    private Coin anualMin;
    private Coin anualMax;
    private Coin max;

    @Override
    protected void onPause() {
        super.onPause();
        requestQueue = Volley.newRequestQueue(this);
        getValues();

    }

    @Override
    protected void onResume() {
        super.onResume();
        drawChart();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bitcoin);
        onPause();
        requestQueue = Volley.newRequestQueue(this);
        refreshActualValue();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                onResume();

//                System.out.println("-------"+chartBitcoins.size());
            }
        },1000);
//        drawChart();
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
                            Coin actual = gson.fromJson(json, Coin.class);
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
                            max = gson.fromJson(json, Coin.class);
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
                            anualMax = gson.fromJson(json, Coin.class);
                            TextView anualMaxPrice = (TextView) findViewById(R.id.anualMax);
                            anualMaxPrice.setText(anualMax.showPrice());
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
                            anualMin= gson.fromJson(json, Coin.class);
                            TextView anualMinPrice = (TextView) findViewById(R.id.anualMin);
                            anualMinPrice.setText(anualMin.showPrice());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, (Response.ErrorListener) Throwable::printStackTrace);
        requestQueue.add(request);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url+"/getLastMonth", null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            Gson gson = new Gson();
                            String json = response.toString();
                            Coin[] lastMonth= gson.fromJson(json, Coin[].class);
                            chartCoins.addAll(Arrays.asList(lastMonth));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                }, (Response.ErrorListener) Throwable::printStackTrace);
        requestQueue.add(jsonArrayRequest);
    }

    public void drawChart(){
        ArrayList<Coin> chartData = new ArrayList<Coin>();
        chartData.addAll(chartCoins);

        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("CHART_COINS", chartData);
        Chart chart =  new Chart();
        chart.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_view,chart).commit();

    }
}