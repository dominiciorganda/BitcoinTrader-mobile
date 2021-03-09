package com.example.bitcointrader.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

import com.example.bitcointrader.Entities.Coin;
import com.example.bitcointrader.Fragments.Chart;
import com.example.bitcointrader.R;
import com.example.bitcointrader.Request.RequestCallBack;
import com.example.bitcointrader.Request.RequestRetriever;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


public class BitcoinActivity extends AppCompatActivity {

    private RequestRetriever requestRetriever = new RequestRetriever();
    private String url = "http://192.168.56.1:8081/CoinTrader/bitcoin";
    private List<Coin> chartCoins = new ArrayList<>();
    private Coin anualMin = new Coin();
    private Coin anualMax = new Coin();
    private Coin max = new Coin();

    @Override
    protected void onPause() {
        super.onPause();
        getValues();
    }

    @Override
    protected void onResume() {
        super.onResume();
        drawChart();
        fillStats();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bitcoin);
        onPause();
        refreshActualValue();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                onResume();
            }
        }, 500);
    }

    public void refreshActualValue() {
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
        requestRetriever.getCoin(url + "/getActual", this.getApplicationContext(), new RequestCallBack<Coin>() {
            @Override
            public void onSuccess(Coin coin) {
                Coin actual = coin;
                TextView actualPrice = (TextView) findViewById(R.id.actualPrice);
                actualPrice.setText(actual.showPrice());
            }
        });
    }

    private void getValues() {
        requestRetriever.getCoin(url + "/getMax", this.getApplicationContext(), new RequestCallBack<Coin>() {
            @Override
            public void onSuccess(Coin coin) {
                max = coin;
            }
        });
        requestRetriever.getCoin(url + "/getAnualMax", this.getApplicationContext(), new RequestCallBack<Coin>() {
            @Override
            public void onSuccess(Coin coin) {
                anualMax = coin;
            }
        });
        requestRetriever.getCoin(url + "/getAnualMin", this.getApplicationContext(), new RequestCallBack<Coin>() {
            @Override
            public void onSuccess(Coin coin) {
                anualMin = coin;
            }
        });
        requestRetriever.getCoinList(url + "/getLastMonth", this.getApplicationContext(), new RequestCallBack<List<Coin>>() {
            @Override
            public void onSuccess(List<Coin> coins) {
                chartCoins = new ArrayList<>();
                chartCoins.addAll(coins);
            }
        });
    }

    public void fillStats() {
        TextView maxPrice = (TextView) findViewById(R.id.maxPrice);
        maxPrice.setText(max.showPrice());

        TextView anualMaxPrice = (TextView) findViewById(R.id.anualMax);
        anualMaxPrice.setText(anualMax.showPrice());

        TextView anualMinPrice = (TextView) findViewById(R.id.anualMin);
        anualMinPrice.setText(anualMin.showPrice());
    }

    public void drawChart() {
        ArrayList<Coin> chartData = new ArrayList<Coin>();
        chartData.addAll(chartCoins);

        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("CHART_COINS", chartData);
        Chart chart = new Chart();
        chart.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_view, chart).commit();

    }
}