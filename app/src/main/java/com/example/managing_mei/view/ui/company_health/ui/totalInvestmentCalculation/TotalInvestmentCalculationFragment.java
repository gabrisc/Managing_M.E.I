package com.example.managing_mei.view.ui.company_health.ui.totalInvestmentCalculation;

import android.app.Dialog;
import android.os.Bundle;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Switch;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.managing_mei.R;
import com.example.managing_mei.adapters.AdapterCheckListItem;
import com.example.managing_mei.adapters.AdapterInvestimentItem;
import com.example.managing_mei.model.entities.CheckListItem;
import com.example.managing_mei.model.entities.InvestimentItem;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import static com.example.managing_mei.utils.FireBaseConfig.firebaseInstance;
import static com.example.managing_mei.utils.FireBaseConfig.getIdUser;
import static com.example.managing_mei.utils.FormatDataUtils.cleanFormatValues;
import static com.example.managing_mei.utils.FormatDataUtils.formatMonetaryValue;


public class TotalInvestmentCalculationFragment extends Fragment implements AdapterInvestimentItem.OnInvestimentItemListener {

    private List<InvestimentItem> investimentItems = new ArrayList<>();
    private AdapterInvestimentItem adapterInvestimentItem;
    private RecyclerView recyclerView;
    private ImageButton imageButtonAddNewInvest;

    public TotalInvestmentCalculationFragment() {
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_total_investment_calculation, container, false);

        recyclerView = root.findViewById(R.id.recyclerViewInvestiment);
        imageButtonAddNewInvest = root.findViewById(R.id.imageButtonAAddnewInvestiment);

        firebaseInstance.getReference()
                .child(getIdUser())
                .child("InvestimentItem")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        investimentItems.clear();
                        for (DataSnapshot ds: snapshot.getChildren()){
                            InvestimentItem investimentItem = ds.getValue(InvestimentItem.class);
                            investimentItems.add(investimentItem);
                        }
                        reloadRecyclerClient();
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        String x = String.valueOf(error);
                    }
                });

        imageButtonAddNewInvest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                applyDiscount(false,0);
            }
        });

        return root;
    }

    private void reloadRecyclerClient(){
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext().getApplicationContext()));
        adapterInvestimentItem = new AdapterInvestimentItem(investimentItems,this.getContext().getApplicationContext(),this::onInvestimentItemClick);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapterInvestimentItem);
    }


    private void applyDiscount(Boolean isEditAndDelete, int position){
        final Dialog dialog = new Dialog(getContext());
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.dialog_investiment);

        TextInputLayout editTextValueFromInvestiment = dialog.findViewById(R.id.editTextNumberDecimalValueFromInvestiment);
        TextInputLayout editTextNameFromInvestiment = dialog.findViewById(R.id.editTextNomeDoInvestimento);
        Button buttonAdd = dialog.findViewById(R.id.buttonConfirmInvestiment);
        Button buttonDelete = dialog.findViewById(R.id.buttonCancelInvestiment);

        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        Spinner spinnerQuitado = dialog.findViewById(R.id.spinnertipoPagamento);
        Spinner spinnerTypeOfInvestiment = dialog.findViewById(R.id.spinnertimeOfInvestiment);

        editTextValueFromInvestiment.getEditText().setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b){
                    editTextValueFromInvestiment.getEditText().setText(cleanFormatValues(editTextValueFromInvestiment.getEditText().getText().toString()));
                } else {
                    editTextValueFromInvestiment.getEditText().setText(formatMonetaryValue(Double.parseDouble(editTextValueFromInvestiment.getEditText().getText().toString())));
                }
            }
        });

        List<String> arrayPagamentoQuitado = new ArrayList<>();
        arrayPagamentoQuitado.add("Pedente");
        arrayPagamentoQuitado.add("Realizado");

        List<String> arrayTypeOfInvestiment = new ArrayList<>();
        arrayTypeOfInvestiment.add("Futuro");
        arrayTypeOfInvestiment.add("Realizado");

        SpinnerAdapter spinnerAdapterType= new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item ,arrayPagamentoQuitado);
        spinnerQuitado.setAdapter(spinnerAdapterType);

        SpinnerAdapter spinnerAdapterTime= new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item,arrayTypeOfInvestiment);
        spinnerTypeOfInvestiment.setAdapter(spinnerAdapterTime);

        if(isEditAndDelete) {

            editTextNameFromInvestiment.getEditText().setText(investimentItems.get(position).getNameFromInvestiment().toUpperCase());
            editTextValueFromInvestiment.getEditText().setText(investimentItems.get(position).getValueFromInvestiment().toString());
            spinnerTypeOfInvestiment.setSelection(arrayTypeOfInvestiment.indexOf(investimentItems.get(position).getTypeOfInvestiment()));
            spinnerQuitado.setSelection(arrayPagamentoQuitado.indexOf(investimentItems.get(position).getIsSettled()));

            buttonAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    InvestimentItem investimentItem =  new InvestimentItem(editTextNameFromInvestiment.getEditText().getText().toString(),
                            spinnerQuitado.getSelectedItem().toString(),
                            spinnerTypeOfInvestiment.getSelectedItem().toString(),
                            Double.valueOf(editTextValueFromInvestiment.getEditText().getText().toString()));

                    investimentItem.save();
                    dialog.dismiss();
                }
            });

            buttonDelete.setText("DELETE");

            buttonDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    investimentItems.get(position).delete();
                    dialog.dismiss();
                }
            });

        } else {
            buttonAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (editTextNameFromInvestiment.getEditText().getText().toString().isEmpty()){

                    } else if (editTextValueFromInvestiment.getEditText().getText().toString().isEmpty()) {

                    } else {
                        InvestimentItem investimentItem =  new InvestimentItem(editTextNameFromInvestiment.getEditText().getText().toString(),
                                spinnerQuitado.getSelectedItem().toString(),
                                spinnerTypeOfInvestiment.getSelectedItem().toString(),
                                Double.valueOf(cleanFormatValues(editTextValueFromInvestiment.getEditText().getText().toString())));

                        investimentItem.save();
                        dialog.dismiss();
                    }

                }
            });

            buttonDelete.setVisibility(View.INVISIBLE);
        }
        dialog.create();
        dialog.show();
    }

@Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onInvestimentItemClick(int position) {
        applyDiscount(true,position);
    }
}