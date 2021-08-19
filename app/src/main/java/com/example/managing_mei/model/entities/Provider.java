package com.example.managing_mei.model.entities;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;

import org.jetbrains.annotations.NotNull;

import java.util.Date;

import static com.example.managing_mei.utils.FireBaseConfig.firebaseInstance;
import static com.example.managing_mei.utils.FireBaseConfig.getIdUser;

public class Provider {

    private String id;
    private String fantasyName;
    private String CNPJ;
    private String phoneNumber;
    private String email;
    private String address;
    private Float evaluation;
    protected String mensage;


    public Provider(String id, String fantasyName, String CNPJ, String phoneNumber, String email, String address, Float evaluation) {
        this.id = id;
        this.fantasyName = fantasyName;
        this.CNPJ = CNPJ;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.address = address;
        this.evaluation = evaluation;
    }

    public Provider() {
    }

    public String save(){
        firebaseInstance.getReference()
                .child(getIdUser())
                .child("providers")
                .child(String.valueOf(this.getId()))
                .setValue(this)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        mensage= "Fornecedor Adicionado";
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
                .child("providers")
                .child(String.valueOf(this.getId()))
                .removeValue();
        return "Fornecedor Removido";
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFantasyName() {
        return fantasyName;
    }

    public void setFantasyName(String fantasyName) {
        this.fantasyName = fantasyName;
    }

    public String getCNPJ() {
        return CNPJ;
    }

    public void setCNPJ(String CNPJ) {
        this.CNPJ = CNPJ;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Float getEvaluation() {
        return evaluation;
    }

    public void setEvaluation(Float evaluation) {
        this.evaluation = evaluation;
    }

}
