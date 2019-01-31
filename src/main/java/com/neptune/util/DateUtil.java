/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.neptune.util;

import com.neptune.common.DataHolder;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.logging.Level;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

/**
 *
 * @author ragesh.raveendran
 */
public class DateUtil {

    private static final DateUtil INSTANCE = new DateUtil();
    public static final String DEFAULT_DATE_FORMAT = "yyyyMMdd";
    public static final String DATE_FORMAT_YYYY_MM_DD = "yyyy-MM-dd";
    public static final String DATE_FORMAT_YY_MM_DD = "yyMMdd";
    public static final String DATE_FORMAT_HH_MM_SS = "HH:mm:ss";
    public static final String DATE_FORMAT_HH_MM = "HHmm";
    public static final String DATE_FORMAT_HH_MM_SS_SSSS = "HH:mm:ss:SSSS";
    public static final String DATE_FORMAT_HHMMSS = "HHmmss";
    public static final String DATE_FORMAT_Y_MM_DD = "yy-MM-dd";
    public static final String DATE_FORMAT_MM_DD_YYYY = "MM/dd/yyyy";
    public static final String DATE_FORMAT_MMDDYYYY = "yyyy/MM/dd";
    public static final String DATE_FORMAT_YYMMDD = "yy/MM/dd";
    public static final String DATE_FORMAT_YYYY_MM_DD_YYYY_HH_mm_ss_0 = "yyyy-MM-dd HH:mm:ss.0";
    public static final String DATE_FORMAT__M_d_y_hh_mm_ss_a = "M/d/y hh:mm:ss a";
    public static final String DATE_FORMAT__MMDDYYHHMM = "MMddyyHHmm";

    public static final String DATE_FORMAT_NEPTUNE = "yyyy:MM:dd:HH:mm:ss";

    public static DateUtil getInstance() {
        return INSTANCE;
    }

