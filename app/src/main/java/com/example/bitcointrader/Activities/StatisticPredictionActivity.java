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
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;


public class StatisticPredictionActivity extends AppCompatActivity {

    private RequestRetriever requestRetriever = new RequestRetriever();
    private List<Coin> chartCoins = new ArrayList<>();
    private Intent intent;
    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistic_prediction);


        getValues();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                drawChart();

            }
        }, 4500);
//        initializeData();
    }

    public void initializeData() {
        intent = getIntent();
        url = intent.getStringExtra("url");
    }

    public void getValues() {
        requestRetriever.getCoinList(Urls.BITCOIN + "/getAll", this.getApplicationContext(), new IRequestCallBack<List<Coin>>() {
            @Override
            public void onSuccess(List<Coin> coins) {
                System.out.println(coins);
                chartCoins = new ArrayList<>();
                chartCoins.addAll(coins);
                drawChart();
            }
        });
    }

    private float linearRegression(int x){
        //constants
//        a = 0.28587743 si b = 1.3548479
        float a = (float) 0.28587743;
        float b = (float) 1.3548479;

        // Functie: 10 ^ (a*ln(x) - b)
        float ln = (float) Math.log(x);
        float result = a*ln - b;

        return (float) Math.pow(10, result);
    }

    private void drawChart() {
        LineChart chart = (LineChart) findViewById(R.id.chart);

        List<Entry> entries = new ArrayList<Entry>();
        List<Entry> ent = new ArrayList<Entry>();
        int i = 0;


        for (Coin data : chartCoins) {
            i++;

            entries.add(new Entry(i,  scaleCbr(data.getPrice())));
            float result = linearRegression(i);
            System.out.println(result);
            ent.add(new Entry(i, result));
        }

//        System.out.println(chartCoins);

        LineDataSet dataSet = new LineDataSet(entries, "");
        dataSet.setDrawFilled(true);
        dataSet.setDrawCircles(false);
        dataSet.setDrawCircleHole(false);

        dataSet.setValueTextSize(0);
        dataSet.setDrawHorizontalHighlightIndicator(false);
        dataSet.setDrawVerticalHighlightIndicator(false);
        dataSet.setFillDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.orange));

        LineDataSet dataSet1 = new LineDataSet(ent, "");
        dataSet1.setDrawFilled(false);
        dataSet1.setDrawCircles(false);
        dataSet1.setDrawCircleHole(false);

        dataSet1.setValueTextSize(0);
        dataSet1.setDrawHorizontalHighlightIndicator(false);
        dataSet1.setDrawVerticalHighlightIndicator(false);
//        dataSet1.setFillDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.darkblue));

        ArrayList<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
        dataSets.add(dataSet); // add the datasets
        dataSets.add(dataSet1);
        LineData lineData = new LineData(dataSets);

        chart.setData(lineData);
        chart.getAxisLeft().setDrawGridLines(false);
        chart.getXAxis().setDrawGridLines(false);
        chart.getAxisRight().setDrawGridLines(false);
        chart.getDescription().setEnabled(false);
        chart.getAxisLeft().setDrawLabels(false);
        chart.getAxisRight().setDrawLabels(false);
        chart.getXAxis().setDrawLabels(false);
        chart.getLegend().setEnabled(false);

        chart.getAxisLeft().setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                DecimalFormat mFormat;
                mFormat = new DecimalFormat("##.###");
                return mFormat.format(unScaleCbr(value));
            }

        });
        chart.getAxisLeft().setAxisMinimum(scaleCbr(1));
        chart.getAxisLeft().setAxisMaximum(scaleCbr(10000000));
        chart.getAxisLeft().setLabelCount(8, true);
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

    private float scaleCbr(double cbr) {
        return (float) (Math.log10(cbr));
    }

    private float unScaleCbr(double cbr) {
        double calcVal = Math.pow(10, cbr);
        return (float) (calcVal);
    }
}