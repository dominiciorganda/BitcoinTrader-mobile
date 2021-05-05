package com.example.bitcointrader.Entities;

import android.content.Context;
import android.widget.TextView;

import com.example.bitcointrader.Entities.Coin;
import com.example.bitcointrader.R;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.utils.MPPointF;

import java.util.ArrayList;
import java.util.List;

public class Popup extends MarkerView {

    private TextView tvContent;

    private List<Coin> chartCoins = new ArrayList<>();
    private ChartType chartType;

    public Popup(Context context, int layoutResource, List<Coin> chartCoins, ChartType chartType) {
        super(context, layoutResource);
        // this markerview only displays a textview
        tvContent = (TextView) findViewById(R.id.tvContent);
        this.chartCoins.addAll(chartCoins);
        this.chartType = chartType;
    }


    // callbacks everytime the MarkerView is redrawn, can be used to update the
// content (user-interface)
    @Override
    public void refreshContent(Entry e, Highlight highlight) {
        if (chartType == ChartType.LINEAR) {
            int value = (int) e.getY();
            String date = chartCoins.get((int) e.getX() - 1).getDate().substring(5).replace("-", ".");
            tvContent.setText("" + value + "\n" + date);
        } // set the entry-value as the display text}
        if (chartType == ChartType.LOGARITMIC) {
            float value = e.getY();
            String date = chartCoins.get((int) e.getX() - 1).getDate().substring(5).replace("-", ".");
            tvContent.setText("" + (int) unScaleCbr(value) + "\n" + date);
        }
        super.refreshContent(e, highlight);
    }

    private MPPointF mOffset;

    @Override
    public MPPointF getOffset() {

        if (mOffset == null) {
            // center the marker horizontally and vertically
            mOffset = new MPPointF(-(getWidth() / 2), -getHeight());
        }

        return mOffset;
    }

    private float scaleCbr(double cbr) {
        return (float) (Math.log10(cbr));
    }

    private float unScaleCbr(double cbr) {
        double calcVal = Math.pow(10, cbr);
        return (float) (calcVal);
    }
}