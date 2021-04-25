package com.example.bitcointrader.Entities;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.bitcointrader.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;

public class CoinAdapter extends ArrayAdapter<CoinTypes> {
    private ArrayList<CoinTypes> dataSet = new ArrayList<>();
    Context context;


    public CoinAdapter(@NonNull Context context, int resource, @NonNull CoinTypes[] objects) {
        super(context, resource, objects);
        this.dataSet.addAll(Arrays.asList(objects));
        this.context = context;
    }


    // View lookup cache
    private static class ViewHolder {
        TextView coinName;
        ImageView logo;
        TextView shortcut;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        CoinTypes coinType = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag
        if (convertView == null) {
            // If there's no view to re-use, inflate a brand new view for row
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.listviewcoin, parent, false);
            viewHolder.coinName = (TextView) convertView.findViewById(R.id.name);
            viewHolder.logo = (ImageView) convertView.findViewById(R.id.logo);
            viewHolder.shortcut = (TextView) convertView.findViewById(R.id.shortcut);
            // Cache the viewHolder object inside the fresh view
            convertView.setTag(viewHolder);
        } else {
            // View is being recycled, retrieve the viewHolder object from tag
            viewHolder = (ViewHolder) convertView.getTag();
        }
        // Populate the data from the data object via the viewHolder object
        // into the template view.
        viewHolder.coinName.setText(CoinTypes.getName(coinType));
        viewHolder.logo.setBackgroundResource(CoinTypes.getDrawable(coinType));
        viewHolder.shortcut.setText(CoinTypes.getShortcut(coinType));

        // Return the completed view to render on screen
        return convertView;
    }

}
