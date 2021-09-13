package com.example.managing_mei.view.ui.main.ui.product.addProduct;

import androidx.annotation.NonNull;
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
import android.widget.Toast;

import com.example.managing_mei.R;
import com.example.managing_mei.model.entities.CashFlowItem;
import com.example.managing_mei.model.entities.Product;
import com.example.managing_mei.model.entities.Provider;
import com.example.managing_mei.model.entities.QuantitiesTypes;
import com.example.managing_mei.model.entities.QuantityType;
import com.example.managing_mei.view.ui.main.ui.ManagementActivity;
import com.example.managing_mei.view.ui.main.ui.product.helpToCalc.HelpToCalcSealValueActivity;
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

import static com.example.managing_mei.utils.FireBaseConfig.firebaseDbReference;
import static com.example.managing_mei.utils.FireBaseConfig.firebaseInstance;
import static com.example.managing_mei.utils.FireBaseConfig.getIdUser;

public class AddProductActivity extends AppCompatActivity {

    private Spinner spinnerTipoDoProduto,spinnerUnidadeDeMedida, spinnerFornecedores;
    private TextView contatorDaSeekBar, tituloDaQuantidade;
    private SeekBar seekBar;
    private TextInputLayout valorDeVenda,despensas,nomeDoProduto;
    private Button botaoAdicionarProduto,botaoCancelarCadastroDeProduto;
    private Product produtoDefinitivo = new Product();

    private final LinkedList<Provider> listaDeFornecedoresRecuperada = new LinkedList<>();
    private Integer fornecedorEscolhido;

    private final LinkedList<String> tipoDoProduto = new LinkedList<>();
    private Integer tipoProdutoEscolhido;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        tituloDaQuantidade = findViewById(R.id.textViewTitleQuantity);
        spinnerTipoDoProduto = findViewById(R.id.spinnerTypeProduct);
        spinnerUnidadeDeMedida = findViewById(R.id.spinnerTypeOfQuantityProduct);
        spinnerFornecedores = findViewById(R.id.spinnerProviderProduct);
        contatorDaSeekBar = findViewById(R.id.textViewCounterProduct);
        seekBar = findViewById(R.id.seekBarProduct);
        nomeDoProduto = findViewById(R.id.editTextNameProduct);
        valorDeVenda = findViewById(R.id.editTextSealValueProduct);
        despensas = findViewById(R.id.editTextExpenseValueProduct);
        botaoAdicionarProduto = findViewById(R.id.buttonAddProduct);
        botaoCancelarCadastroDeProduto = findViewById(R.id.buttonCancelProduct);


        Bundle bundle = getIntent().getExtras();
        valorDeVenda.getEditText().setText(""+bundle.getDouble("SealValue"));
        despensas.getEditText().setText(""+bundle.getDouble("expenses"));

        listenerForSeekBar(1000,1);
        setValuesInSpinnerProductType();
        setValuesInSpinnerUnidadeDeMedida();
        setValuesInSpinnerProviders();

///////////////////////////////////////////////////////////////////////////////////////////////////

        botaoCancelarCadastroDeProduto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), ManagementActivity.class));
            }
        });

///////////////////////////////////////////////////////////////////////////////////////////////////

        botaoAdicionarProduto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validarCampos()){
                    nomeDoProduto.getEditText().setText("0");
                    CashFlowItem cashFlowItem = new CashFlowItem(0,produtoDefinitivo.getQuantity()*produtoDefinitivo.getExpenseValue(),"Compra de produtos");
                    cashFlowItem.save();
                    produtoDefinitivo.save();
                    startActivity(new Intent(getApplicationContext(), ManagementActivity.class));
                }
            }
        });
    }

    private boolean validarCampos() {
        produtoDefinitivo.setId(firebaseDbReference.push().getKey());

        if (nomeDoProduto.getEditText().getText().toString().isEmpty()){
            Toast.makeText(getApplicationContext(),"Preencha o nome",Toast. LENGTH_SHORT).show();
            return false;
        } else if (valorDeVenda.getEditText().getText().toString().isEmpty()){
            Toast.makeText(getApplicationContext(),"Preencha o valor de venda",Toast. LENGTH_SHORT).show();
            return false;
        } else if (despensas.getEditText().getText().toString().isEmpty()){
            Toast.makeText(getApplicationContext(),"Preencha as despesas",Toast. LENGTH_SHORT).show();
            return false;
        } else {
            produtoDefinitivo.setName(nomeDoProduto.getEditText().getText().toString());
            produtoDefinitivo.setExpenseValue(Double.valueOf(despensas.getEditText().getText().toString()));
            produtoDefinitivo.setSealValue(Double.valueOf(valorDeVenda.getEditText().getText().toString()));
        }

        produtoDefinitivo.setTypeQuantity(spinnerUnidadeDeMedida.getSelectedItem().toString());
        produtoDefinitivo.setType(tipoDoProduto.get(tipoProdutoEscolhido));
        produtoDefinitivo.setProviderId(listaDeFornecedoresRecuperada.get(fornecedorEscolhido).getId());

        produtoDefinitivo.setQuantity(Integer.parseInt(contatorDaSeekBar.getText().toString()));

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

                        ArrayAdapter adapter = new ArrayAdapter(getApplicationContext(),
                                                                R.layout.item_spinner,
                                                                listaDeFornecedoresRecuperada.stream()
                                                                                             .map(Provider::getFantasyName)
                                                                                             .collect(Collectors.toList()));
                        spinnerFornecedores.setAdapter(adapter);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        String x =String.valueOf(error);
                    }
                });

        spinnerFornecedores.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                listaDeFornecedoresRecuperada.stream().forEach(p -> {
                    if (p.getFantasyName().equals(spinnerFornecedores.getSelectedItem().toString())){
                        fornecedorEscolhido = listaDeFornecedoresRecuperada.indexOf(p);
                    }
                });
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}});
    }

    private void setValuesInSpinnerUnidadeDeMedida() {
        LinkedList<QuantityType> quantitiesTypes = new LinkedList<>();

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
                spinnerUnidadeDeMedida.setAdapter(arrayAdapter);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });

    }

    private void setValuesInSpinnerProductType() {
        tipoDoProduto.add("PRODUTO");
        tipoDoProduto.add("SERVIÇO");

        ArrayAdapter arrayAdapter = new ArrayAdapter(getApplicationContext(),R.layout.item_spinner,tipoDoProduto);
        spinnerTipoDoProduto.setAdapter(arrayAdapter);
        spinnerTipoDoProduto.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (spinnerTipoDoProduto.getSelectedItem().toString().equals("SERVIÇO")) {
                    tituloDaQuantidade.setVisibility(View.INVISIBLE);
                    spinnerUnidadeDeMedida.setVisibility(View.INVISIBLE);
                    seekBar.setVisibility(View.INVISIBLE);
                }else if (spinnerTipoDoProduto.getSelectedItem().toString().equals("PRODUTO")){
                    spinnerUnidadeDeMedida.setVisibility(View.VISIBLE);
                    seekBar.setVisibility(View.VISIBLE);
                }
                tipoProdutoEscolhido = tipoDoProduto.indexOf(spinnerTipoDoProduto.getSelectedItem().toString());
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    public void listenerForSeekBar(Integer max,Integer min){
        contatorDaSeekBar.setText("1");
        seekBar.setMax(max);
        seekBar.setMin(min);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @SuppressLint("SetTextI18n")
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                contatorDaSeekBar.setText("" + seekBar.getProgress());
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });
    }



}