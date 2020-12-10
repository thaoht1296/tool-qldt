/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ducnhan.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author nhani
 * Duoc su dung de kiem tra ma sinh vien.
 */
public class MyUtils {
    public static boolean checkMsv(String strMaSv){
        String strRegex = "^[bBcC]{1}\\d{2}[dDcCnaANqQtTsSkK]{4}\\d{3}$";
        Pattern pattern = Pattern.compile(strRegex);
        Matcher matcher = pattern.matcher(strMaSv);
        
        if (matcher.find()) {
            return true;
        }
        
        return false;
    }
}
