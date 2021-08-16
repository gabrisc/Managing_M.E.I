package com.example.managing_mei.view.ui.main.ui.product.EditProduct;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.managing_mei.R;
import com.example.managing_mei.model.entities.Product;

public class EditProductActivity extends AppCompatActivity {

    private Product economicOperation;
    private TextView counter,titleOfQuantity;
    private EditText name,expense,seal;
    private Spinner spinnerTypeOfQuantity;
    private SeekBar seekBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_product);


        economicOperation = findEconomicOperationSelected();
        name = findViewById(R.id.editTextNameProductUpdate);
        seekBar = findViewById(R.id.seekBarForQuantityUpdate);
        expense = findViewById(R.id.editTextBuyValueUpdate);
        seal = findViewById(R.id.editTextSellValueUpdate);
        counter = findViewById(R.id.textViewQuantidadeUpdate);
        spinnerTypeOfQuantity = findViewById(R.id.spinnerUnidadeDeMedidaUpdateEO);
        titleOfQuantity=findViewById(R.id.textViewQuantidadeUpdateTitle);


        setSpinners();
        setValues();
        setButtonActions();
    }

    private EconomicOperation findEconomicOperationSelected() {
        EconomicOperation economicOperationSelect = new EconomicOperation();
        Bundle bundle = getIntent().getExtras();
        economicOperationSelect.setId(bundle.getString("id"));
        economicOperationSelect.setName(bundle.getString("Name"));
        economicOperationSelect.setType(bundle.getString("type"));
        economicOperationSelect.setSealValue(bundle.getDouble("SealValue"));
        economicOperationSelect.setExpenseValue(bundle.getDouble("ExpenseValue"));
        economicOperationSelect.setContributionValue(bundle.getDouble("ContributionValue"));
        economicOperationSelect.setQuantity(bundle.getInt("Quantity"));
        economicOperationSelect.setDate(bundle.getString("Date"));
        economicOperationSelect.setTypeQuantity("typeQuantity");
        return economicOperationSelect;
    }
}