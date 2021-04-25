package com.example.bitcointrader.Entities;

import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.bitcointrader.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class WalletAdapter extends ArrayAdapter<WalletCoin> {
    private ArrayList<WalletCoin> dataSet = new ArrayList<>();
    Context context;

    public WalletAdapter(@NonNull Context context, int resource, @NonNull List<WalletCoin> objects) {
        super(context, resource, objects);
        this.dataSet.addAll(objects);
        this.context = context;

    }

    // View lookup cache
    private static class ViewHolder {
        TextView coinName;
        ImageView logo;
        TextView price;
        TextView amount;
        TextView value;
        ImageView trendingUp;
        ImageView trendingDown;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        WalletCoin walletCoin = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag
        if (convertView == null) {
            // If there's no view to re-use, inflate a brand new view for row
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.listviewentity, parent, false);
            viewHolder.coinName = (TextView) convertView.findViewById(R.id.name);
            viewHolder.logo = (ImageView) convertView.findViewById(R.id.logo);
            viewHolder.price = (TextView) convertView.findViewById(R.id.price);
            viewHolder.amount = (TextView) convertView.findViewById(R.id.amount);
            viewHolder.value = (TextView) convertView.findViewById(R.id.value);
            viewHolder.trendingUp = (ImageView) convertView.findViewById(R.id.trendingup);
            viewHolder.trendingDown = (ImageView) convertView.findViewById(R.id.trendingdown);
            // Cache the viewHolder object inside the fresh view
            convertView.setTag(viewHolder);
        } else {
            // View is being recycled, retrieve the viewHolder object from tag
            viewHolder = (ViewHolder) convertView.getTag();
        }
        // Populate the data from the data object via the viewHolder object
        // into the template view.
        viewHolder.coinName.setText(CoinTypes.getName(walletCoin.getCoinName()));
        viewHolder.logo.setBackgroundResource(CoinTypes.getDrawable(walletCoin.getCoinName()));
        viewHolder.price.setText(String.format(Locale.US, "%.0f", walletCoin.getActualPrice()) + " USD");
        if (walletCoin.getAmount() == (int) walletCoin.getAmount())
            viewHolder.amount.setText(String.format("%d", (int) walletCoin.getAmount()) + " " + CoinTypes.getShortcut(walletCoin.getCoinName()));
        else
            viewHolder.amount.setText("" + walletCoin.getAmount() + " " + CoinTypes.getShortcut(walletCoin.getCoinName()));
        viewHolder.value.setText(String.format(Locale.US, "%.2f", walletCoin.getValue()) + " USD");

        if (walletCoin.getValue() > walletCoin.getPaid()) {
            viewHolder.trendingUp.setVisibility(View.VISIBLE);
            viewHolder.trendingDown.setVisibility(View.GONE);
        } else {
            viewHolder.trendingDown.setVisibility(View.VISIBLE);
            viewHolder.trendingUp.setVisibility(View.GONE);
        }

        // Return the completed view to render on screen
        return convertView;
    }
}
