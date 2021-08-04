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
import com.example.managing_mei.view.ui.login.PresentationFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;

import static com.example.managing_mei.utils.FireBaseConfig.firebaseAuth;
import static com.example.managing_mei.utils.FireBaseConfig.firebaseDbReference;

public class BusinessSingUpActivity extends AppCompatActivity {

    private EditText textFantasyName,textAdress,textCNPJ,phoneNumber,businessBranch;
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

                if (businessBranch.getText() == null){
                    Toast toast=Toast. makeText(getApplicationContext(),"O ramo nao pode estar vazio",Toast. LENGTH_LONG);
                    toast. show();
                }

                if (phoneNumber.getText() == null){
                    Toast toast=Toast. makeText(getApplicationContext(),"O telefone está vazio",Toast. LENGTH_LONG);
                    toast. show();
                }

                if (textFantasyName.getText() == null){
                    Toast toast=Toast. makeText(getApplicationContext(),"O nome fantasia está vazio",Toast. LENGTH_LONG);
                    toast. show();
                }

                if (textCNPJ.getText() == null){
                    Toast toast=Toast. makeText(getApplicationContext(),"O CNPJ ou CPF nao pode ficar vazio",Toast. LENGTH_LONG);
                    toast. show();
                } else {
                    Business business = new Business(
                            firebaseDbReference.push().getKey(),
                            email,
                            password,
                            textFantasyName.getText().toString().toUpperCase(),
                            textCNPJ.getText().toString(),
                            textAdress.getText().toString(),
                            phoneNumber.getText().toString(),
                            businessBranch.getText().toString(),
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

}