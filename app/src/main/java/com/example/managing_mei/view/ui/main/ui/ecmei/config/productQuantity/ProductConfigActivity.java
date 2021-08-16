package com.example.managing_mei.view.ui.main.ui.ecmei.config.productQuantity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.Toast;

import com.example.managing_mei.R;
import com.example.managing_mei.model.entities.PaymentsTypes;
import com.example.managing_mei.model.entities.QuantitiesTypes;
import com.example.managing_mei.model.entities.QuantityType;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.example.managing_mei.utils.FireBaseConfig.getIdUser;

public class ProductConfigActivity extends AppCompatActivity {

    private Set<QuantityType> mainListOfQuantityTypes = new HashSet<>();
    private Set<Chip> chipsToShow= new HashSet<>();
    private Button buttonSaveProductConfig,buttonCancelProductConfiig;
    private ImageButton imageButtonAddNewType;
    private Switch switchHoursOnly;
    private ChipGroup chipGroupForProductConfig;
    private EditText editTextNewTypeForProductConfig;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_config);

        buttonSaveProductConfig = findViewById(R.id.buttonSaveProductConfig);
        buttonCancelProductConfiig = findViewById(R.id.buttonCancelProductConfiig);
        imageButtonAddNewType = findViewById(R.id.imageButtonAddNewType);
        switchHoursOnly = findViewById(R.id.switchHoursOnly);
        chipGroupForProductConfig = findViewById(R.id.chipGroupForProductConfig);
        editTextNewTypeForProductConfig = findViewById(R.id.editTextNewTypeForProductConfig);

        mainListOfQuantityTypes.clear();

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference(getIdUser()+"/QuantitiesTypes");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                QuantitiesTypes post = dataSnapshot.getValue(QuantitiesTypes.class);
                System.out.println(post);
                mainListOfQuantityTypes.addAll(post.getQuantityTypeArrayList());
                reloadChipGroup();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });

        imageButtonAddNewType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                QuantityType quantityType = new QuantityType();
                quantityType.setNome(editTextNewTypeForProductConfig.getText().toString());
                quantityType.setStatus(false);
                mainListOfQuantityTypes.add(quantityType);
                reloadChipGroup();
            }
        });
        savePaymentsTypes();
    }

    private void reloadChipGroup(){
        chipsToShow.clear();
        createChipList(mainListOfQuantityTypes).stream().forEach(chip -> {
            if (!chipsToShow.contains(chip)){
                chipsToShow.add(chip);
            }
        });
        editTextNewTypeForProductConfig.setText("");
        chipGroupForProductConfig.removeAllViews();
        chipsToShow.stream().forEach(chip -> chipGroupForProductConfig.addView(chip));
    }
    private List<Chip> createChipList(Set<QuantityType> mainListOfQuantityTypes){
        List<Chip> chips = new ArrayList<>();

        mainListOfQuantityTypes.stream().forEach(quantityType -> {
            Chip chip = new Chip(this);
            chip.setId(ViewCompat.generateViewId());
            chip.setText(quantityType.getNome());
            chip.setCheckable(true);
            chip.setChipIconVisible(true);
            chip.setCheckedIconVisible(true);
            chip.setCloseIconVisible(true);
            if (quantityType.getStatus()){
                chip.setChecked(true);
            }else {
                chip.setChecked(false);
            }
            chip.setOnCloseIconClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    chipGroupForProductConfig.removeView(chip);
                    mainListOfQuantityTypes.removeIf(quantityType1 -> quantityType1.getNome().equals(quantityType.getNome()));
                }
            });
            chip.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    quantityType.setStatus(true);
                }
            });
            chips.add(chip);
        });
        return chips;
    }
    private void savePaymentsTypes(){
        buttonSaveProductConfig.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                QuantitiesTypes quantitiesTypes = new QuantitiesTypes(mainListOfQuantityTypes);
                Toast toast=Toast. makeText(getApplicationContext(),quantitiesTypes.save(),Toast. LENGTH_SHORT);
                toast. show();
            }
        });

    }
}