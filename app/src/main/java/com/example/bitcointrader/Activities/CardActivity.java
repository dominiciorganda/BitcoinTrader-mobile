package com.example.bitcointrader.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.craftman.cardform.Card;
import com.craftman.cardform.CardForm;
import com.craftman.cardform.OnPayBtnClickListner;
import com.example.bitcointrader.Entities.CoinTypes;
import com.example.bitcointrader.Entities.CommonUtils;
import com.example.bitcointrader.R;
import com.example.bitcointrader.Request.IRequestCallBack;
import com.example.bitcointrader.Request.RequestRetriever;
import com.example.bitcointrader.util.Urls;
import com.google.android.material.slider.LabelFormatter;
import com.google.android.material.slider.Slider;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.util.Currency;

public class CardActivity extends AppCompatActivity {
    private TextView amount;
    private CardForm cardForm;
    private Button payButton;
    private AlertDialog dialog;
    private RequestRetriever requestRetriever = new RequestRetriever();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card);

        cardForm = (CardForm) findViewById(R.id.card_form);
        amount = (TextView) (cardForm.getRootView().findViewById(R.id.payment_amount));
        payButton = (Button) (cardForm.getRootView().findViewById(R.id.btn_pay));


        cardForm.setPayBtnClickListner(new OnPayBtnClickListner() {
            @Override
            public void onClick(Card card) {
                //Your code here!! use card.getXXX() for get any card property
                //for instance card.getName();
                AlertDialog.Builder builder = new AlertDialog.Builder(cardForm.getContext(), R.style.PreferenceDialogLight);

                builder.setTitle("Confirm Payment");
                builder.setMessage("Add  " + amount.getText().toString() + "$ to wallet?");
                builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String price = amount.getText().toString().replace("$", "");
                        JSONObject body = new JSONObject();
                        try {
                            body.put("amount", price);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        requestRetriever.transaction(Urls.ADDFUNDS, getApplicationContext(), new IRequestCallBack() {
                            @Override
                            public void onSuccess(Object response) {
//                                    System.out.println(response.toString());
                                if (response.toString().equals("Transaction added")) {
                                    Toast.makeText(getApplicationContext(), amount.getText().toString() + " added to wallet", Toast.LENGTH_SHORT).show();

                                    requestRetriever.getMoney(Urls.GETMONEY, getApplicationContext(), new IRequestCallBack() {
                                        @Override
                                        public void onSuccess(Object response) {

                                        }
                                    });

                                } else
                                    Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
                            }

                        }, body);
                    }
                });

                dialog = builder.create();
                dialog.show();


            }
        });
        showPopup();
    }


    public void showPopup() {

        // Create an alert builder
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select Topup Amount");

        // set the custom layout
        final View customLayout = getLayoutInflater().inflate(R.layout.layout_money_popup, null);
        builder.setView(customLayout);

        Slider slider = customLayout.findViewById(R.id.slider);
        EditText price = customLayout.findViewById(R.id.amountPrice);

        slider.setLabelFormatter(new LabelFormatter() {
            @NonNull
            @Override
            public String getFormattedValue(float value) {
                NumberFormat numberFormat = NumberFormat.getCurrencyInstance();
                numberFormat.setMaximumFractionDigits(0);
                numberFormat.setCurrency(Currency.getInstance("USD"));
                return numberFormat.format(value);
            }
        });

        price.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                double value = 0;
                if (!charSequence.toString().equals(""))
                    value = Double.parseDouble(charSequence.toString());
                if (value >= 10 && value <= 10000) {
                    slider.setValue((float) value);
                    dialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(true);
                } else
                    dialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        slider.addOnSliderTouchListener(new Slider.OnSliderTouchListener() {
            @Override
            public void onStartTrackingTouch(@NonNull Slider slider) {

            }

            @Override
            public void onStopTrackingTouch(@NonNull Slider slider) {
                int value = (int) slider.getValue();
                price.setText("" + value);
            }
        });

        // add a button
        builder.setPositiveButton("Add Card", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                double value = 0;
                if (!price.getText().toString().equals(""))
                    value = Double.parseDouble(price.getText().toString());
                if (value >= 10 && value <= 10000) {
                    amount.setText("$" + price.getText() + ".00");
                    cardForm.setAmount(amount.getText().toString());
                    payButton.setText("Payer " + amount.getText().toString());
                } else {
                    Toast.makeText(getApplicationContext(), "Selected amount is not valid", Toast.LENGTH_LONG).show();
                    dialog.dismiss();
                }
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                System.out.println("hello");
            }
        });

        // create and show
        // the alert dialog
        dialog = builder.create();
        dialog.show();
    }
}