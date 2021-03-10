package com.example.bitcointrader.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ProgressBar;

import com.example.bitcointrader.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Loading#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Loading extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER


    // TODO: Rename and change types of parameters
    private ProgressBar progressBar;

    public Loading() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment Loading.
     */
    // TODO: Rename and change types and number of parameters
    public static Loading newInstance() {
        Loading fragment = new Loading();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_loading, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        progressBar = getView().findViewById(R.id.progress_bar);
    }

    public void disableLoadingScreen() {
        progressBar.setVisibility(View.GONE);
    }
}