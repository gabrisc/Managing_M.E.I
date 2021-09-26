package com.example.managing_mei.view.ui.company_health.ui.cashFlow;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.managing_mei.R;
import com.example.managing_mei.adapters.AdapterCashFlowItem;
import com.example.managing_mei.model.entities.CashFlowItem;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import static com.example.managing_mei.utils.Contants.PAGAMENTO_CASH_FLOW_ITEM_TYPE;
import static com.example.managing_mei.utils.Contants.RECEBIMENTO_CASH_FLOW_ITEM_TYPE;
import static com.example.managing_mei.utils.FireBaseConfig.firebaseInstance;
import static com.example.managing_mei.utils.FireBaseConfig.getIdUser;
import static com.example.managing_mei.utils.FormatDataUtils.cleanFormat;
import static com.example.managing_mei.utils.FormatDataUtils.cleanFormatValues;
import static com.example.managing_mei.utils.FormatDataUtils.formatMonetaryValuePositiveOrNegative;
import static java.util.stream.Collectors.toSet;

public class CashFlowFragment extends Fragment implements AdapterCashFlowItem.OnCashFlowItemListener {

    private TextView totalOfDay, totalOfPayment, totalOfReceived;
    private Double totalOfDayValue, totalOfPaymentValue, totalOfReceivedValue;
    private ImageButton AddNewCashFlowItem,imageButtonFilter;
    private List<CashFlowItem> allCashFlowDtos = new ArrayList<>();
    private Set<CashFlowItem> cashFlowItemsSelectByDate;
    private RecyclerView recyclerView;
    private AdapterCashFlowItem adapterClient;
    private Integer toggleButtonState = RECEBIMENTO_CASH_FLOW_ITEM_TYPE;
    private Spinner spinnerDay,spinnerMonth,spinnerYear;
    private Dialog mDialogView;
    private Set<String> days,months,year;
    private SimpleDateFormat dateFullFormat,dayFormat,monthFormat,yearFormat,monthYearFormat;

