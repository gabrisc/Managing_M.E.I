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
import com.google.android.material.textfield.TextInputLayout;

import org.apache.commons.validator.routines.EmailValidator;

import static com.example.managing_mei.utils.FormatDataUtils.cleanFormat;
import static com.example.managing_mei.utils.FormatDataUtils.formatCpfOrCnpj;
import static com.example.managing_mei.utils.FormatDataUtils.formatPhoneNumber;

public class EditProvidersActivity extends AppCompatActivity {

    private Provider provider = new Provider();
    private TextInputLayout fantasyNameProvider, emailProvider, telefoneProvider,addressProvider,cnpjProvider;
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

        fantasyNameProvider.getEditText().setText(provider.getFantasyName());
        emailProvider.getEditText().setText(provider.getEmail());
        telefoneProvider.getEditText().setText(formatPhoneNumber(provider.getPhoneNumber()));
        addressProvider.getEditText().setText(provider.getAddress());
        evaluationProvider.setRating(provider.getEvaluation());
        cnpjProvider.getEditText().setText(formatCpfOrCnpj(provider.getCNPJ()));

        telefoneProvider.getEditText().setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                telefoneProvider.getEditText().setText(formatPhoneNumber(telefoneProvider.getEditText().getText().toString()));
            }
        });

        cnpjProvider.getEditText().setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                cnpjProvider.getEditText().setText(formatCpfOrCnpj(cnpjProvider.getEditText().getText().toString()));
            }
        });

        setButtonActions();
    }
    private void setButtonActions() {
        atualizarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validarCampos()){
                    provider.save();
                    finish();
                }
            }
        });

        cancelarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
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
        if (fantasyNameProvider.getEditText().getText().toString().isEmpty()){
            Toast.makeText(getApplicationContext(),"Preencha o nome",Toast. LENGTH_SHORT).show();
            return false;
        } else if (emailProvider.getEditText().getText().toString().isEmpty()){
            Toast.makeText(getApplicationContext(),"Preencha o valor de venda",Toast. LENGTH_SHORT).show();
            return false;
        } else if (!EmailValidator.getInstance().isValid(emailProvider.getEditText().getText().toString())) {
            Toast.makeText(getApplicationContext(),"E-mail Invalido",Toast. LENGTH_SHORT).show();
            return false;
        } else if (telefoneProvider.getEditText().getText().toString().isEmpty()) {
            Toast.makeText(getApplicationContext(), "Preencha as despesas", Toast.LENGTH_SHORT).show();
            return false;
        } else if (cleanFormat(telefoneProvider.getEditText().getText().toString()).length()<6) {
            Toast.makeText(getApplicationContext(), "Telefone Invalido", Toast.LENGTH_SHORT).show();
            return false;
        } else if (addressProvider.getEditText().getText().toString().isEmpty()) {
            Toast.makeText(getApplicationContext(), "Preencha as despesas", Toast.LENGTH_SHORT).show();
            return false;
        } else if (cleanFormat(cnpjProvider.getEditText().getText().toString()).length()<8) {
            Toast.makeText(getApplicationContext(), "CPNJ/CPF Invalido", Toast.LENGTH_SHORT).show();
            return false;
        } else if (cnpjProvider.getEditText().getText().toString().isEmpty()) {
            Toast.makeText(getApplicationContext(), "Preencha as despesas", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            provider.setFantasyName(fantasyNameProvider.getEditText().getText().toString());
            provider.setEmail(emailProvider.getEditText().getText().toString());
            provider.setPhoneNumber(telefoneProvider.getEditText().getText().toString());
            provider.setAddress(addressProvider.getEditText().getText().toString());
            provider.setEvaluation(evaluationProvider.getRating());
            provider.setCNPJ(cnpjProvider.getEditText().getText().toString());

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