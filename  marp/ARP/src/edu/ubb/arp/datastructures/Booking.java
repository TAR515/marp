package edu.ubb.arp.datastructures;

public class Booking {
	private int week;
	private double ratio;

	public Booking() {
		week = -1;
		ratio = -1;
	}

	public Booking(int week, double ratio) {
		this.week = week;
		this.ratio = ratio;
	}

	public int getWeek() {
		return week;
	}

	public void setWeek(int week) {
		this.week = week;
	}

	public double getRatio() {
		return ratio;
	}

	public void setRatio(double ratio) {
		this.ratio = ratio;
	}

}
