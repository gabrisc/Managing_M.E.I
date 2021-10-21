package com.example.managing_mei.model.entities;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;

import org.jetbrains.annotations.NotNull;

import java.util.Date;

import static com.example.managing_mei.utils.FireBaseConfig.firebaseDbReference;
import static com.example.managing_mei.utils.FireBaseConfig.firebaseInstance;
import static com.example.managing_mei.utils.FireBaseConfig.getIdUser;

public class CheckListItem {

    private String id;
    private String name;
    private Boolean status;
    private Date date;

    public CheckListItem(String id, String name, Boolean status, Date date) {
        this.id = id;
        this.name = name;
        this.status = status;
        this.date = date;
    }

    public CheckListItem(String name, Boolean status, Date date) {
        this.id = firebaseDbReference.push().getKey();
        this.name = name;
        this.status = status;
        this.date = date;
    }

    public CheckListItem() {}

    public void save(){
        firebaseInstance.getReference()
                        .child(getIdUser())
                        .child("CheckListItem")
                        .child(this.getId())
                        .setValue(this)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull @NotNull Exception e) {

                            }
                        });
    }

    public void delete(){
        firebaseInstance.getReference()
                        .child(getIdUser())
                        .child("CheckListItem")
                        .child(this.getId())
                        .removeValue()
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull @NotNull Task<Void> task) {
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull @NotNull Exception e) {
                            }
                        });
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
