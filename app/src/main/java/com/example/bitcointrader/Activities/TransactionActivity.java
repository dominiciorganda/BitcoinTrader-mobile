package com.example.bitcointrader.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.bitcointrader.Entities.CoinTypes;
import com.example.bitcointrader.Entities.CommonUtils;
import com.example.bitcointrader.Entities.Transaction;
import com.example.bitcointrader.Entities.TransactionAdapter;
import com.example.bitcointrader.Entities.WalletAdapter;
import com.example.bitcointrader.Fragments.Loading;
import com.example.bitcointrader.R;
import com.example.bitcointrader.Request.IRequestCallBack;
import com.example.bitcointrader.Request.RequestRetriever;
import com.example.bitcointrader.util.Urls;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TransactionActivity extends AppCompatActivity {
    private CoinTypes coinTypes;
    private RequestRetriever requestRetriever = new RequestRetriever();
    private List<Transaction> transactions = new ArrayList<>();
    private TextView coinName;
    private ImageView coinImage;
    private ListView listView;
    private TransactionAdapter adapter;
    private View loadingScreen;
    private RelativeLayout relativeLayout;
    private Loading loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction);

        coinTypes = (CoinTypes) getIntent().getSerializableExtra("coinName");
        setVisibilities();
        getTransactions();



        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                for (Transaction transaction : transactions)
                    transaction.setTransactionDate(getDate(transaction.getTransactionDate()));
//                System.out.println(transactions);
                loading.disableLoadingScreen();
                listView.setVisibility(View.VISIBLE);
                relativeLayout.setVisibility(View.VISIBLE);
                adapter = new TransactionAdapter(getApplicationContext(),
                        R.layout.transactionlistview, transactions);
////                System.out.println(walletCoins.size());
                listView.setAdapter(adapter);
                adapter.notifyDataSetChanged();

            }
        }, 2000);


    }

    public String getDate(String input) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        Date date = null;
        try {
            date = dateFormat.parse(input);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        dateFormat.applyPattern("MMM dd, h:mm a");
        System.out.println(input);
        System.out.println(dateFormat.format(date));
        return dateFormat.format(date);
    }

    public void getTransactions() {
        requestRetriever.getTransactions(Urls.AllTRANSACTIONS, getApplicationContext(), new IRequestCallBack<List<Transaction>>() {
            @Override
            public void onSuccess(List<Transaction> transactionList) {
                System.out.println(transactionList.size());
                transactions.clear();
                for (Transaction transaction : transactionList)
                    if (transaction.getCoin() == coinTypes)
                        transactions.add(transaction);
                System.out.println(transactions);
            }
        });
    }

    public void setVisibilities() {
        loadingScreen = findViewById(R.id.fragment_loading_screen);
        setLoadingScreen();
        coinImage = findViewById(R.id.transacactionCoinImage);
        coinName = findViewById(R.id.transactionCoinName);
        listView = findViewById(R.id.transactionlistview);
        coinImage.setBackgroundResource(CoinTypes.getDrawable(coinTypes));
        coinName.setText(CoinTypes.getName(coinTypes));
        relativeLayout = findViewById(R.id.layout);
        listView.setVisibility(View.GONE);
        relativeLayout.setVisibility(View.GONE);
    }

    public void setLoadingScreen() {
        Bundle bundle = new Bundle();
        loading = new Loading();
        loading.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_loading_screen, loading).commit();
    }
}