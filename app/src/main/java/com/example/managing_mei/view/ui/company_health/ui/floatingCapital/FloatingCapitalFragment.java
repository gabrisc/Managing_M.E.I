package com.example.managing_mei.view.ui.company_health.ui.floatingCapital;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.managing_mei.R;

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