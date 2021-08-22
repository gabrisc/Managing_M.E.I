package com.example.managing_mei.model.entities;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;

import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;
import java.util.Date;

import static com.example.managing_mei.utils.FireBaseConfig.firebaseDbReference;
import static com.example.managing_mei.utils.FireBaseConfig.firebaseInstance;
import static com.example.managing_mei.utils.FireBaseConfig.getIdUser;

public class CashFlowItem {
    private String id ;
    private Integer type; // 0 : saida de dinheiro , 1 : entrada de dinheiro
    private Double value;
    private Date date ;
    private Integer year;
    private Integer month ;
    private Integer dayOfMonth;
    private String description;

    public CashFlowItem(Integer type, Double value, String description) {
        LocalDate currentdate = LocalDate.now();
        this.id = firebaseDbReference.push().getKey();
        this.type = type;
        this.value = value;
        this.date = new Date();
        this.year = currentdate.getYear();
        this.month = currentdate.getMonthValue();
        this.dayOfMonth = currentdate.getDayOfMonth();;
        this.description = description;
    }

    public CashFlowItem() {}

    public void save(){
        firebaseInstance.getReference()
                .child(getIdUser())
                .child("CashFlowItens")
                .child(this.getId())
                .setValue(this)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull @NotNull Exception e) {

            }
        });

    }

    public void delete(){
        this.setId(firebaseDbReference.push().getKey());
        firebaseInstance.getReference()
                        .child(getIdUser())
                        .child("CashFlowItens")
                        .child(String.valueOf(this.getYear()))
                        .child(String.valueOf(this.getMonth()))
                        .child(String.valueOf(this.getDayOfMonth()))
                        .child(this.getId())
                        .removeValue()
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull @NotNull Task<Void> task) {
                                }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull @NotNull Exception e) {
                                }
                        });
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Integer getMonth() {
        return month;
    }

    public void setMonth(Integer month) {
        this.month = month;
    }

    public Integer getDayOfMonth() {
        return dayOfMonth;
    }

    public void setDayOfMonth(Integer dayOfMonth) {
        this.dayOfMonth = dayOfMonth;
    }
}
