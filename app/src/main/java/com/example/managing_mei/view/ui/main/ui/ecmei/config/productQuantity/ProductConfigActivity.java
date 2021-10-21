package com.example.managing_mei.view.ui.main.ui.ecmei.config.productQuantity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.Toast;

import com.example.managing_mei.R;
import com.example.managing_mei.adapters.AdapterClient;
import com.example.managing_mei.adapters.AdapterConfigElement;
import com.example.managing_mei.model.entities.Client;
import com.example.managing_mei.model.entities.QuantityType;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.example.managing_mei.utils.FireBaseConfig.firebaseDbReference;
import static com.example.managing_mei.utils.FireBaseConfig.firebaseInstance;
import static com.example.managing_mei.utils.FireBaseConfig.getIdUser;

public class ProductConfigActivity extends AppCompatActivity implements AdapterConfigElement.OnClickListener {

    private List<QuantityType> mainListOfQuantityTypes = new ArrayList<>();
    private Button botaoSalvar,botaoCancelar,botaoAdicionarUnidade;
    private RecyclerView recyclerView;
    private AdapterConfigElement adapterConfigElement;
    private AlertDialog alertDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_config);

        recyclerView = findViewById(R.id.recyclerViewConfigUnitMeds);
        botaoSalvar = findViewById(R.id.buttonSaveProductConfig);
        botaoCancelar = findViewById(R.id.buttonCancelProductConfigjsdpogfbnasdoi);
        botaoAdicionarUnidade = findViewById(R.id.buttonNewMeds2);

        firebaseInstance.getReference()
                .child(getIdUser())
                .child("QuantitiesTypes")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot ds: snapshot.getChildren()){
                            QuantityType post = ds.getValue(QuantityType.class);
                            mainListOfQuantityTypes.add(post);
                            reloadRecyclerView();
                        }
                        adapterConfigElement.notifyDataSetChanged();
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        String x = String.valueOf(error);
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
                if ((mainListOfQuantityTypes.stream().filter(quantityType -> quantityType.getStatus().equals(true)).collect(Collectors.toList())).size() == 0) {
                    Toast.makeText(getApplicationContext(),"No minimo um item deve estar ativado",Toast. LENGTH_LONG).show();
                } else {
                    salvarElementos();
                }
            }
        });

        botaoAdicionarUnidade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callDialog();
            }
        });


    }

    private void callDialog() {
        View dialog = LayoutInflater.from(this).inflate(R.layout.dialog_new_config_element,null);

        TextInputLayout textInputLayout = dialog.findViewById(R.id.editTextNewTypeForProductConfig);
        Button buttonSalvar = dialog.findViewById(R.id.buttonsave);
        Switch aSwitch = dialog.findViewById(R.id.switchIsHabilitated);

        buttonSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(textInputLayout.getEditText().getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(),"Preencha o Campo",Toast. LENGTH_LONG).show();
                } else {
                    mainListOfQuantityTypes.add(new QuantityType(firebaseDbReference.push().getKey(),
                                                                 textInputLayout.getEditText().getText().toString(),
                                                                 aSwitch.isChecked()));
                    reloadRecyclerView();
                    textInputLayout.getEditText().setText("");
                    alertDialog.dismiss();
                }

            }
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(this).setView(dialog);
        alertDialog=builder.create();
        alertDialog.show();
    }

    private void salvarElementos() {
        mainListOfQuantityTypes.forEach(QuantityType::save);
        this.finish();
    }


    private void reloadRecyclerView(){
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        adapterConfigElement = new AdapterConfigElement(getApplicationContext(),mainListOfQuantityTypes,this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapterConfigElement);
    }

    @Override
    public void onConfigElementClick(int position) {
        View dialog = LayoutInflater.from(this).inflate(R.layout.dialog_new_config_element,null);

        @SuppressLint("UseSwitchCompatOrMaterialCode")
        Switch aSwitch = dialog.findViewById(R.id.switchIsHabilitated);
        TextInputLayout textInputLayout = dialog.findViewById(R.id.editTextNewTypeForProductConfig);
        Button buttonSalvar = dialog.findViewById(R.id.buttonsave);

        textInputLayout.getEditText().setText(mainListOfQuantityTypes.get(position).getNome().toUpperCase());
        aSwitch.setChecked(mainListOfQuantityTypes.get(position).getStatus());
        buttonSalvar.setText("SALVAR");
        buttonSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(textInputLayout.getEditText().getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(),"Preencha o Campo",Toast. LENGTH_LONG).show();
                } else {
                    mainListOfQuantityTypes.get(position).setNome(textInputLayout.getEditText().getText().toString().toUpperCase());
                    mainListOfQuantityTypes.get(position).setStatus(aSwitch.isChecked());
                    reloadRecyclerView();
                    textInputLayout.getEditText().setText("");
                    alertDialog.dismiss();
                }
            }
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(this).setView(dialog);
        alertDialog=builder.create();
        alertDialog.show();
    }


}