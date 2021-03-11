package com.example.bitcointrader.Fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.bitcointrader.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ChartDays#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChartDays extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER


    // TODO: Rename and change types of parameters
    private EditText daysNumber;
    private IFragmentToActivity callBack;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            callBack = (IFragmentToActivity) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement IFragmentToActivity");
        }
    }


    public ChartDays() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ChartDays.
     */
    // TODO: Rename and change types and number of parameters
    public static ChartDays newInstance(String days) {
        ChartDays fragment = new ChartDays();
        Bundle args = new Bundle();

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
        View view = inflater.inflate(R.layout.fragment_chart_days, container, false);
        daysNumber = view.findViewById(R.id.numberDays);
        return view;
    }

    @Override
    public void onDetach() {
        callBack = null;
        super.onDetach();
    }

    private void sendData(String data) {
        callBack.communicate(data);

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (daysNumber != null) {
            daysNumber.setOnKeyListener(new View.OnKeyListener() {
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    // If the event is a key-down event on the "enter" button
                    if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                            (keyCode == KeyEvent.KEYCODE_ENTER)) {
                        // Perform action on key press
                        sendData(daysNumber.getText().toString());
                        return true;
                    }
                    return false;
                }
            });

        } else
            System.out.println("Error");

    }
}