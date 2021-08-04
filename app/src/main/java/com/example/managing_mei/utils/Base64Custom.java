package com.example.managing_mei.utils;

import android.util.Base64;

public class Base64Custom {

    public static String Code64(String uncodetext){
        return Base64.encodeToString(uncodetext.getBytes(),Base64.DEFAULT ).replaceAll("(\\n|\\r)","");
    }

}
