package com.example.managing_mei.view.ui.company_health.ui.profitability;

import android.content.Intent;
import android.os.Bundle;
import android.util.ArraySet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.managing_mei.ActivityAddNewDebt;
import com.example.managing_mei.R;
import com.example.managing_mei.adapters.AdapterDebts;
import com.example.managing_mei.model.entities.CashFlowItem;
import com.example.managing_mei.model.entities.DebtsItem;
import com.example.managing_mei.model.enuns.TypeOfDebts;
import com.example.managing_mei.utils.FormatDataUtils;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import static com.example.managing_mei.utils.FireBaseConfig.firebaseInstance;
import static com.example.managing_mei.utils.FireBaseConfig.getIdUser;

public class DebtsFragment extends Fragment implements AdapterDebts.OnDebtsItemListener {
    //Tipos para o Spinner de tipo divida: valor divida unica, recorrente, fixa
    //spinnerTypeDebt :: Divida Unica, Recorrente
    //spinnerOccurrence :: valor Fixo , parcelado
    //spinnerParcel :: 1 a 12
    //spinnerFrequencyType :: anual,mensal,semanal,trimestral,Semestral

    private RecyclerView recyclerView;
    private ImageButton imageButtonAddNewDebt;
    private AdapterDebts adapterDebts;
    private List<DebtsItem> debtsItems = new ArrayList<>();
    private AlertDialog alertDialog;
    private String StatusDividaSelected;
    public DebtsFragment() {
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_debt, container, false);
        recyclerView = root.findViewById(R.id.reciclerViewNewDebts);
        imageButtonAddNewDebt = root.findViewById(R.id.imageButtonAddNewDebt);
        imageButtonAddNewDebt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), ActivityAddNewDebt.class));
            }
        });
        loadList();
        reloadRecyclerClient();
        return root;
    }


    @Override
    public void onPause() {
        super.onPause();
        debtsItems.clear();
    }

    private void reloadRecyclerClient(){
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext().getApplicationContext()));
        adapterDebts = new AdapterDebts(debtsItems,this.getContext().getApplicationContext(),this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapterDebts);
    }

    private void loadList(){
        debtsItems.clear();
        firebaseInstance.getReference()
                .child(getIdUser())
                .child("debts")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot ds: snapshot.getChildren()){
                            DebtsItem debtsItem = ds.getValue(DebtsItem.class);
                            debtsItems.add(debtsItem);
                        }
                        adapterDebts.notifyDataSetChanged();
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        String x = String.valueOf(error);
                    }
                });
    }

    @Override
    public void OnCheckListItemClick(int position) {
        DebtsItem debtsSelected = debtsItems.get(position);
        View dialog = LayoutInflater.from(getContext()).inflate(R.layout.simple_item_debts_dialog,null);
        TextView textViewDescricaoDividaEdit = dialog.findViewById(R.id.textViewDescricaoDividaEdit);
        TextView textViewValorDividaEdit = dialog.findViewById(R.id.textViewValorDividaEdit);
        TextView textViewTipoDividaEdit = dialog.findViewById(R.id.TextViewTipoDividaEdit);
        Button buttonDeletarEdit = dialog.findViewById(R.id.buttonDeletarEdit);
        RadioGroup radioGroupStatusDividaEdit = dialog.findViewById(R.id.radioGroupStatusDividaEdit);
        RadioButton radioButtonPedenteEdit = dialog.findViewById(R.id.radioButtonPedenteEdit);
        RadioButton radioButtonAtrasadaEdit = dialog.findViewById(R.id.radioButtonAtrasadaEdit);
        RadioButton radioButtonQuitadaEdit = dialog.findViewById(R.id.radioButtonQuitadaEdit);
        Button buttonSalvarEdit = dialog.findViewById(R.id.buttonSalvarEdit);


        textViewDescricaoDividaEdit.setText(debtsSelected.getNameDebts().toUpperCase());
        textViewTipoDividaEdit.setText(debtsSelected.getTypeOfDebts().toString());

        if (debtsSelected.getTypeOfDebts().toString().equals(TypeOfDebts.PARCELADA.toString())) {
            textViewValorDividaEdit.setText(debtsSelected.getNumberOfParcels().toString()+"x "+ FormatDataUtils.formatMonetaryValue(debtsSelected.getDebtsValue()/debtsSelected.getNumberOfParcels()));
        }
        if (debtsSelected.getTypeOfDebts().toString().equals(TypeOfDebts.UNICA.toString())) {
            textViewValorDividaEdit.setText(FormatDataUtils.formatMonetaryValue(debtsSelected.getDebtsValue()));
        }


        buttonDeletarEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                debtsSelected.delete();
                loadList();
                alertDialog.dismiss();
            }
        });

        if (radioButtonPedenteEdit.getText().toString().toUpperCase().equals(debtsSelected.getStatus().toUpperCase())) {
            radioButtonPedenteEdit.setChecked(true);
        } else if(radioButtonAtrasadaEdit.getText().toString().toUpperCase().equals(debtsSelected.getStatus().toUpperCase())) {
            radioButtonAtrasadaEdit.setChecked(true);
        } else if (radioButtonQuitadaEdit.getText().toString().toUpperCase().equals(debtsSelected.getStatus().toUpperCase())) {
            radioButtonQuitadaEdit.setChecked(true);
        }

        radioGroupStatusDividaEdit.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (radioButtonPedenteEdit.isChecked()) {
                    StatusDividaSelected = radioButtonPedenteEdit.getText().toString();
                }
                if (radioButtonAtrasadaEdit.isChecked()) {
                    StatusDividaSelected = radioButtonAtrasadaEdit.getText().toString();
                }
                if (radioButtonQuitadaEdit.isChecked()) {
                    StatusDividaSelected = radioButtonQuitadaEdit.getText().toString();
                }
            }
        });

        buttonSalvarEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             if(Objects.nonNull(StatusDividaSelected)){
                if (StatusDividaSelected.equals(debtsSelected.getStatus().toUpperCase())) {
                    alertDialog.dismiss();
                    debtsItems.clear();
                } else {
                    debtsSelected.setStatus(StatusDividaSelected);
                    debtsSelected.save();
                    debtsItems.clear();
                }
            }
                alertDialog.dismiss();
            }
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext()).setView(dialog);
        alertDialog=builder.create();
        alertDialog.show();
    }
}