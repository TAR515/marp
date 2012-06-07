package edu.ubb.marp.ui;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
/**
 * 
 * @author Vizer Arnold
 *	The About Windows text view	
 *
 */
public class AboutMessage extends View {

	TextView text;
	/**
	 * 
	 * @param Context of the view
	 */
	public AboutMessage(Context context) {
		super(context);
		text = new TextView(context);
		text.setGravity(Gravity.CENTER);
		text.setText(" MARP 1.0 \n 2012 \n Collective Project \n\nCreated by: \n Rakosi Alpar\nTurdean Arnold Robert\nVarga Adorjan\nVizer Arnold ");
	}
	/**
	 * 
	 * @return the textView with a message
	 */
	public View returnView(){
		return text;
	}
}
