package com.example.managing_mei.model.entities;

public class ShoppingCartForSale {
    private Product product;
    private Integer quantitySelected;
    private Double ExpenseTotalForThisItem;
    private Double GainTotalForThisItem;
    private Double SealTotalForThisItem;

    public ShoppingCartForSale(Product product, Integer quantitySelected) {
        this.product = product;
        this.quantitySelected = quantitySelected;
    }

    public ShoppingCartForSale() {
    }


    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Integer getQuantitySelected() {
        return quantitySelected;
    }

    public void setQuantitySelected(Integer quantitySelected) {
        this.quantitySelected = quantitySelected;
    }

    public Double getExpenseTotalForThisItem() {
        return product.getExpenseValue();
    }

    public Double getGainTotalForThisItem() {
        return product.getSealValue()-product.getExpenseValue();
    }

    public Double getSealTotalForThisItem() {
        return product.getSealValue();
    }
}