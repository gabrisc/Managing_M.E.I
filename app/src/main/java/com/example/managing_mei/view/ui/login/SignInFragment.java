package com.example.managing_mei.view.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.managing_mei.R;
import com.example.managing_mei.utils.FireBaseConfig;
import com.example.managing_mei.view.ui.login.signUp.SingUpActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;

public class SignInFragment extends Fragment {

    private TextInputLayout textEmail,textPassword;
    private Button buttonRegister,buttonCancel, buttonIrParaCadastrar;

    public static SignInFragment newInstance() {
        return new SignInFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        textEmail=view.findViewById(R.id.editTextEmailLogin);
        textPassword=view.findViewById(R.id.editTextSenhaLogin);
        buttonRegister = view.findViewById(R.id.buttonLogin);
        buttonIrParaCadastrar = view.findViewById(R.id.buttonIrParaCadastrar);

        buttonIrParaCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), SingUpActivity.class));
            }
        });

        return view;
    }

    public void login(View view){
        if (textEmail.getEditText().getText() == null){
            Toast toast=Toast. makeText(getContext(),"O E-mail está vazio",Toast. LENGTH_LONG);
            toast. show();
        }else if (textPassword.getEditText().getText() == null) {
            Toast toast=Toast. makeText(getContext(),"A senha está vazia", Toast. LENGTH_LONG);
            toast. show();
        }else {
            SingUpUser(textEmail.getEditText().getText().toString(),textPassword.getEditText().getText().toString());
        }
    }

    private void SingUpUser(String email, String password) {
        FireBaseConfig.firebaseAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                try {
                    if (task.isSuccessful()){
                        startActivity(new Intent(getContext(), PresentationFragment.class));
                    }
                }catch (Exception e){
                    Toast toast=Toast. makeText(getContext(),e.getMessage(),Toast. LENGTH_LONG);
                    toast. show();
                }
            }
        });
    }

}