package com.example.managing_mei.model.entities;

public class ProductForSaleVo {
    private Product product;
    private int quantitySelect;

    public ProductForSaleVo(Product product, int quantitySelect) {
        this.product = product;
        this.quantitySelect = quantitySelect;
    }

    public ProductForSaleVo() {
    }


    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public int getQuantitySelect() {
        return quantitySelect;
    }

    public void setQuantitySelect(int quantitySelect) {
        this.quantitySelect = quantitySelect;
    }
}
