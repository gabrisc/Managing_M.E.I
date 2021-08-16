package com.example.managing_mei.model.entities;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.annotations.NotNull;

import java.util.List;

import static com.example.managing_mei.utils.FireBaseConfig.firebaseInstance;
import static com.example.managing_mei.utils.FireBaseConfig.getIdUser;

public class ExpenseFromProducts {

    private String productIdAssociate;
    private String expenseId;
    private Double ExpenseValue;
    private List<Double> expensesValues;
    protected String mensage;

    public ExpenseFromProducts(String productIdAssociate, String expenseId, Double expenseValue) {
        this.productIdAssociate = productIdAssociate;
        this.expenseId = expenseId;
        ExpenseValue = expenseValue;
    }

    public ExpenseFromProducts() {
    }

    public String save(){
        firebaseInstance.getReference()
                .child(getIdUser())
                .child("ExpenseFromProduct")
                .setValue(this)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull @NotNull Exception e) {
                mensage= e.getMessage();
            }
        });
        return mensage;
    }

    public String getProductIdAssociate() {
        return productIdAssociate;
    }

    public void setProductIdAssociate(String productIdAssociate) {
        this.productIdAssociate = productIdAssociate;
    }

    public String getExpenseId() {
        return expenseId;
    }

    public void setExpenseId(String expenseId) {
        this.expenseId = expenseId;
    }

    public Double getExpenseValue() {
        return ExpenseValue;
    }

    public void setExpenseValue(Double expenseValue) {
        ExpenseValue = expenseValue;
    }

    public List<Double> getExpensesValues() {
        return expensesValues;
    }

    public void setExpensesValues(List<Double> expensesValues) {
        this.expensesValues = expensesValues;
    }
}
