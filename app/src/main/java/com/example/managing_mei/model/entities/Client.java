package com.example.managing_mei.model.entities;

import androidx.annotation.NonNull;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;

import org.jetbrains.annotations.NotNull;

import java.util.Date;
import static com.example.managing_mei.utils.FireBaseConfig.firebaseInstance;
import static com.example.managing_mei.utils.FireBaseConfig.getIdUser;

public class Client {

    private String id;
    private String nome;
    private String email;
    private String telefone;
    private Date date;
    protected String mensagem;

    public Client(String id, String nome, String email, String telefone, Date date) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.telefone = telefone;
        this.date = date;
    }

    public Client(){}

    public String delete(){
        firebaseInstance.getReference()
                        .child(getIdUser())
                        .child("clients")
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
                        .child("clients")
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

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
