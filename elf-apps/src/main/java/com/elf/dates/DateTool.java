/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elf.dates;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.util.Calendar;
import java.util.Date;

/**
 *
 * @author bnevins
 */
public class DateTool {
    
    public static void usage() {
        System.out.println("DateTool start_date end_date\nFormat: 20191225");
        System.exit(0);
    }
    
  private static SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
    
     public static void main(String[] args) throws ParseException{
        if(args.length == 0)
            usage();

        LocalDate end;
        LocalDate begin;

        if(args.length == 1) {
            end=getLocalDate();
        }

        else {// if(args.length >= 2) {
            String fname = args[1];
            if(fname.length() < 6)
                System.exit(1);
            if(!Character.isDigit(fname.charAt(0))) {
                // vlc-record-2019-09-18-12h30m31s-20160620_I Caught a BIG One! (WK 285.4) _ Bratayley-TCSaFj7EfRA.mp4-
                if(fname.length() >= 38)
                    fname = fname.substring(32);
                else
                    System.exit(0);
            }
            
            if(!Character.isDigit(fname.charAt(6)) || 
                !Character.isDigit(fname.charAt(7)) ) {
                fname = fname.substring(0,6);
                fname += "15";
            }
            Date date = sdf.parse(fname);
            end = getLocalDate(date);
        }

        // end is setup -- now setup begin
        begin = getLocalDate(args[0]);

        Period diff1 = Period.between(begin, end);
        System.out.println("age: " + diff1.getYears() + " years, " + 
                diff1.getMonths() + " months");
 }
     
     private static LocalDate getLocalDate(int year, int month, int date) {
         return LocalDate.of(year, month, date);
     }
     private static LocalDate getLocalDate() {
         return LocalDate.now();
     }
     
     private static LocalDate getLocalDate(Date d) {
        Calendar c = Calendar.getInstance();
        c.setTime(d);
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH) + 1;
        int date = c.get(Calendar.DATE);
        return getLocalDate(year, month, date);
     }  
     
     private static LocalDate getLocalDate(String dateString) throws ParseException{
        return getLocalDate(sdf.parse(dateString));
     }
}
