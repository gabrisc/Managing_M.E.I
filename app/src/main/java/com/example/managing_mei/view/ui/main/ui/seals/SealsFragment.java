package com.example.managing_mei.view.ui.main.ui.seals;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.managing_mei.R;
import com.example.managing_mei.adapters.AdapterSales;
import com.example.managing_mei.model.entities.Sale;
import com.example.managing_mei.view.ui.main.ui.seals.addSell.AddSellActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import static com.example.managing_mei.utils.FireBaseConfig.firebaseInstance;
import static com.example.managing_mei.utils.FireBaseConfig.getIdUser;

public class SealsFragment extends Fragment implements AdapterSales.OnSaleListerner {

    private ImageButton imageButtonAddNewSells;
    private List<Sale> clientList= new ArrayList<>();
    private RecyclerView recyclerView;
    private AdapterSales adapterClient;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sales, container, false);

        recyclerView = view.findViewById(R.id.recyclerViewSales);
        imageButtonAddNewSells = view.findViewById(R.id.imageButtonAddNewSells);

        callAddProvider();
        loadList();
        reloadRecyclerClient();
        return view;
    }

    private void callAddProvider(){
        imageButtonAddNewSells.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity( new Intent(getContext(),AddSellActivity.class));
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        clientList.clear();
        reloadRecyclerClient();

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    private void loadList(){
        clientList.clear();
        firebaseInstance.getReference()
                .child(getIdUser())
                .child("Sales")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot ds: snapshot.getChildren()){
                            Sale sale = ds.getValue(Sale.class);
                            clientList.add(sale);
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
        adapterClient = new AdapterSales(clientList,this.getContext().getApplicationContext(),this::onSaleListenerClick);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapterClient);
    }

    @Override
    public void onSaleListenerClick(int position) {

    }
}