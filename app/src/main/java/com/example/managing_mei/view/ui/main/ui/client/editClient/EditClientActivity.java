package com.example.managing_mei.view.ui.main.ui.client.editClient;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.managing_mei.R;
import com.example.managing_mei.model.entities.Client;
import com.example.managing_mei.view.ui.main.ui.ManagementActivity;
import com.google.android.material.textfield.TextInputLayout;

import org.apache.commons.validator.routines.EmailValidator;

import static com.example.managing_mei.utils.FormatDataUtils.cleanFormat;
import static com.example.managing_mei.utils.FormatDataUtils.formatPhoneNumber;

public class EditClientActivity extends AppCompatActivity {

    private Client client = new Client();
    private TextInputLayout nomeClient,emailClient,telefoneClient;
    private Button atualizarButton, deleteButton, cancelarButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_client);
        recuperarClient();

        nomeClient = findViewById(R.id.editTextNomeEditClient);
        emailClient = findViewById(R.id.editTextEmailEditClient);
        telefoneClient = findViewById(R.id.editTextPhoneEditClient);
        atualizarButton = findViewById(R.id.buttonSalvarClientEditado);
        deleteButton = findViewById(R.id.buttonDeleteClient);
        cancelarButton = findViewById(R.id.buttonCancelEditClient);

        nomeClient.getEditText().setText(client.getNome().toUpperCase());
        emailClient.getEditText().setText(client.getEmail());
        telefoneClient.getEditText().setText(client.getTelefone());

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               deleteProduct();
            }
        });

        telefoneClient.getEditText().setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onFocusChange(View view, boolean b) {
                telefoneClient.getEditText().setText(formatPhoneNumber(telefoneClient.getEditText().getText().toString()));
            }
        });
        setButtonActions();
    }

    private void setButtonActions() {
        atualizarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validarCampos()){
                    client.save();
                    startActivity(new Intent(getApplicationContext(), ManagementActivity.class));
                }
            }
        });

        cancelarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void recuperarClient() {
        Bundle bundle = getIntent().getExtras();
        client.setId(bundle.getString("id"));
        client.setNome(bundle.getString("nome"));
        client.setEmail(bundle.getString("email"));
        client.setTelefone(formatPhoneNumber(bundle.getString("Telefone")));
    }

    private boolean validarCampos(){
        if (nomeClient.getEditText().getText().toString().isEmpty()){
            Toast.makeText(getApplicationContext(),"Preencha o nome",Toast. LENGTH_SHORT).show();
            return false;
        } else if (emailClient.getEditText().getText().toString().isEmpty()){
            Toast.makeText(getApplicationContext(),"Preencha o valor de venda",Toast. LENGTH_SHORT).show();
            return false;
        } else if (!EmailValidator.getInstance().isValid(emailClient.getEditText().getText().toString())) {
            Toast.makeText(getApplicationContext(),"E-mail Invalido",Toast. LENGTH_SHORT).show();
            return false;
        } else if (telefoneClient.getEditText().getText().toString().isEmpty()) {
            Toast.makeText(getApplicationContext(), "Preencha as despesas", Toast.LENGTH_SHORT).show();
            return false;
        } else if (telefoneClient.getEditText().getText().toString().length()<6) {
        } else {
            client.setNome(nomeClient.getEditText().getText().toString());
            client.setEmail(emailClient.getEditText().getText().toString());
            cleanFormat(telefoneClient.getEditText().getText().toString());
        }
        return true;
    }

    private void deleteProduct(){
        client.delete();
        Toast toast=Toast. makeText(getApplicationContext(),"Cliente Deletado",Toast. LENGTH_SHORT);
        toast.show();
        this.finish();
    }

}