package com.example.managing_mei.view.ui.company_health.ui.profitability;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.managing_mei.R;


public class ProfitabilityFragment extends Fragment {

    public ProfitabilityFragment() {
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_profitability, container, false);

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}