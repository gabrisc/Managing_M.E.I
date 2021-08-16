package com.example.managing_mei.view.ui.main.ui.ecmei.config;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.managing_mei.R;
import com.example.managing_mei.view.ui.main.ui.ecmei.config.payments.PaymentsConfigActivity;
import com.example.managing_mei.view.ui.main.ui.ecmei.config.productQuantity.ProductConfigActivity;

public class ConfigForAppActivity extends AppCompatActivity {
    private View paymentView,viewProducts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config_for_app);

        paymentView = findViewById(R.id.viewPagamentos);
        viewProducts = findViewById(R.id.viewProducts);
        paymentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), PaymentsConfigActivity.class));
            }
        });

        viewProducts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), ProductConfigActivity.class));
            }
        });


    }
}