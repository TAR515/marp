package edu.ubb.arp.logic;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
 
public class as {
 public static void main(String args[]){
as difference = new as();
 }
as() {
 Calendar cal1 = new GregorianCalendar();
 Calendar cal2 = new GregorianCalendar();

 cal1.set(2008, 8, 1); 
 cal2.set(2008, 9, 31);
 System.out.println("Days= "+daysBetween(cal1.getTime(),cal2.getTime()));
 }
 public int daysBetween(Date d1, Date d2){
 return (int)( (d2.getTime() - d1.getTime()) / (1000 * 60 * 60 * 24));
 }
 }