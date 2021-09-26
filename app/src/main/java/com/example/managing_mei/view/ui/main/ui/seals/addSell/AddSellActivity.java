package com.example.managing_mei.view.ui.main.ui.seals.addSell;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.managing_mei.R;
import com.example.managing_mei.adapters.AdapterClient;
import com.example.managing_mei.adapters.AdapterProductForSales;
import com.example.managing_mei.model.entities.Client;
import com.example.managing_mei.model.entities.Product;
import com.example.managing_mei.model.entities.ProductForSaleVo;
import com.example.managing_mei.model.enuns.TypeOfProduct;
import com.example.managing_mei.view.ui.main.ui.seals.calcSellValue.CalcSellValueActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import static com.example.managing_mei.utils.FireBaseConfig.firebaseInstance;
import static com.example.managing_mei.utils.FireBaseConfig.getIdUser;

public class AddSellActivity extends AppCompatActivity implements AdapterProductForSales.OnProductOperationForSaleListener, AdapterClient.OnClientListener{

    private AdapterProductForSales adapterProduct;
    private AdapterClient adapterClient;
    private List<Product> listProduct= new ArrayList<>();
    private List<Client> clientList= new ArrayList<>();
    public final static Set<ProductForSaleVo> economicOperationForSaleVoArrayList = new HashSet<>();
    public static Client clientSelected;
    private RecyclerView recyclerView;
    private AlertDialog alertDialog;
    private TextView textViewOrder;
    private Button buttonConclusionSelect,buttonCancel,buttonProsseguirNewSeal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_sell);
        buttonConclusionSelect = findViewById(R.id.imageButtonConclusionSelect);
        textViewOrder= findViewById(R.id.textViewOrder);
        buttonCancel = findViewById(R.id.buttonCancelarFromAddNewSell);
        buttonProsseguirNewSeal = findViewById(R.id.buttonProsseguirNewSeal);

        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        buttonProsseguirNewSeal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clientSelected = new Client(" ","SEM CLIENTE"," "," ");
                clientList.clear();
                findAllEconomicOperation();
                reloadRecyclerEconomicOperation();
                buttonProsseguirNewSeal.setVisibility(View.INVISIBLE);
            }
        });


        reloadRecyclerClient();

        buttonConclusionSelect.setVisibility(View.INVISIBLE);
        buttonConclusionSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity( new Intent(getApplicationContext(), CalcSellValueActivity.class));
            }
        });

        firebaseInstance.getReference()
                        .child(getIdUser())
                        .child("clients")
                        .addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for (DataSnapshot ds: snapshot.getChildren()){
                                    Client client = ds.getValue(Client.class);
                                    clientList.add(client);
                                    reloadRecyclerClient();
                                }
                                reloadRecyclerClient();
                                adapterClient.notifyDataSetChanged();
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                String x = String.valueOf(error);
                            }
                        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        this.finish();
        economicOperationForSaleVoArrayList.clear();
    }

    private void callDialogForProduct(Product product,int position,Boolean isService){
        View mDialogView = LayoutInflater.from(this).inflate(R.layout.dialog_select_item,null);

        TextView textViewMensage = mDialogView.findViewById(R.id.TextViewTitle);
        TextView counter = mDialogView.findViewById(R.id.textViewCounterAddQuantity);

        Button conclusionButton = mDialogView.findViewById(R.id.imageButtonConclusionAddQuantity);
        Button cancelButton = mDialogView.findViewById(R.id.imageButtonCancelAddQuantity);

        SeekBar seekBar = mDialogView.findViewById(R.id.seekBarQuantityForAddInSale);


        seekBar.setMax(product.getQuantity());
        seekBar.setMin(1);

        if (isService){
            textViewMensage.setText("Deseja adicionar esse serviço?");
            conclusionButton.setVisibility(View.VISIBLE);
            cancelButton.setVisibility(View.VISIBLE);
            counter.setVisibility(View.INVISIBLE);
            seekBar.setVisibility(View.INVISIBLE);
        }

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(this).setView(mDialogView).setTitle("Quantidade");
        alertDialog=builder.create();

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @SuppressLint("DefaultLocale")
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                counter.setText(String.format("%d", i));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        conclusionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (counter.getText().toString().equals("0")) {
                    Toast.makeText(AddSellActivity.this, "Selecione uma quantidade", Toast.LENGTH_SHORT).show();
                } else {
                    if(Integer.parseInt(counter.getText().toString()) == product.getQuantity()) {
                        addEconomicOperation(new ProductForSaleVo(product, product.getQuantity()));
                        listProduct.remove(position);
                        reloadRecyclerEconomicOperation();
                        alertDialog.dismiss();
                        buttonVisibilityEnable(true);
                        buttonConclusionSelect.setVisibility(View.VISIBLE);
                    } else {
                        int quantityResult = product.getQuantity() - Integer.parseInt(counter.getText().toString());
                        listProduct.get(position).setQuantity(quantityResult);
                        addEconomicOperation(new ProductForSaleVo(product, Integer.parseInt(counter.getText().toString())));
                        reloadRecyclerEconomicOperation();
                        alertDialog.dismiss();
                        buttonVisibilityEnable(true);
                        buttonConclusionSelect.setVisibility(View.VISIBLE);
                    }
                }
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        alertDialog.show();
    }

    private void addEconomicOperation(ProductForSaleVo productForSaleVo){
        boolean find =false;
        for(Iterator<ProductForSaleVo> it = economicOperationForSaleVoArrayList.iterator(); it.hasNext();){
            ProductForSaleVo e= it.next();
            if (e.getProduct().getId().equals(productForSaleVo.getProduct().getId())){
                e.setQuantitySelect(productForSaleVo.getQuantitySelect() + e.getQuantitySelect());
                economicOperationForSaleVoArrayList.add(e);
                find=true;
            }
        }
        if(!find){
            economicOperationForSaleVoArrayList.add(productForSaleVo);
        }
    }

