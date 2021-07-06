package com.example.compraagro.Validation;

import com.vinaygaba.creditcardview.CardType;
import com.vinaygaba.creditcardview.CreditCardView;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by culqi on 1/19/17.
 */

public class Validation {

    public static boolean luhn(String number){
        int s1 = 0, s2 = 0;
        String reverse = new StringBuffer(number).reverse().toString();
        for(int i = 0 ;i < reverse.length();i++){
            int digit = Character.digit(reverse.charAt(i), 10);
            if(i % 2 == 0){//this is for odd digits, they are 1-indexed in the algorithm
                s1 += digit;
            }else{//add 2 * digit for 0-4, add 2 * digit - 9 for 5-9
                s2 += 2 * digit;
                if(digit >= 5){
                    s2 -= 9;
                }
            }
        }
        return (s1 + s2) % 10 == 0;
    }

    public void  bin(String bin, CreditCardView creditCardView) {

        if(bin.length() > 1) {
            if(Integer.valueOf(bin.substring(0,2)) == 41) {
                creditCardView.setType(CardType.VISA);
            } else if (Integer.valueOf(bin.substring(0,2)) == 51) {
                creditCardView.setType(CardType.MASTERCARD);
            } else {
                creditCardView.setType(CardType.DISCOVER);
            }
        } else {
            creditCardView.setType(CardType.DISCOVER);
        }

    }

    public boolean month(String month) {
        if(!month.equals("")){
            if(Integer.valueOf(""+month) > 12) {
                return true;
            }
        }
        return false;
    }

    public boolean year(String year){
        Date today = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(today);
        if(!year.equals("")){
            if(Integer.valueOf("20"+year) < calendar.get(Calendar.YEAR)) {
                return true;
            }
        }
        return false;
    }

}
