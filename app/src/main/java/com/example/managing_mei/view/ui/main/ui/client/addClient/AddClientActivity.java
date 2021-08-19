package com.example.managing_mei.view.ui.main.ui.client.addClient;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.managing_mei.R;
import com.example.managing_mei.model.entities.Client;
import com.example.managing_mei.view.ui.main.ui.ManagementActivity;
import com.example.managing_mei.view.ui.main.ui.client.ClientFragment;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;

import static com.example.managing_mei.utils.FireBaseConfig.firebaseDbReference;

public class AddClientActivity extends AppCompatActivity {

    private TextView date;
    private Button buttonCancelAddClient,buttonAddClient;
    private EditText name,email,telefone;
    private SimpleDateFormat simpleDateFormat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_client);

        simpleDateFormat=new SimpleDateFormat("dd/MM/yyyy");

        date = findViewById(R.id.textViewDataAddClient);
        name = findViewById(R.id.editTextNameClient);
        email= findViewById(R.id.editTextClientEmail);
        telefone = findViewById(R.id.editTextClientNumTelefone);
        buttonCancelAddClient = findViewById(R.id.buttonCancelAddClient);
        buttonAddClient = findViewById(R.id.buttonAddClient);
        date.setText(simpleDateFormat.format(System.currentTimeMillis()));

        addNewClient();
        cancelRegistrer();
    }

    private void addNewClient() {
        buttonAddClient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validFields();
            }
        });
    }

    private void cancelRegistrer() {
        buttonCancelAddClient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), ClientFragment.class));
            }
        });
    }

    private void validFields(){
        if (name.getText().toString().isEmpty()){
            Toast toast=Toast. makeText(getApplicationContext(),"Entre com o nome",Toast. LENGTH_SHORT);
            toast. show();
        }else if (email.getText().toString().isEmpty() ){
            Toast toast=Toast. makeText(getApplicationContext(),"Entre com o email ou telefone",Toast. LENGTH_SHORT);
            toast. show();
        }else if(telefone.getText().toString().isEmpty()){

        } else {
            saveClient(new Client(firebaseDbReference.push().getKey(),
                                  name.getText().toString(),
                                  email.getText().toString(),
                                  telefone.getText().toString()
            ));
        }
    }

    private void saveClient(Client client) {
        Toast toast=Toast. makeText(getApplicationContext(),client.save(),Toast. LENGTH_SHORT);
        toast. show();
        startActivity(new Intent(getApplicationContext(), ManagementActivity.class));
    }
}