//#################################  VISIBILIDADE DOS BOTOES  ######################################

    private void buttonVisibilityEnable(Boolean enable){
        if (enable.equals(Boolean.TRUE)){
            buttonConclusionSelect.setVisibility(View.VISIBLE);
        }else{
            buttonConclusionSelect.setVisibility(View.INVISIBLE);
        }
    }

//##################################################################################################

    private void reloadRecyclerEconomicOperation(){
        textViewOrder.setText("SELECIONE O QUE SERÁ VENDIDO");
        recyclerView= findViewById(R.id.recyclerViewprodutosavenda);

        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        adapterProduct = new AdapterProductForSales(listProduct,getApplicationContext(),this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapterProduct);
    }

    private void findAllEconomicOperation(){
        firebaseInstance.getReference()
                .child(getIdUser())
                .child("product")
                .addValueEventListener(new ValueEventListener() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        listProduct.clear();
                        for (DataSnapshot ds: snapshot.getChildren()){
                            Product product= ds.getValue(Product.class);
                            listProduct.add(product);
                        }
                        adapterProduct.notifyDataSetChanged();
                        if (listProduct.isEmpty()){
                            buttonProsseguirNewSeal.setVisibility(View.VISIBLE);
                            buttonProsseguirNewSeal.setText("Não é possivel prosseguir");
                            buttonProsseguirNewSeal.setClickable(false);
                            buttonConclusionSelect.setVisibility(View.INVISIBLE);
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        String x = String.valueOf(error);
                    }
                });
    }

    @Override
    public void onEconomicOperationForSaleClick(int position) {
        Product economicOperationSelect = listProduct.get(position);
        if (economicOperationSelect.getType().equals(TypeOfProduct.PRODUTO.toString())){
            callDialogForProduct(economicOperationSelect,position,false);
        }else{
            callDialogForProduct(economicOperationSelect,position,true);
        }
    }

//##################################################################################################

    private void reloadRecyclerClient(){
        textViewOrder.setText("SELECIONE O CLIENTE");
        recyclerView = findViewById(R.id.recyclerViewprodutosavenda);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        adapterClient = new AdapterClient(clientList,getApplicationContext(),this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapterClient);
    }

    private void findAllClients(){

    }

    @Override
    public void onClientOperationClick(int position) {
        clientSelected = clientList.get(position);
        clientList.clear();
        findAllEconomicOperation();
        reloadRecyclerEconomicOperation();
        buttonProsseguirNewSeal.setVisibility(View.INVISIBLE);
    }
}