    public boolean isCurrentDateInBetween(Date startDate, Date endDate) {
        Date date = getDate(formatDate(new Date(), DATE_FORMAT_HH_MM_SS), DATE_FORMAT_HH_MM_SS);
        if (date.after(getDate(formatDate(startDate, DATE_FORMAT_HH_MM_SS), DATE_FORMAT_HH_MM_SS)) && date.before(getDate(formatDate(endDate, DATE_FORMAT_HH_MM_SS), DATE_FORMAT_HH_MM_SS))) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isDateInBetween(Date currentDate, Date startDate, Date endDate) {
        if ((currentDate.after(startDate) || (currentDate.compareTo(startDate) == 0 ? true : false))
                && (currentDate.before(endDate) || (currentDate.compareTo(endDate) == 0 ? true : false))) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isCurrentTimeInBetween(Date startTimeXML, Date endTimeXML) {
//        Date startTime = toDate(startTimeXML);
//        Date endTime = toDate(endTimeXML);
        Date date = getDate(formatDate(new Date(), DATE_FORMAT_HH_MM_SS), DATE_FORMAT_HH_MM_SS);
        if ((date.after(startTimeXML) || (date.compareTo(startTimeXML) == 0 ? true : false)) && date.before(endTimeXML)) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isRepeat(String repeat, String programmeStartTime) {
        if (repeat.matches("[a-zA-Z ]*\\d+.*")) {
            return isCurrentDate(repeat, programmeStartTime);
        } else {
            return isCurrentDay(repeat);
        }
    }

    public boolean isDailyProgramForToday(String repeat, String programmeStartTime) {
        if (repeat.matches("[a-zA-Z ]*\\d+.*")) {
            return isCurrentDate(repeat, programmeStartTime);
        } else {
            return false;
        }
    }

    public boolean isCurrentDay(String repeat) {
        int day = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
        if (day == 1) {
            return repeat.contains("Sun");
        }
        if (day == 2) {
            return repeat.contains("Mon");
        }
        if (day == 3) {
            return repeat.contains("Tue");
        }
        if (day == 4) {
            return repeat.contains("Wed");
        }
        if (day == 5) {
            return repeat.contains("Thu");
        }
        if (day == 6) {
            return repeat.contains("Fri");
        }
        if (day == 7) {
            return repeat.contains("Sat");
        } else {
            return false;
        }
    }

    public Date addTime(String length) {
        Date currDate = getDate(formatDate(new Date(), DATE_FORMAT_HH_MM_SS), DATE_FORMAT_HH_MM_SS);
        String[] lengthSplitArr = length.split(":");
        int hrs = currDate.getHours() + Integer.parseInt(lengthSplitArr[0]);
        int min = currDate.getMinutes() + Integer.parseInt(lengthSplitArr[1]);
        int sec = currDate.getSeconds() + Integer.parseInt(lengthSplitArr[2]);
        if (sec > 59) {
            min = min + (sec / 60);
            sec = sec % 60;
        }
        if (min > 59) {
            hrs = hrs + min / 60;
            min = min % 60;
        }
        if (hrs > 23) {
            hrs = hrs % 24;
        }

        String digit = "00";
        StringBuilder endtime = new StringBuilder();
        endtime.append(digit.subSequence(0, 2 - String.valueOf(hrs).length())).append(String.valueOf(hrs));
        endtime.append(":");
        endtime.append(digit.subSequence(0, 2 - String.valueOf(min).length())).append(String.valueOf(min));
        endtime.append(":");
        endtime.append(digit.subSequence(0, 2 - String.valueOf(sec).length())).append(String.valueOf(sec));
        return DateUtil.getInstance().getDate(endtime.toString(), DATE_FORMAT_HH_MM_SS);
    }

    public int getTimeDifferenceInSeconds(Date startTime) {
        Date curreDate = DateUtil.getInstance().getDate(DateUtil.getInstance().formatDate(new Date(),
                DateUtil.DATE_FORMAT_HHMMSS), DateUtil.DATE_FORMAT_HHMMSS);

        int hour = curreDate.getHours() - startTime.getHours();
        int min = curreDate.getMinutes() - startTime.getMinutes();
        int seconds = curreDate.getSeconds() - startTime.getSeconds();
        System.out.println("hour " + hour);
        System.out.println("hour " + min);
        System.out.println("hour " + seconds);
        seconds = seconds + min * 60 + hour * 3600;

        return seconds;
    }

    public boolean isCurrentDay(Date repeat) {
        return (getDate(formatDate(new Date(), DEFAULT_DATE_FORMAT), DEFAULT_DATE_FORMAT).compareTo(repeat)) == 0 ? true : false;
    }

    public Date getDate(String dateStr, String dateFormat) {
        Date date = null;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat);
        simpleDateFormat.setLenient(false);
        try {
            date = simpleDateFormat.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
            NeptuneLogger.getInstance().getLogger().log(Level.SEVERE, null, e);
        } catch (Exception e) {
            NeptuneLogger.getInstance().getLogger().log(Level.SEVERE, null, e);
        }
        return date;
    }

    public String formatDate(Date date, String dateFormat) {
        String dateStr = null;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat);
        simpleDateFormat.setLenient(false);
        dateStr = simpleDateFormat.format(date);
        return dateStr;
    }

    public String formatDate(XMLGregorianCalendar date, String dateFormat) {
        String dateStr = null;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat);
        simpleDateFormat.setLenient(false);
        dateStr = simpleDateFormat.format(toDate(date));
        return dateStr;
    }

    public Date toDate(XMLGregorianCalendar calendar) {
        if (calendar == null) {
            return null;
        }
        return getDate(formatDate(calendar.toGregorianCalendar().getTime(), DATE_FORMAT_HH_MM_SS), DATE_FORMAT_HH_MM_SS);
    }

