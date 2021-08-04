package com.example.managing_mei.view.ui.company_health.ui.totalInvestmentCalculation;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.managing_mei.databinding.FragmentHomeBinding;
import com.example.managing_mei.databinding.FragmentTotalInvestmentCalculationBinding;

public class TotalInvestmentCalculationFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private FragmentTotalInvestmentCalculationBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);

    binding = FragmentTotalInvestmentCalculationBinding.inflate(inflater, container, false);
    View root = binding.getRoot();

        return root;
    }

@Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}