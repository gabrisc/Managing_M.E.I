package com.example.managing_mei.model.entities;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;

import org.jetbrains.annotations.NotNull;

import static com.example.managing_mei.utils.FireBaseConfig.firebaseInstance;
import static com.example.managing_mei.utils.FireBaseConfig.getIdUser;
import static com.example.managing_mei.utils.FormatDataUtils.formatTextToUpperOrLowerCase;

public class PaymentType {

    private String id;
    private String nome;
    private Boolean status;
    private String mensage;

    public PaymentType(String id, String nome, Boolean status) {
        this.id = id;
        this.nome = nome;
        this.status = status;
    }

    public PaymentType() {}

    public void delete(){
        firebaseInstance.getReference()
                .child(getIdUser())
                .child("PaymentsType")
                .child(String.valueOf(this.getId()))
                .removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<Void> task) {}})
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull @NotNull Exception e) {}});

    }

    public void save() {
        firebaseInstance.getReference()
                .child(getIdUser())
                .child("PaymentsType")
                .child(String.valueOf(this.getId()))
                .setValue(this)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {}})
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull @NotNull Exception e) {}});
    }

    public String getNome() {
        return formatTextToUpperOrLowerCase(nome, true);
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public String getId() { return id; }

    public void setId(String id) { this.id = id; }
}
