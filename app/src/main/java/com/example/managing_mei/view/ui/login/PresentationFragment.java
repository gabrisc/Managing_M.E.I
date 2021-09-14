package com.example.managing_mei.view.ui.login;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.managing_mei.R;
import com.example.managing_mei.utils.FireBaseConfig;
import com.example.managing_mei.view.ui.main.ui.ManagementActivity;

public class PresentationFragment extends Fragment {

    public static PresentationFragment newInstance() {
        return new PresentationFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_presentation, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        verifyLogin();
    }

    private void verifyLogin() {
        if (FireBaseConfig.firebaseAuth.getCurrentUser()!=null){
            openMenu();
        }
    }
    private void openMenu(){startActivity(new Intent(getContext(), ManagementActivity.class));}

}