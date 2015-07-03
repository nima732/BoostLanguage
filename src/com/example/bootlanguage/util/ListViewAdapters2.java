package com.example.bootlanguage.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.example.boostlanguage.R;
import com.example.boostlanguage.entity.Sentences;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class ListViewAdapters2 extends ArrayAdapter<Sentences> {

	
	
	public ListViewAdapters2(Context context, int resource) {
		super(context, resource);
	}

	
	public ListViewAdapters2(Context context, int resource , List<Sentences> sentenceses) {
		super(context, resource,sentenceses);
	}
	
	 @Override
	    public View getView(int position, View convertView, ViewGroup parent) {

	        View v = convertView;

	        if (v == null) {
	            LayoutInflater vi;
	            vi = LayoutInflater.from(getContext());
	            v = vi.inflate(R.layout.clomn_row, null);
	        }

	        Sentences p = getItem(position);

	        
	        if (p != null) {
	        
	        	
	        	TextView txtFirst=(TextView) v.findViewById(R.id.clomRowWold);
				TextView txtSecond=(TextView) v.findViewById(R.id.clomnRowTransWorld);
				TextView txtThird=(TextView) v.findViewById(R.id.clomnRowDate);
			
	        	
				

	            if (txtFirst != null) {
	                txtFirst.setText(p.getWorld());
	            }

	            if (txtSecond != null) {
	                txtSecond.setText(p.getWorldTrans());
	            }

	            if (txtThird != null) {
	                txtThird.setText(String.valueOf(p.getTime()));
	            }
	        }

	        return v;
	    }


}
