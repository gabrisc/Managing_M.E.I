package com.example.managing_mei.model.entities;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;

import org.jetbrains.annotations.NotNull;

import static com.example.managing_mei.utils.FireBaseConfig.firebaseInstance;
import static com.example.managing_mei.utils.FireBaseConfig.getIdUser;

public class Product {

    private String id;
    private String name;
    private Double sealValue;
    private Double expenseValue;
    private String type;
    private Integer quantity;
    private String typeQuantity;
    private String providerId;
    protected String mensage;


    public Product(Double expenseValue,String id, String name, String provider, Integer quantity, Double sealValue, String type, String typeQuantity) {
        this.id = id;
        this.name = name;
        this.sealValue = sealValue;
        this.expenseValue = expenseValue;
        this.type = type;
        this.quantity = quantity;
        this.typeQuantity = typeQuantity;
        this.providerId = provider;
    }

    public Product() {
    }


    public String save(){
        firebaseInstance.getReference()
                .child(getIdUser())
                .child("product")
                .child(String.valueOf(this.getId()))
                .setValue(this)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        mensage= "Adicionado com sucesso";
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull @NotNull Exception e) {
                        mensage= e.getMessage();
                    }
                });
        return mensage;
    }

    public String delete(){
        firebaseInstance.getReference()
                .child(getIdUser())
                .child("product")
                .child(String.valueOf(this.getId()))
                .removeValue()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<Void> task) {
                        mensage= "Apagado com sucesso";
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull @NotNull Exception e) {
                        mensage= e.getMessage();
                    }
                });
        return mensage;
    }




    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getSealValue() {
        return sealValue;
    }

    public void setSealValue(Double sealValue) {
        this.sealValue = sealValue;
    }

    public Double getExpenseValue() {
        return expenseValue;
    }

    public void setExpenseValue(Double expenseValue) {
        this.expenseValue = expenseValue;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMensage() {
        return mensage;
    }

    public void setMensage(String mensage) {
        this.mensage = mensage;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }


    public String getTypeQuantity() {
        return typeQuantity;
    }

    public void setTypeQuantity(String typeQuantity) {
        this.typeQuantity = typeQuantity;
    }

    public String getProviderId() {
        return providerId;
    }

    public void setProviderId(String providerId) {
        this.providerId = providerId;
    }
}
