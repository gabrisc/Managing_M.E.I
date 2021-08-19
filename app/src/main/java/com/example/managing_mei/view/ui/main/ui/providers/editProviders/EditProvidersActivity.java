package com.example.managing_mei.view.ui.main.ui.providers.editProviders;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import com.example.managing_mei.R;
import com.example.managing_mei.model.entities.Provider;
import com.example.managing_mei.view.ui.main.ui.ManagementActivity;

public class EditProvidersActivity extends AppCompatActivity {

    private Provider provider = new Provider();
    private EditText fantasyNameProvider, emailProvider, telefoneProvider,addressProvider,cnpjProvider;
    private RatingBar evaluationProvider;
    private Button atualizarButton, deleteButton, cancelarButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_providers);
        recuperarProvider();

        fantasyNameProvider = findViewById(R.id.editTextFantasyNameProvider);
        emailProvider = findViewById(R.id.editTextEmailProvider);
        telefoneProvider = findViewById(R.id.editTextTelefoneProvider);
        addressProvider = findViewById(R.id.editTextAdressProvider);
        evaluationProvider = findViewById(R.id.ratingBarForProvider);
        cnpjProvider = findViewById(R.id.editTextDocumentProvider);

        atualizarButton = findViewById(R.id.buttonUpdateProvider);
        deleteButton = findViewById(R.id.buttonDeleteProvider);
        cancelarButton = findViewById(R.id.buttonCancelUpdateProvider);

        fantasyNameProvider.setText(provider.getFantasyName());
        emailProvider.setText(provider.getEmail());
        telefoneProvider.setText(provider.getPhoneNumber());
        addressProvider.setText(provider.getAddress());
        evaluationProvider.setRating(provider.getEvaluation());
        cnpjProvider.setText(provider.getCNPJ());

        setButtonActions();
    }
    private void setButtonActions() {
        atualizarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validarCampos()){
                    provider.save();
                    startActivity(new Intent(getApplicationContext(), ManagementActivity.class));
                }
            }
        });

        cancelarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), ManagementActivity.class));
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteProvider();
            }
        });
    }

    private boolean validarCampos(){
        if (fantasyNameProvider.getText().toString().isEmpty()){
            Toast.makeText(getApplicationContext(),"Preencha o nome",Toast. LENGTH_SHORT).show();
            return false;
        } else if (emailProvider.getText().toString().isEmpty()){
            Toast.makeText(getApplicationContext(),"Preencha o valor de venda",Toast. LENGTH_SHORT).show();
            return false;
        } else if (telefoneProvider.getText().toString().isEmpty()){
            Toast.makeText(getApplicationContext(),"Preencha as despesas",Toast. LENGTH_SHORT).show();
            return false;
        } else if(addressProvider.getText().toString().isEmpty()){
            Toast.makeText(getApplicationContext(),"Preencha as despesas",Toast. LENGTH_SHORT).show();
            return false;
        } else if(cnpjProvider.getText().toString().isEmpty()){
            Toast.makeText(getApplicationContext(),"Preencha as despesas",Toast. LENGTH_SHORT).show();
            return false;
        } else {
            provider.setFantasyName(fantasyNameProvider.getText().toString());
            provider.setEmail(emailProvider.getText().toString());
            provider.setPhoneNumber(telefoneProvider.getText().toString());
            provider.setAddress(addressProvider.getText().toString());
            provider.setEvaluation(evaluationProvider.getRating());
            provider.setCNPJ(cnpjProvider.getText().toString());

        }
        return true;
    }

    private void recuperarProvider() {
        Bundle bundle = getIntent().getExtras();

        provider.setId(bundle.getString("id"));
        provider.setFantasyName(bundle.getString("fantasyName"));
        provider.setEmail(bundle.getString("email"));
        provider.setPhoneNumber(bundle.getString("phone"));
        provider.setAddress(bundle.getString("Address"));
        provider.setEvaluation(Float.valueOf(bundle.getString("evaluation")));
        provider.setCNPJ(bundle.getString("CNPJ"));
    }

    private void deleteProvider(){
        provider.delete();
        Toast toast=Toast. makeText(getApplicationContext(),"Deletado",Toast. LENGTH_SHORT);
        toast.show();
        this.finish();
    }
}