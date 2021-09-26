package com.example.managing_mei.view.ui.main.ui.product.EditProduct;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.example.managing_mei.R;
import com.example.managing_mei.model.entities.Product;
import com.example.managing_mei.model.entities.Provider;
import com.example.managing_mei.model.entities.QuantitiesTypes;
import com.example.managing_mei.model.entities.QuantityType;
import com.example.managing_mei.view.ui.main.ui.ManagementActivity;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import static com.example.managing_mei.utils.FireBaseConfig.firebaseInstance;
import static com.example.managing_mei.utils.FireBaseConfig.getIdUser;
import static com.example.managing_mei.utils.FormatDataUtils.cleanFormat;
import static com.example.managing_mei.utils.FormatDataUtils.cleanFormatValues;
import static com.example.managing_mei.utils.FormatDataUtils.formatMonetaryValue;

public class EditProductActivity extends AppCompatActivity {

    private Product productRecuperado = new Product();
    private Product productEditado = new Product();
    private TextView contadorDoSeekBar, tituloTipoQuantidade,titleSeek;
    private TextInputLayout nomeProduto, despesasProduto, valorDeVendaProduto;
    private Spinner spinnerTipoDeQuantidade, spinnerFornecedor;
    private SeekBar seekBar;
    private Button buttonAtualizar, buttonCancelar,buttonDeletar;
    private final LinkedList<Provider> listaDeFornecedoresRecuperada = new LinkedList<>();
    private Integer fornecedorEscolhido;
    private Switch switchWhioutProvider;
    private TextView textViewTitleProviderEditProduct;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_product);

        recuperarProduto();

        tituloTipoQuantidade = findViewById(R.id.TextViewTypeProduct);
        nomeProduto = findViewById(R.id.editTextNameProductEdit);
        valorDeVendaProduto = findViewById(R.id.editTextSealValueProductEdit);
        despesasProduto = findViewById(R.id.editTextExpenseValueProductEdit);
        spinnerFornecedor = findViewById(R.id.spinnerProviderProduct);
        titleSeek = findViewById(R.id.textViewTitleQuantity);
        seekBar = findViewById(R.id.seekBarProduct);
        spinnerTipoDeQuantidade = findViewById(R.id.spinnerTypeOfQuantityProductEdit);
        contadorDoSeekBar = findViewById(R.id.textViewCounterProductEdit);
        buttonAtualizar = findViewById(R.id.buttonAddProductEdit);
        buttonCancelar = findViewById(R.id.buttonCancelProductEdit);
        buttonDeletar = findViewById(R.id.buttonDeleteProduct);
        switchWhioutProvider = findViewById(R.id.switchWhioutProvider);
        textViewTitleProviderEditProduct = findViewById(R.id.textViewTitleProviderEditProduct);

        buttonCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    this.finalize();
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }
            }
        });

        switchWhioutProvider.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    spinnerFornecedor.setVisibility(View.INVISIBLE);
                    textViewTitleProviderEditProduct.setVisibility(View.INVISIBLE);
                } else {
                    spinnerFornecedor.setVisibility(View.VISIBLE);
                    textViewTitleProviderEditProduct.setVisibility(View.VISIBLE);
                    setValuesInSpinnerProviders();
                }
            }
        });

        tituloTipoQuantidade.setText(productRecuperado.getType().toUpperCase());
        nomeProduto.getEditText().setText(productRecuperado.getName().toUpperCase());
        despesasProduto.getEditText().setText(formatMonetaryValue(productRecuperado.getExpenseValue()));
        contadorDoSeekBar.setText(productRecuperado.getQuantity().toString());
        valorDeVendaProduto.getEditText().setText(formatMonetaryValue(productRecuperado.getSealValue()));

        despesasProduto.getEditText().setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b){
                    despesasProduto.getEditText().setText(cleanFormatValues(despesasProduto.getEditText().getText().toString()));
                } else {
                    despesasProduto.getEditText().setText(formatMonetaryValue(Double.valueOf(despesasProduto.getEditText().getText().toString())));
                }
            }
        });
        valorDeVendaProduto.getEditText().setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b){
                    valorDeVendaProduto.getEditText().setText(cleanFormatValues(valorDeVendaProduto.getEditText().getText().toString()));
                } else {
                    valorDeVendaProduto.getEditText().setText(formatMonetaryValue(Double.valueOf(valorDeVendaProduto.getEditText().getText().toString())));
                }
            }
        });

        setSpinnerQuantitiesType();
        setValuesInSpinnerProviders();
        setButtonActions();
        setSeekBar();
    }

    private void recuperarProduto() {
        Bundle bundle = getIntent().getExtras();
        productRecuperado.setId(bundle.getString("id"));
        productRecuperado.setType(bundle.getString("type"));
        productRecuperado.setExpenseValue(Double.valueOf(bundle.getString("ExpenseValue")));
        productRecuperado.setName(bundle.getString("Name"));
        productRecuperado.setQuantity(Integer.valueOf(bundle.getString("Quantity")));
        productRecuperado.setSealValue(Double.valueOf(bundle.getString("SealValue")));
        productRecuperado.setTypeQuantity(bundle.getString("typeQuantity"));
        productRecuperado.setProviderId(bundle.getString("ProviderId"));
    }

    private boolean validarCampos() {
        if (nomeProduto.getEditText().getText().toString().isEmpty()){
            Toast.makeText(getApplicationContext(),"Preencha o nome",Toast. LENGTH_SHORT).show();
            return false;
        } else if (valorDeVendaProduto.getEditText().getText().toString().isEmpty()){
            Toast.makeText(getApplicationContext(),"Preencha o valor de venda",Toast. LENGTH_SHORT).show();
            return false;
        } else if (despesasProduto.getEditText().getText().toString().isEmpty()){
            Toast.makeText(getApplicationContext(),"Preencha as despesas",Toast. LENGTH_SHORT).show();
            return false;
        } else {
            productRecuperado.setName(nomeProduto.getEditText().getText().toString());
            productRecuperado.setExpenseValue(Double.valueOf(cleanFormatValues(despesasProduto.getEditText().getText().toString())));
            productRecuperado.setSealValue(Double.valueOf(cleanFormatValues(valorDeVendaProduto.getEditText().getText().toString())));
        }
        productRecuperado.setTypeQuantity(spinnerTipoDeQuantidade.getSelectedItem().toString());
        productRecuperado.setType(spinnerTipoDeQuantidade.getSelectedItem().toString());
        productRecuperado.setProviderId(listaDeFornecedoresRecuperada.get(fornecedorEscolhido).getId());
        productRecuperado.setQuantity(Integer.parseInt(contadorDoSeekBar.getText().toString()));
        return true;
    }



    private void setValuesInSpinnerProviders() {
        firebaseInstance.getReference()
                .child(getIdUser())
                .child("providers")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        listaDeFornecedoresRecuperada.clear();
                        for (DataSnapshot ds:snapshot.getChildren()){
                            Provider provider = ds.getValue(Provider.class);
                            listaDeFornecedoresRecuperada.add(provider);
                        }
                        if (productRecuperado.getProviderId().equals("SEM FORNECEDOR")){
                            switchWhioutProvider.setChecked(true);
                            switchWhioutProvider.setClickable(false);
                        } else {
                            Provider providerToMove = listaDeFornecedoresRecuperada.stream().filter(provider -> provider.getId().equals(productRecuperado.getProviderId())).findAny().orElse(null);
                            listaDeFornecedoresRecuperada.remove(providerToMove);
                            listaDeFornecedoresRecuperada.addFirst(providerToMove);
                            ArrayAdapter adapter = new ArrayAdapter(getApplicationContext(), R.layout.item_spinner, listaDeFornecedoresRecuperada.stream().map(Provider::getFantasyName).collect(Collectors.toList()));
                            spinnerFornecedor.setAdapter(adapter);
                            spinnerFornecedor.setSelection(listaDeFornecedoresRecuperada.indexOf(providerToMove));
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        String x =String.valueOf(error);
                    }
                });

        spinnerFornecedor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                listaDeFornecedoresRecuperada.stream().forEach(p -> {
                    if (p.getFantasyName().equals(spinnerFornecedor.getSelectedItem().toString())){
                        fornecedorEscolhido = listaDeFornecedoresRecuperada.indexOf(p);
                    }
                });
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}});
    }

    private void setSeekBar() {
        seekBar.setMax(1000);
        seekBar.setMin(1);
        seekBar.setProgress(productRecuperado.getQuantity());
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                contadorDoSeekBar.setText(""+i);
                contadorDoSeekBar.addTextChangedListener(new TextWatcher() {
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

    private void setSpinnerQuantitiesType() {
        List<QuantityType> quantitiesTypes = new ArrayList<>();

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference(getIdUser()+"/QuantitiesTypes");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                QuantitiesTypes post = dataSnapshot.getValue(QuantitiesTypes.class);
                System.out.println(post);
                quantitiesTypes.addAll(post.getQuantityTypeArrayList());

                List<String> listOfQuantitiesNames = new ArrayList<>();
                quantitiesTypes.stream().forEach(quantityType ->{
                    listOfQuantitiesNames.add(quantityType.getNome().toUpperCase());
                });
                ArrayAdapter arrayAdapter = new ArrayAdapter(getApplicationContext(),R.layout.item_spinner,listOfQuantitiesNames);
                spinnerTipoDeQuantidade.setAdapter(arrayAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });


        spinnerTipoDeQuantidade.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void setButtonActions() {
        buttonAtualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validarCampos()){
                    productRecuperado.save();
                    startActivity(new Intent(getApplicationContext(),ManagementActivity.class));
                }
            }
        });

        buttonCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        buttonDeletar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteProduct();
            }
        });
    }

    private void deleteProduct(){
        productRecuperado.delete();
        Toast toast=Toast. makeText(getApplicationContext(),"Produto Deletado",Toast. LENGTH_LONG);
        toast.show();
        finish();
    }

}