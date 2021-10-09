package com.example.managing_mei.view.ui.company_health.ui.profitability;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.managing_mei.ActivityAddNewDebt;
import com.example.managing_mei.R;
import com.example.managing_mei.adapters.AdapterDebts;
import com.example.managing_mei.model.entities.DebtsItem;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.HashSet;
import java.util.Set;


import static com.example.managing_mei.utils.FireBaseConfig.firebaseInstance;
import static com.example.managing_mei.utils.FireBaseConfig.getIdUser;


public class ProfitabilityFragment extends Fragment implements AdapterDebts.OnDebtsItemListener {
    //Tipos para o Spinner de tipo divida: valor divida unica, recorrente, fixa
    //spinnerTypeDebt :: Divida Unica, Recorrente
    //spinnerOccurrence :: valor Fixo , parcelado
    //spinnerParcel :: 1 a 12
    //spinnerFrequencyType :: anual,mensal,semanal,trimestral,Semestral

    private RecyclerView recyclerView;
    private ImageButton imageButtonAddNewDebt;
    private AdapterDebts adapterDebts;
    private Set<DebtsItem> debtsItems = new HashSet<>();
    public ProfitabilityFragment() {
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_debt, container, false);

        recyclerView          = root.findViewById(R.id.reciclerViewNewDebts);
        imageButtonAddNewDebt = root.findViewById(R.id.imageButtonAddNewDebt);

        imageButtonAddNewDebt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            startActivity(new Intent(getContext(), ActivityAddNewDebt.class));
            }
        });

        loadList();
        reloadRecyclerClient();

        return root;
    }

    @Override
    public void onPause() {
        super.onPause();
        debtsItems.clear();
        reloadRecyclerClient();
    }

    private void reloadRecyclerClient(){
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext().getApplicationContext()));
        adapterDebts = new AdapterDebts(debtsItems,this.getContext().getApplicationContext(),this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapterDebts);

    }

    private void loadList(){
        debtsItems.clear();

        firebaseInstance.getReference()
                .child(getIdUser())
                .child("debts")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot ds: snapshot.getChildren()){
                            DebtsItem debtsItem = ds.getValue(DebtsItem.class);
                            debtsItems.add(debtsItem);
                        }
                        reloadRecyclerClient();
                        adapterDebts.notifyDataSetChanged();
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        String x = String.valueOf(error);
                    }
                });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void OnCheckListItemClick(int position) {

    }
}