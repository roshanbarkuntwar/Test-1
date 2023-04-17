/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lhs.taxcpcAdmin.utilities;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lhs.javautilities.LhsStringUtility;

/**
 *
 * @author akash.dev
 */
@Component
public class DateTimeUtil {

	@Autowired
	private LhsStringUtility utl;

    public static int compareDate(Date date1, Date date2) {
        int result = 0;
        if (date1 != null && date2 != null) {
            result = date1.compareTo(date2);
        }
        return result;
    }

    public static Date getDateFromDateSting(String dateString) {
        Date date = null;
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        try {
            date = df.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public static int getDateDifferenceInDays(Date date1, Date date2) {
        int numberDays = 0;

        /* long diff = date2.getTime() - date1.getTime();
         long diffSeconds = diff / 1000 % 60;
         long diffMinutes = diff / (60 * 1000) % 60;
         long diffHours = diff / (60 * 60 * 1000);*/
        numberDays = (int) ((date2.getTime() - date1.getTime()) / (1000 * 60 * 60 * 24));

        return numberDays;
    }

    public static long getDateDifferenceInMillis(Date olderDate, Date newerDate) {

        long difference = 0;

        long oldMillis = olderDate.getTime();	// get milliseconds in older date
        long newMillis = newerDate.getTime();	// get milliseconds in newer date

        difference = newMillis - oldMillis;		// get difference in milliseconds

        return difference;

    }

    public String get_sysdate(String format) {
        String today = "";
        try {
            Calendar currentDate = Calendar.getInstance();
            SimpleDateFormat formatter = new SimpleDateFormat(format);
            today = formatter.format(currentDate.getTime());
        } catch (Exception ex) {
            today = "";
            //System.out.println(ex.getMessage());
        }
        return today;
    }// end get_sysdate

    public String getCutrrentTimeStamp() {
        DateFormat format = new SimpleDateFormat("yyyy_MM_dd_hh_mm_ss_SS");
        String timeStamp = format.format(new Date());
        return timeStamp;
    }

    public boolean check_sysdate_between_two_date(String format, String StartDateStr, String EndDateStr) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        Date startDate = null;
        Date endDate = null;
        try {
            startDate = sdf.parse(StartDateStr);
            endDate = sdf.parse(EndDateStr);
        } catch (ParseException ex) {
            //System.out.println("Exception on checking between dates..." + ex.getMessage());
        }
        Date d = new Date();
        String currDt = sdf.format(d);
        if ((d.after(startDate) && (d.before(endDate))) || (currDt.equals(sdf.format(startDate)) || currDt.equals(sdf.format(endDate)))) {
            //System.out.println("Date is Between " + StartDateStr + " to " + EndDateStr);
            return true;
        } else {
            //System.out.println("Date is Not Between " + StartDateStr + " to " + EndDateStr);
            return false;
        }
    }

    public boolean check_date_between_two_dates(String format, Date StartDate, Date EndDate, Date OriginalDate) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        Date d = OriginalDate;
        String currDt = sdf.format(d);
        try {
            if ((d.after(StartDate) && (d.before(EndDate))) || (currDt.equals(sdf.format(StartDate)) || currDt.equals(sdf.format(EndDate)))) {
                //System.out.println("Date is Between " + sdf.format(StartDate) + " to " + sdf.format(EndDate));
                return true;
            } else {
                //System.out.println("Date is Not Between " + sdf.format(StartDate) + " to " + sdf.format(EndDate));
                return false;
            }
        } catch (Exception e) {
            //System.out.println("Date is Not Between" + sdf.format(StartDate) + " to " + sdf.format(EndDate));
            return false;
        }
    }

