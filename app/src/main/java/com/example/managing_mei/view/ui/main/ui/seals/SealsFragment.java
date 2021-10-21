package com.example.managing_mei.view.ui.main.ui.seals;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.managing_mei.R;
import com.example.managing_mei.adapters.AdapterClient;
import com.example.managing_mei.adapters.AdapterSales;
import com.example.managing_mei.model.entities.Client;
import com.example.managing_mei.model.entities.Sale;
import com.example.managing_mei.utils.FormatDataUtils;
import com.example.managing_mei.view.ui.main.ui.seals.addSell.AddSellActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import static com.example.managing_mei.utils.FireBaseConfig.firebaseInstance;
import static com.example.managing_mei.utils.FireBaseConfig.getIdUser;
import static com.example.managing_mei.utils.FormatDataUtils.formatDateToStringFormated;
import static com.example.managing_mei.utils.FormatDataUtils.formatMonetaryParcelSale;
import static com.example.managing_mei.utils.FormatDataUtils.formatMonetaryValue;

public class SealsFragment extends Fragment implements AdapterSales.OnSaleListerner,AdapterClient.OnClientListener{

    private ImageButton imageButtonAddNewSells;
    private List<Sale> clientList= new ArrayList<>();
    private RecyclerView recyclerView;
    private AdapterSales adapterSales;
    private AlertDialog alertDialog;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sales, container, false);

        recyclerView = view.findViewById(R.id.recyclerViewSales);
        imageButtonAddNewSells = view.findViewById(R.id.imageButtonAddNewSells);

        callAddProvider();
        loadList();
        reloadRecyclerClient();
        return view;
    }

    private void callAddProvider(){
        imageButtonAddNewSells.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity( new Intent(getContext(),AddSellActivity.class));
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        clientList.clear();
        reloadRecyclerClient();

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    private void loadList(){
        clientList.clear();
        firebaseInstance.getReference()
                .child(getIdUser())
                .child("Sales")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot ds: snapshot.getChildren()){
                            Sale sale = ds.getValue(Sale.class);
                            clientList.add(sale);
                        }
                        adapterSales.notifyDataSetChanged();
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        String x = String.valueOf(error);
                    }
                });
    }

    private void reloadRecyclerClient(){

        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext().getApplicationContext()));
        adapterSales = new AdapterSales(clientList,this.getContext().getApplicationContext(),this::onSaleListenerClick);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapterSales);
    }

    @Override
    public void onSaleListenerClick(int position) {
        View dialog = LayoutInflater.from(getContext()).inflate(R.layout.dialog_sale_complete,null);
        Sale sale = clientList.get(position);
        List<Client> clientListFake = new ArrayList<>();

        sale.getEconomicOperationForSaleVoList().forEach(productForSaleVo -> {
            clientListFake.add(new Client("",(productForSaleVo.getProduct().getName().toUpperCase()
                                                 +"  x "+
                                                 productForSaleVo.getQuantitySelect()
                                                 + " "+
                                                 productForSaleVo.getProduct().getTypeQuantity().toLowerCase()),"",""));
        });

        RecyclerView recyclerViewItensToShow;
        TextView valorFinal,desconto,lucro,formaDePagament,dataVenda,nomeCliente;
        Button button;

        nomeCliente = dialog.findViewById(R.id.textViewNomeClientShowSale);
        recyclerViewItensToShow = dialog.findViewById(R.id.recyclerViewShowSale);
        valorFinal = dialog.findViewById(R.id.textViewValorFinalShowSale);
        desconto = dialog.findViewById(R.id.textViewDescontoShowSale);
        lucro = dialog.findViewById(R.id.textViewLucroShowSale);
        formaDePagament = dialog.findViewById(R.id.textViewFormaDePagamentoShowSale);
        dataVenda = dialog.findViewById(R.id.textViewDataShowSale);
        button = dialog.findViewById(R.id.buttonVoltarShowSale);

        if (sale.isDividedSale()) {
            valorFinal.setText(formatMonetaryParcelSale(sale.getParcelValue(),sale.getDividedQuantity()));
        } else {
            valorFinal.setText(formatMonetaryValue(sale.getTotalValueFromProductsAndDiscount()));
        }
        nomeCliente.setText(sale.getClient().getNome().toUpperCase());
        desconto.setText(formatMonetaryValue(sale.getTotalDiscountFromSeal()));
        dataVenda.setText(sale.getDate());
        lucro.setText(formatMonetaryValue(sale.getGain()));
        formaDePagament.setText(sale.getPaymentType().toUpperCase());

        recyclerViewItensToShow.setLayoutManager(new LinearLayoutManager(dialog.getContext()));
        AdapterClient adapterClient = new AdapterClient(clientListFake,dialog.getContext(),this::onClientOperationClick);
        recyclerViewItensToShow.setHasFixedSize(true);
        recyclerViewItensToShow.setAdapter(adapterClient);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext()).setView(dialog);
        alertDialog=builder.create();
        alertDialog.show();
    }

    @Override
    public void onClientOperationClick(int position) {

    }
}