 package com.example.managing_mei.utils;

import com.example.managing_mei.model.enuns.FrequencyDebts;
import com.example.managing_mei.model.enuns.TypeOfDebts;

public class Contants {
    public final static String PAGAMENTO = "PAGAMENTO";
    public final static String RECEBIMENTO = "RECEBIMENTO";

    public final static Integer RECEBIMENTO_CASH_FLOW_ITEM_TYPE = 1;
    public final static Integer PAGAMENTO_CASH_FLOW_ITEM_TYPE = 0;

    public final static String[] listOfParcelOptions = {"1x","2x","3x","4x","5x","6x","7x","8x","9x","10x","11x","12x"};

    public final static String[] listOfTypeDebt = {TypeOfDebts.UNICA.toString(),TypeOfDebts.PARCELADA.toString()};

    public final static String[] listOfFrequencyType = {FrequencyDebts.DIARIA.toString(),FrequencyDebts.SEMANAL.toString(),FrequencyDebts.MENSAL.toString(),FrequencyDebts.TRIMESTRAL.toString(), FrequencyDebts.SEMESTRAL.toString(), FrequencyDebts.ANUAL.toString()};

}
