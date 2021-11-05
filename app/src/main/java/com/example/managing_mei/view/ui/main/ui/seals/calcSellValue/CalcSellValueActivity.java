package com.example.managing_mei.view.ui.main.ui.seals.calcSellValue;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.managing_mei.R;
import com.example.managing_mei.adapters.AdapterProductForSaleVo;
import com.example.managing_mei.model.entities.CashFlowItem;
import com.example.managing_mei.model.entities.PaymentType;
import com.example.managing_mei.model.entities.PaymentsTypes;
import com.example.managing_mei.model.entities.Product;
import com.example.managing_mei.model.entities.ProductForSaleVo;
import com.example.managing_mei.model.entities.Provider;
import com.example.managing_mei.model.entities.Sale;
import com.example.managing_mei.view.ui.main.ui.ManagementActivity;
import com.example.managing_mei.view.ui.main.ui.ecmei.config.payments.PaymentsConfigActivity;
import com.example.managing_mei.view.ui.main.ui.seals.SealsFragment;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import static com.example.managing_mei.utils.Contants.listOfParcelOptions;
import static com.example.managing_mei.utils.FireBaseConfig.firebaseDbReference;
import static com.example.managing_mei.utils.FireBaseConfig.firebaseInstance;
import static com.example.managing_mei.utils.FireBaseConfig.getIdUser;
import static com.example.managing_mei.utils.FormatDataUtils.cleanFormat;
import static com.example.managing_mei.utils.FormatDataUtils.formatDateToStringFormated;
import static com.example.managing_mei.utils.FormatDataUtils.formatMonetaryValue;
import static com.example.managing_mei.utils.FormatDataUtils.formatMonetaryValueDouble;
import static com.example.managing_mei.view.ui.main.ui.seals.addSell.AddSellActivity.clientSelected;
import static com.example.managing_mei.view.ui.main.ui.seals.addSell.AddSellActivity.economicOperationForSaleVoArrayList;

public class CalcSellValueActivity extends AppCompatActivity implements AdapterProductForSaleVo.OnEconomicOperationForSaleVo{

    private Spinner spinnerPaymentstype;
    private RecyclerView recyclerView;

