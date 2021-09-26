package com.example.managing_mei.view.ui.main.ui.product.helpToCalc;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;

import com.example.managing_mei.R;
import com.example.managing_mei.model.entities.ExpenseFromProducts;
import com.example.managing_mei.view.ui.main.ui.product.addProduct.AddProductActivity;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import static com.example.managing_mei.utils.FireBaseConfig.firebaseDbReference;
import static com.example.managing_mei.utils.FormatDataUtils.cleanFormatValues;
import static com.example.managing_mei.utils.FormatDataUtils.formatMonetaryValue;
import static com.example.managing_mei.utils.FormatDataUtils.formatMonetaryValuePositiveOrNegative;

public class HelpToCalcSealValueActivity extends AppCompatActivity {

    private TextView valueWithGain,countOfGainPercent;
    private SeekBar gainSeekBar;
    private TextInputLayout editTextExpense;
    private ImageButton imageButtonAddNewExpense;
    private Button SaveValuesButton,CancelCalcButton;
    private ChipGroup chipGroupForCalc;
    private Set<Chip> chipsToShow= new HashSet<>();
    private Set<ExpenseFromProducts> mainListOfQuantityTypes = new HashSet<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help_to_calc_seal_value);

        chipGroupForCalc = findViewById(R.id.chipGroupForCal);
        valueWithGain = findViewById(R.id.textViewValueWithGain);
        countOfGainPercent = findViewById(R.id.textViewCountValue);
        gainSeekBar = findViewById(R.id.seekBar);
        editTextExpense = findViewById(R.id.editTextNumberDecimal);
        imageButtonAddNewExpense = findViewById(R.id.imageButtonAddNewExpense);
        SaveValuesButton = findViewById(R.id.SaveValuesButton);
        CancelCalcButton = findViewById(R.id.CancelCalcButton);
        mainListOfQuantityTypes.clear();


        SaveValuesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               if(calcTotalExpenses().isNaN() || calcTotalExpenses().equals(0.0)){
                   Toast.makeText(getApplicationContext(),"Adicione as suas despesas",Toast. LENGTH_SHORT).show();
               }else{
                   Intent intent = new Intent(getApplicationContext(), AddProductActivity.class);
                   Bundle bundle = new Bundle();
                   bundle.putDouble("expenses",calcTotalExpenses());
                   bundle.putDouble("SealValue",Double.parseDouble(cleanFormatValues(valueWithGain.getText().toString())));
                   intent.putExtras(bundle);
                   startActivity(intent);
               }
            }
        });

        editTextExpense.getEditText().setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b){
                    editTextExpense.getEditText().setText(cleanFormatValues(editTextExpense.getEditText().getText().toString()));
                } else {
                    editTextExpense.getEditText().setText(formatMonetaryValue(Double.valueOf(editTextExpense.getEditText().getText().toString())));
                }
            }
        });


        setActionForSeekBar();
        cancelCalcValue();
        AddNewExpense();
    }

    private void setActionForSeekBar() {
        gainSeekBar.setMin(1);
        gainSeekBar.setMax(300);
        gainSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                countOfGainPercent.setText(""+i);
                calcValue();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    private void AddNewExpense(){
        imageButtonAddNewExpense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ExpenseFromProducts expenseFromProducts = new ExpenseFromProducts();
                if (editTextExpense.getEditText().getText().toString().equals("") || editTextExpense.getEditText().getText().toString().equals(null)){
                    expenseFromProducts.setExpenseValue(0.0);
                }else{
                    expenseFromProducts.setExpenseValue(Double.valueOf(editTextExpense.getEditText().getText().toString()));
                }
                expenseFromProducts.setExpenseId(firebaseDbReference.push().getKey());
                editTextExpense.getEditText().setText("");
                mainListOfQuantityTypes.add(expenseFromProducts);
                reloadChipGroup();
            }
        });
    }

    private void calcValue() {
        Double value = calcTotalExpenses()+((Double.parseDouble(countOfGainPercent.getText().toString())/100)*calcTotalExpenses());
        valueWithGain.setText(formatMonetaryValue(value));
    }


    private Double calcTotalExpenses(){
        return mainListOfQuantityTypes.stream().mapToDouble(ExpenseFromProducts::getExpenseValue).sum();
    }

    private void cancelCalcValue(){
        CancelCalcButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void reloadChipGroup(){
        chipsToShow.clear();
        createChipList(mainListOfQuantityTypes).stream().forEach(chip -> {
            if (!chipsToShow.contains(chip)){
                chipsToShow.add(chip);
            }
        });
        editTextExpense.getEditText().setText("");
        chipGroupForCalc.removeAllViews();
        chipsToShow.stream().forEach(chip -> chipGroupForCalc.addView(chip));
        calcValue();
    }

    private List<Chip> createChipList(Set<ExpenseFromProducts> mainListOfQuantityTypes){
        List<Chip> chips = new ArrayList<>();

        mainListOfQuantityTypes.stream().forEach(expenseFromProducts -> {
            Chip chip = new Chip(this);
            chip.setId(ViewCompat.generateViewId());
            chip.setText(formatMonetaryValuePositiveOrNegative(expenseFromProducts.getExpenseValue(),false));
            chip.setTextColor(Color.parseColor("#FF0000"));
            chip.setCheckable(true);
            chip.setChipIconVisible(true);
            chip.setCheckedIconVisible(true);
            chip.setCloseIconVisible(true);
            chip.setOnCloseIconClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    chipGroupForCalc.removeView(chip);
                    mainListOfQuantityTypes.removeIf(expense -> expense.getExpenseId().equals(expenseFromProducts.getExpenseId()));
                }
            });
            chips.add(chip);
        });
        return chips;
    }

}