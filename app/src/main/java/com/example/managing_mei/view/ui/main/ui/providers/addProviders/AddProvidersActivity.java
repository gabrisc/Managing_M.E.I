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

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;

import static com.example.managing_mei.utils.FireBaseConfig.firebaseDbReference;

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


        data.setText(simpleDateFormat.format(System.currentTimeMillis()).toString());

        buttonAdd = findViewById(R.id.buttonAddProvider);
        buttonCancel = findViewById(R.id.buttonCancelProvider);

        actionForButtonAdd();
        actionForButtonCancel();
    }
    private void validateFields(){

        if (fantasyName.getEditText().getText().toString().isEmpty()){
            Toast toast=Toast.makeText(getApplicationContext(),"Entre com o nome do ",Toast. LENGTH_SHORT);
            toast. show();
        }

        if (address.getEditText().getText().toString().isEmpty()){
            Toast toast=Toast.makeText(getApplicationContext(),"Entre com o nome",Toast. LENGTH_SHORT);
            toast. show();
        }

        if (cnpj.getEditText().getText().toString().isEmpty()){
            Toast toast=Toast.makeText(getApplicationContext(),"Entre com o nome",Toast. LENGTH_SHORT);
            toast. show();
        }

        if (phoneNumber.getEditText().getText().toString().isEmpty()){
            Toast toast=Toast.makeText(getApplicationContext(),"Entre com o nome",Toast. LENGTH_SHORT);
            toast. show();
        }

        if (email.getEditText().getText().toString().isEmpty()) {
            Toast toast = Toast.makeText(getApplicationContext(), "Entre com o nome", Toast.LENGTH_SHORT);
            toast.show();
        } else {
            saveProvider(new Provider(
                            firebaseDbReference.push().getKey(),
                            fantasyName.getEditText().getText().toString(),
                            cnpj.getEditText().getText().toString(),
                            phoneNumber.getEditText().getText().toString(),
                            email.getEditText().getText().toString(),
                            address.getEditText().getText().toString(),
                            evaluation.getRating()));
        }
    }

    private void saveProvider(Provider provider) {

        Toast toast=Toast. makeText(getApplicationContext(),provider.save(),Toast. LENGTH_SHORT);
        toast. show();
    }

    private void actionForButtonCancel() {
        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), ProvidersFragment.class));
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