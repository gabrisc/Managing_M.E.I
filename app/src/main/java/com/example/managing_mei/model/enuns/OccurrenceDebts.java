package com.example.managing_mei.model.enuns;

public enum OccurrenceDebts {
    VALOR_FIXO,
    VALOR_PARCELADO;

    OccurrenceDebts() {
    }

    public OccurrenceDebts getOccurrenceDebtsByIds(String id){
        switch (id){
            case"VALOR_FIXO":
                return OccurrenceDebts.VALOR_FIXO;
            case "VALOR_PARCELADO":
                return OccurrenceDebts.VALOR_PARCELADO;

        }
        return null;
    }
}


