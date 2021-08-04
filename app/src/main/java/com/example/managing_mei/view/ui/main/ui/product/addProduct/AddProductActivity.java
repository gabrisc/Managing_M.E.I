package com.example.managing_mei.view.ui.main.ui.product.addProduct;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.managing_mei.R;
import com.example.managing_mei.view.ui.main.ui.product.helpToCalc.HelpToCalcSealValueActivity;

public class AddProductActivity extends AppCompatActivity {

    private Spinner spinnerProductType,spinnerUnidadeDeMedida,spinnerProviders;
    private TextView counterProduct,date;
    private SeekBar seekBar;
    private EditText sellValue,buyValue,nameProduct;
    private Button buttonAddProduct,buttonCancel;
    private ImageButton buttonHelpToCalc;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        spinnerProductType = findViewById(R.id.spinnerTypeProduct);
        spinnerUnidadeDeMedida = findViewById(R.id.spinnerTypeOfQuantityProduct);
        spinnerProviders = findViewById(R.id.spinnerProviderProduct);
        counterProduct = findViewById(R.id.textViewCounterProduct);
        date = findViewById(R.id.textViewDateProduct);
        seekBar = findViewById(R.id.seekBarProduct);
        nameProduct = findViewById(R.id.editTextNameProduct);
        sellValue = findViewById(R.id.editTextSealValueProduct);
        buyValue = findViewById(R.id.editTextExpenseValueProduct);
        buttonAddProduct = findViewById(R.id.buttonAddProduct);
        buttonCancel = findViewById(R.id.buttonCancelProduct);
        buttonHelpToCalc = findViewById(R.id.imageButtonHelpToCalc);

        listenerForSeekBar(1000,1,1);

        actionForButtonAdd();
        actionForButtonCancel();
        actionForButtonCalcHelp();
    }

    private void actionForButtonCalcHelp() {
        buttonHelpToCalc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), HelpToCalcSealValueActivity.class));
            }
        });
    }

    private void actionForButtonAdd() {
        buttonAddProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    private void actionForButtonCancel() {
        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


            }
        });

    }

    public void listenerForSeekBar(Integer max,Integer min, Integer progress){
        seekBar.setMax(max);
        seekBar.setMin(min);
        seekBar.setProgress(progress);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @SuppressLint("SetTextI18n")
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                counterProduct.setText("" + seekBar.getProgress());
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });
    }



}