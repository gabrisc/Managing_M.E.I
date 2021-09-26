package com.example.managing_mei.view.ui.company_health.ui.profitability;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.managing_mei.R;
import com.example.managing_mei.model.DebtsItem;
import com.example.managing_mei.utils.FormatDataUtils;
import com.google.android.material.textfield.TextInputLayout;


public class ProfitabilityFragment extends Fragment {

    private RadioGroup radioGroup;
    private Switch aSwitch;
    private Button button;
    private TextInputLayout debtsValue,debsDescription;
    private TextView titleFrequency;
    private DebtsItem debtsItemToSave;

    public ProfitabilityFragment() {
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_profitability, container, false);
/*
        radioGroup = root.findViewById(R.id.);
        aSwitch = root.findViewById(R.id.);
        button = root.findViewById(R.id.);
        debsDescription = root.findViewById(R.id.);
        debtsValue = root.findViewById(R.id.);
        button = root.findViewById(R.id.);
        titleFrequency = root.findViewById(R.id.);
*/
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                debtsItemToSave.setFrequency(i);
            }
        });

        debtsValue.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b){
                    debtsValue.getEditText().setText(FormatDataUtils.cleanFormatValues(debtsValue.getEditText().getText().toString()));
                } else {
                    debtsValue.getEditText().setText(FormatDataUtils.formatMonetaryValue(Double.valueOf(debtsValue.getEditText().getText().toString())));
                }
            }
        });

        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b) {
                    radioGroup.setVisibility(View.INVISIBLE);
                    titleFrequency.setVisibility(View.INVISIBLE);
                } else {
                    radioGroup.setVisibility(View.VISIBLE);
                    titleFrequency.setVisibility(View.VISIBLE);
                }
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}