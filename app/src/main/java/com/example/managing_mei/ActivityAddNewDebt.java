package com.example.managing_mei;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.managing_mei.model.entities.CashFlowItem;
import com.example.managing_mei.model.entities.DebtsItem;
import com.example.managing_mei.model.enuns.FrequencyDebts;
import com.example.managing_mei.model.enuns.OccurrenceDebts;
import com.example.managing_mei.model.enuns.TypeOfDebts;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.example.managing_mei.utils.Contants.listOfFrequencyType;
import static com.example.managing_mei.utils.Contants.listOfParcelOptions;
import static com.example.managing_mei.utils.Contants.listOfTypeDebt;
import static com.example.managing_mei.utils.FireBaseConfig.firebaseDbReference;

public class ActivityAddNewDebt extends AppCompatActivity {

    private TextInputLayout debtsValue,debsDescription;
    private TextView textViewValorTotalParcelado, textViewTitleFromSpinners;
    private Button buttonCancel,buttonAdd;
    private Spinner spinnerTipoDivida, spinnerNumParcelas;
    private RadioGroup radioGroupStatusDebts;
    private String StatusDividaSelected;
    private RadioButton radioButtonPedenteStatus,radioButtonAtrasadaStatus,radioButtonQuitadaStatus;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_debt);

        debtsValue = findViewById(R.id.editTextValorDaDivida);
        debsDescription = findViewById(R.id.editTextNomeDivida);
        spinnerTipoDivida = findViewById(R.id.spinnerTypeDebt);
        spinnerNumParcelas = findViewById(R.id.spinnerParcelDebt);
        textViewValorTotalParcelado = findViewById(R.id.textViewValueParceFromBill);
        textViewTitleFromSpinners = findViewById(R.id.textViewTitleFormDebtsParcel);
        buttonCancel = findViewById(R.id.buttonCancelNewDebt);
        buttonAdd = findViewById(R.id.buttonSaveNewDebt);
        spinnerNumParcelas.setVisibility(View.INVISIBLE);
        textViewValorTotalParcelado.setVisibility(View.INVISIBLE);
        textViewTitleFromSpinners.setVisibility(View.INVISIBLE);
        radioGroupStatusDebts = findViewById(R.id.RadioGroupStatusDebts);
        radioButtonQuitadaStatus = findViewById(R.id.radioButtonQuitadaStatus);
        radioButtonAtrasadaStatus = findViewById(R.id.radioButtonAtrasadaStatus);
        radioButtonPedenteStatus = findViewById(R.id.radioButtonPedenteStatus);

        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        validFields();

        spinnerTipoDivida.setAdapter(new ArrayAdapter(getApplicationContext(), R.layout.item_spinner,listOfTypeDebt));
        actionForSpinnerTipoDivida();

        spinnerNumParcelas.setAdapter(new ArrayAdapter(getApplicationContext(), R.layout.item_spinner,listOfParcelOptions));

        debsDescription.getEditText().setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {

            }
        });

        debtsValue.getEditText().setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {

            }
        });

        if (radioButtonPedenteStatus.isChecked()) {
            StatusDividaSelected = radioButtonPedenteStatus.getText().toString();
        }
        if (radioButtonAtrasadaStatus.isChecked()) {
            StatusDividaSelected = radioButtonAtrasadaStatus.getText().toString();
        }
        if (radioButtonQuitadaStatus.isChecked()) {
            StatusDividaSelected =radioButtonQuitadaStatus.getText().toString();
        }

    }

    private void validFields() {
        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (debsDescription.getEditText().getText().toString().isEmpty()){
                    Toast toast=Toast. makeText(getApplicationContext(),"O E-mail não e valido",Toast. LENGTH_LONG);
                    toast. show();
                }else if(debtsValue.getEditText().getText().toString().isEmpty()){
                    Toast toast=Toast. makeText(getApplicationContext(),"O E-mail não e valido",Toast. LENGTH_LONG);
                    toast. show();
                } else {
                    if (spinnerTipoDivida.getSelectedItem().toString().equals(TypeOfDebts.PARCELADA.toString())) {
                        saveDebts(new DebtsItem(firebaseDbReference.push().getKey(),
                                      debsDescription.getEditText().getText().toString(),
                                      Double.valueOf(debtsValue.getEditText().getText().toString()),
                                      TypeOfDebts.valueOf(spinnerTipoDivida.getSelectedItem().toString()),
                                      true,
                                      Integer.valueOf(spinnerNumParcelas.getSelectedItemPosition()),
                                      StatusDividaSelected
                                      ));

                    } else if(spinnerTipoDivida.getSelectedItem().toString().equals(TypeOfDebts.UNICA.toString())) {
                        saveDebts(new DebtsItem(firebaseDbReference.push().getKey(),
                                                debsDescription.getEditText().getText().toString(),
                                                Double.valueOf(debtsValue.getEditText().getText().toString()),
                                                TypeOfDebts.valueOf(spinnerTipoDivida.getSelectedItem().toString()),
                                                StatusDividaSelected
                                                ));

                        saveCashFlowItem(new CashFlowItem(0, Double.valueOf(debtsValue.getEditText().getText().toString()),debsDescription.getEditText().getText().toString()));
                    }

                }
            }
        });
    }

    private void saveCashFlowItem(CashFlowItem cashFlowItem) {
        cashFlowItem.save();
    }

    private void saveDebts(DebtsItem debtsItem){
        debtsItem.save();
        Toast toast=Toast. makeText(getApplicationContext(),"Divida Cadastrada",Toast. LENGTH_LONG);
        toast.show();
        finish();
    }

    private void actionForSpinnerTipoDivida() {
        spinnerTipoDivida.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (TypeOfDebts.IsEqualsToParcelada(spinnerTipoDivida.getSelectedItem().toString())) {
                    spinnerNumParcelas.setVisibility(View.VISIBLE);
                    textViewValorTotalParcelado.setVisibility(View.VISIBLE);
                    textViewValorTotalParcelado.setText("X");
                    textViewTitleFromSpinners.setVisibility(View.VISIBLE);
                    textViewTitleFromSpinners.setText("Quantidade de Parcelas");
                }
                if(TypeOfDebts.IsEqualsToRecorrente(spinnerTipoDivida.getSelectedItem().toString())) {
                    spinnerNumParcelas.setVisibility(View.INVISIBLE);
                    textViewValorTotalParcelado.setVisibility(View.INVISIBLE);
                    textViewValorTotalParcelado.setText("X");
                    textViewTitleFromSpinners.setVisibility(View.VISIBLE);
                    textViewTitleFromSpinners.setText("Selecione a Ocorrencia");
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });
    }
}