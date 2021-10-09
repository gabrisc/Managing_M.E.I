package com.example.managing_mei.model.entities;

import androidx.annotation.NonNull;

import com.example.managing_mei.model.enuns.FrequencyDebts;
import com.example.managing_mei.model.enuns.OccurrenceDebts;
import com.example.managing_mei.model.enuns.TypeOfDebts;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;

import org.jetbrains.annotations.NotNull;

import static com.example.managing_mei.utils.FireBaseConfig.firebaseInstance;
import static com.example.managing_mei.utils.FireBaseConfig.getIdUser;

public class DebtsItem {

    private String id;
    private String nameDebts;
    private Double debtsValue;
    private TypeOfDebts typeOfDebts;
    private FrequencyDebts frequencyDebts;
    private Boolean itsDebtsParcels;
    private Integer numberOfParcels;
    private Boolean itsPaid;

    public DebtsItem() {
    }

    public DebtsItem(String id, String nameDebts, Double debtsValue, TypeOfDebts typeOfDebts, FrequencyDebts frequencyDebts, Boolean itsDebtsParcels, Integer numberOfParcels, Boolean itsPaid) {
        this.id = id;
        this.nameDebts = nameDebts;
        this.debtsValue = debtsValue;
        this.typeOfDebts = typeOfDebts;
        this.frequencyDebts = frequencyDebts;
        this.itsDebtsParcels = itsDebtsParcels;
        this.numberOfParcels = numberOfParcels;
        this.itsPaid = itsPaid;
    }

    public DebtsItem(String id, String nameDebts, Double debtsValue, TypeOfDebts typeOfDebts, Boolean itsPaid) {
        this.id = id;
        this.nameDebts = nameDebts;
        this.debtsValue = debtsValue;
        this.typeOfDebts = typeOfDebts;
        this.itsPaid = itsPaid;
    }

    public DebtsItem(String id, String nameDebts, Double debtsValue, TypeOfDebts typeOfDebts, Boolean itsDebtsParcels, Integer numberOfParcels, Boolean itsPaid) {
        this.id = id;
        this.nameDebts = nameDebts;
        this.debtsValue = debtsValue;
        this.typeOfDebts = typeOfDebts;
        this.itsDebtsParcels = itsDebtsParcels;
        this.numberOfParcels = numberOfParcels;
        this.itsPaid = itsPaid;
    }

    public DebtsItem(String id, String nameDebts, Double debtsValue, TypeOfDebts typeOfDebts, FrequencyDebts frequencyDebts, Boolean itsPaid) {
        this.id = id;
        this.nameDebts = nameDebts;
        this.debtsValue = debtsValue;
        this.typeOfDebts = typeOfDebts;
        this.frequencyDebts = frequencyDebts;
        this.itsPaid = itsPaid;
    }

    public void save(){
        firebaseInstance.getReference()
                .child(getIdUser())
                .child("debts")
                .child(String.valueOf(this.getId()))
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNameDebts() {
        return nameDebts;
    }

    public void setNameDebts(String nameDebts) {
        this.nameDebts = nameDebts;
    }

    public Double getDebtsValue() {
        return debtsValue;
    }

    public void setDebtsValue(Double debtsValue) {
        this.debtsValue = debtsValue;
    }

    public TypeOfDebts getTypeOfDebts() {
        return typeOfDebts;
    }

    public void setTypeOfDebts(TypeOfDebts typeOfDebts) {
        this.typeOfDebts = typeOfDebts;
    }



    public FrequencyDebts getFrequencyDebts() {
        return frequencyDebts;
    }

    public void setFrequencyDebts(FrequencyDebts frequencyDebts) {
        this.frequencyDebts = frequencyDebts;
    }

    public Boolean getItsDebtsParcels() {
        return itsDebtsParcels;
    }

    public void setItsDebtsParcels(Boolean itsDebtsParcels) {
        this.itsDebtsParcels = itsDebtsParcels;
    }

    public Integer getNumberOfParcels() {
        return numberOfParcels;
    }

    public void setNumberOfParcels(Integer numberOfParcels) {
        this.numberOfParcels = numberOfParcels;
    }

    public Boolean getItsPaid() {
        return itsPaid;
    }

    public void setItsPaid(Boolean itsPaid) {
        this.itsPaid = itsPaid;
    }
}
