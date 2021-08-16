package com.example.managing_mei.view.ui.main.ui.ecmei.config.payments;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.managing_mei.R;
import com.example.managing_mei.model.entities.PaymentType;
import com.example.managing_mei.model.entities.PaymentsTypes;
import com.example.managing_mei.model.entities.Provider;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import static com.example.managing_mei.utils.FireBaseConfig.firebaseDbReference;
import static com.example.managing_mei.utils.FireBaseConfig.firebaseInstance;
import static com.example.managing_mei.utils.FireBaseConfig.getIdUser;

public class PaymentsConfigActivity extends AppCompatActivity {

    private ChipGroup chipGroup;

    private Set<PaymentType> paymentTypeList = new HashSet<>();
    private Set<Chip> chipsToSave = new HashSet<Chip>();
    private TextView groupTitle;
    private ImageButton imageButtonAddPaymentType;
    private EditText editTextAddNewPaymentType;
    private Button buttonSaveNewPaymentsTypes,buttonCancelPaymentsTypes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payments_config);

        editTextAddNewPaymentType = findViewById(R.id.editTextAddNewPaymentType);
        chipGroup = findViewById(R.id.PaymentTypeChipGroup);
        imageButtonAddPaymentType = findViewById(R.id.imageButtonAddPaymentType);
        buttonSaveNewPaymentsTypes = findViewById(R.id.buttonSaveNewPaymentsTypes);

        paymentTypeList.clear();

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference(getIdUser()+"/PaymentsTypes");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                PaymentsTypes post = dataSnapshot.getValue(PaymentsTypes.class);
                System.out.println(post);
                paymentTypeList.addAll(post.getPaymentTypeList());
                reloadChipGroup();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });


        imageButtonAddPaymentType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PaymentType newPaymentType = new PaymentType();
                newPaymentType.setNome(editTextAddNewPaymentType.getText().toString());
                newPaymentType.setStatus(false);
                paymentTypeList.add(newPaymentType);
                reloadChipGroup();
            }
        });

        savePaymentsTypes();
    }

    private void reloadChipGroup() {
        chipsToSave.clear();
        createChipList(paymentTypeList).stream().forEach(chip -> {
            if (!chipsToSave.contains(chip)){
                chipsToSave.add(chip);
            }
        });
        editTextAddNewPaymentType.setText("");
        chipGroup.removeAllViews();
        chipsToSave.stream().forEach(chip -> chipGroup.addView(chip));
    }

    private List<Chip> createChipList(Set<PaymentType> paymentTypeList) {
        List<Chip> chips = new ArrayList<>();

        paymentTypeList.stream().forEach(paymentType -> {
            Chip chip = new Chip(this);
            chip.setId(ViewCompat.generateViewId());
            chip.setText(paymentType.getNome());
            chip.setCheckable(true);
            chip.setChipIconVisible(true);
            chip.setCheckedIconVisible(true);
            chip.setCloseIconVisible(true);
            if (paymentType.getStatus()){
                chip.setChecked(true);
            }else {
                chip.setChecked(false);
            }
            chip.setOnCloseIconClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    chipGroup.removeView(chip);
                    paymentTypeList.removeIf(paymentType1 -> paymentType1.getNome()==paymentType.getNome());
                }
            });
            chip.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    paymentType.setStatus(true);
                }
            });
            chips.add(chip);
        });

        return chips;
    }

    private void savePaymentsTypes(){
        buttonSaveNewPaymentsTypes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PaymentsTypes paymentsTypes = new PaymentsTypes(paymentTypeList);
                Toast toast=Toast. makeText(getApplicationContext(),paymentsTypes.save(),Toast. LENGTH_SHORT);
                toast. show();
            }
        });

    }


}