    public CashFlowFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cash_flow, container, false);
        cashFlowItemsSelectByDate = new HashSet<>();
        recyclerView = view.findViewById(R.id.recyclerViewCashFlowItem);
        totalOfDay = view.findViewById(R.id.textViewSaldoDoDia);
        totalOfPayment = view.findViewById(R.id.textViewTotalDePagamentos);
        totalOfReceived = view.findViewById(R.id.textViewTotalDeRecebimentos);
        AddNewCashFlowItem = view.findViewById(R.id.imageButtonAddNewFlowOfCash);
        imageButtonFilter = view.findViewById(R.id.imageButtonFilter);
        days = new HashSet<>();
        months = new HashSet<>();
        year = new HashSet<>();
        dateFullFormat = new SimpleDateFormat("dd/MM/yyyy");
        dayFormat = new SimpleDateFormat("dd");
        monthFormat = new SimpleDateFormat("MM");
        yearFormat = new SimpleDateFormat("yyyy");
        monthYearFormat = new SimpleDateFormat("MM/yyyy");
        mDialogView =  new Dialog(getContext());
        mDialogView.setContentView(R.layout.dialog_filter_cash_flow);
        spinnerDay = mDialogView.findViewById(R.id.spinnerDay);
        spinnerMonth = mDialogView.findViewById(R.id.spinnerMonth);
        spinnerYear = mDialogView.findViewById(R.id.spinnerYear);

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
                        setValuesInSpinners();
                        callDialog();

                        AddNewCashFlowItem.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                addNewFlow();
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

    private void calcTotalOfDay() {
        totalOfDayValue = totalOfReceivedValue-totalOfPaymentValue;
        totalOfDay.setText(""+totalOfDayValue.toString());
    }

    private void calcTotalOfReceived() {
        totalOfReceivedValue = cashFlowItemsSelectByDate.stream().filter(cashFlowItem -> cashFlowItem.getType().equals(1)).map(CashFlowItem::getValue).reduce(0.0,(valor, total) -> total+=valor);
        totalOfReceived.setText(""+totalOfReceivedValue.toString());
    }

    private void calcTotalOfPayment() {
        totalOfPaymentValue = cashFlowItemsSelectByDate.stream()
                .filter(cashFlowItem -> cashFlowItem.getType().equals(0))
                .map(CashFlowItem::getValue)
                .reduce(0.0,(valor, total) -> total+=valor);
        totalOfPayment.setText(""+totalOfPaymentValue.toString());
    }

    private void addNewFlow(){
        final Dialog dialog = new Dialog(getContext());
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.dialog_add_new_cash_item);

        Button buttonAdd = dialog.findViewById(R.id.buttonCancelAddingNewCashItem);
        Switch aSwitch = dialog.findViewById(R.id.switchTypeNewCashItem);
        TextInputLayout editTextValueForNewCashItem = dialog.findViewById(R.id.editTextValueForCashFlowItem);
        TextInputLayout editTextDescription = dialog.findViewById(R.id.editTextValueForCashFlowNameItem);

        aSwitch.setText("Entrada de dinheiro");
        aSwitch.setChecked(true);

        if (Objects.nonNull(editTextValueForNewCashItem.getEditText().getText().toString())){
            if (aSwitch.isChecked()){
                Double value  = editTextValueForNewCashItem.getEditText().getText().toString().equals("") ? Double.valueOf("0.0") : Double.valueOf(editTextValueForNewCashItem.getEditText().getText().toString());
                editTextValueForNewCashItem.getEditText().setText(formatMonetaryValuePositiveOrNegative(value,false));
            } else {
                Double value = editTextValueForNewCashItem.getEditText().getText().toString().equals("") ? Double.valueOf("0.0") :Double.valueOf(editTextValueForNewCashItem.getEditText().getText().toString());
                editTextValueForNewCashItem.getEditText().setText(formatMonetaryValuePositiveOrNegative(value,true));
            }
        }

        editTextValueForNewCashItem.getEditText().setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b){
                    editTextValueForNewCashItem.getEditText().setText(cleanFormatValues(editTextValueForNewCashItem.getEditText().getText().toString()));
                }else{
                    //vou ter que tranformar a troca de valores de positivo para negativo em um metodo
                }
            }
        });

        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    aSwitch.setText("Saída de dinheiro");
                    toggleButtonState = PAGAMENTO_CASH_FLOW_ITEM_TYPE;
                } else {
                    aSwitch.setText("Entrada de dinheiro");
                    toggleButtonState = RECEBIMENTO_CASH_FLOW_ITEM_TYPE;
                }
            }
        });

        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (editTextDescription.getEditText().getText().toString().isEmpty()) {
                    Toast.makeText(getContext(),"Preencha a descrição",Toast. LENGTH_SHORT).show();
                } else if (editTextValueForNewCashItem.getEditText().getText().toString().isEmpty()) {
                    Toast.makeText(getContext(),"Preencha o valor",Toast. LENGTH_SHORT).show();
                } else {
                    CashFlowItem cashFlowItem = new CashFlowItem(toggleButtonState,Double.parseDouble(cleanFormat(editTextValueForNewCashItem.getEditText().getText().toString())),editTextDescription.getEditText().getText().toString());
                    cashFlowItem.save();
                    setValuesInSpinners();
                    dialog.dismiss();
                }
            }
        });
        dialog.create();
        dialog.show();
    }

    public void callDialog(){
        imageButtonFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Switch switchToFilterByDay = mDialogView.findViewById(R.id.switchToFilterByDay);
                switchToFilterByDay.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                        if (b){
                            spinnerDay.setVisibility(View.INVISIBLE);
                            spinnerDay.setEnabled(false);
                        }else{
                            spinnerDay.setVisibility(View.VISIBLE);
                            spinnerDay.setEnabled(true);
                            reloadSpinnerDays(getUniqueSetDate());
                        }
                    }
                });
                Button buttonFilter = mDialogView.findViewById(R.id.buttonFilter);

                buttonFilter.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        cashFlowItemsSelectByDate.clear();
                        if (switchToFilterByDay.isChecked()) {
                            setValuesForListInRecicler(spinnerMonth.getSelectedItem().toString()+"/"+
                                                       spinnerYear.getSelectedItem().toString());
                        } else {
                            setValuesForListInRecicler(spinnerDay.getSelectedItem().toString()+"/"+
                                                       spinnerMonth.getSelectedItem().toString()+"/"+
                                                       spinnerYear.getSelectedItem().toString());
                        }
                        mDialogView.dismiss();
                    }
                });



                mDialogView.create();
                mDialogView.show();
            }
        });
    }

    private void reloadRecyclerClient(){
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext().getApplicationContext()));
        adapterClient = new AdapterCashFlowItem(new ArrayList<>(cashFlowItemsSelectByDate),
                                                this.getContext().getApplicationContext(),
                                                this::onCashFlowItemClick);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapterClient);
    }

    public void setValuesInSpinners(){
        Set<Date> dates = getUniqueSetDate();
        reloadSpinnerDYears(dates);
        reloadSpinnerMonths(dates);
        reloadSpinnerDays(dates);
        setValuesForListInRecicler(dateFullFormat.format(dates.iterator().next()));
    }

    public void setValuesForListInRecicler(String dateSelect){
        cashFlowItemsSelectByDate.clear();
        if (dateSelect.length()==7) {
            cashFlowItemsSelectByDate = allCashFlowDtos.stream().map(cashFlowItem -> monthYearFormat.format(cashFlowItem.getDate()).equals(dateSelect) ? cashFlowItem: null).collect(toSet());
        } else {
            cashFlowItemsSelectByDate = allCashFlowDtos.stream().map(cashFlowItem -> dateFullFormat.format(cashFlowItem.getDate()).equals(dateSelect) ? cashFlowItem: null).collect(toSet());
        }
        cashFlowItemsSelectByDate.removeIf(Objects::isNull);
        reloadRecyclerClient();
        calcTotalOfReceived();
        calcTotalOfPayment();
        calcTotalOfDay();
    }

    private Set<Date> getUniqueSetDate(){
        Map<String, Date> map = new HashMap<>();
        allCashFlowDtos.forEach(cashFlowItem -> {map.put(cashFlowItem.getId(),cashFlowItem.getDate()); });

        return map.entrySet().stream().map(stringDateEntry -> stringDateEntry.getValue()).collect(toSet());
    }

    private void reloadSpinnerDays(Set<Date> allUniqueDates){
        days = allUniqueDates.stream().map(date -> monthFormat.format(date).equals(spinnerMonth.getSelectedItem().toString()) ? dayFormat.format(date) : null).collect(toSet());
        days.removeIf(Objects::isNull);
        spinnerDay.setAdapter(new ArrayAdapter(getContext().getApplicationContext(),R.layout.item_spinner,days.toArray()));
    }
    private void reloadSpinnerMonths(Set<Date> allUniqueDates){
        months = allUniqueDates.stream().map(date -> yearFormat.format(date).equals(spinnerYear.getSelectedItem().toString())? monthFormat.format(date): null).collect(toSet());
        months.removeIf(Objects::isNull);
        spinnerMonth.setAdapter(new ArrayAdapter(getContext().getApplicationContext(),R.layout.item_spinner,months.toArray()));
        spinnerMonth.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
             reloadSpinnerDays(getUniqueSetDate());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }
    private void reloadSpinnerDYears(Set<Date> allUniqueDates){
        year = allUniqueDates.stream().map(date -> yearFormat.format(date)).collect(toSet());
        spinnerYear.setAdapter(new ArrayAdapter(getContext().getApplicationContext(),R.layout.item_spinner,year.toArray()));
    }

    @Override
    public void onCashFlowItemClick(int position) {}

}