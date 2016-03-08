package com.medikeen.pharmacy.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.medikeen.pharmacy.R;


/**
 * Created by Varun on 2/21/2016.
 */
public class AboutUsFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_about_us, container, false);

        return rootView;
    }
}

