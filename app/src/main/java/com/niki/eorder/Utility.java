package com.niki.eorder;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

public class Utility {
    public String toIDR(long money){
        DecimalFormat indonesia = (DecimalFormat) DecimalFormat.getCurrencyInstance();
        DecimalFormatSymbols rupiah = new DecimalFormatSymbols();

        rupiah.setCurrencySymbol("IDR ");
        rupiah.setGroupingSeparator('.');
        indonesia.setMinimumFractionDigits(0);
        indonesia.setDecimalFormatSymbols(rupiah);

        return indonesia.format(money);
    }

    public String capitalizeString(String word){
        word = word.replaceAll("_", " ");
        String words[] = word.split("\\s");
        String capitalizeWord = "";

        for (String w : words){
            String first = w.substring(0, 1);
            String afterFirst = w.substring(1);

            capitalizeWord += first.toUpperCase() + afterFirst + " ";
        }

        return  capitalizeWord.trim();
    }

}
