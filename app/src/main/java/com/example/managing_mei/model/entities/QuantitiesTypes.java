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

public class QuantitiesTypes {
    private String id;
    private List<QuantityType> quantityTypeArrayList = new ArrayList<>();
    protected String mensage;


    public QuantitiesTypes(String id, List<QuantityType> quantityTypeArrayList) {
        this.id = id;
        this.quantityTypeArrayList = quantityTypeArrayList;
    }

    public QuantitiesTypes( Set<QuantityType> quantityTypeArrayList) {
        this.id = firebaseDbReference.push().getKey();
        parseSetToList(quantityTypeArrayList);
    }

    private void parseSetToList(Set<QuantityType> quantityTypeArrayList) {
        this.quantityTypeArrayList.addAll(quantityTypeArrayList);
    }

    public QuantitiesTypes() {
    }

    public String save(){
        firebaseInstance.getReference()
                .child(getIdUser())
                .child("QuantitiesTypes")
                .setValue(this)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        mensage= "Unidade de medidas foram salvas";
                    }
                }).addOnFailureListener(new OnFailureListener() {
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

    public List<QuantityType> getQuantityTypeArrayList() {
        return quantityTypeArrayList;
    }

    public void setQuantityTypeArrayList(List<QuantityType> quantityTypeArrayList) {
        this.quantityTypeArrayList = quantityTypeArrayList;
    }

}