    private Set<ProductForSaleVo> set = new HashSet<>();
    private Set<PaymentType> paymentTypeList = new HashSet<>();
    private Sale sale;
    private Double valuedivided;
    private Switch switchIsDivided;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calc_sell_value);

        recyclerView = findViewById(R.id.RecyclerViewEconomicOperationClosingSale);
        spinnerPaymentstype = findViewById(R.id.listOfPaymentsTypeClosingSale);
        switchIsDivided = findViewById(R.id.switchIsDivided);
        TextView clientNameTextView = findViewById(R.id.textViewClientNameClosingSale);
        Button conclusionButton = findViewById(R.id.imageButtonConclusionSaleAddButton);


        setActionForCancelButton();

        sale = new Sale(firebaseDbReference.push().getKey(), formatDateToStringFormated(System.currentTimeMillis()),clientSelected);
        set.addAll(economicOperationForSaleVoArrayList);
        clientNameTextView.setText(sale.getClient().getNome().toUpperCase());

        addListToSale();
        loadList();
        setFinalValue();
        setActionForImageButtonSealConfig();
        setListPaymentsTypes();

        conclusionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    discountDialog();
            }
        });

        spinnerPaymentstype.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                sale.setPaymentType(spinnerPaymentstype.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                sale.setPaymentType(spinnerPaymentstype.getSelectedItem().toString());
            }
        });
    }

    @SuppressLint("SetTextI18n")
    private void createParcelDialog() {
        AlertDialog alertDialog;
        View mDialogView = LayoutInflater.from(this).inflate(R.layout.dialog_divided_buy,null);

        TextView textViewValueDividedTextView = mDialogView.findViewById(R.id.textViewValueDivided);
        TextView textViewTotalValueOfDivided = mDialogView.findViewById(R.id.textViewTotalValueOfDivided);
        Button imageButtonCancelParcelSelect = mDialogView.findViewById(R.id.imageButtonCancelParcelSelect);
        Button imageButtonAcceptParcelSelect = mDialogView.findViewById(R.id.imageButtonAcceptParcelSelect);
        Spinner spinnerParcelValue = mDialogView.findViewById(R.id.spinnerParcelValue);

        textViewTotalValueOfDivided.setText(formatMonetaryValue(sale.getTotalValueFromProductsAndDiscount()));

        imageButtonAcceptParcelSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sale.setDividedSale(true);
                sale.setDividedQuantity(Integer.parseInt(cleanFormat(spinnerParcelValue.getSelectedItem().toString())));
                sale.setParcelValue(valuedivided);
                closeSeal();
            }
        });

        imageButtonCancelParcelSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });


        spinnerParcelValue.setAdapter(new ArrayAdapter(getApplicationContext(), R.layout.item_spinner,listOfParcelOptions));
        spinnerParcelValue.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                valuedivided = sale.getTotalValueFromProductsAndDiscount()/Double.parseDouble(cleanFormat(spinnerParcelValue.getSelectedItem().toString()));
                textViewValueDividedTextView.setText(spinnerParcelValue.getSelectedItem().toString()+formatMonetaryValue(valuedivided));
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        AlertDialog.Builder builder = new AlertDialog.Builder(this).setView(mDialogView);
        alertDialog=builder.create();
        alertDialog.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        paymentTypeList.clear();
    }

    @Override
    protected void onPause() {
        super.onPause();
        paymentTypeList.clear();
    }

    private void setFinalValue(){
        TextView finalText = findViewById(R.id.textViewFinalValueClosingSale);
        finalText.setText(formatMonetaryValue(sale.getTotalValueFromProductsAndDiscount()));
    }

    public void loadList(){
        AdapterProductForSaleVo adapterEconomicOperationForSaleVo = new AdapterProductForSaleVo(set,getApplicationContext(), this::onEconomicOperationForSaleVoClick,true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapterEconomicOperationForSaleVo);
    }

    private void setListPaymentsTypes(){
        paymentTypeList.clear();
        firebaseInstance.getReference()
                .child(getIdUser())
                .child("PaymentsType")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                        for (DataSnapshot ds:snapshot.getChildren()){
                            PaymentType provider = ds.getValue(PaymentType.class);
                            paymentTypeList.add(provider);
                        }
                        if (paymentTypeList.stream().filter(paymentType -> paymentType.getStatus().equals(true)).map(PaymentType::getNome).collect(Collectors.toList()).size()==0){
                            paymentTypeList.add(new PaymentType(firebaseDbReference.push().getKey(),"Não Especificado",true));
                        }
                        ArrayAdapter arrayAdapter = new ArrayAdapter(getApplicationContext(),
                                                                     R.layout.item_spinner,
                                                                     paymentTypeList.stream().filter(paymentType -> paymentType.getStatus().equals(true)).map(PaymentType::getNome).collect(Collectors.toList()));
                        spinnerPaymentstype.setAdapter(arrayAdapter);

                    }

                    @Override
                    public void onCancelled(@NonNull @NotNull DatabaseError error) {

                    }
                });
            }

    public void discountDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setMessage("Deseja aplicar um desconto ?");

        alertDialog.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                applyDiscount();
            }
        });

        alertDialog.setNegativeButton("Não", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (switchIsDivided.isChecked()){
                    createParcelDialog();
                } else {
                    closeSeal();
                }
            }
        });

        alertDialog.create();
        alertDialog.show();
    }

    private void closeSeal(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        if (switchIsDivided.isChecked()){
            alertDialog.setMessage("Deseja concluir a venda?\n Total: " + sale.getDividedQuantity().toString() + "x " + formatMonetaryValue(sale.getTotalValueFromProductsAndDiscount()/sale.getDividedQuantity()));
        } else {
            alertDialog.setMessage("Deseja concluir a venda?\n Total:  "+ formatMonetaryValue(sale.getTotalValueFromProductsAndDiscount()));
        }

        alertDialog.setPositiveButton("sim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                saveSale();
                finish();
            }
        });

        alertDialog.setNegativeButton("não", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        alertDialog.create();
        alertDialog.show();
    }

    private void addListToSale(){
        List<ProductForSaleVo> economicOperationForSaleVoList= new ArrayList<>();
        for(Iterator<ProductForSaleVo> it = set.iterator(); it.hasNext();){
            ProductForSaleVo e= it.next();
            economicOperationForSaleVoList.add(e);
        }
        sale.setEconomicOperationForSaleVoList(economicOperationForSaleVoList);
    }

    private void saveSale() {
        addListToSale();
        List<CashFlowItem> cashFlowItems = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        LocalDate currentdate = LocalDate.now();

        if (switchIsDivided.isChecked()){
            double parcelValue = formatMonetaryValueDouble(sale.getTotalValueFromProductsAndDiscount()/sale.getDividedQuantity());

            for (int i=0; i <= (sale.getDividedQuantity()-1); i++){
                calendar.set(currentdate.getYear(),currentdate.getMonth().getValue()+i,currentdate.getDayOfMonth());
                cashFlowItems.add(new CashFlowItem(1,parcelValue,calendar.getTime(),currentdate.getYear(),currentdate.getMonth().getValue()+i,currentdate.getDayOfMonth(),"Compra Parcelada em "+sale.getDividedQuantity().toString()+"x"+formatMonetaryValue(sale.getTotalValueFromProductsAndDiscount()/sale.getDividedQuantity())));
            }
            cashFlowItems.forEach(CashFlowItem::save);
        } else {
            CashFlowItem cashFlowItem = new CashFlowItem(1,sale.getTotalValueFromProductsAndDiscount(),"Compra de produtos");
            cashFlowItem.save();
        }


        sale.save();
        reduceFromBuyProducts();
        Toast.makeText(CalcSellValueActivity.this, "Adicionado", Toast.LENGTH_SHORT).show();
        //startActivity(new Intent(getApplicationContext(), SealsFragment.class));
        this.finish();
    }


    private void reduceFromBuyProducts() {
        sale.getEconomicOperationForSaleVoList().forEach(productForSaleVo -> {
            productForSaleVo.getProduct().save();
        });
    }

    @Override
    public void onEconomicOperationForSaleVoClick(int position) {
        AlertDialog alertDialog;
        ProductForSaleVo economicOperationForSaleVo = set.iterator().next();
        View mDialogView = LayoutInflater.from(this).inflate(R.layout.dialog_select_item,null);

        Button buttonDeleteEconomicOperation=mDialogView.findViewById(R.id.imageButtonCancelAddQuantity);
        Button buttonAddQuantity=mDialogView.findViewById(R.id.imageButtonConclusionAddQuantity);
        SeekBar seekBar=mDialogView.findViewById(R.id.seekBarQuantityForAddInSale);
        TextView counter = mDialogView.findViewById(R.id.textViewCounterAddQuantity);

        seekBar.setProgress(Integer.parseInt(String.valueOf(economicOperationForSaleVo.getQuantitySelect())));
        seekBar.setMin(1);
        seekBar.setMax(economicOperationForSaleVo.getProduct().getQuantity());
        counter.setText(String.valueOf(economicOperationForSaleVo.getQuantitySelect()));

        AlertDialog.Builder builder = new AlertDialog.Builder(this).setView(mDialogView);
        alertDialog=builder.create();

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                counter.setText(String.valueOf(progress));
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }});

        buttonDeleteEconomicOperation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });

        buttonAddQuantity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Integer.parseInt(counter.getText().toString())!=0){
                    economicOperationForSaleVo.setQuantitySelect(Integer.valueOf(counter.getText().toString()));
                    loadList();
                    setFinalValue();
                    alertDialog.dismiss();
                }else{
                    Toast.makeText(CalcSellValueActivity.this, "Selecione uma quantidade", Toast.LENGTH_SHORT).show();
                }
            }
        });
        buttonDeleteEconomicOperation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                set.remove(position);
                if (set.isEmpty()){
                    startActivity(new Intent(getApplicationContext(), ManagementActivity.class));
                    try { this.finalize(); } catch (Throwable throwable) { throwable.printStackTrace(); }
                }
                loadList();
                setFinalValue();
            }});
        alertDialog.show();

    }

    private void applyDiscount(){
        AlertDialog alertDialog;
        View mDialogView = LayoutInflater.from(this).inflate(R.layout.dialog_discount,null);

        Button butttonCancel = mDialogView.findViewById(R.id.buttonDeleteDiscountClosingSale);
        Button buttonAddDiscount = mDialogView.findViewById(R.id.buttonAddDiscountClosingSale);
        TextInputLayout editText = mDialogView.findViewById(R.id.editTextDiscountClosingSale);

        AlertDialog.Builder builder = new AlertDialog.Builder(this).setView(mDialogView);
        alertDialog=builder.create();


        butttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });

        buttonAddDiscount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String discout=editText.getEditText().getText().toString();
                if (discout.equals(0)){
                    Toast.makeText(CalcSellValueActivity.this, "Adicione um valor", Toast.LENGTH_SHORT).show();

                }if (discout.equals(sale.getTotalValueFromProducts())) {
                    Toast.makeText(CalcSellValueActivity.this, "O desconto esta iqual ao total", Toast.LENGTH_SHORT).show();

                }else{
                    sale.setTotalDiscountFromSeal(Double.parseDouble(editText.getEditText().getText().toString()));
                    if (switchIsDivided.isChecked()) {
                        alertDialog.dismiss();
                        createParcelDialog();
                    } else {
                        closeSeal();
                    }
                }
                alertDialog.dismiss();
            }});
        alertDialog.show();
    }

    private void setActionForCancelButton() {
        Button cancelButton = findViewById(R.id.imageButtonConclusionSaleCancelButton);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
    private void setActionForImageButtonSealConfig() {
        ImageButton imageButtonSealConfig = findViewById(R.id.imageButtonSealConfig);
        imageButtonSealConfig.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), PaymentsConfigActivity.class));
            }
        });
    }

}