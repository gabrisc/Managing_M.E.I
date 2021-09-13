package com.example.managing_mei.model.entities;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import org.jetbrains.annotations.NotNull;
import static com.example.managing_mei.utils.FireBaseConfig.firebaseDbReference;
import static com.example.managing_mei.utils.FireBaseConfig.firebaseInstance;
import static com.example.managing_mei.utils.FireBaseConfig.getIdUser;

public class InvestimentItem {

    private String id;
    private String nameFromInvestiment;
    private String isSettled; //quitado
    private String typeOfInvestiment;


    private Double valueFromInvestiment;


    public InvestimentItem( String nameFromInvestiment, String isSettled, String typeOfInvestiment, Double valueFromInvestiment) {
        this.id = firebaseDbReference.push().getKey();
        this.nameFromInvestiment = nameFromInvestiment;
        this.isSettled = isSettled;
        this.typeOfInvestiment = typeOfInvestiment;
        this.valueFromInvestiment = valueFromInvestiment;
    }

    public void save(){
        firebaseInstance.getReference()
                .child(getIdUser())
                .child("InvestimentItem")
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
        //this.setId(firebaseDbReference.push().getKey());
        firebaseInstance.getReference()
                .child(getIdUser())
                .child("InvestimentItem")
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


    public InvestimentItem() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNameFromInvestiment() {
        return nameFromInvestiment;
    }

    public void setNameFromInvestiment(String nameFromInvestiment) {
        this.nameFromInvestiment = nameFromInvestiment;
    }

    public Double getValueFromInvestiment() {
        return valueFromInvestiment;
    }

    public void setValueFromInvestiment(Double valueFromInvestiment) {
        this.valueFromInvestiment = valueFromInvestiment;
    }

    public String getTypeOfInvestiment() {
        return typeOfInvestiment;
    }

    public void setTypeOfInvestiment(String typeOfInvestiment) {
        this.typeOfInvestiment = typeOfInvestiment;
    }

    public String getIsSettled() {
        return isSettled;
    }

    public void setIsSettled(String isSettled) {
        this.isSettled = isSettled;
    }





}
