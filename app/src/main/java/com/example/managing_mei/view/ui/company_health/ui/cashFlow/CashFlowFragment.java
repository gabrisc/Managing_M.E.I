package com.example.managing_mei.view.ui.company_health.ui.cashFlow;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.ArraySet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.managing_mei.R;
import com.example.managing_mei.adapters.AdapterCashFlowItem;
import com.example.managing_mei.model.entities.CashFlowItem;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Date;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.NavigableSet;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

import static com.example.managing_mei.utils.FireBaseConfig.firebaseInstance;
import static com.example.managing_mei.utils.FireBaseConfig.getIdUser;
import static com.example.managing_mei.utils.FormatDataUtils.formatDateToStringFormated;
import static com.example.managing_mei.utils.FormatDataUtils.formatMonetaryValue;
import static com.example.managing_mei.utils.FormatDataUtils.formatMonetaryValuePositiveOrNegative;

public class CashFlowFragment extends Fragment implements AdapterCashFlowItem.OnCashFlowItemListener {

    private RecyclerView  recyclerView;
    private TextView saldoTotal,totalPagamentos,totalRecebimentos;
    private Spinner spinnerYear,spinnerDays,spinnerMonth;
    private ImageButton filterBotao;
    private AdapterCashFlowItem adapterCashFlowItem;
    private List<CashFlowItem> cashFlowItemList = new ArrayList<>();
    private Set <Date> dates = new HashSet<>();
    private Date dataSelecionada;
    private List<CashFlowItem> filteredList = new ArrayList<>();
    private Button buttonFilter;
    private SimpleDateFormat dateFullFormat,dayFormat,monthFormat,yearFormat;
    private Dialog alertDialogFilter,alertDialogAddNewCashFlow;

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
        saldoTotal = view.findViewById(R.id.textViewSaldoDoDia);
        totalPagamentos = view.findViewById(R.id.textViewTotalDePagamentos);
        totalRecebimentos = view.findViewById(R.id.textViewTotalDeRecebimentos);
        filterBotao = view.findViewById(R.id.imageButtonFilter);
        dateFullFormat = new SimpleDateFormat("dd/MM/yyyy");
        dayFormat = new SimpleDateFormat("dd");
        monthFormat = new SimpleDateFormat("MM");
        yearFormat = new SimpleDateFormat("yyyy");
        alertDialogFilter = new Dialog(getContext());
        alertDialogFilter.setContentView(R.layout.dialog_filter_cash_flow);
        spinnerYear = alertDialogFilter.findViewById(R.id.spinnerYear);
        spinnerDays = alertDialogFilter.findViewById(R.id.spinnerDay);
        spinnerMonth = alertDialogFilter.findViewById(R.id.spinnerMonth);
        buttonFilter = alertDialogFilter.findViewById(R.id.buttonFilter);