    public boolean check_date_less_then_selected_dates(String format, Date EndDate, Date OriginalDate) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        Date d = OriginalDate;
        String currDt = sdf.format(d);
        try {
            if (((d.before(EndDate)) || currDt.equals(sdf.format(EndDate)))) {
                //System.out.println("Date is Between " + sdf.format(StartDate) + " to " + sdf.format(EndDate));
                return true;
            } else {
                //System.out.println("Date is Not Between " + sdf.format(StartDate) + " to " + sdf.format(EndDate));
                return false;
            }
        } catch (Exception e) {
            //System.out.println("Date is Not Between" + sdf.format(StartDate) + " to " + sdf.format(EndDate));
            return false;
        }
    }

    public boolean check_date_less_then_selected_dates(Date fromDate, Date toDate, Date selectedDate) { // This method is overload to check proper validation in manage deductee
        boolean flag = false;
        try {
            Calendar from_cal = Calendar.getInstance();
            from_cal.setTime(fromDate);
            Calendar to_cal = Calendar.getInstance();
            to_cal.setTime(toDate);
            Calendar sel_cal = Calendar.getInstance();
            sel_cal.setTime(selectedDate);
            if ((from_cal.before(sel_cal) || from_cal.equals(sel_cal)) && (to_cal.after(sel_cal) || to_cal.equals(sel_cal))) {
                flag = true;
            }

        } catch (Exception e) {
            flag = false;
        }

        return flag;
    }//end medhod;

    public String getDateMinus23Months(Date dateValue) {
        String returnDate = "";
        try {
            if (dateValue != null) {
                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(dateValue);
                calendar.add(Calendar.MONTH, -23);
                returnDate = sdf.format(calendar.getTime());
            }
        } catch (Exception e) {
            returnDate = null;
        }
        return returnDate;
    }// end method

    public String get_SysDateMinus23Months() {
        String returnDate = "";
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(calendar.getTime());
            calendar.add(Calendar.MONTH, -23);
            returnDate = sdf.format(calendar.getTime());
        } catch (Exception e) {
            returnDate = null;
        }
        return returnDate;
    }// end method

    public SimpleDateFormat getDefaultDateFormat() {
        return new SimpleDateFormat("dd-MM-yyyy");
    }

    public SimpleDateFormat getDefaultTimeStampFormat() {
        return new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
    }

    public Timestamp getDefaultTimestamp() {
        return new Timestamp(new Date().getTime());
    }

    public Date getQuarterFromDate(String accYear, Integer quarterNo) {
        Date d = null;
        try {
            if (!utl.isNull(accYear)) {
                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                String arr[] = accYear.split("-");
                if (null != quarterNo) {
                    switch (quarterNo) {
                        case 1: {
                            String dateStr = "01-04-20" + arr[0];
                            d = sdf.parse(dateStr);
                            break;
                        }
                        case 2: {
                            String dateStr = "01-07-20" + arr[0];
                            d = sdf.parse(dateStr);
                            break;
                        }
                        case 3: {
                            String dateStr = "01-10-20" + arr[0];
                            d = sdf.parse(dateStr);
                            break;
                        }
                        case 4: {
                            String dateStr = "01-01-20" + arr[1];
                            d = sdf.parse(dateStr);
                            break;
                        }
                        default:
                            break;
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();

        }

        return d;
    }

    public Date getQuarterToDate(String accYear, Integer quarterNo) {
        Date d = null;
        try {
            if (!utl.isNull(accYear)) {
                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                String arr[] = accYear.split("-");
                if (null != quarterNo) {
                    switch (quarterNo) {
                        case 1: {
                            String dateStr = "30-06-20" + arr[0];
                            d = sdf.parse(dateStr);
                            break;
                        }
                        case 2: {
                            String dateStr = "30-09-20" + arr[0];
                            d = sdf.parse(dateStr);
                            break;
                        }
                        case 3: {
                            String dateStr = "31-12-20" + arr[0];
                            d = sdf.parse(dateStr);
                            break;
                        }
                        case 4: {
                            String dateStr = "31-03-20" + arr[1];
                            d = sdf.parse(dateStr);
                            break;
                        }
                        default:
                            break;
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();

        }

        return d;
    }

    public Date getYearBegDate(String accYear) {
        Date d = null;

        try {
            if (!utl.isNull(accYear)) {
                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                String arr[] = accYear.split("-");

                String dateStr = "01-04-20" + arr[0];
                d = sdf.parse(dateStr);

            }
        } catch (Exception e) {

        }

        return d;
    }

    public Date getYearEndDate(String accYear) {
        Date d = null;

        try {
            if (!utl.isNull(accYear)) {
                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                String arr[] = accYear.split("-");

                String dateStr = "31-03-20" + arr[1];
                d = sdf.parse(dateStr);

            }
        } catch (Exception e) {

        }

        return d;
    }
//
//    public static void main(String arr[]) {
//
//    }
}
