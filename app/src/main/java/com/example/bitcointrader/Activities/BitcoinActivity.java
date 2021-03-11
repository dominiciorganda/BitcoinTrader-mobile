package com.example.bitcointrader.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;

import com.example.bitcointrader.Entities.Coin;
import com.example.bitcointrader.Fragments.Chart;
import com.example.bitcointrader.Fragments.ChartDays;
import com.example.bitcointrader.Fragments.IFragmentToActivity;
import com.example.bitcointrader.Fragments.Loading;
import com.example.bitcointrader.Fragments.Stats;
import com.example.bitcointrader.R;
import com.example.bitcointrader.Request.IRequestCallBack;
import com.example.bitcointrader.Request.RequestRetriever;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


public class BitcoinActivity extends AppCompatActivity implements ICoinActivity, IFragmentToActivity {

    private RequestRetriever requestRetriever = new RequestRetriever();
    private String url = "http://192.168.56.1:8081/CoinTrader/bitcoin";
    private List<Coin> chartCoins = new ArrayList<>();
    private Coin anualMin = new Coin();
    private Coin anualMax = new Coin();
    private Coin max = new Coin();
    private Coin actual = new Coin();
    private Timer timer = new Timer();
    private TimerTask doAsynchronousTask;
    private View loadingScreen;
    private View chart;
    private View stats;
    private Loading loading;
    private View chartDays;

    @Override
    protected void onPause() {
        super.onPause();
        doAsynchronousTask.cancel();
        timer.cancel();
        timer.purge();
    }

    @Override
    protected void onResume() {
        super.onResume();
        drawChart();
        fillStats();
        setChartDays();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bitcoin);
        setVisibilities();
        getValues();
        refreshActualValue();


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                onResume();
                loading.disableLoadingScreen();
                stats.setVisibility(View.VISIBLE);
                chart.setVisibility(View.VISIBLE);
                chartDays.setVisibility(View.VISIBLE);

            }
        }, 1000);

        setChartDays();
    }

    public void refreshActualValue() {
        final Handler handler = new Handler();
        timer = new Timer();
        doAsynchronousTask = new TimerTask() {
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

    public void getActualValue() {
        requestRetriever.getCoin(url + "/getActual", this.getApplicationContext(), new IRequestCallBack<Coin>() {
            @Override
            public void onSuccess(Coin coin) {
                actual = coin;
                TextView actualPrice = (TextView) findViewById(R.id.actualPrice);
                actualPrice.setText(actual.showPrice());
            }
        });
    }

    public void getValues() {
        requestRetriever.getCoin(url + "/getMax", this.getApplicationContext(), new IRequestCallBack<Coin>() {
            @Override
            public void onSuccess(Coin coin) {
                max = coin;
            }
        });
        requestRetriever.getCoin(url + "/getAnualMax", this.getApplicationContext(), new IRequestCallBack<Coin>() {
            @Override
            public void onSuccess(Coin coin) {
                anualMax = coin;
            }
        });
        requestRetriever.getCoin(url + "/getAnualMin", this.getApplicationContext(), new IRequestCallBack<Coin>() {
            @Override
            public void onSuccess(Coin coin) {
                anualMin = coin;
            }
        });
        requestRetriever.getCoinList(url + "/getLastMonth", this.getApplicationContext(), new IRequestCallBack<List<Coin>>() {
            @Override
            public void onSuccess(List<Coin> coins) {
                chartCoins = new ArrayList<>();
                chartCoins.addAll(coins);
            }
        });
    }

    public void setVisibilities() {
        loadingScreen = findViewById(R.id.fragment_loading_screen);
        setLoadingScreen();
        chart = findViewById(R.id.fragment_chart);
        stats = findViewById(R.id.fragment_stats);
        chartDays = findViewById(R.id.fragment_daysnumber);
        chart.setVisibility(View.GONE);
        stats.setVisibility(View.GONE);
        chartDays.setVisibility(View.GONE);
    }

    public void setLoadingScreen() {
        Bundle bundle = new Bundle();
        loading = new Loading();
        loading.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_loading_screen, loading).commit();
    }

    public void fillStats() {
        Bundle bundle = new Bundle();
        bundle.putParcelable("MAX", max);
        bundle.putParcelable("ANUALMAX", anualMax);
        bundle.putParcelable("ANUALMIN", anualMin);
        bundle.putParcelable("ACTUAL", actual);
        Stats stats = new Stats();
        stats.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_stats, stats).commit();

    }

    public void drawChart() {
        ArrayList<Coin> chartData = new ArrayList<Coin>();
        chartData.addAll(chartCoins);

        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("CHART_COINS", chartData);
        Chart chart = new Chart();
        chart.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_chart, chart).commit();

    }

    public void setChartDays() {
        Bundle bundle = new Bundle();
        ChartDays chartDays = new ChartDays();
        chartDays.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_daysnumber, chartDays).commit();
    }

    @Override
    public void communicate(String data) {
        requestRetriever.getCoinList(url + "/getLastX/" + data, getApplicationContext(), new IRequestCallBack<List<Coin>>() {
            @Override
            public void onSuccess(List<Coin> coins) {
                chartCoins = new ArrayList<>();
                chartCoins.addAll(coins);
                drawChart();
            }
        });
    }

}