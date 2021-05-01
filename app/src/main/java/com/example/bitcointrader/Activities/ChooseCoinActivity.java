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
import com.example.bitcointrader.R;

import java.util.ArrayList;
import java.util.List;

public class ChooseCoinActivity extends AppCompatActivity {
    private CoinAdapter adapter;
    private ListView listView;
    private CoinTypes[] types;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_coin);

        if (getIntent().getStringArrayListExtra("coins") == null)
            adapter = new CoinAdapter(getApplicationContext(), R.layout.listviewcoin, CoinTypes.values());
        else {
//            System.out.println(getIntent().getStringArrayListExtra("coins"));
            ArrayList<String> values = getIntent().getStringArrayListExtra("coins");
            types = new CoinTypes[values.size()];
            int i = 0;
            for (String value : values) {
                types[i] = CoinTypes.getCoinType(value);
                i++;
            }
            adapter = new CoinAdapter(getApplicationContext(), R.layout.listviewcoin, types);
        }


        listView = (ListView) findViewById(R.id.listviewCoins);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                if (getIntent().getStringArrayListExtra("coins") == null) {
                    intent.putExtra("coin", CoinTypes.values()[position]);
                }
                else {
                    intent.putExtra("coin", types[position]);
                }
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }

}