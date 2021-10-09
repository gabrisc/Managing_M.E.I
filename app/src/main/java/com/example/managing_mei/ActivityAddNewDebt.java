package com.example.managing_mei;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.managing_mei.model.entities.DebtsItem;
import com.example.managing_mei.model.enuns.FrequencyDebts;
import com.example.managing_mei.model.enuns.OccurrenceDebts;
import com.example.managing_mei.model.enuns.TypeOfDebts;
import com.google.android.material.textfield.TextInputLayout;

import static com.example.managing_mei.utils.Contants.listOfFrequencyType;
import static com.example.managing_mei.utils.Contants.listOfParcelOptions;
import static com.example.managing_mei.utils.Contants.listOfTypeDebt;
import static com.example.managing_mei.utils.FireBaseConfig.firebaseDbReference;

public class ActivityAddNewDebt extends AppCompatActivity {

    private TextInputLayout debtsValue,debsDescription;
    private TextView textViewValorTotalParcelado, textViewTitleFromSpinners;
    private DebtsItem debtsItemToSave;
    private Switch switchIsPaid;

    private Button buttonCancel,buttonAdd;
    private Spinner spinnerFrequencia, spinnerTipoDivida, spinnerNumParcelas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_debt);

        debtsValue                 = findViewById(R.id.editTextValorDaDivida);
        debsDescription            = findViewById(R.id.editTextNomeDivida);

        spinnerFrequencia = findViewById(R.id.spinnerOccurence);
        spinnerTipoDivida = findViewById(R.id.spinnerTypeDebt);
        spinnerNumParcelas = findViewById(R.id.spinnerParcelDebt);
        textViewValorTotalParcelado = findViewById(R.id.textViewValueParceFromBill);
        textViewTitleFromSpinners = findViewById(R.id.textViewTitleFormDebtsParcel);
        buttonCancel               = findViewById(R.id.buttonCancelNewDebt);
        buttonAdd                  = findViewById(R.id.buttonSaveNewDebt);
        switchIsPaid               = findViewById(R.id.switchIsPaid);

        spinnerFrequencia.setVisibility(View.INVISIBLE);
        spinnerNumParcelas.setVisibility(View.INVISIBLE);
        textViewValorTotalParcelado.setVisibility(View.INVISIBLE);
        textViewTitleFromSpinners.setVisibility(View.INVISIBLE);

        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

                validFields();

        spinnerFrequencia.setAdapter(new ArrayAdapter(getApplicationContext(), R.layout.item_spinner,listOfFrequencyType));
        actionForSpinnerFrequencia();

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
                                      Boolean.valueOf(switchIsPaid.isChecked())));

                    } else if(spinnerTipoDivida.getSelectedItem().toString().equals(TypeOfDebts.RECORRENTE.toString())) {
                        saveDebts(new DebtsItem(firebaseDbReference.push().getKey(),
                                                debsDescription.getEditText().getText().toString(),
                                                Double.valueOf(debtsValue.getEditText().getText().toString()),
                                                TypeOfDebts.valueOf(spinnerTipoDivida.getSelectedItem().toString()),
                                                FrequencyDebts.valueOf(spinnerFrequencia.getSelectedItem().toString()),
                                                Boolean.valueOf(switchIsPaid.isChecked())));

                    } else if(spinnerTipoDivida.getSelectedItem().toString().equals(TypeOfDebts.UNICA.toString())) {
                        saveDebts(new DebtsItem(firebaseDbReference.push().getKey(),
                                                debsDescription.getEditText().getText().toString(),
                                                Double.valueOf(debtsValue.getEditText().getText().toString()),
                                                TypeOfDebts.valueOf(spinnerTipoDivida.getSelectedItem().toString()),
                                                Boolean.valueOf(switchIsPaid.isChecked())));
                    }

                }
            }
        });
    }



    private void saveDebts(DebtsItem debtsItem){
        debtsItem.save();



        Toast toast=Toast. makeText(getApplicationContext(),"Divida Cadastrada",Toast. LENGTH_LONG);
        toast.show();
        this.finish();
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
                    spinnerFrequencia.setVisibility(View.INVISIBLE);
                }
                if(TypeOfDebts.IsEqualsToRecorrente(spinnerTipoDivida.getSelectedItem().toString())) {
                    spinnerNumParcelas.setVisibility(View.INVISIBLE);
                    textViewValorTotalParcelado.setVisibility(View.INVISIBLE);
                    textViewValorTotalParcelado.setText("X");
                    textViewTitleFromSpinners.setVisibility(View.VISIBLE);
                    textViewTitleFromSpinners.setText("Selecione a Ocorrencia");
                    spinnerFrequencia.setVisibility(View.VISIBLE);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });
    }

    private void actionForSpinnerFrequencia() {
        spinnerFrequencia.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (OccurrenceDebts.VALOR_PARCELADO.toString().equals(spinnerFrequencia.getSelectedItem().toString())) {
                    spinnerNumParcelas.setVisibility(View.VISIBLE);
                    textViewValorTotalParcelado.setVisibility(View.VISIBLE);
                    textViewTitleFromSpinners.setVisibility(View.VISIBLE);
                } else {
                    spinnerNumParcelas.setVisibility(View.INVISIBLE);
                    textViewValorTotalParcelado.setVisibility(View.INVISIBLE);
                    textViewTitleFromSpinners.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });
    }
}