package com.user.aditya.gradbud;

/**
 * Created by Aditya on 2/2/2018.
 */

public class Encrypt {
    String password;
    Encrypt(String pass)
    {
        password=pass;
    }
    String encrypt_pass()
    {
        int len=password.length();
        String exp="",encrypted="";
        for(int i=0;i<len;i++)
            exp+="0";
        encrypted+=exp;
        for(int i=0;i<len;i++)
            encrypted+=(int)password.charAt(i)+exp;
        encrypted+=String.valueOf(len);
        return encrypted;
    }
}
