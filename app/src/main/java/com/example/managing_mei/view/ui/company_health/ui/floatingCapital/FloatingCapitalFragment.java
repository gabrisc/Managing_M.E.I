package com.example.managing_mei.view.ui.company_health.ui.floatingCapital;

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

import com.example.managing_mei.R;
import com.example.managing_mei.databinding.FragmentFloatingCapitalBinding;
import com.example.managing_mei.databinding.FragmentNotificationsBinding;
import com.example.managing_mei.databinding.FragmentTotalInvestmentCalculationBinding;

import java.util.zip.Inflater;

public class FloatingCapitalFragment extends Fragment {

    public FloatingCapitalFragment() {
    }

    @Override
    public void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_floating_capital, container, false);

        return view;
    }

@Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}