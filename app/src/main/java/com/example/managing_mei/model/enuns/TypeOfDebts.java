package com.example.managing_mei.model.enuns;

public enum TypeOfDebts {
  UNICA,//1
  PARCELADA,//2
  RECORRENTE;//3

    TypeOfDebts() {}

  public static Boolean IsEqualsToParcelada(String value){
      if(TypeOfDebts.valueOf(value).equals(PARCELADA)){
        return true;
      } else {
        return false;
      }

  }
  public static Boolean IsEqualsToRecorrente(String value){
    if(TypeOfDebts.valueOf(value).equals(RECORRENTE)){
      return true;
    } else {
      return false;
    }

  }

  public static TypeOfDebts integerToTypeOfDebts(Integer value){
    switch (value){
      case 1:
        return UNICA;
      case 2:
        return PARCELADA;
      case 3:
        return RECORRENTE;
      default:
        throw new IllegalStateException("Unexpected value: " + value);
    }
  }
}
