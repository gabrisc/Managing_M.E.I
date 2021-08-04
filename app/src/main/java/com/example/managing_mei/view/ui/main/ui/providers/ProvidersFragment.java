package com.example.managing_mei.view.ui.main.ui.providers;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.example.managing_mei.R;
import com.example.managing_mei.view.ui.main.ui.providers.addProviders.AddProvidersActivity;

public class ProvidersFragment extends Fragment {


    private ImageButton imageButtonAddProvider;

    public ProvidersFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_providers, container, false);

        imageButtonAddProvider = view.findViewById(R.id.imageButtonAddProvider);
        callAddProvider();

        return view;
    }

    private void callAddProvider(){
        imageButtonAddProvider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), AddProvidersActivity.class));
            }
        });

    }
}