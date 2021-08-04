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
import com.google.firebase.auth.AuthResult;

import static com.example.managing_mei.utils.FireBaseConfig.firebaseAuth;
import static com.example.managing_mei.utils.FireBaseConfig.firebaseDbReference;


public class SingUpActivity extends AppCompatActivity {

    private EditText textEmail,textPassword,textPersonName;
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

                if (!(textEmail.getText() == null)){
                    Toast toast=Toast. makeText(getApplicationContext(),"O E-mail está vazio",Toast. LENGTH_LONG);
                    toast. show();
                }

                if (!(textPersonName.getText() == null)){
                    Toast toast=Toast. makeText(getApplicationContext(),"O nome fantasia está vazio",Toast. LENGTH_LONG);
                    toast. show();
                }

                if (textPassword.getText().toString().length()<7 || textPassword.getText().toString().isEmpty()) {
                    Toast toast=Toast. makeText(getApplicationContext(),"A senha está vazia",Toast. LENGTH_LONG);
                    toast. show();
                } else {
                    Bundle bundle = new Bundle();
                    bundle.putString("email",textEmail.getText().toString());
                    bundle.putString("senha",textPassword.getText().toString());
                    bundle.putString("name",textPersonName.getText().toString());

                    Intent intent = new Intent(getApplicationContext(),BusinessSingUpActivity.class);
                    intent.putExtra("bundle",bundle);

                    startActivity(intent);
                }
            }
        });

        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });


    }
}