    public Date addDays(Date date, int days) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, days); //minus number would decrement the days
        return calendar.getTime();
    }

    public Date coverageDateCheckAndFix(Date inDate) {
        return inDate;
        // effective date always should start from first of month
        // if date from edi file is not on first of the month
        // we change it to first of month
//        if (inDate == null) {
//            return null;
//        }
//        Calendar cal = Calendar.getInstance();
//        cal.setTime(inDate);
//        if (cal.get(Calendar.DATE) != 1) {
//            cal.set(Calendar.MONTH, (cal.get(Calendar.MONTH) + 1));
//            cal.set(Calendar.DATE, 1);
//        }
//        return cal.getTime();
    }

    public Date getOneDayBefore(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, -1);
        return calendar.getTime();
    }

    public Date getOneDayAfter(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, +1);
        return calendar.getTime();
    }

    public Date getStartOfaYear(Date effectivedate) {
        Calendar date = Calendar.getInstance();
        date.setTime(effectivedate);
        int year = date.get(Calendar.YEAR);
        Calendar calenderLastDate = Calendar.getInstance();
        calenderLastDate.set(Calendar.YEAR, year);
        calenderLastDate.set(Calendar.MONTH, 0);
        calenderLastDate.set(Calendar.DAY_OF_MONTH, 1);
        calenderLastDate.set(Calendar.HOUR_OF_DAY, 0);
        calenderLastDate.set(Calendar.MINUTE, 0);
        calenderLastDate.set(Calendar.SECOND, 0);
        calenderLastDate.set(Calendar.MILLISECOND, 0);
        return calenderLastDate.getTime();
    }

    public Date getEndOfaYear(Date effectivedate) {
        Calendar date = Calendar.getInstance();
        date.setTime(effectivedate);
        int year = date.get(Calendar.YEAR);
        Calendar calenderLastDate = Calendar.getInstance();
        calenderLastDate.set(Calendar.YEAR, year);
        calenderLastDate.set(Calendar.MONTH, 11);
        calenderLastDate.set(Calendar.DAY_OF_MONTH, 31);
        calenderLastDate.set(Calendar.HOUR_OF_DAY, 0);
        calenderLastDate.set(Calendar.MINUTE, 0);
        calenderLastDate.set(Calendar.SECOND, 0);
        calenderLastDate.set(Calendar.MILLISECOND, 0);
        return calenderLastDate.getTime();
    }

    /**
     * Return XML Gregorain Calender from Gregorian
     *
     * @param calender
     * @return
     */
    public XMLGregorianCalendar getXMLGregorian(
            final GregorianCalendar calender) {
        DatatypeFactory datatypeFactory = null;
        try {
            datatypeFactory = DatatypeFactory.newInstance();
        } catch (final DatatypeConfigurationException ex) {
            NeptuneLogger.getInstance().getLogger().info(ex.getMessage());
        }
        return datatypeFactory.newXMLGregorianCalendar(calender);

    }

    /**
     * Return XML Gregorian Calender from Date
     *
     * @param date
     * @return
     */
    public XMLGregorianCalendar getXMLGregorianFromDate(
            final Date date) {
        if (date != null) {
            GregorianCalendar calender = new GregorianCalendar();
            calender.setTime(date);
            return getXMLGregorian(calender);
        }
        return null;
    }

    public boolean isCurrentTimeEqulsSystemTime(XMLGregorianCalendar date, Date currentTime) {
        Date campaignDate = toDate(date);
        return (currentTime.compareTo(campaignDate) == 0) ? true : false;
    }

    public boolean isCampaignPlayorderContainsCurrentTime(String timeRepeat, Date currentTime) {
        String[] playOrderStringArray = timeRepeat.split(",");
       for (String playOrderStringArray1 : playOrderStringArray) {
            Date date = DateUtil.getInstance().getDate(playOrderStringArray1.trim(), DATE_FORMAT_HH_MM_SS);
            if(date.compareTo(currentTime) == 0){
                return true;
            }

        }
        return false;
    }

//    if a true happens to be the return type it means that this is definitely a daily program.
    private boolean isCurrentDate(String repeat, String programmeStartTime) {
        if (0 == formatDate(DateUtil.getInstance().getDate(repeat, DATE_FORMAT_NEPTUNE), DATE_FORMAT_YY_MM_DD)
                .compareTo(formatDate(Calendar.getInstance().getTime(),DATE_FORMAT_YY_MM_DD))) {
            //placeholder saying that this stuff is going to be a hard reset on the current
//            playing program and invoke this daily program when its starttime matches the current time
            if(null != programmeStartTime){
                DateFormat dateFormatter = new SimpleDateFormat(DATE_FORMAT_HH_MM_SS);
                DataHolder.nextDailyProgrammeTime = getDate(dateFormatter.format(getDate(programmeStartTime, DATE_FORMAT_NEPTUNE)), DATE_FORMAT_HH_MM_SS);
            }
            return true;
        } else {
            return false;
        }
    }
}
