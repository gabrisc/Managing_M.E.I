package com.example.managing_mei.model.enuns;

public enum FrequencyDebts {
    DIARIA,//1
    MENSAL,//2
    SEMANAL,//3
    TRIMESTRAL,//4
    SEMESTRAL,//5
    ANUAL;//6

    FrequencyDebts() {
    }

    public Integer frequencyDebtsToInteger(FrequencyDebts debts){
        switch (debts){
            case ANUAL:
                return 6;
            case SEMESTRAL:
                return 5;
            case TRIMESTRAL:
                return 4;
            case SEMANAL:
                return 3;
            case MENSAL:
                return 2;
            case DIARIA:
                return 1;
            default:
                throw new IllegalStateException("Unexpected value: " + debts);
        }
    }

    public FrequencyDebts integerToFrequencyDebts(Integer value){
        switch (value){
            case 6:
                return FrequencyDebts.ANUAL;
            case 5:
                return FrequencyDebts.SEMESTRAL;
            case 4:
                return FrequencyDebts.TRIMESTRAL;
            case 3:
                return FrequencyDebts.SEMANAL;
            case 2:
                return FrequencyDebts.MENSAL;
            case 1:
                return FrequencyDebts.DIARIA;
            default:
                throw new IllegalStateException("Unexpected value: " + value);
        }
    }

}
