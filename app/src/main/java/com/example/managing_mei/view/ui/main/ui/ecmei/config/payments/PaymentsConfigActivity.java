package com.example.managing_mei.view.ui.main.ui.ecmei.config.payments;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.managing_mei.R;
import com.example.managing_mei.adapters.AdapterConfigElement;
import com.example.managing_mei.model.entities.PaymentType;
import com.example.managing_mei.model.entities.PaymentsTypes;
import com.example.managing_mei.model.entities.QuantityType;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.example.managing_mei.utils.FireBaseConfig.firebaseDbReference;
import static com.example.managing_mei.utils.FireBaseConfig.firebaseInstance;
import static com.example.managing_mei.utils.FireBaseConfig.getIdUser;

public class PaymentsConfigActivity extends AppCompatActivity implements AdapterConfigElement.OnClickListener{

    private List<PaymentType> paymentTypeList = new ArrayList<>();
    private Button butaoSalvar, botaoCancelar, butaoNovo;
    private RecyclerView recyclerView;
    private AdapterConfigElement adapterConfigElement;
    private AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payments_config);

        recyclerView = findViewById(R.id.recyclerViewListOfPaymentTypes);
        butaoNovo = findViewById(R.id.buttonNewFormaPagamento);
        butaoSalvar = findViewById(R.id.buttonSaveNewPaymentsTypes);
        botaoCancelar = findViewById(R.id.buttonCancelNewPaymentForm);

        firebaseInstance.getReference()
                .child(getIdUser())
                .child("PaymentsType")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot ds: snapshot.getChildren()){
                            PaymentType post = ds.getValue(PaymentType.class);
                            paymentTypeList.add(post);
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

        butaoSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                salvarElementos();
            }
        });

        butaoNovo.setOnClickListener(new View.OnClickListener() {
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
        TextView textView  = dialog.findViewById(R.id.textViewExempleelemente);
        TextView title = dialog.findViewById(R.id.textViewTitleNewConfig);

        textView.setText("Exemplo:Dinheiro,Cheque");
        title.setText("Nova Forma de Pagamento");
        buttonSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(textInputLayout.getEditText().getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(),"Preencha o Campo",Toast. LENGTH_LONG).show();
                } else {
                    paymentTypeList.add(new PaymentType(firebaseDbReference.push().getKey(),
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
        paymentTypeList.forEach(PaymentType::save);
        this.finish();
    }


    private void reloadRecyclerView(){
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        adapterConfigElement = new AdapterConfigElement(getApplicationContext(),paymentTypeList,this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapterConfigElement);
    }


    @Override
    public void onConfigElementClick(int position) {
        View dialog = LayoutInflater.from(this).inflate(R.layout.dialog_new_config_element,null);

        TextInputLayout textInputLayout = dialog.findViewById(R.id.editTextNewTypeForProductConfig);
        Button buttonSalvar = dialog.findViewById(R.id.buttonsave);
        Switch aSwitch = dialog.findViewById(R.id.switchIsHabilitated);
        TextView textView  = dialog.findViewById(R.id.textViewExempleelemente);
        TextView title = dialog.findViewById(R.id.textViewTitleNewConfig);

        title.setText("Nova Forma de Pagamento");
        textInputLayout.getEditText().setText(paymentTypeList.get(position).getNome().toUpperCase());
        aSwitch.setChecked(paymentTypeList.get(position).getStatus());

        textView.setText("Exemplo:Dinheiro,Cheque");
        buttonSalvar.setText("SALVAR");

        buttonSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(textInputLayout.getEditText().getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(),"Preencha o Campo",Toast. LENGTH_LONG).show();
                } else {
                    paymentTypeList.get(position).setNome(textInputLayout.getEditText().getText().toString().toUpperCase());
                    paymentTypeList.get(position).setStatus(aSwitch.isChecked());
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