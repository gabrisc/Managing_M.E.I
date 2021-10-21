package com.example.managing_mei.view.ui.main.ui.product.helpToCalc;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.managing_mei.R;
import com.example.managing_mei.adapters.AdapterDoubleValues;
import com.example.managing_mei.adapters.AdapterProvider;
import com.example.managing_mei.utils.FormatDataUtils;
import com.example.managing_mei.view.ui.main.ui.product.addProduct.AddProductActivity;
import com.example.managing_mei.view.ui.main.ui.seals.addSell.AddSellActivity;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.List;

import static com.example.managing_mei.utils.FormatDataUtils.cleanFormatValues;
import static com.example.managing_mei.utils.FormatDataUtils.formatMonetaryValue;
import static com.example.managing_mei.utils.FormatDataUtils.formatMonetaryValueDouble;

public class HelpToCalcSealValueActivity extends AppCompatActivity implements AdapterDoubleValues.OnDoubleItemListener{

    private TextView textViewValorCalculado,contador;
    private SeekBar seekbar;
    private Button botaoSalvar, botaoCancelar,botaoAdicionar;
    private RecyclerView recyclerView;
    private List<Double> listOfValoresAgregados = new ArrayList<>();
    private AdapterDoubleValues adapterDoubleValues;
    private AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help_to_calc_seal_value);

        contador = findViewById(R.id.textViewCountValue);
        textViewValorCalculado = findViewById(R.id.textViewValueWithGain);
        recyclerView = findViewById(R.id.recyclerViewElementosDeDespesas);
        botaoAdicionar = findViewById(R.id.buttonNovaDespesa);
        seekbar = findViewById(R.id.seekBar);
        botaoSalvar = findViewById(R.id.SaveValuesButton);
        botaoCancelar = findViewById(R.id.CancelCalcButton);

        seekbar.setMax(1000);
        seekbar.setMin(1);
        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                contador.setText(""+i);
                atualizarValorCalculado();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        botaoAdicionar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callDialogAddValue();
            }
        });

        botaoCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             finish();
            }
        });

        botaoSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(textViewValorCalculado.getText().toString().equals(0.0)){
                    Toast.makeText(getApplicationContext(),"Adicione as suas despesas",Toast. LENGTH_SHORT).show();
                }else{
                    Intent intent = new Intent(getApplicationContext(), AddProductActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putDouble("expenses",calcularDespesasTotais());
                    bundle.putDouble("SealValue",calcularValorComLucro());
                    intent.putExtras(bundle);
                    startActivity(intent);
                    finish();
                }

            }
        });
    }

    private double calcularDespesasTotais() {
        return formatMonetaryValueDouble(listOfValoresAgregados.stream().mapToDouble(Double::doubleValue).sum());
    }

    private double calcularValorComLucro() {
        return formatMonetaryValueDouble((calcularDespesasTotais()*(Double.parseDouble(String.valueOf(seekbar.getProgress()))/100))+calcularDespesasTotais());
    }

    private void callDialogAddValue() {
        View dialog = LayoutInflater.from(this).inflate(R.layout.dialog_new_calc_seal_item,null);

        TextInputLayout editTextValorAgregado = dialog.findViewById(R.id.editTextNumberDecimalValorAgregado);
        Button butaoAdicionarValor =  dialog.findViewById(R.id.buttonAddNewValueInHelp);

        butaoAdicionarValor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (editTextValorAgregado.getEditText().getText().toString().isEmpty()) {
                    Toast.makeText(HelpToCalcSealValueActivity.this, "Adicione um valor", Toast.LENGTH_SHORT).show();
                } else {
                    listOfValoresAgregados.add(Double.parseDouble(editTextValorAgregado.getEditText().getText().toString()));
                    atualizarValorCalculado();
                    atualizarRecyclerView();
                    alertDialog.dismiss();
                }
            }
        });
        AlertDialog.Builder builder = new AlertDialog.Builder(this).setView(dialog);
        alertDialog=builder.create();
        alertDialog.show();
    }

    private void atualizarRecyclerView() {

        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        adapterDoubleValues = new AdapterDoubleValues(listOfValoresAgregados,getApplicationContext(),this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapterDoubleValues);

    }

    private void atualizarValorCalculado() {
        textViewValorCalculado.setText(FormatDataUtils.formatMonetaryValue(calcularValorComLucro()));
    }


    @Override
    public void OnDoubleItemClick(int position) {
        View dialog = LayoutInflater.from(this).inflate(R.layout.dialog_new_calc_seal_item,null);

        TextInputLayout editTextValorAgregado = dialog.findViewById(R.id.editTextNumberDecimalValorAgregado);
        Button butaoAdicionarValor =  dialog.findViewById(R.id.buttonAddNewValueInHelp);
        butaoAdicionarValor.setText("DELETE");
        editTextValorAgregado.getEditText().setText(String.valueOf(formatMonetaryValueDouble(listOfValoresAgregados.get(position))));
        butaoAdicionarValor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listOfValoresAgregados.remove(position);
                atualizarValorCalculado();
                atualizarRecyclerView();
                alertDialog.dismiss();
            }
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(this).setView(dialog);
        alertDialog=builder.create();
        alertDialog.show();
    }
}