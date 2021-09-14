package com.example.managing_mei.utils;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FireBaseConfig {

    public static FirebaseDatabase firebaseInstance = FirebaseDatabase.getInstance();
    public static DatabaseReference firebaseDbReference = firebaseInstance.getReference();
    public static FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private static FirebaseAuth autenticacao;

    public static String getIdUser() {
        return Base64Custom.Code64(firebaseAuth.getCurrentUser().getEmail());
    }


    //Metodo de retorno
    public static FirebaseAuth getFireBaseAutenticacao(){

        if (autenticacao==null){
            autenticacao=FirebaseAuth.getInstance();
        }
        return autenticacao;
    }
}
