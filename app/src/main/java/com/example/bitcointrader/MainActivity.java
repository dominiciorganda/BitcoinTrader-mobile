package com.example.bitcointrader;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.IMarker;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


public class MainActivity extends AppCompatActivity {

    private RequestQueue requestQueue;
    private String url = "http://192.168.56.1:8081/CoinTrader/bitcoin";
    private List<Bitcoin> chartBitcoins = new ArrayList<>();
    private Bitcoin anualMin;
    private Bitcoin anualMax;
    private Bitcoin max;

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
        setContentView(R.layout.activity_main);
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
                            max = gson.fromJson(json, Bitcoin.class);
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
                            anualMax = gson.fromJson(json, Bitcoin.class);
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
                            anualMin= gson.fromJson(json, Bitcoin.class);
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
                            Bitcoin[] lastYear= gson.fromJson(json, Bitcoin[].class);
                            chartBitcoins.addAll(Arrays.asList(lastYear));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                }, (Response.ErrorListener) Throwable::printStackTrace);
        requestQueue.add(jsonArrayRequest);
    }

    public void drawChart(){

        LineChart chart = (LineChart) findViewById(R.id.chart);


        List<Entry> entries = new ArrayList<Entry>();
        int i =0;
        for (Bitcoin data : chartBitcoins) {
            i++;

            entries.add(new Entry(i, (float)data.getPrice()));
        }


        LineDataSet dataSet = new LineDataSet(entries,"");
        LineData lineData = new LineData(dataSet);
        dataSet.setDrawFilled(true);
        dataSet.setFillDrawable(ContextCompat.getDrawable(this, R.drawable.gradient));
        dataSet.setDrawCircles(true);
        dataSet.setDrawCircleHole(true);
        dataSet.setCircleHoleColor(Color.BLACK);
        dataSet.setCircleRadius(10);
        dataSet.setCircleHoleRadius(8);
        dataSet.setValueTextSize(0);
        dataSet.setDrawHorizontalHighlightIndicator(false);
        dataSet.setDrawVerticalHighlightIndicator(false);




        chart.setData(lineData);
        chart.getAxisLeft().setDrawGridLines(false);
        chart.getXAxis().setDrawGridLines(false);
        chart.getAxisRight().setDrawGridLines(false);
        chart.getDescription().setEnabled(false);
        chart.getAxisLeft().setDrawLabels(false);
        chart.getAxisRight().setDrawLabels(false);
        chart.getXAxis().setDrawLabels(false);
        chart.getLegend().setEnabled(false);
        chart.invalidate(); // refresh


        IMarker marker = new Popup(MainActivity.this, R.layout.popup,chartBitcoins);
        chart.setMarker(marker);

        chart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
//                Toast.makeText(MainActivity.this, "The text you want to display", Toast.LENGTH_LONG).show();

                float x=e.getX();
                float y=e.getY();
                System.out.println(x);
                System.out.println(chartBitcoins.get((int)x-1).getPrice());
            }

            @Override
            public void onNothingSelected() {

            }
        });

    }
}