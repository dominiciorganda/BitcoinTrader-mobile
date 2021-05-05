package com.example.bitcointrader.Fragments;

import android.graphics.Color;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.bitcointrader.Entities.ChartType;
import com.example.bitcointrader.Entities.Coin;
import com.example.bitcointrader.Entities.Popup;
import com.example.bitcointrader.R;
import com.example.bitcointrader.util.CoinUrls;
import com.example.bitcointrader.util.Urls;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.IMarker;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Chart#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Chart extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String CHART_COINS = "coins";
    private static final String URL = "URL";
    private static final String CHART_TYPE = "charttype";

    // TODO: Rename and change types of parameters
    private ArrayList<Coin> chartCoins;
    private String url;
    private ChartType chartType;


    public Chart() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment Chart.
     */
    // TODO: Rename and change types and number of parameters
    public static Chart newInstance(ArrayList<Coin> chartCoins) {
        Chart fragment = new Chart();
        Bundle args = new Bundle();
        args.putParcelableArrayList(CHART_COINS, chartCoins);

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
        View view = inflater.inflate(R.layout.fragment_chart, container, false);
        if (getArguments() != null) {
            chartCoins = getArguments().getParcelableArrayList("CHART_COINS");
            url = getArguments().getString("URL");
            chartType = (ChartType) getArguments().getSerializable("CHART_TYPE");
        } else
            System.out.println("ok");

        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (chartCoins != null && chartCoins.size() != 0) {
            drawChart();
        } else
            System.out.println("Error");

    }


    public void drawChart() {
        LineChart chart = (LineChart) getView().findViewById(R.id.diagramm);

        List<Entry> entries = new ArrayList<Entry>();
        int i = 0;
        for (Coin data : chartCoins) {
            i++;

            if (chartType == ChartType.LINEAR)
                entries.add(new Entry(i, (float) data.getPrice()));
            if (chartType == ChartType.LOGARITMIC)
                entries.add(new Entry(i, scaleCbr(data.getPrice())));
        }


        LineDataSet dataSet = new LineDataSet(entries, "");
        LineData lineData = new LineData(dataSet);
        dataSet.setDrawFilled(true);


        if (url != null) {
            CoinUrls coinUrls = CoinUrls.find(url);

            switch (coinUrls) {
                case BITCOIN:
                case DOGECOIN:
                case BINANCECOIN:
                    dataSet.setFillDrawable(ContextCompat.getDrawable(getContext(), R.drawable.orange));
                    break;
                case ETHEREUM:
                case LITECOIN:
                case FILECOIN:
                    dataSet.setFillDrawable(ContextCompat.getDrawable(getContext(), R.drawable.lightblue));
                    break;
                case ELROND:
                    dataSet.setFillDrawable(ContextCompat.getDrawable(getContext(), R.drawable.darkblue));
                    break;
                case BITCOINCASH:
                    dataSet.setFillDrawable(ContextCompat.getDrawable(getContext(), R.drawable.lightgreen));
                    break;
                default:
                    dataSet.setFillDrawable(ContextCompat.getDrawable(getContext(), R.drawable.gradient));
            }
        } else
            dataSet.setFillDrawable(ContextCompat.getDrawable(getContext(), R.drawable.gradient));


//        dataSet.setFillDrawable(ContextCompat.getDrawable(getContext(), R.drawable.gradient));
        if (chartCoins.size() > 50) {
            dataSet.setDrawCircles(false);
            dataSet.setDrawCircleHole(false);
        } else {
            dataSet.setDrawCircles(true);
            dataSet.setDrawCircleHole(true);
        }

        dataSet.setCircleHoleColor(Color.BLACK);
        dataSet.setCircleRadius(10);
        dataSet.setCircleHoleRadius(8);
        dataSet.setValueTextSize(0);
        dataSet.setDrawHorizontalHighlightIndicator(false);
        dataSet.setDrawVerticalHighlightIndicator(false);


        chart.setData(lineData);
        chart.getAxisLeft().setDrawGridLines(false);
        chart.getXAxis().setDrawGridLines(false);
        chart.getAxisRight().setDrawGridLines(false);
        chart.getDescription().setEnabled(false);
        chart.getAxisLeft().setDrawLabels(false);
        chart.getAxisRight().setDrawLabels(false);
        chart.getXAxis().setDrawLabels(false);
        chart.getLegend().setEnabled(false);
        chart.invalidate(); // refresh

        IMarker marker;
        
        if (chartType == ChartType.LOGARITMIC) {
            chart.getAxisLeft().setValueFormatter(new ValueFormatter() {
                @Override
                public String getFormattedValue(float value) {
                    DecimalFormat mFormat;
                    mFormat = new DecimalFormat("##.###");
                    return mFormat.format(unScaleCbr(value));
                }

            });
            chart.getAxisLeft().setAxisMinimum(scaleCbr(1));
            chart.getAxisLeft().setAxisMaximum(scaleCbr(100000));
            chart.getAxisLeft().setLabelCount(6, true);
            marker = new Popup(getContext(), R.layout.popup, chartCoins, ChartType.LOGARITMIC);
        } else
            marker = new Popup(getContext(), R.layout.popup, chartCoins, ChartType.LINEAR);


        chart.setMarker(marker);

        chart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
//                Toast.makeText(MainActivity.this, "The text you want to display", Toast.LENGTH_LONG).show();

                float x = e.getX();
                float y = e.getY();
                System.out.println(x);
                System.out.println(chartCoins.get((int) x - 1).getPrice());
            }

            @Override
            public void onNothingSelected() {

            }
        });
    }

    private float scaleCbr(double cbr) {
        return (float) (Math.log10(cbr));
    }

    private float unScaleCbr(double cbr) {
        double calcVal = Math.pow(10, cbr);
        return (float) (calcVal);
    }
}