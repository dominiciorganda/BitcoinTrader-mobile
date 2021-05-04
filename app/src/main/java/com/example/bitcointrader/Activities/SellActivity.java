package com.example.bitcointrader.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bitcointrader.Entities.Coin;
import com.example.bitcointrader.Entities.CoinTypes;
import com.example.bitcointrader.Entities.CommonUtils;
import com.example.bitcointrader.Entities.WalletCoin;
import com.example.bitcointrader.R;
import com.example.bitcointrader.Request.IRequestCallBack;
import com.example.bitcointrader.Request.RequestRetriever;
import com.example.bitcointrader.util.Urls;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class SellActivity extends AppCompatActivity {
    private LinearLayout chooseCoin;
    private TextView currency;
    private List<WalletCoin> coins;
    private WalletCoin actualWalletCoin;
    private CoinTypes coinType;
    private RequestRetriever requestRetriever = new RequestRetriever();
    private Coin coin = new Coin("", 0);
    private TextView sellActualPrice;
    private ImageView image;
    private TextView name;
    private TextView amount;
    private EditText sellCoinAmount;
    private TextView sellUsdAmount;
    private LinearLayout sellButton;
    private LinearLayout availableLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sell);

        chooseCoin = findViewById(R.id.chooseSellCoin);
        currency = findViewById(R.id.currency);
        sellActualPrice = findViewById(R.id.actualSellPrice);
        coins = getIntent().getParcelableArrayListExtra("walletCoins");
        image = findViewById(R.id.sellcoinImage);
        name = findViewById(R.id.sellcoinName);
        amount = findViewById(R.id.amountCoins);
        sellCoinAmount = findViewById(R.id.sellCoinAmount);
        sellUsdAmount = findViewById(R.id.sellUsdAmount);
        sellButton = findViewById(R.id.sellButton);
        availableLayout = findViewById(R.id.availableLayout);
        sellCoinAmount.setEnabled(false);
        sellButton.setVisibility(View.GONE);
        availableLayout.setVisibility(View.GONE);

        chooseCoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<String> coinTypes = new ArrayList<>();
                for (WalletCoin walletCoin : coins)
                    coinTypes.add(walletCoin.getCoinName().toString());
                Intent intent = new Intent(SellActivity.this, ChooseCoinActivity.class);
                intent.putStringArrayListExtra("coins", (ArrayList<String>) coinTypes);
                startActivityForResult(intent, 1);
            }
        });

        System.out.println(coins);


    }

    public void setEditable() {
        sellCoinAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                    sellCoinAmount.setTextColor(Color.BLACK);
                if (charSequence.length() > 0) {
                    if (!sellCoinAmount.getText().toString().equals(".")) {
                        double price = Double.parseDouble(sellCoinAmount.getText().toString());
                        price *= coin.getPrice();
                        sellUsdAmount.setText(String.format(Locale.US, "%.2f", price) + " US$");
                    }
                } else
                    sellUsdAmount.setText("0");
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
//                coinAmount.setTextColor(getColor(R.color.gri));
                sellCoinAmount.setText("0");
                sellUsdAmount.setText("0");
                setEditable();
                sellButton.setVisibility(View.VISIBLE);
                availableLayout.setVisibility(View.VISIBLE);

                System.out.println(data.getSerializableExtra("coin"));
                coinType = (CoinTypes) data.getSerializableExtra("coin");
                name.setText(CoinTypes.getName(coinType));
                image.setBackgroundResource(CoinTypes.getDrawable(coinType));
                currency.setText(CoinTypes.getShortcut(coinType));
                currency.setTextSize(25);
                sellCoinAmount.setEnabled(true);

                for (WalletCoin walletCoin : coins)
                    if (walletCoin.getCoinName() == coinType) {
                        actualWalletCoin = walletCoin;
                        break;
                    }
                setAmountLayout();

                String url = CoinTypes.getCoinUrl(coinType);
                if (!url.equals("NONE"))
                    requestRetriever.getCoin(url + "/getActual", getApplicationContext(), new IRequestCallBack() {
                        @Override
                        public void onSuccess(Object resultCoin) {
                            coin = (Coin) resultCoin;
                            sellActualPrice.setText(String.format(Locale.US, "%.2f", coin.getPrice()) + " USD");
                        }
                    });

                sellButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (!sellCoinAmount.getText().toString().equals("0") && !sellCoinAmount.getText().toString().equals("")
                                && !sellUsdAmount.getText().toString().equals("0") && !sellUsdAmount.getText().toString().equals("0.00")
                                && !sellUsdAmount.getText().toString().equals("0.00 US$")) {
//                            System.out.println(coinType);
//                            System.out.println(sellCoinAmount.getText().toString());
//                            System.out.println(coin.getPrice());
//                            System.out.println(sellUsdAmount.getText().toString());

                            double sellAmount = Double.parseDouble(sellCoinAmount.getText().toString());


                            if (actualWalletCoin.getAmount() >= sellAmount) {
                                JSONObject body = new JSONObject();
                                try {
                                    body.put("coin", coinType.toString());
                                    body.put("amount", sellCoinAmount.getText().toString());
                                    body.put("actualPrice", coin.getPrice());
                                    String paidPrice = sellUsdAmount.getText().toString().substring(0, sellUsdAmount.getText().toString().length() - 4);
                                    body.put("paidPrice", paidPrice);
                                    body.put("type", "SELL");
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                requestRetriever.transaction(Urls.TRANSACTION, getApplicationContext(), new IRequestCallBack() {
                                    @Override
                                    public void onSuccess(Object response) {
//                                    System.out.println(response.toString());
                                        if (response.toString().equals("Transaction added")) {
                                            Toast.makeText(getApplicationContext(), CoinTypes.getName(coinType) + " sold", Toast.LENGTH_SHORT).show();
                                            actualWalletCoin.setAmount(actualWalletCoin.getAmount() - sellAmount);
                                            setAmountLayout();

                                            requestRetriever.getMoney(Urls.GETMONEY, getApplicationContext(), new IRequestCallBack() {
                                                @Override
                                                public void onSuccess(Object coin) {
//                                                    System.out.println(CommonUtils.getPrefString(getApplicationContext(),"money"));
                                                }
                                            });
                                        } else
                                            Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();

                                    }

                                }, body);
                            } else
                                Toast.makeText(getApplicationContext(), "Selected amount is too large!", Toast.LENGTH_LONG).show();
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

    public void setAmountLayout() {
        if (actualWalletCoin.getAmount() == (int) actualWalletCoin.getAmount())
            amount.setText(String.format(Locale.US, "%d", (int) actualWalletCoin.getAmount()) + " " + CoinTypes.getShortcut(coinType));
        else
            amount.setText(String.format(Locale.US, "%.3f", actualWalletCoin.getAmount()) + " " + CoinTypes.getShortcut(coinType));
    }
}