package com.example.managing_mei.view.ui.main.ui.providers;

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

import com.example.managing_mei.R;
import com.example.managing_mei.adapters.AdapterProvider;
import com.example.managing_mei.model.entities.Provider;
import com.example.managing_mei.view.ui.main.ui.providers.addProviders.AddProvidersActivity;
import com.example.managing_mei.view.ui.main.ui.providers.editProviders.EditProvidersActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import static com.example.managing_mei.utils.FireBaseConfig.firebaseInstance;
import static com.example.managing_mei.utils.FireBaseConfig.getIdUser;

public class ProvidersFragment extends Fragment implements AdapterProvider.OnProviderListener{

    private ImageButton imageButtonAddProvider;
    private List<Provider> providerList = new ArrayList<>();
    private RecyclerView recyclerView;
    private AdapterProvider adapterClient;

    public ProvidersFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_providers, container, false);

        recyclerView = view.findViewById(R.id.recyclerViewProviders);
        imageButtonAddProvider = view.findViewById(R.id.imageButtonAddProvider);

        callAddProvider();
        loadList();
        reloadRecyclerClient();
        return view;
    }

    @Override
    public void onPause() {
        super.onPause();
        providerList.clear();
    }

    @Override
    public void onStop() {
        super.onStop();
        providerList.clear();
    }

    private void callAddProvider(){
        imageButtonAddProvider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), AddProvidersActivity.class));
            }
        });

    }

    private void loadList(){
        providerList.clear();
        firebaseInstance.getReference()
                .child(getIdUser())
                .child("providers")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot ds: snapshot.getChildren()){
                            Provider provider = ds.getValue(Provider.class);
                            providerList.add(provider);
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
        adapterClient = new AdapterProvider(providerList,this.getContext().getApplicationContext(),this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapterClient);
    }

    @Override
    public void onProviderClick(int position) {
        Provider provider = providerList.get(position);
        Intent intent = new Intent(getContext().getApplicationContext(), EditProvidersActivity.class);
        Bundle bundle = new Bundle();

        bundle.putString("id",provider.getId());
        bundle.putString("fantasyName",provider.getFantasyName());
        bundle.putString("Address",provider.getAddress());
        bundle.putString("CNPJ",provider.getCNPJ());
        bundle.putString("email",provider.getEmail());
        bundle.putString("evaluation",provider.getEvaluation().toString());
        bundle.putString("phone",provider.getPhoneNumber());

        intent.putExtras(bundle);
        startActivity(intent);
    }
}