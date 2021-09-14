package com.example.managing_mei.view.ui.login.signUp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.managing_mei.R;
import com.example.managing_mei.model.entities.Business;
import com.example.managing_mei.model.entities.CheckListItem;
import com.example.managing_mei.model.entities.PaymentType;
import com.example.managing_mei.model.entities.PaymentsTypes;
import com.example.managing_mei.model.entities.QuantitiesTypes;
import com.example.managing_mei.model.entities.QuantityType;
import com.example.managing_mei.view.ui.login.MainActivity;
import com.example.managing_mei.view.ui.login.PresentationFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static com.example.managing_mei.utils.FireBaseConfig.firebaseAuth;
import static com.example.managing_mei.utils.FireBaseConfig.firebaseDbReference;

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
        buttonRegister = findViewById(R.id.buttonCadastrar_se);
        buttonCancel = findViewById(R.id.buttonCancelar);


        phoneNumber.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        phoneNumber.getEditText().setOnFocusChangeListener(new View.OnFocusChangeListener() {
            //(61) 98592-4198
            @SuppressLint("SetTextI18n")
            @Override
            public void onFocusChange(View view, boolean b) {
                phoneNumber.getEditText().setText(phoneNumber.getEditText().getText().toString().replaceAll("\\W",""));
                phoneNumber.getEditText().setText(phoneNumber.getEditText().getText().toString().replaceAll("[a-zA-Z\\s]",""));
                String phone =phoneNumber.getEditText().getText().toString();
                    if (!(phone.length() == 0) && phone.length()>=11){
                        phoneNumber.getEditText().setText("("+phone.substring(0,2)+") "+phone.substring(2,7)+"-"+phone.substring(7,11));
                    }
            }
        });

        textCNPJ.getEditText().setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onFocusChange(View view, boolean b) {
                String document = textCNPJ.getEditText().getText().toString();
                if (!(document.length()==0) && document.length()<=14){
                    document.replaceAll("\\W","");
                    if(document.length()==14){
                        //is CNPJ
                        //XX.XXX.XXX/0001-XX
                        textCNPJ.getEditText().setText(document.substring(0,2)+"."+document.substring(2,5)+"."+document.substring(5,8)+"/"+document.substring(8,12)+"-"+document.substring(12,14));
                    }

                    if (document.length()==11){
                        //is CPF
                        //050.743.901-50
                        textCNPJ.getEditText().setText(document.substring(0,3)+"."+document.substring(3,6)+"."+document.substring(6,9)+"-"+document.substring(9,11));
                    }
                } else {
                    textCNPJ.getEditText().setText(textCNPJ.getEditText().getText().toString().replaceAll("\\W",""));
                }
            }
        });

        recoverClientData();
        verefyFields();
    }

    private void recoverClientData(){
        Bundle bundle = getIntent().getExtras().getBundle("bundle");
        personName = bundle.getString("name");
        email = bundle.getString("email");
        password = bundle.getString("senha");
    }

    public void verefyFields(){
        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (businessBranch.getEditText().getText() == null){
                    Toast toast=Toast. makeText(getApplicationContext(),"O ramo nao pode estar vazio",Toast. LENGTH_LONG);
                    toast. show();
                }

                if (phoneNumber.getEditText().getText() == null){
                    Toast toast=Toast. makeText(getApplicationContext(),"O telefone está vazio",Toast. LENGTH_LONG);
                    toast. show();
                }

                if (textFantasyName.getEditText().getText() == null){
                    Toast toast=Toast. makeText(getApplicationContext(),"O nome fantasia está vazio",Toast. LENGTH_LONG);
                    toast. show();
                }

                if (textCNPJ.getEditText().getText() == null){
                    Toast toast=Toast. makeText(getApplicationContext(),"O CNPJ ou CPF nao pode ficar vazio",Toast. LENGTH_LONG);
                    toast. show();
                } else {
                    Business business = new Business(
                            firebaseDbReference.push().getKey(),
                            email,
                            password,
                            textFantasyName.getEditText().getText().toString().toUpperCase(),
                            textCNPJ.getEditText().getText().toString(),
                            textAdress.getEditText().getText().toString(),
                            phoneNumber.getEditText().getText().toString(),
                            businessBranch.getEditText().getText().toString(),
                            personName);

                    SingUpUser(business);
                }
            }
        });
    }

    private void SingUpUser(Business business) {
        firebaseAuth.createUserWithEmailAndPassword(business.getEmail(), business.getPassword()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    business.setUid(firebaseAuth.getUid());
                    business.save();
                    createBasicPayments();
                    createBasicCheckListItem();
                    createBasicQuantities();

                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                }else{
                    try {
                        throw task.getException();
                    }catch (Exception e){
                        Toast toast=Toast. makeText(getApplicationContext(),e.getMessage(),Toast. LENGTH_LONG);
                        toast. show();
                    }
                }
            }
        });

    }

    private void createBasicCheckListItem() {
        CheckListItem checkListItem= new CheckListItem("Limpar Ponto de venda",true);
        checkListItem.save();
    }

    private void createBasicPayments() {
        Set<PaymentType> paymentTypeSet = new HashSet<>();
        paymentTypeSet.add(new PaymentType("À vista",true));
        paymentTypeSet.add(new PaymentType("Debito",true));
        paymentTypeSet.add(new PaymentType("Credito",true));
        paymentTypeSet.add(new PaymentType("PIX",true));
        PaymentsTypes paymentsTypes = new PaymentsTypes(paymentTypeSet);
        paymentsTypes.save();
    }

    private void createBasicQuantities() {
        Set<QuantityType> quantityTypes = new HashSet<>();
        quantityTypes.add(new QuantityType("UND",true));
        quantityTypes.add(new QuantityType("CXS",true));
        quantityTypes.add(new QuantityType("M²",true));
        quantityTypes.add(new QuantityType("METROS",true));
        quantityTypes.add(new QuantityType("KG",true));
        QuantitiesTypes quantitiesTypes = new QuantitiesTypes(quantityTypes);
        quantitiesTypes.save();
    }

}