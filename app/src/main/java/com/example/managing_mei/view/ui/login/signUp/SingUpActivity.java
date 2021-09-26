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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;

import org.apache.commons.validator.routines.EmailValidator;

import static com.example.managing_mei.utils.FireBaseConfig.firebaseAuth;
import static com.example.managing_mei.utils.FireBaseConfig.firebaseDbReference;


public class SingUpActivity extends AppCompatActivity {

    private TextInputLayout textEmail,textPassword,textPersonName;
    private Button buttonCadastrar,buttonCancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sing_up);

        textEmail = findViewById(R.id.editTextEmailCadastro);
        textPassword = findViewById(R.id.editTextSenhaCadastro);
        textPersonName = findViewById(R.id.editTextNomeCadastro);
        buttonCadastrar = findViewById(R.id.buttonContinuarCadastro);
        buttonCancel = findViewById(R.id.buttonCancelarCadastro);

        buttonCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!EmailValidator.getInstance().isValid(textEmail.getEditText().getText().toString())){
                    Toast toast=Toast. makeText(getApplicationContext(),"O E-mail não e valido",Toast. LENGTH_LONG);
                    toast. show();
                } else {
                    if ((textEmail.getEditText().getText().toString().isEmpty())){
                        Toast toast=Toast. makeText(getApplicationContext(),"O E-mail está vazio",Toast. LENGTH_LONG);
                        toast. show();
                    }else if ((textPersonName.getEditText().getText().toString().isEmpty())){
                        Toast toast=Toast. makeText(getApplicationContext(),"O nome está vazio",Toast. LENGTH_LONG);
                        toast. show();
                    }else if (textPassword.getEditText().getText().toString().isEmpty()) {
                        Toast toast=Toast. makeText(getApplicationContext(),"A senha está vazia",Toast. LENGTH_LONG);
                        toast. show();
                    } else if (textPassword.getEditText().getText().toString().length()<7 && textPassword.getEditText().getText().toString().length()>0) {
                        Toast toast=Toast. makeText(getApplicationContext(),"A senha e menor que 7 digitos",Toast. LENGTH_LONG);
                        toast. show();
                    } else {
                        firebaseAuth.createUserWithEmailAndPassword(textEmail.getEditText().getText().toString(), textPassword.getEditText().getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()){
                                    Bundle bundle = new Bundle();
                                    bundle.putString("email", textEmail.getEditText().getText().toString());
                                    bundle.putString("senha", textPassword.getEditText().getText().toString());
                                    bundle.putString("name", textPersonName.getEditText().getText().toString());

                                    Intent intent = new Intent(getApplicationContext(), BusinessSingUpActivity.class);
                                    intent.putExtra("bundle", bundle);
                                    startActivity(intent);
                                }else{
                                    try {
                                        throw task.getException();
                                    }catch (Exception e){
                                        Toast toast=Toast. makeText(getApplicationContext(),"E-mail ja esta cadastrado",Toast. LENGTH_LONG);
                                        toast. show();
                                    }
                                }
                            }
                        });
                    }
                }
            }
        });

        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


    }
}




