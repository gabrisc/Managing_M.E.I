package com.example.managing_mei.view.ui.company_health.ui.cashFlow;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.managing_mei.R;
import com.example.managing_mei.adapters.AdapterCashFlowItem;
import com.example.managing_mei.model.entities.CashFlowItem;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.example.managing_mei.utils.FireBaseConfig.firebaseInstance;
import static com.example.managing_mei.utils.FireBaseConfig.getIdUser;

public class CashFlowFragment extends Fragment implements AdapterCashFlowItem.OnCashFlowItemListener {

    private AlertDialog alertDialog;

    private TextView totalOfDay, totalOfPayment, totalOfReceived;
    private Double totalOfDayValue, totalOfPaymentValue, totalOfReceivedValue;
    private ImageButton AddNewCashFlowItem;


    private List<CashFlowItem> cashFlowDtos = new ArrayList<>();
    private RecyclerView recyclerView;
    private AdapterCashFlowItem adapterClient;

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

        AddNewCashFlowItem = view.findViewById(R.id.imageButtonAddNewFlowOfCash);

        //loadList();

        cashFlowDtos.clear();
        firebaseInstance.getReference()
                .child(getIdUser())
                .child("CashFlowItens")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot ds: snapshot.getChildren()){
                            CashFlowItem cashFlowItem = ds.getValue(CashFlowItem.class);
                            cashFlowDtos.add(cashFlowItem);
                        }
                        adapterClient.notifyDataSetChanged();
                        calcTotalOfPayment();
                        calcTotalOfReceived();
                        calcTotalOfDay();
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        String x = String.valueOf(error);
                    }
                });

        reloadRecyclerClient();

        AddNewCashFlowItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addNewChasFlowItem();
            }

            private void addNewChasFlowItem() {

            }
        });
        return view;
    }

    private void calcTotalOfDay() {
        totalOfDayValue = totalOfReceivedValue-totalOfPaymentValue;
        totalOfDay.setText(""+totalOfDayValue.toString());
    }

    private void calcTotalOfReceived() {
        totalOfReceivedValue = cashFlowDtos.stream()
                                           .filter(cashFlowItem -> cashFlowItem.getType().equals(1))
                                           .map(CashFlowItem::getValue)
                                           .reduce(0.0,(valor, total) -> total+=valor);

        totalOfReceived.setText(""+totalOfReceivedValue.toString());
    }

    private void calcTotalOfPayment() {
        totalOfPaymentValue = cashFlowDtos.stream()
                                          .filter(cashFlowItem -> cashFlowItem.getType().equals(0))
                                          .map(CashFlowItem::getValue)
                                          .reduce(0.0,(valor, total) -> total+=valor);

        totalOfPayment.setText(""+totalOfPaymentValue.toString());
    }

    private void applyDiscount(){
        View mDialogView = LayoutInflater.from(getContext().getApplicationContext()).inflate(R.layout.dialog_add_new_cash_item,null);

       // ImageButton butttonCancel = mDialogView.findViewById(R.id.);
        //ImageButton buttonAddDiscount = mDialogView.findViewById(R.id.);
       // EditText editText = mDialogView.findViewById(R.id.);

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext().getApplicationContext()).setView(mDialogView).setTitle("DESCONTO");
        alertDialog=builder.create();
       // editText.setInputType(InputType.TYPE_CLASS_NUMBER);
        alertDialog.show();
    }

    private void reloadRecyclerClient(){
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext().getApplicationContext()));
        adapterClient = new AdapterCashFlowItem(cashFlowDtos,this.getContext().getApplicationContext(),this::onCashFlowItemClick);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapterClient);
    }

    private void loadList(){

    }

    @Override
    public void onCashFlowItemClick(int position) {

    }
}