package com.example.managing_mei.utils;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FormatDataUtils {

    private static DecimalFormat formatter = new DecimalFormat("#0.00");

    public static String formatPhoneNumber(final String phoneNumber) {
        String phoneNumberFormater = cleanFormat(phoneNumber);

        if (!(phoneNumberFormater.length() == 0) && phoneNumberFormater.length()>=11){
            phoneNumberFormater = ("("+phoneNumberFormater.substring(0,2)+") "+
                    phoneNumberFormater.substring(2,7)+"-"+
                    phoneNumberFormater.substring(7,11));
        } else {
            return phoneNumberFormater;
        }
        return phoneNumberFormater;
    }

    public static String formatCpfOrCnpj(final String identification) {
        String document = cleanFormat(identification);

        if (!(document.length()==0) && document.length()<=14){
            document.replaceAll("\\W","");

            if(document.length()==14){
                document = document.substring(0,2)+"."+document.substring(2,5)+"."+document.substring(5,8)+"/"+document.substring(8,12)+"-"+document.substring(12,14);
            }

            if (document.length()==11){
                document = document.substring(0,3)+"."+document.substring(3,6)+"."+document.substring(6,9)+"-"+document.substring(9,11);
            }

        } else {
            document = document.replaceAll("\\W","");
        }
        return document;
    }

    public static String cleanFormat(String phoneNumber){
        phoneNumber = phoneNumber.replaceAll("\\W","");
        return phoneNumber.replaceAll("[a-zA-Z\\s]","");
    }
    public static String cleanFormatValues(String phoneNumber){
        return phoneNumber.replace("R$","");
    }

    public static String formatMonetaryValue(Double value) {
        return "R$ "+formatter.format(value);
    }

    public static String formatMonetaryValuePositiveOrNegative(Double value,boolean isPositivo) {
        if (isPositivo){
            return "R$ "+"+"+formatter.format(value);
        } else {
            return "R$ "+"-"+formatter.format(value);
        }
    }

    public static String formatTextToUpperOrLowerCase(String stringToConvert,boolean isUpper){
        if (isUpper){
            return stringToConvert.toUpperCase();
        } else {
            return stringToConvert.toLowerCase();
        }
    }

    public static String formatDateToStringFormated(long currentTimeMillis) {
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("dd/MM/yyyy");
        return simpleDateFormat.format(currentTimeMillis);
    }

}
