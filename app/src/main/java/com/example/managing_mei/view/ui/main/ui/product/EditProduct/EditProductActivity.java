package com.example.managing_mei.view.ui.main.ui.product.EditProduct;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.example.managing_mei.R;
import com.example.managing_mei.model.entities.PaymentsTypes;
import com.example.managing_mei.model.entities.Product;
import com.example.managing_mei.model.entities.Provider;
import com.example.managing_mei.model.entities.QuantityType;
import com.example.managing_mei.view.ui.main.ui.ecmei.config.productQuantity.ProductConfigActivity;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.example.managing_mei.utils.FireBaseConfig.firebaseInstance;
import static com.example.managing_mei.utils.FireBaseConfig.getIdUser;
import static com.example.managing_mei.utils.FormatDataUtils.cleanFormatValues;
import static com.example.managing_mei.utils.FormatDataUtils.formatMonetary;
import static com.example.managing_mei.utils.FormatDataUtils.formatMonetaryValue;
import static com.example.managing_mei.utils.FormatDataUtils.formatMonetaryValueDouble;

public class EditProductActivity extends AppCompatActivity {

    private Product productRecuperado = new Product();
    private TextInputLayout valorDeVenda,despensas,nomeDoProduto;
    private Spinner spinnerFornecedor,spinnerTipoQuantidade;
    private Switch aSwitchSemFornececor;
    private Button botaoDeletar,botaoSalvar;
    private ImageButton imageButtonConfig;
    private SeekBar seekBarQuantidade;
    private TextView tituloQuantidade,tituloFornecedor,textViewContador;
    private List<Provider> listaDeFornecedores = new ArrayList<>();
    private List<QuantityType> listaDeTipoDeQuantidade = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_product);

        nomeDoProduto = findViewById(R.id.editTextNameProductEdit);
        valorDeVenda = findViewById(R.id.editTextSellValueInAddProductEdit);
        despensas = findViewById(R.id.editTextExpenseValueProductEdit);

        spinnerFornecedor = findViewById(R.id.spinnerProviderProductEdit);
        spinnerTipoQuantidade = findViewById(R.id.spinnerTypeOfQuantityProductEdit);

        aSwitchSemFornececor = findViewById(R.id.switchWhioutProviderEdit);

        botaoDeletar = findViewById(R.id.buttonDeleteProductEdit);
        botaoSalvar = findViewById(R.id.buttonSaveProductEdit);

        imageButtonConfig = findViewById(R.id.imageButtonConfigProductEditEdit);

        seekBarQuantidade = findViewById(R.id.seekBarProductEdit);

        tituloFornecedor = findViewById(R.id.textViewTitleSpinerProviderEdit);
        tituloQuantidade = findViewById(R.id.textViewTitleQuantityEdit);
        textViewContador = findViewById(R.id.textViewCounterProductEdit);

        Bundle bundle = getIntent().getExtras();
        firebaseInstance.getReference()
                .child(getIdUser())
                .child("product")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Product provider = null;
                        for (DataSnapshot ds: snapshot.getChildren()){
                            provider = ds.getValue(Product.class);
                            if (provider.getId().equals(bundle.getString("id"))) {
                                break;
                            }
                        }
                        productRecuperado.setType(provider.getType());
                        productRecuperado.setExpenseValue(provider.getExpenseValue());
                        productRecuperado.setId(provider.getId());
                        productRecuperado.setName(provider.getName());
                        productRecuperado.setSealValue(provider.getSealValue());
                        productRecuperado.setTypeQuantity(provider.getTypeQuantity());
                        productRecuperado.setQuantity(provider.getQuantity());
                        setValuesInSeekBar();
                        productRecuperado.setProviderId(provider.getProviderId());
                        fillFields();
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        String x = String.valueOf(error);
                    }
                });



        valorDeVenda.getEditText().setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b){
                    valorDeVenda.getEditText().setText(cleanFormatValues(valorDeVenda.getEditText().getText().toString()));
                }else{
                    if (!valorDeVenda.getEditText().getText().toString().isEmpty()){
                        valorDeVenda.getEditText().setText(formatMonetaryValue(Double.parseDouble(valorDeVenda.getEditText().getText().toString())));
                    }
                }
            }
        });

        despensas.getEditText().setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b){
                    despensas.getEditText().setText(cleanFormatValues(despensas.getEditText().getText().toString()));
                }else{
                    if (!despensas.getEditText().getText().toString().isEmpty()) {
                        despensas.getEditText().setText(formatMonetaryValue(Double.parseDouble(despensas.getEditText().getText().toString())));
                    }
                }
            }
        });

        botaoDeletar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deletarProduto();
            }
        });

        botaoSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                salvarProduto();
            }
        });

        imageButtonConfig.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listaDeTipoDeQuantidade.clear();
                startActivity(new Intent(getApplicationContext(), ProductConfigActivity.class));
            }
        });

        aSwitchSemFornececor.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    tituloFornecedor.setVisibility(View.INVISIBLE);
                    spinnerFornecedor.setVisibility(View.INVISIBLE);
                } else {
                    tituloFornecedor.setVisibility(View.VISIBLE);
                    spinnerFornecedor.setVisibility(View.VISIBLE);
                }
            }
        });

        setValuesInSpinnerFornecedor();
    }

    @Override
    protected void onStart() {
        super.onStart();
        listaDeTipoDeQuantidade.clear();
    }

    @SuppressLint("SetTextI18n")
    private void setValuesInSeekBar() {
        seekBarQuantidade.setMax(1000);
        seekBarQuantidade.setMin(1);
        seekBarQuantidade.setProgress(productRecuperado.getQuantity());
        textViewContador.setText(productRecuperado.getQuantity().toString());
        seekBarQuantidade.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                textViewContador.setText(""+i);
                textViewContador.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }
                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }
                    @Override
                    public void afterTextChanged(Editable editable) {
                        productRecuperado.setQuantity(Integer.parseInt(editable.toString()));}
                });
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar){

            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }});
    }

    private void setValuesInSpinnerTipoQuantidade() {
        listaDeTipoDeQuantidade.clear();
        firebaseInstance.getReference()
                .child(getIdUser())
                .child("QuantitiesTypes")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot ds:snapshot.getChildren()){
                            QuantityType provider = ds.getValue(QuantityType.class);
                            listaDeTipoDeQuantidade.add(provider);
                        }

                        QuantityType quantityTypeToMove = listaDeTipoDeQuantidade.stream().filter(quantityType -> quantityType.getNome().equals(productRecuperado.getTypeQuantity())).findFirst().orElse(null);
                        if (quantityTypeToMove.getStatus().equals(false)) {
                            quantityTypeToMove.setStatus(true);
                        }

                        ArrayAdapter adapter = new ArrayAdapter(getApplicationContext(), R.layout.item_spinner, listaDeTipoDeQuantidade.stream().filter(quantityType -> quantityType.getStatus().equals(true)).map(QuantityType::getNome).collect(Collectors.toList()));
                        spinnerTipoQuantidade.setAdapter(adapter);
                        Integer position = listaDeTipoDeQuantidade.stream().filter(quantityType -> quantityType.getStatus().equals(true)).collect(Collectors.toList()).indexOf(quantityTypeToMove);
                        spinnerTipoQuantidade.setSelection(position);

                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        String x =String.valueOf(error);
                    }
                });
    }

    private void setValuesInSpinnerFornecedor() {

        firebaseInstance.getReference()
                .child(getIdUser())
                .child("providers")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        listaDeFornecedores.clear();
                        for (DataSnapshot ds:snapshot.getChildren()){
                            Provider provider = ds.getValue(Provider.class);
                            listaDeFornecedores.add(provider);
                        }

                        ArrayAdapter adapter = new ArrayAdapter(getApplicationContext(),R.layout.item_spinner,listaDeFornecedores.stream().map(Provider::getFantasyName).collect(Collectors.toList()));
                        spinnerFornecedor.setAdapter(adapter);

                        if (productRecuperado.getProviderId().equals("SEM FORNECEDOR")){
                                tituloFornecedor.setVisibility(View.INVISIBLE);
                                spinnerFornecedor.setVisibility(View.INVISIBLE);
                                aSwitchSemFornececor.setChecked(true);
                        } else {
                            Provider providerToMove = listaDeFornecedores.stream()
                                    .filter(provider -> provider.getId().equals(productRecuperado.getProviderId()))
                                    .findAny()
                                    .orElse(null);
                            spinnerFornecedor.setSelection(listaDeFornecedores.indexOf(providerToMove));
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        String x =String.valueOf(error);
                    }
                });
    }

    private void deletarProduto() {
        productRecuperado.delete();
        Toast.makeText(getApplicationContext(),"Produto Deletado",Toast. LENGTH_LONG).show();
        finish();
    }

    private void salvarProduto() {
        if (nomeDoProduto.getEditText().getText().toString().isEmpty()) {
            Toast.makeText(getApplicationContext(),"Produto Deletado",Toast. LENGTH_LONG).show();
        }else if (valorDeVenda.getEditText().getText().toString().isEmpty()) {
            Toast.makeText(getApplicationContext(),"Produto Deletado",Toast. LENGTH_LONG).show();
        } else if (despensas.getEditText().getText().toString().isEmpty()) {
            Toast.makeText(getApplicationContext(),"Produto Deletado",Toast. LENGTH_LONG).show();
        } else {
            productRecuperado.setName(nomeDoProduto.getEditText().getText().toString().toUpperCase());
            productRecuperado.setSealValue(formatMonetary(valorDeVenda.getEditText().getText().toString()));
            productRecuperado.setExpenseValue(formatMonetary(despensas.getEditText().getText().toString()));
            productRecuperado.setQuantity(Integer.parseInt(textViewContador.getText().toString()));
            productRecuperado.setTypeQuantity(spinnerTipoQuantidade.getSelectedItem().toString());
            if (aSwitchSemFornececor.isChecked()){
                productRecuperado.setProviderId("SEM FORNECEDOR");
            } else {
                productRecuperado.setProviderId(listaDeFornecedores.stream().filter(provider -> provider.getFantasyName().equals(spinnerFornecedor.getSelectedItem().toString())).map(Provider::getId).collect(Collectors.joining()));
            }
            productRecuperado.save();
            finish();
        }
    }

    private void fillFields() {
        nomeDoProduto.getEditText().setText(productRecuperado.getName().toUpperCase());
        valorDeVenda.getEditText().setText(formatMonetaryValue(productRecuperado.getSealValue()));
        despensas.getEditText().setText(formatMonetaryValue(productRecuperado.getExpenseValue()));
        setValuesInSpinnerTipoQuantidade();
        setValuesInSpinnerFornecedor();

    }

}