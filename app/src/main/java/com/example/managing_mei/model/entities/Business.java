package com.example.managing_mei.model.entities;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;

import org.jetbrains.annotations.NotNull;

import static com.example.managing_mei.utils.FireBaseConfig.firebaseInstance;
import static com.example.managing_mei.utils.FireBaseConfig.getIdUser;

public class Business {
    private String id;
    private String email;
    private String password;
    private String fantasyName;
    private String CNPJ;
    private String adress;
    private String phoneNumber;
    private String businessBranch;
    protected String mensagem;
    private String personName;
    private String uid;

    public Business(String id, String email, String password, String fantasyName, String CNPJ, String adress, String phoneNumber, String businessBranch, String personName) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.fantasyName = fantasyName;
        this.CNPJ = CNPJ;
        this.adress = adress;
        this.phoneNumber = phoneNumber;
        this.businessBranch = businessBranch;
        this.personName = personName;
    }

    public Business(){}

    public String delete(){
        firebaseInstance.getReference()
                .child(getIdUser())
                .child("MEI")
                .child(String.valueOf(this.getId()))
                .removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<Void> task) {
                mensagem = "Apagado com sucesso";
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull @NotNull Exception e) {
                        mensagem = e.getMessage();
                    }
                });
        return mensagem;
    }

    public String save() {
        firebaseInstance.getReference()
                .child(getIdUser())
                .child("MEI")
                .child(String.valueOf(this.getId()))
                .setValue(this)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        mensagem = "Cadastrado com sucesso";
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull @NotNull Exception e) {
                        mensagem = e.getMessage();
                    }
                });
        return mensagem;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public String getAdress() {
        return adress;
    }

    public void setAdress(String adress) {
        this.adress = adress;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getBusinessBranch() {
        return businessBranch;
    }

    public void setBusinessBranch(String businessBranch) {
        this.businessBranch = businessBranch;
    }

    public String getPersonName() {
        return personName;
    }

    public void setPersonName(String personName) {
        this.personName = personName;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

}
