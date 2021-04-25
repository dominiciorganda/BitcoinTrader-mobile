package com.example.bitcointrader.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.bitcointrader.Entities.Coin;
import com.example.bitcointrader.Entities.CoinAdapter;
import com.example.bitcointrader.Entities.CoinTypes;
import com.example.bitcointrader.Entities.WalletAdapter;
import com.example.bitcointrader.R;

public class ChooseCoinActivity extends AppCompatActivity {
    private CoinAdapter adapter;
    private ListView listView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_coin);


        adapter = new CoinAdapter(getApplicationContext(), R.layout.listviewcoin, CoinTypes.values());
        listView = (ListView) findViewById(R.id.listviewCoins);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                intent.putExtra("coin", CoinTypes.values()[position]);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }

}