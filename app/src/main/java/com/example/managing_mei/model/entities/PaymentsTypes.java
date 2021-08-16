package com.example.managing_mei.model.entities;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static com.example.managing_mei.utils.FireBaseConfig.firebaseDbReference;
import static com.example.managing_mei.utils.FireBaseConfig.firebaseInstance;
import static com.example.managing_mei.utils.FireBaseConfig.getIdUser;

public class PaymentsTypes {

    private String id;
    private List<PaymentType> paymentTypeList = new ArrayList<>();
    protected String mensage;

    public PaymentsTypes(Set<PaymentType> paymentTypeSet){
        this.id = firebaseDbReference.push().getKey();
        parseSetToList(paymentTypeSet);
    }


    public PaymentsTypes(String id, List<PaymentType> paymentTypeList) {
        this.id = id;
        this.paymentTypeList = paymentTypeList;
    }

    public PaymentsTypes() {
    }

    public String save(){
        firebaseInstance.getReference()
                .child(getIdUser())
                .child("PaymentsTypes")
                .setValue(this)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        mensage= "Tipos de pagamentos foram salvos";
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull @NotNull Exception e) {
                mensage= e.getMessage();
            }
        });
        return mensage;
    }
    private void parseSetToList(Set<PaymentType> list){
        this.paymentTypeList.addAll(list);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<PaymentType> getPaymentTypeList() {
        return paymentTypeList;
    }

    public void setPaymentTypeList(List<PaymentType> paymentTypeList) {
        this.paymentTypeList = paymentTypeList;
    }
}
