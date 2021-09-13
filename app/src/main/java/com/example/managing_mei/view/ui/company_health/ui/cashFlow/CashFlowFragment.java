package com.example.managing_mei.view.ui.company_health.ui.cashFlow;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.managing_mei.R;
import com.example.managing_mei.adapters.AdapterCashFlowItem;
import com.example.managing_mei.model.entities.CashFlowItem;
import com.example.managing_mei.view.ui.company_health.CompanyHealthActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static com.example.managing_mei.utils.Contants.PAGAMENTO;
import static com.example.managing_mei.utils.Contants.PAGAMENTO_CASH_FLOW_ITEM_TYPE;
import static com.example.managing_mei.utils.Contants.RECEBIMENTO;
import static com.example.managing_mei.utils.Contants.RECEBIMENTO_CASH_FLOW_ITEM_TYPE;
import static com.example.managing_mei.utils.FireBaseConfig.firebaseInstance;
import static com.example.managing_mei.utils.FireBaseConfig.getIdUser;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;
import static java.util.stream.Collectors.toSet;

public class CashFlowFragment extends Fragment implements AdapterCashFlowItem.OnCashFlowItemListener {

    private TextView totalOfDay, totalOfPayment, totalOfReceived;
    private Double totalOfDayValue, totalOfPaymentValue, totalOfReceivedValue;
    private ImageButton AddNewCashFlowItem;
    private List<CashFlowItem> allCashFlowDtos = new ArrayList<>();
    private List<CashFlowItem> cashFlowItemsSelectByDate = new ArrayList<>();
    private RecyclerView recyclerView;
    private AdapterCashFlowItem adapterClient;
    private Integer toggleButtonState = RECEBIMENTO_CASH_FLOW_ITEM_TYPE;
    private Spinner spinnerDate;

    public CashFlowFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cash_flow, container, false);

       recyclerView = view.findViewById(R.id.recyclerViewCashFlowItem);
       totalOfDay = view.findViewById(R.id.textViewSaldoDoDia);
       totalOfPayment = view.findViewById(R.id.textViewTotalDePagamentos);
       totalOfReceived = view.findViewById(R.id.textViewTotalDeRecebimentos);
       spinnerDate = view.findViewById(R.id.spinnerDateOfFlow);
       AddNewCashFlowItem = view.findViewById(R.id.imageButtonAddNewFlowOfCash);

       allCashFlowDtos.clear();

       firebaseInstance.getReference()
                       .child(getIdUser())
                       .child("CashFlowItens")
                       .addValueEventListener(new ValueEventListener() {
                           @Override
                           public void onDataChange(@NonNull DataSnapshot snapshot) {
                               for (DataSnapshot ds: snapshot.getChildren()){
                                   CashFlowItem cashFlowItem = ds.getValue(CashFlowItem.class);
                                   allCashFlowDtos.add(cashFlowItem);
                               }
                               setValuesInSpinner();
                           }
                           @Override
                           public void onCancelled(@NonNull DatabaseError error) {
                               String x = String.valueOf(error);
                           }
                       });

        AddNewCashFlowItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                applyDiscount();
            }
        });
        return view;
    }

    private void setValuesInSpinner() {
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Map<String,String> map = new HashMap<>();

        allCashFlowDtos.forEach(cashFlowItem -> { map.put(cashFlowItem.getId(),dateFormat.format(cashFlowItem.getDate())); });

        ArrayAdapter arrayAdapter = new ArrayAdapter(getContext().getApplicationContext(),R.layout.item_spinner, map.entrySet().stream().map(stringStringEntry -> stringStringEntry.getValue()).collect(toSet()).toArray());
        spinnerDate.setAdapter(arrayAdapter);
        spinnerDate.setSelection(map.entrySet().stream().map(stringStringEntry -> stringStringEntry.getValue()).collect(toSet()).size()-1);
        spinnerDate.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                cashFlowItemsSelectByDate.clear();
                allCashFlowDtos.forEach(cashFlowItem -> {
                    if (dateFormat.format(cashFlowItem.getDate()).equals(spinnerDate.getSelectedItem().toString())){
                        cashFlowItemsSelectByDate.add(cashFlowItem);
                    }
                });
                reloadRecyclerClient();
                calcTotalOfReceived();
                calcTotalOfPayment();
                calcTotalOfDay();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void calcTotalOfDay() {
        totalOfDayValue = totalOfReceivedValue-totalOfPaymentValue;
        totalOfDay.setText(""+totalOfDayValue.toString());
    }

    private void calcTotalOfReceived() {
        totalOfReceivedValue = cashFlowItemsSelectByDate.stream()
                                           .filter(cashFlowItem -> cashFlowItem.getType().equals(1))
                                           .map(CashFlowItem::getValue)
                                           .reduce(0.0,(valor, total) -> total+=valor);
        totalOfReceived.setText(""+totalOfReceivedValue.toString());
    }

    private void calcTotalOfPayment() {
         totalOfPaymentValue = cashFlowItemsSelectByDate.stream()
                                          .filter(cashFlowItem -> cashFlowItem.getType().equals(0))
                                          .map(CashFlowItem::getValue)
                                          .reduce(0.0,(valor, total) -> total+=valor);
        totalOfPayment.setText(""+totalOfPaymentValue.toString());
    }

    private void applyDiscount(){
        final Dialog dialog = new Dialog(getContext());
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.dialog_add_new_cash_item);

        Button buttonAdd = dialog.findViewById(R.id.buttonCancelAddingNewCashItem);
        ToggleButton toggleButtonType = dialog.findViewById(R.id.toggleButtonTypeNewCashItem);
        EditText editTextValueForNewCashItem = dialog.findViewById(R.id.editTextValueForAddnewCashItem);
        EditText editTextDescription = dialog.findViewById(R.id.editTextValueForAddnewCashItem);

        toggleButtonType.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                //On is PAGAMENTO //Off is RECEBIMENTO
                if (b){
                    toggleButtonState = PAGAMENTO_CASH_FLOW_ITEM_TYPE;
                }else{
                    toggleButtonState = RECEBIMENTO_CASH_FLOW_ITEM_TYPE;
                }
            }
        });

        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (editTextDescription.getText().toString().isEmpty()) {
                    Toast.makeText(getContext(),"Preencha a descrição",Toast. LENGTH_SHORT).show();
                } else if (editTextValueForNewCashItem.getText().toString().isEmpty()) {
                    Toast.makeText(getContext(),"Preencha o valor",Toast. LENGTH_SHORT).show();
                } else {
                    CashFlowItem cashFlowItem = new CashFlowItem(toggleButtonState,Double.parseDouble(editTextValueForNewCashItem.getText().toString()),editTextDescription.getText().toString());
                    cashFlowItem.save();
                    setValuesInSpinner();
                    dialog.dismiss();
                }
            }
        });
        dialog.create();
        dialog.show();
    }

    private void reloadRecyclerClient(){
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext().getApplicationContext()));
        adapterClient = new AdapterCashFlowItem(cashFlowItemsSelectByDate,this.getContext().getApplicationContext(),this::onCashFlowItemClick);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapterClient);
    }

    @Override
    public void onCashFlowItemClick(int position) {

    }
}