package com.example.managing_mei.view.ui.login.signUp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.managing_mei.R;
import com.example.managing_mei.model.entities.Business;
import com.example.managing_mei.model.entities.CheckListItem;
import com.example.managing_mei.model.entities.PaymentType;
import com.example.managing_mei.model.entities.PaymentsTypes;
import com.example.managing_mei.model.entities.QuantityType;
import com.example.managing_mei.view.ui.login.MainActivity;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import static com.example.managing_mei.utils.FireBaseConfig.firebaseAuth;
import static com.example.managing_mei.utils.FireBaseConfig.firebaseDbReference;
import static com.example.managing_mei.utils.FormatDataUtils.cleanFormat;
import static com.example.managing_mei.utils.FormatDataUtils.formatCpfOrCnpj;
import static com.example.managing_mei.utils.FormatDataUtils.formatPhoneNumber;

public class BusinessSingUpActivity extends AppCompatActivity {

    private TextInputLayout textFantasyName,textAdress,textCNPJ,phoneNumber,businessBranch;
    private String personName, email, password;
    private Button buttonRegister,buttonCancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_sing_up);

        textFantasyName = findViewById(R.id.editTextNomeFantasia);
        businessBranch = findViewById(R.id.editTextRamoEmpresarial);
        textAdress= findViewById(R.id.editTextEndereco);
        textCNPJ= findViewById(R.id.editTextCPF_CNPJ);
        phoneNumber = findViewById(R.id.editTextTelefone);
        buttonRegister = findViewById(R.id.buttonCadastrase);
        buttonCancel = findViewById(R.id.buttonCancelar);

        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    finish();
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }
            }
        });

        phoneNumber.getEditText().setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onFocusChange(View view, boolean b) {
                phoneNumber.getEditText().setText(formatPhoneNumber(phoneNumber.getEditText().getText().toString()));
            }
        });

        textCNPJ.getEditText().setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onFocusChange(View view, boolean b) {
                textCNPJ.getEditText().setText(formatCpfOrCnpj(textCNPJ.getEditText().getText().toString()));
            }
        });

        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                verefyFields();
            }
        });

        recoverClientData();
    }

    private void recoverClientData(){
        Bundle bundle = getIntent().getExtras().getBundle("bundle");
        personName = bundle.getString("name");
        email = bundle.getString("email");
        password = bundle.getString("senha");
    }

    private void verefyFields(){
        if (businessBranch.getEditText().getText().toString().isEmpty()){
            Toast toast=Toast. makeText(getApplicationContext(),"Preencha o ramo empresarial",Toast. LENGTH_LONG);
            toast. show();
        } else if (phoneNumber.getEditText().getText().toString().isEmpty()){
            Toast toast=Toast. makeText(getApplicationContext(),"Preencha o telefone",Toast. LENGTH_LONG);
            toast. show();
        } else if (textFantasyName.getEditText().getText().toString().isEmpty()){
            Toast toast=Toast. makeText(getApplicationContext(),"Preencha o nome fantasia",Toast. LENGTH_LONG);
            toast. show();
        } else if (textAdress.getEditText().getText().toString().isEmpty()){
            Toast toast=Toast. makeText(getApplicationContext(),"Preencha o  endereço",Toast. LENGTH_LONG);
            toast. show();
        } else if (textCNPJ.getEditText().getText().toString().isEmpty()){
            Toast toast=Toast. makeText(getApplicationContext(),"O CNPJ/CPF nao pode ficar vazio",Toast. LENGTH_LONG);
            toast. show();
        } else if (textCNPJ.getEditText().getText().toString().length()< 10) {
            Toast toast=Toast. makeText(getApplicationContext(),"O CNPJ/CPF não e valido",Toast. LENGTH_LONG);
            toast. show();
        } else if(phoneNumber.getEditText().getText().toString().length()<6) {
            Toast toast=Toast. makeText(getApplicationContext(),"O Telefone não e valido",Toast. LENGTH_LONG);
            toast. show();
        } else {
            Business business = new Business(
                    firebaseDbReference.push().getKey(),
                    email,
                    password,
                    textFantasyName.getEditText().getText().toString().toUpperCase(),
                    cleanFormat(textCNPJ.getEditText().getText().toString()),
                    textAdress.getEditText().getText().toString(),
                    cleanFormat(phoneNumber.getEditText().getText().toString()),
                    businessBranch.getEditText().getText().toString(),
                    personName);

            SingUpUser(business);
        }
    }

    private void SingUpUser(Business business) {
        business.setUid(firebaseAuth.getUid());
        business.save();
        createBasicPayments();
        createBasicCheckListItem();
        createBasicQuantities();
        Toast toast=Toast. makeText(getApplicationContext(),"Logando...",Toast. LENGTH_LONG);
        toast.show();
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
    }

    private void createBasicCheckListItem() {
        CheckListItem checkListItem= new CheckListItem("Limpar Ponto de venda",false,new Date());
        checkListItem.save();
    }

    private void createBasicPayments() {
        Set<PaymentType> paymentTypeSet = new HashSet<>();
        paymentTypeSet.add(new PaymentType(firebaseDbReference.push().getKey(),"À VISTA",true));
        paymentTypeSet.add(new PaymentType(firebaseDbReference.push().getKey(),"PIX",true));
        paymentTypeSet.forEach(PaymentType::save);
    }

    private void createBasicQuantities() {
        Set<QuantityType> quantityTypes = new HashSet<>();
        quantityTypes.add(new QuantityType(firebaseDbReference.push().getKey(),"UND",true));
        quantityTypes.add(new QuantityType(firebaseDbReference.push().getKey(),"KG",true));
        quantityTypes.forEach(QuantityType::save);
    }


}