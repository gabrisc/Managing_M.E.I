package com.example.managing_mei.model.entities;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;

import org.jetbrains.annotations.NotNull;

import java.util.Date;

import static com.example.managing_mei.utils.FireBaseConfig.firebaseInstance;
import static com.example.managing_mei.utils.FireBaseConfig.getIdUser;

public class Product {

    private String id;
    private String name;
    private Double sealValue;
    private Double expenseValue;
    private String type;
    private Integer quantity;
    private Date date;
    private Double contributionValue;
    private String typeQuantity;
    private Provider provider;
    protected String mensage;

    public Product(String id, String name, Double sealValue, Double expenseValue, String type, Integer quantity, Date date, Double contributionValue, String typeQuantity, Provider provider) {
        this.id = id;
        this.name = name;
        this.sealValue = sealValue;
        this.expenseValue = expenseValue;
        this.type = type;
        this.quantity = quantity;
        this.date = date;
        this.contributionValue = contributionValue;
        this.typeQuantity = typeQuantity;
        this.provider = provider;
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

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Double getContributionValue() {
        return contributionValue;
    }

    public void setContributionValue(Double contributionValue) {
        this.contributionValue = contributionValue;
    }

    public String getTypeQuantity() {
        return typeQuantity;
    }

    public void setTypeQuantity(String typeQuantity) {
        this.typeQuantity = typeQuantity;
    }

    public Provider getProvider() {
        return provider;
    }

    public void setProvider(Provider provider) {
        this.provider = provider;
    }
}
