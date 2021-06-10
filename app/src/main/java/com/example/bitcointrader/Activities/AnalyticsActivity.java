package com.example.bitcointrader.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.bitcointrader.Entities.ChartType;
import com.example.bitcointrader.Entities.Coin;
import com.example.bitcointrader.Entities.CommonUtils;
import com.example.bitcointrader.Entities.Popup;
import com.example.bitcointrader.Entities.WalletAdapter;
import com.example.bitcointrader.Fragments.Loading;
import com.example.bitcointrader.R;
import com.example.bitcointrader.Request.IRequestCallBack;
import com.example.bitcointrader.Request.RequestRetriever;
import com.example.bitcointrader.util.Urls;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.IMarker;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class AnalyticsActivity extends AppCompatActivity {
    private RequestRetriever requestRetriever = new RequestRetriever();
    private List<Coin> chartCoins = new ArrayList<>();
    private Loading loading;
    private View loadingScreen;
    private LinearLayout linearLayout;
    private TextView walletText, noMoney;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analytics);
        setVisibilities();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                getValues();

            }
        }, 1000);
    }

    public void getValues() {
        requestRetriever.getCoinList(Urls.ANALYTICS, this.getApplicationContext(), new IRequestCallBack<List<Coin>>() {
            @Override
            public void onSuccess(List<Coin> coins) {
                chartCoins = new ArrayList<>();
                chartCoins.addAll(coins);
                System.out.println(chartCoins);
                loading.disableLoadingScreen();
                linearLayout.setVisibility(View.VISIBLE);
                walletText.setText("@" + CommonUtils.getPrefString(getApplicationContext(), "username") + " wallet evolution");
                drawChart();
            }
        });
    }

    public void drawChart() {
        LineChart chart = (LineChart) findViewById(R.id.diagramm);
        chart.setVisibility(View.VISIBLE);

        if (chartCoins.isEmpty()) {
            chart.setVisibility(View.GONE);
            System.out.println("No money in wallet");
            noMoney.setVisibility(View.VISIBLE);
            return;
        }

        List<Entry> entries = new ArrayList<Entry>();
        int i = 0;
        for (Coin data : chartCoins) {
            i++;
            entries.add(new Entry(i, (float) data.getPrice()));
        }


        LineDataSet dataSet = new LineDataSet(entries, "");
        LineData lineData = new LineData(dataSet);
        dataSet.setDrawFilled(true);


        dataSet.setFillDrawable(ContextCompat.getDrawable(this, R.drawable.analytics));


//        dataSet.setFillDrawable(ContextCompat.getDrawable(getContext(), R.drawable.gradient));
        if (chartCoins.size() > 20) {
            dataSet.setDrawCircles(false);
            dataSet.setDrawCircleHole(false);
        } else {
            dataSet.setDrawCircles(true);
            dataSet.setDrawCircleHole(true);
        }

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
        chart.getAxisLeft().setTextSize(15f);
        chart.getAxisRight().setDrawLabels(false);
        chart.getXAxis().setDrawLabels(false);
        chart.getLegend().setEnabled(false);
        chart.invalidate(); // refresh

        IMarker marker;
        marker = new Popup(this, R.layout.popup, chartCoins, ChartType.LINEAR);


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

    public void setVisibilities() {
        loadingScreen = findViewById(R.id.fragment_loading_screen);
        setLoadingScreen();
        LineChart chart = (LineChart) findViewById(R.id.diagramm);
        chart.setVisibility(View.GONE);
        linearLayout = (LinearLayout) findViewById(R.id.layout1);
        linearLayout.setVisibility(View.GONE);
        walletText = (TextView) findViewById(R.id.walletText);
        noMoney = findViewById(R.id.nomoney);
        noMoney.setVisibility(View.GONE);
    }


    public void setLoadingScreen() {
        Bundle bundle = new Bundle();
        loading = new Loading();
        loading.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_loading_screen, loading).commit();
    }
}