package com.example.managing_mei.view.ui.company_health;

import android.app.Activity;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.example.managing_mei.R;
import com.example.managing_mei.databinding.ActivityCompanyHealthBinding;
import com.example.managing_mei.databinding.ActivityManagementBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class CompanyHealthActivity extends AppCompatActivity {

    private ActivityCompanyHealthBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityCompanyHealthBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //BottomNavigationView navView = findViewById(R.id.nav_view_company_health);
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_company_health);
        NavigationUI.setupWithNavController(binding.navViewCompanyHealth, navController);
    }
}
