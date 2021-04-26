package com.example.bitcointrader.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.bitcointrader.Entities.Coin;
import com.example.bitcointrader.Entities.CoinTypes;
import com.example.bitcointrader.R;
import com.example.bitcointrader.Request.IRequestCallBack;
import com.example.bitcointrader.Request.RequestRetriever;
import com.example.bitcointrader.util.Urls;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;

public class BuyActivity extends AppCompatActivity {
    private TextView chooseCoin;
    private ImageView image;
    private TextView name;
    private TextView buyActualPrice;
    private CoinTypes coinType;
    private RequestRetriever requestRetriever = new RequestRetriever();
    private Coin coin = new Coin("", 0);
    private EditText coinAmount;
    private EditText usdAmount;
    private TextView buyShortcut;
    private LinearLayout button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy);

        chooseCoin = findViewById(R.id.chooseCoin);
        image = findViewById(R.id.coinImage);
        name = findViewById(R.id.coinName);
        buyActualPrice = findViewById(R.id.actualBuyPrice);
        coinAmount = findViewById(R.id.coinAmount);
        usdAmount = findViewById(R.id.usdAmount);
        buyShortcut = findViewById(R.id.buyShortcut);
        button = findViewById(R.id.buyButton);
        button.setVisibility(View.GONE);

        chooseCoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(BuyActivity.this, ChooseCoinActivity.class), 1);
            }
        });

        if (coin.getDate().equals("")) {
            coinAmount.setEnabled(false);
            usdAmount.setEnabled(false);
        }


    }

    public void setEditable() {
        coinAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if (!usdAmount.hasFocus()) {
                    coinAmount.setTextColor(Color.BLACK);
                    if (charSequence.length() > 0) {
                        if (!coinAmount.getText().toString().equals(".")) {
                            double price = Double.parseDouble(coinAmount.getText().toString());
                            price *= coin.getPrice();
                            usdAmount.setText(String.format(Locale.US, "%.2f", price));
                        }
                    } else
                        usdAmount.setText("0");
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        usdAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!coinAmount.hasFocus()) {

                    if (charSequence.length() > 0) {
                        if(!usdAmount.getText().toString().equals(".")){
                            double price = Double.parseDouble(usdAmount.getText().toString());
                            price /= coin.getPrice();
                            coinAmount.setText(String.format(Locale.US, "%.2f", price));
                        }
                    } else
                        coinAmount.setText("0");
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                coinAmount.setTextColor(getColor(R.color.gri));
                coinAmount.setText("0");
                usdAmount.setText("0");
                coinAmount.setEnabled(true);
                usdAmount.setEnabled(true);
                setEditable();
                button.setVisibility(View.VISIBLE);

                System.out.println(data.getSerializableExtra("coin"));
                coinType = (CoinTypes) data.getSerializableExtra("coin");
                name.setText(CoinTypes.getName(coinType));
                image.setBackgroundResource(CoinTypes.getDrawable(coinType));
                buyShortcut.setText(CoinTypes.getShortcut(coinType));
                String url = CoinTypes.getCoinUrl(coinType);
                if (!url.equals("NONE"))
                    requestRetriever.getCoin(url + "/getActual", getApplicationContext(), new IRequestCallBack() {
                        @Override
                        public void onSuccess(Object resultCoin) {
                            coin = (Coin) resultCoin;
                            buyActualPrice.setText(String.format(Locale.US, "%.2f", coin.getPrice()) + " USD");
                        }
                    });

                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (!coinAmount.getText().toString().equals("0") && !coinAmount.getText().toString().equals("0.00")
                                && !usdAmount.getText().toString().equals("0") && !usdAmount.getText().toString().equals("0.00")) {
                            System.out.println(coinType);
                            System.out.println(coinAmount.getText().toString());
                            System.out.println(coin.getPrice());
                            System.out.println(usdAmount.getText().toString());

                            JSONObject body = new JSONObject();
                            try {
                                body.put("coin", coinType.toString());
                                body.put("amount", coinAmount.getText().toString());
                                body.put("actualPrice", coin.getPrice());
                                body.put("paidPrice", usdAmount.getText().toString());

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            requestRetriever.buy(Urls.BUY, getApplicationContext(), new IRequestCallBack() {
                                @Override
                                public void onSuccess(Object response) {
                                    System.out.println(response.toString());
                                }
                            }, body);
                        }
                    }
                });

            }
        }
    }


    @Override
    protected void onResume() {
        super.onResume();

    }
}