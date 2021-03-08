package com.example.bitcointrader;

import android.graphics.Color;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.provider.SyncStateContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.IMarker;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

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

    // TODO: Rename and change types of parameters
    private ArrayList<Coin> chartCoins;


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
        } else
            System.out.println("ok");

        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (chartCoins != null && chartCoins.size()!=0) {
            drawChart();
        } else
            System.out.println("Error");

    }




    public void drawChart(){
        LineChart chart = (LineChart) getView().findViewById(R.id.diagramm);

        List<Entry> entries = new ArrayList<Entry>();
        int i =0;
        for (Coin data : chartCoins) {
            i++;

            entries.add(new Entry(i, (float)data.getPrice()));
        }


        LineDataSet dataSet = new LineDataSet(entries,"");
        LineData lineData = new LineData(dataSet);
        dataSet.setDrawFilled(true);
        dataSet.setFillDrawable(ContextCompat.getDrawable(getContext(), R.drawable.gradient));
        dataSet.setDrawCircles(true);
        dataSet.setDrawCircleHole(true);
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


        IMarker marker = new Popup(getContext(), R.layout.popup, chartCoins);
        chart.setMarker(marker);

        chart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
//                Toast.makeText(MainActivity.this, "The text you want to display", Toast.LENGTH_LONG).show();

                float x=e.getX();
                float y=e.getY();
                System.out.println(x);
                System.out.println(chartCoins.get((int)x-1).getPrice());
            }

            @Override
            public void onNothingSelected() {

            }
        });
    }
}