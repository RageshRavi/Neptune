/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.neptune.comparators;

import java.util.Comparator;


/**
 *
 * @author ragesh.raveendran
 */
import com.neptune.schema.Media.Channels.Station.Programme;
import com.neptune.util.DateUtil;
import static com.neptune.util.DateUtil.DATE_FORMAT_HH_MM_SS;
import static com.neptune.util.DateUtil.DATE_FORMAT_NEPTUNE;
import java.util.Date;
public class ProgrammeStartdateComparator implements Comparator{

    @Override
    public int compare(Object o1, Object o2) {
       Programme p1 = (Programme) o1;
       Programme p2 = (Programme) o2;
       
       Date p1StartDate = DateUtil.getInstance().getDate(p1.getStarttime(), DATE_FORMAT_NEPTUNE);
       p1StartDate = DateUtil.getInstance().getDate(DateUtil.getInstance().
               formatDate(p1StartDate, DATE_FORMAT_HH_MM_SS), DATE_FORMAT_HH_MM_SS);
       
       Date p2StartDate = DateUtil.getInstance().getDate(p2.getStarttime(), DATE_FORMAT_NEPTUNE);
       p2StartDate = DateUtil.getInstance().getDate(DateUtil.getInstance().
               formatDate(p2StartDate, DATE_FORMAT_HH_MM_SS), DATE_FORMAT_HH_MM_SS);
       
       
       
       return p1StartDate.compareTo(p2StartDate);
    }
    
}
