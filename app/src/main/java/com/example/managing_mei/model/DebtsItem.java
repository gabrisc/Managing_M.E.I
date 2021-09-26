package com.example.managing_mei.model;

public class DebtsItem {
    private String id;
    private String nameDebts;
    private Double debtsValue;
    private Boolean itsPaid;
    private Integer frequency;

    public DebtsItem(String id, String nameDebts, Double debtsValue, Boolean itsPaid, Integer frequency) {
        this.id = id;
        this.nameDebts = nameDebts;
        this.debtsValue = debtsValue;
        this.itsPaid = itsPaid;
        this.frequency = frequency;
    }

    public DebtsItem() {
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

    public Boolean getItsPaid() {
        return itsPaid;
    }

    public void setItsPaid(Boolean itsPaid) {
        this.itsPaid = itsPaid;
    }

    public Integer getFrequency() {
        return frequency;
    }

    public void setFrequency(Integer frequency) {
        this.frequency = frequency;
    }
}
