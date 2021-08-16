package com.example.managing_mei.view.ui.main.ui.product.addProduct;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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
import com.example.managing_mei.model.entities.Product;
import com.example.managing_mei.model.entities.Provider;
import com.example.managing_mei.model.entities.QuantitiesTypes;
import com.example.managing_mei.model.entities.QuantityType;
import com.example.managing_mei.view.ui.main.ui.product.helpToCalc.HelpToCalcSealValueActivity;
import com.google.android.gms.common.util.CollectionUtils;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static android.text.TextUtils.isEmpty;
import static com.example.managing_mei.utils.FireBaseConfig.firebaseDbReference;
import static com.example.managing_mei.utils.FireBaseConfig.firebaseInstance;
import static com.example.managing_mei.utils.FireBaseConfig.getIdUser;

public class AddProductActivity extends AppCompatActivity {

    private Spinner spinnerProductType,spinnerUnidadeDeMedida,spinnerProviders;
    private TextView counterProduct,date,textViewTitleQuantity;
    private SeekBar seekBar;
    private EditText sellValue,buyValue,nameProduct;
    private Button buttonAddProduct,buttonCancel;
    private ImageButton buttonHelpToCalc;
    public static boolean status;
    private List<Provider> providerList;
    private List<String> providersNames;
    private Provider provider;
    private Product definitiveProduct = new Product();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        textViewTitleQuantity = findViewById(R.id.textViewTitleQuantity);
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
        providerList = new ArrayList<>();

        firebaseInstance.getReference()
                .child(getIdUser())
                .child("providers")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        providerList.clear();
                        for (DataSnapshot ds:snapshot.getChildren()){
                            Provider provider = ds.getValue(Provider.class);
                            providerList.add(provider);
                            providersNames = new ArrayList<>();
                            providerList.stream().forEach(p -> {
                                providersNames.add(p.getFantasyName().toUpperCase());
                            });
                            ArrayAdapter adapter = new ArrayAdapter(getApplicationContext(),R.layout.item_spinner,providersNames);
                            spinnerProviders.setAdapter(adapter);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        String x =String.valueOf(error);
                    }
                });

        nameProduct.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {
                if(isEmpty(editable.toString())) {
                    Toast toast=Toast. makeText(getApplicationContext(),"Preencha o nome",Toast. LENGTH_SHORT);
                    toast. show();
                } else {
                    definitiveProduct.setName(editable.toString());
                }
            }
        });

        sellValue.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {
                if (isEmpty(editable.toString())){
                    Toast toast=Toast. makeText(getApplicationContext(),"Preencha o valor de venda",Toast. LENGTH_SHORT);
                    toast. show();
                } else {
                    definitiveProduct.setSealValue(Double.parseDouble(editable.toString()));
                }
            }
        });

        buyValue.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {
                if (isEmpty(editable.toString())){
                    Toast toast=Toast. makeText(getApplicationContext(),"Preencha o custo",Toast. LENGTH_SHORT);
                    toast. show();
                } else {
                    definitiveProduct.setExpenseValue(Double.parseDouble(editable.toString()));
                }
            }
        });

        counterProduct.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {
                if(isEmpty(editable.toString())) {
                    Toast toast=Toast. makeText(getApplicationContext(),"Preencha a quantidade",Toast. LENGTH_SHORT);
                    toast. show();
                } else {
                    definitiveProduct.setQuantity(Integer.parseInt(editable.toString()));
                }
            }
        });

        buttonAddProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                definitiveProduct.setDate(new Date());
                definitiveProduct.setId(firebaseDbReference.push().getKey());
                definitiveProduct.setProvider(provider);
                definitiveProduct.setTypeQuantity(spinnerUnidadeDeMedida.getSelectedItem().toString());
                definitiveProduct.save();
            }
        });

        listenerForSeekBar(1000,1,1);
        setValuesInSpinnerProductType();
        setValuesInSpinnerUnidadeDeMedida();
        setValuesInSpinnerProviders();

        actionForButtonCancel();
        actionForButtonCalcHelp();

    }

    private void setValuesInSpinnerProviders() {

        spinnerProviders.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                providerList.stream().forEach(p -> {
                    if (p.getFantasyName().equals(spinnerProviders.getSelectedItem().toString())){
                        provider =  p;
                    }
                });

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}});
    }

    private void setValuesInSpinnerUnidadeDeMedida() {
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
                spinnerUnidadeDeMedida.setAdapter(arrayAdapter);
                spinnerUnidadeDeMedida.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });


    }

    private void setValuesInSpinnerProductType() {
        ArrayAdapter arrayAdapter = new ArrayAdapter(getApplicationContext(),R.layout.item_spinner, new String[]{"PRODUTO", "SERVIÇO"});
        spinnerProductType.setAdapter(arrayAdapter);
        spinnerProductType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (spinnerProductType.getSelectedItem().toString().equals("SERVIÇO")) {
                    textViewTitleQuantity.setVisibility(View.INVISIBLE);
                    spinnerUnidadeDeMedida.setVisibility(View.INVISIBLE);
                    seekBar.setVisibility(View.INVISIBLE);
                }else if (spinnerProductType.getSelectedItem().toString().equals("PRODUTO")){
                    spinnerUnidadeDeMedida.setVisibility(View.VISIBLE);
                    seekBar.setVisibility(View.VISIBLE);
                }
                definitiveProduct.setType(spinnerProductType.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (status){
            Bundle bundle = getIntent().getExtras();
            sellValue.setText(""+bundle.getDouble("SealValue"));
            buyValue.setText(""+bundle.getDouble("expenses"));
        }

    }

    private void actionForButtonCalcHelp() {
        buttonHelpToCalc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), HelpToCalcSealValueActivity.class));
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
        counterProduct.setText("0");
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