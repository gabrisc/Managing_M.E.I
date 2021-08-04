package com.example.managing_mei.view.ui.main.ui.client;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.example.managing_mei.R;
import com.example.managing_mei.view.ui.main.ui.client.addClient.AddClientActivity;

public class ClientFragment extends Fragment {

    private ImageButton imageButtonAddClient;

    public ClientFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_client, container, false);
        imageButtonAddClient = view.findViewById(R.id.imageButtonAddClient);
        callAddFragment();
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


}