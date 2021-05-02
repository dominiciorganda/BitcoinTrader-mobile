package com.example.bitcointrader.Entities;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.bitcointrader.R;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class TransactionAdapter extends ArrayAdapter<Transaction> {
    private ArrayList<Transaction> dataSet = new ArrayList<>();
    Context context;

    public TransactionAdapter(@NonNull Context context, int resource, @NonNull List<Transaction> objects) {
        super(context, resource, objects);
        this.dataSet.addAll(objects);
        this.context = context;

    }

    // View lookup cache
    private static class ViewHolder {
        ImageView arrow;
        TextView type;
        TextView date;
        TextView amount;
        TextView paidPrice;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Transaction transaction = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag
        if (convertView == null) {
            // If there's no view to re-use, inflate a brand new view for row
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.transactionlistview, parent, false);
            viewHolder.amount = convertView.findViewById(R.id.amount);
            viewHolder.arrow = convertView.findViewById(R.id.arrow);
            viewHolder.date = convertView.findViewById(R.id.date);
            viewHolder.paidPrice = convertView.findViewById(R.id.paidPrice);
            viewHolder.type = convertView.findViewById(R.id.type);
            // Cache the viewHolder object inside the fresh view
            convertView.setTag(viewHolder);
        } else {
            // View is being recycled, retrieve the viewHolder object from tag
            viewHolder = (ViewHolder) convertView.getTag();
        }
        // Populate the data from the data object via the viewHolder object
        // into the template view.
        DecimalFormat df = new DecimalFormat("0");
        df.setMaximumFractionDigits(340);
        String amount = df.format(transaction.getAmount());
        if (transaction.getType() == TransactionType.BUY) {
            viewHolder.arrow.setBackgroundResource(R.drawable.circledown);
            viewHolder.type.setText("Bought");
            viewHolder.amount.setText("+" + amount + " " + CoinTypes.getShortcut(transaction.getCoin()));
            viewHolder.amount.setTextColor(Color.parseColor("#0FDD75"));
        } else {
            viewHolder.arrow.setBackgroundResource(R.drawable.circleup);
            viewHolder.type.setText("Sold");
            viewHolder.amount.setText("-" + amount + " " + CoinTypes.getShortcut(transaction.getCoin()));
            viewHolder.amount.setTextColor(Color.parseColor("#FF3838"));
        }
        viewHolder.paidPrice.setText(String.valueOf(transaction.getPaidPrice()) + " USD");
        viewHolder.date.setText(transaction.getTransactionDate());


        // Return the completed view to render on screen
        return convertView;
    }
}
