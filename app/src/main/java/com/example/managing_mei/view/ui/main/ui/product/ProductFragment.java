package com.example.managing_mei.view.ui.main.ui.product;


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
import com.example.managing_mei.adapters.AdapterProduct;
import com.example.managing_mei.adapters.AdapterProvider;
import com.example.managing_mei.model.entities.Product;
import com.example.managing_mei.model.entities.Provider;
import com.example.managing_mei.view.ui.main.ui.product.EditProduct.EditProductActivity;
import com.example.managing_mei.view.ui.main.ui.product.addProduct.AddProductActivity;
import com.example.managing_mei.view.ui.main.ui.providers.addProviders.AddProvidersActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import static com.example.managing_mei.utils.FireBaseConfig.firebaseInstance;
import static com.example.managing_mei.utils.FireBaseConfig.getIdUser;

public class ProductFragment extends Fragment implements AdapterProduct.OnProductListerner{

    private ImageButton imageButtonAddProduct;
    private List<Product> clientList= new ArrayList<>();
    private RecyclerView recyclerView;
    private AdapterProduct adapterClient;
    private int positionEconomicOperationSelect;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);

        imageButtonAddProduct = view.findViewById(R.id.imageButtonAddProduct);
        recyclerView = view.findViewById(R.id.recyclerViewProduct);


        loadList();
        reloadRecyclerClient();
        callAddProvider();
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    private void callAddProvider(){
        imageButtonAddProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), AddProductActivity.class));
            }
        });

    }


    private void loadList(){
        clientList.clear();
        firebaseInstance.getReference()
                .child(getIdUser())
                .child("product")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot ds: snapshot.getChildren()){
                            Product provider = ds.getValue(Product.class);
                            clientList.add(provider);
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
        adapterClient = new AdapterProduct(clientList,this.getContext().getApplicationContext(),this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapterClient);
    }

    @Override
    public void onProductListenerClick(int position) {
        Product operation = clientList.get(position);
        positionEconomicOperationSelect =position;

        Intent intent= new Intent(getContext().getApplicationContext(), EditProductActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("id",operation.getId());
        bundle.putString("type",operation.getType());
        bundle.putDouble("ContributionValue",operation.getContributionValue());
        bundle.putDouble("ExpenseValue",operation.getExpenseValue());
        bundle.putString("Date",operation.getDate().toString());
        bundle.putString("Name",operation.getName());
        bundle.putInt("Quantity",operation.getQuantity());
        bundle.putDouble("SealValue",operation.getSealValue());
        bundle.putString("typeQuantity",operation.getTypeQuantity());
        intent.putExtras(bundle);
        startActivity(intent);
    }
}