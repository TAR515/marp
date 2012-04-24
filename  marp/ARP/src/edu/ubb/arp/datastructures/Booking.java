package edu.ubb.arp.datastructures;

public class Booking {
	private int week;
	private int ratio;
	private boolean isLeader;

	public Booking() {
		week = -1;
		ratio = -1;
	}

	public Booking(int week, int ratio) {
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

	public void setRatio(int ratio) {
		this.ratio = ratio;
	}

	public boolean isLeader() {
		return isLeader;
	}

	public void setLeader(boolean isLeader) {
		this.isLeader = isLeader;
	}

}
