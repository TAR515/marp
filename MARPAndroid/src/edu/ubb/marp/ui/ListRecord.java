package edu.ubb.marp.ui;

public class ListRecord {
	 public String item;
	 public String subitem;
	    
	    public ListRecord(String item, String subitem) {
	        this.item = item;
	        this.subitem = subitem;
	    }
	    public ListRecord(){
	    	this.item = "";
	    	this.subitem = "";
	    }
	    public String getItem(){
	    	return this.item;
	    }
	    public String getSubitem(){
	    	return this.subitem;
	    }
	    public void setItem(String s){
	    	this.item = s;
	    }
	    public void setStubitem(String s){
	    	this.subitem = s;
	    }
}
