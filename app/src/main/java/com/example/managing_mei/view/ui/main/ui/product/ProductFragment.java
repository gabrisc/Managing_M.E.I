package com.example.managing_mei.view.ui.main.ui.product;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.managing_mei.R;
import com.example.managing_mei.view.ui.main.ui.product.addProduct.AddProductActivity;
import com.example.managing_mei.view.ui.main.ui.providers.addProviders.AddProvidersActivity;

public class ProductFragment extends Fragment {

    private ImageButton imageButtonAddProduct;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);

        imageButtonAddProduct = view.findViewById(R.id.imageButtonAddProduct);
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
}