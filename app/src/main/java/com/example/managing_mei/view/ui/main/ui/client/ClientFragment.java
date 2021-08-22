package com.example.managing_mei.view.ui.main.ui.client;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.managing_mei.R;
import com.example.managing_mei.adapters.AdapterClient;
import com.example.managing_mei.model.entities.Client;
import com.example.managing_mei.model.entities.Product;
import com.example.managing_mei.view.ui.main.ui.client.addClient.AddClientActivity;
import com.example.managing_mei.view.ui.main.ui.client.editClient.EditClientActivity;
import com.example.managing_mei.view.ui.main.ui.product.EditProduct.EditProductActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import static com.example.managing_mei.utils.FireBaseConfig.firebaseInstance;
import static com.example.managing_mei.utils.FireBaseConfig.getIdUser;

public class ClientFragment extends Fragment  implements AdapterClient.OnClientListener {

    private ImageButton imageButtonAddClient;
    private List<Client> clientList= new ArrayList<>();
    private RecyclerView recyclerView;
    private AdapterClient adapterClient;

    public ClientFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_client, container, false);

        recyclerView = view.findViewById(R.id.recyclerViewClient);
        imageButtonAddClient = view.findViewById(R.id.imageButtonAddClient);

        callAddFragment();
        loadList();
        reloadRecyclerClient();
        return view;
    }

    private void callAddFragment() {
        imageButtonAddClient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), AddClientActivity.class));
            }
        });
    }

    private void loadList(){
        clientList.clear();
        firebaseInstance.getReference()
                .child(getIdUser())
                .child("clients")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot ds: snapshot.getChildren()){
                            Client client = ds.getValue(Client.class);
                            clientList.add(client);
                        }
                        adapterClient.notifyDataSetChanged();
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        String x = String.valueOf(error);
                    }
                });
    }

    private void reloadRecyclerClient(){
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext().getApplicationContext()));
        adapterClient = new AdapterClient(clientList,this.getContext().getApplicationContext(),this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapterClient);
    }

    @Override
    public void onClientOperationClick(int position) {
        Client operationSelect = clientList.get(position);

        Intent intent= new Intent(getContext().getApplicationContext(), EditClientActivity.class);
        Bundle bundle = new Bundle();

        bundle.putString("id",operationSelect.getId());
        bundle.putString("email",operationSelect.getEmail());
        bundle.putString("nome",operationSelect.getNome());
        bundle.putString("Telefone",operationSelect.getTelefone());

        intent.putExtras(bundle);
        startActivity(intent);
    }
}