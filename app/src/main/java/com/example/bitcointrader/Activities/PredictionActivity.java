package com.example.bitcointrader.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import com.example.bitcointrader.Entities.ChartType;
import com.example.bitcointrader.Entities.Coin;
import com.example.bitcointrader.Entities.Popup;
import com.example.bitcointrader.Fragments.Loading;
import com.example.bitcointrader.R;
import com.example.bitcointrader.Request.IRequestCallBack;
import com.example.bitcointrader.Request.RequestRetriever;
import com.example.bitcointrader.util.Urls;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.IMarker;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LegendEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import java.util.ArrayList;
import java.util.List;

public class PredictionActivity extends AppCompatActivity {

    private RequestRetriever requestRetriever = new RequestRetriever();
    private List<Coin> chartCoins = new ArrayList<>();
    private View chart;
    private Loading loading;
    private View loadingScreen;
    private String url;
    private Intent intent;

    @Override
    protected void onResume() {
        super.onResume();
//        drawChart();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prediction);

        initializeData();
        setVisibilities();
        getValues();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                drawChart();
                loading.disableLoadingScreen();
                chart.setVisibility(View.VISIBLE);

            }
        }, 4500);

//        System.out.println(url);
    }

    public void initializeData() {
        intent = getIntent();
        url = intent.getStringExtra("url");
    }

    public void setVisibilities() {
        loadingScreen = findViewById(R.id.loading_screen);
        setLoadingScreen();
        chart = findViewById(R.id.chart);
        chart.setVisibility(View.GONE);

    }

    public void setLoadingScreen() {
        Bundle bundle = new Bundle();
        loading = new Loading();
        loading.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().replace(R.id.loading_screen, loading).commit();
    }

    public void getValues() {
        requestRetriever.getCoinList(url + "/prediction", this.getApplicationContext(), new IRequestCallBack<List<Coin>>() {
            @Override
            public void onSuccess(List<Coin> coins) {
                System.out.println(coins);
                chartCoins = new ArrayList<>();
                chartCoins.addAll(coins);
                drawChart();
            }
        });
    }

    public void drawChart() {
        LineChart chart = (LineChart) findViewById(R.id.chart);

        List<Entry> entries = new ArrayList<Entry>();
        List<Entry> ent = new ArrayList<Entry>();
        int i = 0;
        for (Coin data : chartCoins) {
            i++;

            entries.add(new Entry(i, (float) data.getPrice()));
            ent.add(new Entry(i, (float) data.getPrice() / 2));
        }

//        System.out.println(chartCoins);

        LineDataSet dataSet = new LineDataSet(entries, "");
        LineData lineData = new LineData(dataSet);
        dataSet.setDrawFilled(true);
        if (chartCoins.size() > 0)
            if (chartCoins.get(5).getPrice() > chartCoins.get(6).getPrice())
                dataSet.setFillDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.downgradient));
            else
                dataSet.setFillDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.upgradient));
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
        chart.getAxisLeft().setDrawLabels(true);
        chart.getAxisRight().setDrawLabels(false);
        chart.getAxisLeft().setTextSize(15f);
        chart.getXAxis().setDrawLabels(false);
        Legend legend = chart.getLegend();
        LegendEntry l1 = new LegendEntry("Last 5 days", Legend.LegendForm.SQUARE, 15f, 2f, null, Color.BLUE);
        LegendEntry l2 = new LegendEntry("Next 5 days", Legend.LegendForm.SQUARE, 15f, 10f, null, Color.GREEN);
        if (chartCoins.size() > 0)
            if (chartCoins.get(5).getPrice() > chartCoins.get(6).getPrice())
                l2.formColor = Color.RED;

        legend.setCustom(new LegendEntry[]{l1, l2});
        legend.setTextSize(20f);
        legend.setEnabled(true);

        chart.invalidate(); // refresh


        IMarker marker = new Popup(getApplicationContext(), R.layout.popup, chartCoins, ChartType.LINEAR);
        chart.setMarker(marker);

        chart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
//                Toast.makeText(MainActivity.this, "The text you want to display", Toast.LENGTH_LONG).show();

                float x = e.getX();
                float y = e.getY();
                System.out.println(x);
                System.out.println(chartCoins.get((int) x - 1).getPrice());
            }

            @Override
            public void onNothingSelected() {

            }
        });
    }

}