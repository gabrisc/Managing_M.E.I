package com.example.managing_mei.model.entities;

public class PaymentType {

    private String nome;
    private Boolean status;
    private String mensage;

    public PaymentType(String nome, Boolean status) {
        this.nome = nome;
        this.status = status;
    }

    public PaymentType() {}


    public String getNome() {
        return nome;
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


}
