package com.example.managing_mei.view.ui.main.ui.providers.addProviders;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.managing_mei.R;
import com.example.managing_mei.model.entities.Provider;
import com.example.managing_mei.view.ui.main.ui.providers.ProvidersFragment;
import com.google.android.material.textfield.TextInputLayout;

import org.apache.commons.validator.routines.EmailValidator;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;

import static com.example.managing_mei.utils.FireBaseConfig.firebaseDbReference;
import static com.example.managing_mei.utils.FormatDataUtils.cleanFormat;
import static com.example.managing_mei.utils.FormatDataUtils.formatCpfOrCnpj;
import static com.example.managing_mei.utils.FormatDataUtils.formatPhoneNumber;

public class AddProvidersActivity extends AppCompatActivity {


    private TextInputLayout fantasyName,address, cnpj, phoneNumber, email;
    private RatingBar evaluation;
    private TextView data;
    private Button buttonAdd,buttonCancel;
    private SimpleDateFormat simpleDateFormat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_providers);
        simpleDateFormat=new SimpleDateFormat("dd/MM/yyyy");

        fantasyName = findViewById(R.id.editTextNomeFantasiaProvider);
        address = findViewById(R.id.editTextEnderecoProvider);
        cnpj = findViewById(R.id.editTextCNPJProvider);
        phoneNumber = findViewById(R.id.editTextPhoneNumberProvider);
        email = findViewById(R.id.editTextTextEmailAddressProvider);
        evaluation = findViewById(R.id.ratingBarProvider);
        data = findViewById(R.id.textViewDateProvider);
        buttonAdd = findViewById(R.id.buttonAddProvider);
        buttonCancel = findViewById(R.id.buttonCancelProvider);

        data.setText(simpleDateFormat.format(System.currentTimeMillis()).toString());

        phoneNumber.getEditText().setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                phoneNumber.getEditText().setText(formatPhoneNumber(phoneNumber.getEditText().getText().toString()));
            }
        });

        cnpj.getEditText().setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                cnpj.getEditText().setText(formatCpfOrCnpj(cnpj.getEditText().getText().toString()));
            }
        });

        actionForButtonAdd();
        actionForButtonCancel();
    }
    private void validateFields(){
        if (!EmailValidator.getInstance().isValid(email.getEditText().getText().toString())) {
            Toast toast = Toast.makeText(getApplicationContext(), "O E-mail não e valido", Toast.LENGTH_LONG);
            toast.show();
        } else {
            if (fantasyName.getEditText().getText().toString().isEmpty()){
                Toast toast=Toast.makeText(getApplicationContext(),"Preencha o Nome Fantasia",Toast. LENGTH_LONG);
                toast. show();
            }else if (address.getEditText().getText().toString().isEmpty()){
                Toast toast=Toast.makeText(getApplicationContext(),"Preencha o Endereço",Toast. LENGTH_LONG);
                toast. show();
            }else if (cnpj.getEditText().getText().toString().isEmpty()){
                Toast toast=Toast.makeText(getApplicationContext(),"Preencha o CPNJ",Toast. LENGTH_LONG);
                toast. show();
            }else if (phoneNumber.getEditText().getText().toString().isEmpty()){
                Toast toast=Toast.makeText(getApplicationContext(),"Preencha o Telefone",Toast. LENGTH_LONG);
                toast. show();
            }else if (address.getEditText().getText().toString().isEmpty()){
                Toast toast=Toast.makeText(getApplicationContext(),"Preencha o Endereço",Toast. LENGTH_LONG);
                toast. show();
            }else if (email.getEditText().getText().toString().isEmpty()) {
                Toast toast = Toast.makeText(getApplicationContext(), "Preencha o E-mail", Toast.LENGTH_LONG);
                toast.show();
            } if (cnpj.getEditText().getText().toString().length()<10) {
                Toast toast = Toast.makeText(getApplicationContext(), "CNPJ invalido", Toast.LENGTH_LONG);
                toast.show();
            } else if (phoneNumber.getEditText().getText().toString().length()<6) {
                Toast toast = Toast.makeText(getApplicationContext(), "Telefone invalido", Toast.LENGTH_LONG);
                toast.show();
            } else {
                saveProvider(new Provider(
                        firebaseDbReference.push().getKey(),
                        fantasyName.getEditText().getText().toString(),
                        cleanFormat(cnpj.getEditText().getText().toString()),
                        cleanFormat(phoneNumber.getEditText().getText().toString()),
                        email.getEditText().getText().toString(),
                        address.getEditText().getText().toString(),
                        evaluation.getRating()));
            }
        }
    }

    private void saveProvider(Provider provider) {
        provider.save();
        Toast toast=Toast. makeText(getApplicationContext(),"Fornecedor Cadastrado",Toast. LENGTH_LONG);
        toast. show();
        finish();
    }

    private void actionForButtonCancel() {
        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void actionForButtonAdd(){
        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validateFields();
            }
        });
    }

}