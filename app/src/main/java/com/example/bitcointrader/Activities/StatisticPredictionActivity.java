package com.example.bitcointrader.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.bitcointrader.Entities.ChartType;
import com.example.bitcointrader.Entities.Coin;
import com.example.bitcointrader.Entities.Popup;
import com.example.bitcointrader.Fragments.Loading;
import com.example.bitcointrader.R;
import com.example.bitcointrader.Request.IRequestCallBack;
import com.example.bitcointrader.Request.RequestRetriever;
import com.example.bitcointrader.util.CoinUrls;
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
    private Loading loading;
    private View chart;
    private View loadingScreen;
    private LinearLayout linearLayout;
    private TextView textView, recommand;
    private String coin;
    private TextView information;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistic_prediction);

        initializeData();
        setVisibilities();

        String name = CoinUrls.find(url).toString().toLowerCase();
        coin = name.substring(0, 1).toUpperCase() + name.substring(1);

        getValues();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                drawChart();
                loading.disableLoadingScreen();
                chart.setVisibility(View.VISIBLE);
                linearLayout.setVisibility(View.VISIBLE);
                textView.setText("" + coin + " Price Trendline");
                information.setVisibility(View.VISIBLE);
                recommand.setVisibility(View.VISIBLE);
            }
        }, 1000);
    }

    private void setVisibilities() {
        loadingScreen = findViewById(R.id.loading_screen);
        setLoadingScreen();
        chart = findViewById(R.id.chart);
        chart.setVisibility(View.GONE);
        linearLayout = findViewById(R.id.layout2);
        linearLayout.setVisibility(View.GONE);
        textView = findViewById(R.id.predictionText);
        information = findViewById(R.id.information);
        information.setVisibility(View.GONE);
        recommand = findViewById(R.id.recomandation);
        recommand.setVisibility(View.GONE);

    }

    public void setLoadingScreen() {
        Bundle bundle = new Bundle();
        loading = new Loading();
        loading.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().replace(R.id.loading_screen, loading).commit();
    }

    public void initializeData() {
        intent = getIntent();
        url = intent.getStringExtra("url");
    }

    public void getValues() {
        requestRetriever.getCoinList(url + "/getAll", this.getApplicationContext(), new IRequestCallBack<List<Coin>>() {
            @Override
            public void onSuccess(List<Coin> coins) {
                System.out.println(coins);
                chartCoins = new ArrayList<>();
                chartCoins.addAll(coins);
                drawChart();
            }
        });
    }

    private float linearRegressionBTC(int x) {
        //constants
//        a = 0.28587743 si b = 1.3548479
        float a = (float) 0.28587743;
        float b = (float) 1.3548479;

        // Functie: 10 ^ (a*ln(x) - b)
        float ln = (float) Math.log(x);
        float result = a * ln - b;

        return (float) Math.pow(10, result);
    }

    private float linearRegressionETH(int x) {
        //constants
//        a = 0.28587743 si b = 1.3548479
        float a = (float) 0.14124555;
        float b = (float) 0.25059877;

        // Functie: 10 ^ (a*ln(x) - b)
        float ln = (float) Math.log(x);
        float result = a * ln - b;

        return (float) Math.pow(10, result);
    }

    private void drawChart() {
        LineChart chart = (LineChart) findViewById(R.id.chart);

        List<Entry> entries = new ArrayList<Entry>();
        List<Entry> ent = new ArrayList<Entry>();
        int i = 0;
        CoinUrls coinUrls = CoinUrls.find(url);

        for (Coin data : chartCoins) {
            i++;
            float result = 0;
            entries.add(new Entry(i, scaleCbr(data.getPrice())));

            if (coinUrls != null) {
                if (coinUrls == CoinUrls.BITCOIN)
                    result = linearRegressionBTC(i);
                else if (coinUrls == CoinUrls.ETHEREUM)
                    result = linearRegressionETH(i);
            }
            ent.add(new Entry(i, result));
        }

        float result = 0;
        if (coinUrls != null) {
            if (coinUrls == CoinUrls.BITCOIN)
                result = linearRegressionBTC(chartCoins.size());
            else if (coinUrls == CoinUrls.ETHEREUM)
                result = linearRegressionETH(chartCoins.size());
        }
//        System.out.println(result);
//        System.out.println(scaleCbr(chartCoins.get(chartCoins.size() - 1).getPrice()));
        if (result > scaleCbr(chartCoins.get(chartCoins.size() - 1).getPrice())) {
            information.setTextColor(Color.GREEN);
            information.setText("Invest in " + coin);
        } else {
            information.setTextColor(Color.RED);
            information.setText("Don't invest in " + coin);
        }

//        System.out.println(chartCoins);

        LineDataSet dataSet = new LineDataSet(entries, "");
        dataSet.setDrawFilled(true);
        dataSet.setDrawCircles(false);
        dataSet.setDrawCircleHole(false);

        dataSet.setValueTextSize(0);
        dataSet.setDrawHorizontalHighlightIndicator(false);
        dataSet.setDrawVerticalHighlightIndicator(false);


        switch (coinUrls) {
            case BITCOIN:
            case DOGECOIN:
            case BINANCECOIN:
                dataSet.setFillDrawable(ContextCompat.getDrawable(this, R.drawable.orange));
                break;
            case ETHEREUM:
            case LITECOIN:
            case FILECOIN:
                dataSet.setFillDrawable(ContextCompat.getDrawable(this, R.drawable.lightblue));
                break;
            case ELROND:
                dataSet.setFillDrawable(ContextCompat.getDrawable(this, R.drawable.darkblue));
                break;
            case BITCOINCASH:
                dataSet.setFillDrawable(ContextCompat.getDrawable(this, R.drawable.lightgreen));
                break;
            default:
                dataSet.setFillDrawable(ContextCompat.getDrawable(this, R.drawable.gradient));
        }

        LineDataSet dataSet1 = new LineDataSet(ent, "");
        dataSet1.setDrawFilled(false);
        dataSet1.setDrawCircles(false);
        dataSet1.setDrawCircleHole(false);
        dataSet1.setLineWidth(5);
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
        chart.getAxisLeft().setAxisMaximum(scaleCbr(100000));
        chart.getAxisLeft().setLabelCount(6, true);

        Legend legend = chart.getLegend();
        LegendEntry l1 = new LegendEntry(coin + "s all prices", Legend.LegendForm.SQUARE, 15f, 2f, null, Color.BLUE);
        if (coin.equals("Bitcoin"))
            l1.formColor = Color.parseColor("#fd8500");
        else
            l1.formColor = Color.parseColor("#4d88d3");
        LegendEntry l2 = new LegendEntry("Trendline", Legend.LegendForm.SQUARE, 15f, 10f, null, Color.parseColor("#99ecff"));

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

    private float scaleCbr(double cbr) {
        return (float) (Math.log(cbr));
    }

    private float unScaleCbr(double cbr) {
        double calcVal = Math.exp(cbr);
        return (float) (calcVal);
    }
}