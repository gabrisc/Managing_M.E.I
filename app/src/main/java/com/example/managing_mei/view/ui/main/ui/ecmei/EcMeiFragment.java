package com.example.managing_mei.view.ui.main.ui.ecmei;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.managing_mei.R;
import com.example.managing_mei.view.ui.company_health.CompanyHealthActivity;
import com.example.managing_mei.view.ui.main.ui.ecmei.config.ConfigForAppActivity;
import com.google.firebase.auth.FirebaseAuth;

public class EcMeiFragment extends Fragment {
    private ImageButton openHealthCompany,logoutButton,configButton;
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        openHealthCompany = view.findViewById(R.id.imageButtonOpenHealthCompany);
        logoutButton = view.findViewById(R.id.imageButtonLogout);
        configButton = view.findViewById(R.id.imageButtonConfig);

        OpenCompanyHealth();
        actionForLogoutButton();
        actionForConfigButton();
        return view;
    }

    private void actionForConfigButton() {
        configButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), ConfigForAppActivity.class));
            }
        });
    }

    private void actionForLogoutButton() {
        configButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    private void OpenCompanyHealth() {
        openHealthCompany.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), CompanyHealthActivity.class));
            }
        });

    }


}