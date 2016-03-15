package com.earnapp.helpbucks;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.earnapp.constants.ApplicationConstants;


public class AcceptedBidsFragment extends Fragment {

    private String TAG = ApplicationConstants.TAG_TASK;


    public AcceptedBidsFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View fragmentView = inflater.inflate(R.layout.fragment_accepted_bids, container, false);

        return  fragmentView;
    }


}
