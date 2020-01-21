package com.example.tailorapp.Mens;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.tailorapp.R;

public class FormalWearMenFragment extends Fragment {

    public FormalWearMenFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_formal_wear_men, container, false);
    }

}
