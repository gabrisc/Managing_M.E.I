package com.example.managing_mei.view.ui.main.ui.client.addClient;

import androidx.annotation.ColorInt;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.telephony.PhoneNumberUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.example.managing_mei.R;
import com.example.managing_mei.model.entities.Client;
import com.example.managing_mei.utils.FormatDataUtils;
import com.example.managing_mei.view.ui.main.ui.ManagementActivity;
import com.example.managing_mei.view.ui.main.ui.client.ClientFragment;
import com.google.android.material.textfield.TextInputLayout;

import org.apache.commons.validator.Validator;
import org.apache.commons.validator.routines.AbstractNumberValidator;
import org.apache.commons.validator.routines.EmailValidator;

import java.text.SimpleDateFormat;

import static com.example.managing_mei.utils.FireBaseConfig.firebaseDbReference;
import static com.example.managing_mei.utils.FormatDataUtils.cleanFormat;
import static com.example.managing_mei.utils.FormatDataUtils.formatDateToStringFormated;
import static com.example.managing_mei.utils.FormatDataUtils.formatPhoneNumber;

public class AddClientActivity extends AppCompatActivity {

    private TextView date;
    private Button buttonCancelAddClient,buttonAddClient;
    private TextInputLayout name,email,telefone;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_client);

        date = findViewById(R.id.textViewDataAddClient);
        name = findViewById(R.id.editTextNameClient);
        email= findViewById(R.id.editTextClientEmail);
        telefone = findViewById(R.id.editTextClientNumTelefone);
        date.setText(formatDateToStringFormated(System.currentTimeMillis()));

        buttonCancelAddClient = findViewById(R.id.buttonCancelAddClient);
        buttonAddClient = findViewById(R.id.buttonAddClient);


        telefone.getEditText().setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onFocusChange(View view, boolean b) {
                telefone.getEditText().setText(formatPhoneNumber(telefone.getEditText().getText().toString()));
            }
        });

        addNewClient();
        cancelRegistrer();
    }

    private void addNewClient() {
        buttonAddClient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validFields();
            }
        });
    }

    private void cancelRegistrer() {
        buttonCancelAddClient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @SuppressLint("ResourceAsColor")
    private void validFields(){
        if (!EmailValidator.getInstance().isValid(email.getEditText().getText().toString())){
            Toast toast=Toast. makeText(getApplicationContext(),"O E-mail não e valido",Toast.LENGTH_LONG);
            toast. show();
        } else {
            if (name.getEditText().getText().toString().isEmpty()){
                Toast toast=Toast. makeText(getApplicationContext(),"Preencha o nome",Toast.LENGTH_LONG);
                toast. show();
            } else if (!email.getEditText().getText().toString().isEmpty() || !telefone.getEditText().getText().toString().isEmpty()){
                saveClient(new Client(firebaseDbReference.push().getKey(),
                        name.getEditText().getText().toString(),
                        email.getEditText().getText().toString(),
                        cleanFormat(telefone.getEditText().getText().toString())
                ));
            } else {
                Toast toast=Toast. makeText(getApplicationContext(),"Preencha o email ou telefone",Toast.LENGTH_LONG);
                toast. show();
            }
        }
    }

    private void saveClient(Client client) {
        try {
            client.save();
        } catch (Exception e){
            Toast toast=Toast. makeText(getApplicationContext(),"Não foi possivel cadastrar",Toast.LENGTH_LONG);
            toast. show();
        }
        finish();
    }
}