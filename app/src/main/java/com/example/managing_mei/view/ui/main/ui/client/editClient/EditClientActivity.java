package com.example.managing_mei.view.ui.main.ui.client.editClient;

import androidx.appcompat.app.AppCompatActivity;

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
        telefoneClient.getEditText().setText(client.getTelefone().toString());

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
                startActivity(new Intent(getApplicationContext(), ManagementActivity.class));
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteProduct();
            }
        });
    }

    private void recuperarClient() {
        Bundle bundle = getIntent().getExtras();
        client.setId(bundle.getString("id"));
        client.setNome(bundle.getString("nome"));
        client.setEmail(bundle.getString("email"));
        client.setTelefone(bundle.getString("Telefone"));
    }

    private boolean validarCampos(){
        if (nomeClient.getEditText().getText().toString().isEmpty()){
            Toast.makeText(getApplicationContext(),"Preencha o nome",Toast. LENGTH_SHORT).show();
            return false;
        } else if (emailClient.getEditText().getText().toString().isEmpty()){
            Toast.makeText(getApplicationContext(),"Preencha o valor de venda",Toast. LENGTH_SHORT).show();
            return false;
        } else if (telefoneClient.getEditText().getText().toString().isEmpty()){
            Toast.makeText(getApplicationContext(),"Preencha as despesas",Toast. LENGTH_SHORT).show();
            return false;
        } else {
            client.setNome(nomeClient.getEditText().getText().toString());
            client.setEmail(emailClient.getEditText().getText().toString());
            client.setTelefone(telefoneClient.getEditText().getText().toString());
        }
        return true;
    }

    private void deleteProduct(){
        client.delete();
        Toast toast=Toast. makeText(getApplicationContext(),"Deletado",Toast. LENGTH_SHORT);
        toast.show();
        this.finish();
    }

}