package com.example.managing_mei.model.entities;

public class QuantityType {

    private String nome;
    private Boolean status;

    public QuantityType(String nome, Boolean status) {
        this.nome = nome;
        this.status = status;
    }

    public QuantityType() {
    }

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
