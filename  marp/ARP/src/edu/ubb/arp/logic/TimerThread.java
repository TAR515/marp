package edu.ubb.arp.logic;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class TimerThread implements Runnable {
	private long currentWeek;
	private Calendar startDate;
	private Calendar currentDate;
	
	public TimerThread() {
		startDate = new GregorianCalendar();
		startDate.set(2007, 1, 1);            // Starting week, this is the 0rd week in the database.
		currentDate = new GregorianCalendar();
		
		currentWeek = weeksBetween(startDate.getTime(), currentDate.getTime());
		System.out.println(currentWeek);
		
	} 
   
   
	public void run() { 
		while( true ) { 
			System.out.println("A szal neve:"+ (Thread.currentThread()).getName() ); 
			try{ 
				Thread.sleep( 1000 ); 
				//Thread.sleep( 3600000 ); // sleep for 1 hour
			} catch( InterruptedException e ){} 
		} 
   } 
	
	
	private int weeksBetween(Date d1, Date d2){
		 return (int)( (d2.getTime() - d1.getTime()) / (1000 * 60 * 60 * 24 * 7));
	}
} 