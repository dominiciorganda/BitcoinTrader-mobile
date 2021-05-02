package com.example.bitcointrader.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.bitcointrader.Entities.Coin;
import com.example.bitcointrader.Entities.CoinTypes;
import com.example.bitcointrader.Entities.CommonUtils;
import com.example.bitcointrader.Entities.WalletAdapter;
import com.example.bitcointrader.Entities.WalletCoin;
import com.example.bitcointrader.Fragments.Loading;
import com.example.bitcointrader.R;
import com.example.bitcointrader.Request.IRequestCallBack;
import com.example.bitcointrader.Request.RequestRetriever;
import com.example.bitcointrader.util.Urls;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class WalletActivity extends AppCompatActivity {
    private RequestRetriever requestRetriever = new RequestRetriever();
    private List<WalletCoin> walletCoins = new ArrayList<>();
    private Loading loading;
    private View loadingScreen;
    private View layout;
    private ListView listView;
    private Timer timer = new Timer();
    private TimerTask doAsynchronousTask;
    private WalletAdapter adapter;
    private TextView money;
    private TextView user;
    private LinearLayout buy, sell, coinlist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet);

//        getActualValue();
//        refreshWallet();
        setVisibilities();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                loading.disableLoadingScreen();
                listView.setVisibility(View.VISIBLE);
                layout.setVisibility(View.VISIBLE);
                adapter = new WalletAdapter(getApplicationContext(),
                        R.layout.listviewentity, walletCoins);
//                System.out.println(walletCoins.size());
                listView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                user.setText("@" + CommonUtils.getPrefString(getApplicationContext(), "username"));

            }
        }, 2000);

        coinlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(WalletActivity.this, CoinListActivity.class));
            }
        });

        buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(WalletActivity.this, BuyActivity.class));
            }
        });

        sell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(WalletActivity.this, SellActivity.class);
                intent.putParcelableArrayListExtra("walletCoins", (ArrayList<? extends Parcelable>) walletCoins);
                startActivity(intent);
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                WalletCoin walletCoin = (WalletCoin) adapterView.getItemAtPosition(i);
                CoinTypes coinTypes = walletCoin.getCoinName();
                Intent intent = new Intent(WalletActivity.this, TransactionActivity.class);
                intent.putExtra("coinName", coinTypes);
                startActivity(intent);
            }
        });

    }

    public void refreshWallet() {
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
        requestRetriever.getWallet(Urls.WALLET + "/getWallet", this.getApplicationContext(), new IRequestCallBack<List<WalletCoin>>() {
            @Override
            public void onSuccess(List<WalletCoin> wallet) {
//                walletCoins.addAll(wallet);
                runOnUiThread(new Runnable() {
                    public void run() {
                        if (adapter != null)
                            adapter.notifyDataSetChanged();
                        walletCoins.clear();
                        walletCoins.addAll(wallet);
//                        System.out.println(walletCoins);
                        double sum = 0;
                        for (WalletCoin walletCoin : walletCoins)
                            sum += walletCoin.getValue();
                        money.setText(String.format(Locale.US, "%.2f", sum) + " $");
                    }
                });
            }
        });
    }

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
        getActualValue();
        refreshWallet();


    }

    public void setVisibilities() {
        loadingScreen = findViewById(R.id.fragment_loading_screen);
        setLoadingScreen();
        listView = (ListView) findViewById(R.id.listview);
        money = findViewById(R.id.money);
        user = findViewById(R.id.user);
        coinlist = findViewById(R.id.coinlist);
        buy = findViewById(R.id.buy);
        sell = findViewById(R.id.sell);
        listView.setVisibility(View.GONE);
        layout = findViewById(R.id.layout);
        layout.setVisibility(View.GONE);
    }


    public void setLoadingScreen() {
        Bundle bundle = new Bundle();
        loading = new Loading();
        loading.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_loading_screen, loading).commit();
    }
}