package edu.ubb.marp.ui;

import java.util.ArrayList;

import edu.ubb.marp.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
/**
 * 
 * @author Rakosi Alpar
 *
 */
public class ListItemAdapter extends ArrayAdapter<ListRecord> {
	private ArrayList<ListRecord> items;
	/**
	 * 
	 * @param context
	 * @param textViewResourceId
	 * @param items
	 */
    public ListItemAdapter(Context context, int textViewResourceId, ArrayList<ListRecord> items) {
        super(context, textViewResourceId, items);
        this.items = items;
    }
    /**
     * 
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if (v == null) {
            LayoutInflater vi = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(R.layout.listitem, null);
        }
      
        ListRecord user = items.get(position);
        if (user != null) {
        	TextView username = (TextView) v.findViewById(R.id.username);
            TextView email = (TextView) v.findViewById(R.id.email);

            if (username != null) {
                username.setText(user.item);
            }	

            if(email != null) {
            	email.setText(user.subitem );
            }
        }
        return v;
    }

}
