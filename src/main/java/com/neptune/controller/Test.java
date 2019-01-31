/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.neptune.controller;

import com.neptune.util.DateUtil;
import static com.neptune.util.DateUtil.DATE_FORMAT_HH_MM_SS;
import java.util.Date;

/**
 *
 * @author ragesh.raveendran
 */
public class Test {
    public static void  main(String args[]){
        Date trackLength =  DateUtil.getInstance().getDate("21:33:15", DATE_FORMAT_HH_MM_SS);
        String length = "02:30:13";
        String[] lengthSplitArr = length.split(":");
        int hrs = trackLength.getHours() + Integer.parseInt(lengthSplitArr[0]);
        int min = trackLength.getMinutes()+ Integer.parseInt(lengthSplitArr[1]);
        int sec = trackLength.getSeconds()+ Integer.parseInt(lengthSplitArr[2]);
        if(sec > 60){
            min = min+(sec/60);
            sec = sec%60;
        }
        if(min > 60){
            hrs = hrs + min/60;
            min = min%60;
        }
        if(hrs > 23){
            hrs = hrs%24;
        }
        
        String digit = "00";
        StringBuilder endtime = new StringBuilder();
        endtime.append(digit.subSequence(0, 2 - String.valueOf(hrs).length())).append(String.valueOf(hrs));
        endtime.append(":");
        endtime.append(digit.subSequence(0, 2 - String.valueOf(min).length())).append(String.valueOf(min));
        endtime.append(":");
        endtime.append(digit.subSequence(0, 2 - String.valueOf(sec).length())).append(String.valueOf(sec));
        
        
        System.out.println(endtime.toString());
//        long sum = trackLength.getTime() + pgmEndDt.getTime(); 
//        Date sumDate = new Date(sum);
//        System.out.println(sumDate);
    }
}
