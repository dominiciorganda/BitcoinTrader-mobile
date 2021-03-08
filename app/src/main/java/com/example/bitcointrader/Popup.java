package com.example.bitcointrader;

import android.content.Context;
import android.widget.TextView;

import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.utils.MPPointF;

import java.util.ArrayList;
import java.util.List;

public class Popup extends MarkerView {

    private TextView tvContent;

    private List<Coin> chartCoins = new ArrayList<>();

    public Popup(Context context, int layoutResource, List<Coin> chartCoins) {
        super(context, layoutResource);
        // this markerview only displays a textview
        tvContent = (TextView) findViewById(R.id.tvContent);
        this.chartCoins.addAll(chartCoins);
    }


    // callbacks everytime the MarkerView is redrawn, can be used to update the
// content (user-interface)
    @Override
    public void refreshContent(Entry e, Highlight highlight) {
        int value = (int) e.getY();
        String date = chartCoins.get((int)e.getX()-1).getDate().substring(5).replace("-",".");
        tvContent.setText("" + value+"\n"+date); // set the entry-value as the display text
        super.refreshContent(e, highlight);
    }

    private MPPointF mOffset;

    @Override
    public MPPointF getOffset() {

        if(mOffset == null) {
            // center the marker horizontally and vertically
            mOffset = new MPPointF(-(getWidth() / 2), -getHeight());
        }

        return mOffset;
    }
}