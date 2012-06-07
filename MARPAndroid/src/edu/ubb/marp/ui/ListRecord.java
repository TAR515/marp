package edu.ubb.marp.ui;

/**
 * 
 * @author Vizer Arnold
 * 
 */
public class ListRecord {
	public String item;
	public String subitem;

	/**
	 * initializes the item and the sub item
	 * @param item is the main item of a ListRecord which will be in a list activity
	 * @param subitem is the sub item of the item, and will be in a list activity
	 */
	public ListRecord(String item, String subitem) {
		this.item = item;
		this.subitem = subitem;
	}
	/**
	 * initializes the item and the sub item
	 */
	public ListRecord() {
		this.item = "";
		this.subitem = "";
	}
	/**
	 * 
	 * @return the item
	 */
	public String getItem() {
		return this.item;
	}
	/**
	 * 
	 * @return the sub item
	 */
	public String getSubitem() {
		return this.subitem;
	}
	/**
	 * 
	 * @param s is the text, which will be set in item
	 */
	public void setItem(String s) {
		this.item = s;
	}
	/**
	 * 
	 * @param s is the text, which will be set in sub item
	 */
	public void setStubitem(String s) {
		this.subitem = s;
	}
}
