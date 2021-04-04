package com.example.bitcointrader.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.bitcointrader.Activities.PredictionActivity;
import com.example.bitcointrader.Entities.Coin;
import com.example.bitcointrader.R;
import com.example.bitcointrader.util.CoinUrls;

import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Stats#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Stats extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String MAX = "maxCoin";
    private static final String ACTUAL = "actualCoin";
    private static final String ANUALMAX = "anualMax";
    private static final String ANUALMIN = "anualMin";
    private static final String URL = "URL";


    // TODO: Rename and change types of parameters
    private Coin maxCoin, anualMax, anualMin, actualCoin;
    private Button prediction;
    private String url;

    public Stats() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment Stats.
     */
    // TODO: Rename and change types and number of parameters
    public static Stats newInstance(Coin maxCoin, Coin anualMax, Coin anualMin, Coin actualCoin) {
        Stats fragment = new Stats();
        Bundle args = new Bundle();
        args.putParcelable(MAX, maxCoin);
        args.putParcelable(ANUALMAX, anualMax);
        args.putParcelable(ANUALMIN, anualMin);
        args.putParcelable(ACTUAL, actualCoin);


        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_stats, container, false);
        if (getArguments() != null) {
            maxCoin = getArguments().getParcelable("MAX");
            anualMax = getArguments().getParcelable("ANUALMAX");
            anualMin = getArguments().getParcelable("ANUALMIN");
            actualCoin = getArguments().getParcelable("ACTUAL");
            url = getArguments().getString("URL");
        } else
            System.out.println("ok");
        prediction = view.findViewById(R.id.prediction);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (maxCoin != null && anualMax != null && anualMin != null && actualCoin != null) {
            fillStats();
        } else
            System.out.println("Error");

        if (prediction != null && url != null) {
            prediction.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getActivity(), PredictionActivity.class);
                    intent.putExtra("url", url);
                    startActivity(intent);
                    System.out.println("hello");
                }
            });
        }

        if (url != null) {
            loadimage();
        }

    }

    public void fillStats() {
        TextView maxPrice = (TextView) getView().findViewById(R.id.maxPrice);
        maxPrice.setText(String.format(Locale.US, "%.2f", maxCoin.getPrice()));

        TextView anualMaxPrice = (TextView) getView().findViewById(R.id.anualMax);
        anualMaxPrice.setText(String.format(Locale.US, "%.2f", anualMax.getPrice()));

        TextView anualMinPrice = (TextView) getView().findViewById(R.id.anualMin);
        anualMinPrice.setText(String.format(Locale.US, "%.2f", anualMin.getPrice()));

        TextView actualPrice = (TextView) getView().findViewById(R.id.actualPrice);
        actualPrice.setText(String.format(Locale.US, "%.2f", actualCoin.getPrice()));
    }


    public void loadimage() {
        ImageView photo = (ImageView) getView().findViewById(R.id.image);
        TextView name = (TextView) getView().findViewById(R.id.text);

        CoinUrls coinUrls = CoinUrls.find(url);

        switch (coinUrls) {
            case BITCOIN:
                photo.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.btc));
                name.setText("BITCOIN");
                break;
            case ETHEREUM:
                photo.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.eth));
                name.setText("ETHEREUM");
                break;
            case LITECOIN:
                photo.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.ltc));
                name.setText("LITECOIN");
                break;
            case FILECOIN:
                photo.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.fil));
                name.setText("FILECOIN");
                break;
            case ELROND:
                photo.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.erd));
                name.setText("ELROND");
                break;
            case BINANCECOIN:
                photo.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.bnb));
                name.setText("BINANCE COIN");
                break;
            case BITCOINCASH:
                photo.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.bch));
                name.setText("BITCOIN CASH");
                break;
            case DASH:
                photo.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.dash));
                name.setText("DASH");
                break;
            case DOGECOIN:
                photo.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.doge));
                name.setText("DOGECOIN");
                break;
            default:
                break;
        }
    }

}