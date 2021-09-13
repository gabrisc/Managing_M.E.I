package com.example.managing_mei.view.ui.login.signUp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
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
                    startActivity(new Intent(getApplicationContext(), PresentationFragment.class));
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
        quantityTypes.add(new QuantityType("m²",true));
        quantityTypes.add(new QuantityType("Metros",true));
        quantityTypes.add(new QuantityType("Grama",true));
        quantityTypes.add(new QuantityType("Kg",true));
        QuantitiesTypes quantitiesTypes = new QuantitiesTypes(quantityTypes);
        quantitiesTypes.save();
    }

}