        alertDialogAddNewCashFlow = new Dialog(getContext());
        alertDialogAddNewCashFlow.setContentView(R.layout.dialog_add_new_cash_item);
        reloadRecyclerClient();
        firebaseInstance.getReference()
                .child(getIdUser())
                .child("CashFlowItens")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot ds: snapshot.getChildren()){
                            CashFlowItem cashFlowItem = ds.getValue(CashFlowItem.class);
                            cashFlowItemList.add(cashFlowItem);
                        }
                        getAllDates();
                        filterBotao.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                filterDialog();
                            }
                        });
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        String x = String.valueOf(error);
                    }
                });

        return view;
    }

    private void filter() {
        filteredList.addAll(cashFlowItemList.stream().filter(cashFlowItem -> dateFullFormat.format(cashFlowItem.getDate()).equals(dateFullFormat.format(dataSelecionada))).collect(Collectors.toList()));
        cashFlowItemList.size();
        filteredList.size();
    }

    private void setAllValuesInMap() {

    }

    @Override
    public void onPause() {
        super.onPause();
        cashFlowItemList.clear();
    }

    @Override
    public void onStop() {
        super.onStop();
        cashFlowItemList.clear();
    }

    private void getAllDates() {
        //Dados fonte para as datas datesAndCashFlowItem
        //Esse metodo deve capturar todas as datas existentes e um SET sendo assim unicas
        dates.addAll(cashFlowItemList.stream().map(CashFlowItem::getDate).collect(Collectors.toSet()));
    }

    private void reloadRecyclerClient(){
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext().getApplicationContext()));
        adapterCashFlowItem = new AdapterCashFlowItem(filteredList,this.getContext().getApplicationContext(),this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapterCashFlowItem);
    }

    private void addNewFlow(){
        final Dialog dialog = new Dialog(getContext());
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.dialog_add_new_cash_item);

        dialog.create();
        dialog.show();
    }

    public void callDialogNewCashFlowItem(){

        alertDialogAddNewCashFlow.create();
        alertDialogAddNewCashFlow.show();
    }

    public void filterDialog(){
        spinnerMonth.setVisibility(View.INVISIBLE);
        spinnerDays.setVisibility(View.INVISIBLE);
        setSpinnerYearValue();
        setSpinnerMonthValue();

        spinnerYear.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    spinnerMonth.setVisibility(View.VISIBLE);
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spinnerMonth.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (!spinnerMonth.getSelectedItem().toString().equals("Selecione...")) {
                    spinnerDays.setVisibility(View.VISIBLE);
                    setSpinnerDayValue();
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        buttonFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (spinnerDays.getSelectedItem().equals("Selecione...") || spinnerMonth.getSelectedItem().equals("Selecione...")) {
                    Toast.makeText(getContext(),"Preencha todos os campos",Toast. LENGTH_LONG).show();
                } else {
                    try {
                        dataSelecionada = new SimpleDateFormat("dd/MM/yyyy").parse(spinnerDays.getSelectedItem().toString()+"/"+ spinnerMonth.getSelectedItem().toString()+"/"+ spinnerYear.getSelectedItem().toString());
                        filter();
                        reloadRecyclerClient();
                        calcTotalValues();
                        alertDialogFilter.dismiss();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        alertDialogFilter.create();
        alertDialogFilter.show();
    }

    private void calcTotalValues() {
        Double saidaTotal = filteredList.stream().filter(cashFlowItem -> cashFlowItem.getType().equals(0)).mapToDouble(CashFlowItem::getValue).sum();
        Double entradaTotal = filteredList.stream().filter(cashFlowItem -> cashFlowItem.getType().equals(1)).mapToDouble(CashFlowItem::getValue).sum();
        saldoTotal.setText(formatMonetaryValue(entradaTotal-saidaTotal));
        totalPagamentos.setText(formatMonetaryValuePositiveOrNegative(saidaTotal,false));
        totalRecebimentos.setText(formatMonetaryValuePositiveOrNegative(entradaTotal,true));
    }

    private void setSpinnerDayValue() {
        Deque<String> daySet  = new ArrayDeque<>();
        daySet.add("Selecione...");
        dates.forEach(s -> { if (monthFormat.format(s).equals(spinnerMonth.getSelectedItem())) { daySet.add(dayFormat.format(s)); }});
        ArrayAdapter dayAdapter = new ArrayAdapter(getContext(),R.layout.item_spinner,daySet.toArray());
        spinnerDays.setAdapter(dayAdapter);
    }

    private void setSpinnerMonthValue() {
        Deque<String> monthSet  = new ArrayDeque<>();
        dates.forEach(s -> {
            if (yearFormat.format(s).equals(spinnerYear.getSelectedItem())) { monthSet.add(monthFormat.format(s)); }
        });
        monthSet.addFirst("Selecione...");
        ArrayAdapter monthAdapter = new ArrayAdapter(getContext(),R.layout.item_spinner,monthSet.toArray());
        spinnerMonth.setAdapter(monthAdapter);
    }

    private void setSpinnerYearValue() {
        NavigableSet<String> yearList = new TreeSet<>();
        dates.forEach(s -> {
            yearList.add(yearFormat.format(s));
        });
        ArrayAdapter yearAdapter = new ArrayAdapter(getContext(),R.layout.item_spinner,yearList.toArray());
        spinnerYear.setAdapter(yearAdapter);
    }

    @Override
    public void onCashFlowItemClick(int position) {

    }
}
