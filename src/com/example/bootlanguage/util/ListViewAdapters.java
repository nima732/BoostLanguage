package com.example.bootlanguage.util;

import java.util.ArrayList;
import java.util.HashMap;

import com.example.boostlanguage.R;
import com.example.boostlanguage.entity.Sentences;

import android.app.Activity;
import android.content.ClipData.Item;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ListViewAdapters extends BaseAdapter {


	public ArrayList<HashMap<String, String>> mylist;
	Activity activity;
	TextView txtFirst;
	TextView txtSecond;
	TextView txtThird;
	public ListViewAdapters(Activity activity,ArrayList<HashMap<String, String>> list){
		super();
		this.activity=activity;
		this.mylist=list;
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mylist.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return mylist.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub

//		View view = convertView;
//		
//		if (view == null){
//			LayoutInflater layoutInflater ;
//			layoutInflater = activity.getLayoutInflater();
//			view = layoutInflater.inflate(R.layout.clomn_row, null);
//			txtFirst=(TextView) view.findViewById(R.id.clomRowWold);
//			txtSecond=(TextView) view.findViewById(R.id.clomnRowTransWorld);
//			txtThird=(TextView) view.findViewById(R.id.clomnRowDate);
//
//		}
//		
//		Item item = (Item) getItem(position);
		
//		if (view != null){
//			txtFirst=(TextView) convertView.findViewById(R.id.clomRowWold);
//			txtSecond=(TextView) convertView.findViewById(R.id.clomnRowTransWorld);
//			txtThird=(TextView) convertView.findViewById(R.id.clomnRowDate);
//			
//		}
//		
//		HashMap<String, String> map=mylist.get(position);
//		txtFirst.setText(map.get(Constant.FIRST_COLUMN));
//		txtSecond.setText(map.get(Constant.SECOND_COLUMN));
//		txtThird.setText(map.get(Constant.THIRD_COLUMN));

        View v = convertView;
         


		HashMap<String, String> hashMap =  (HashMap<String, String>) getItem(position);
		
		if(hashMap != null){
			
			LayoutInflater	inflater=activity.getLayoutInflater();
			convertView=inflater.inflate(R.layout.clomn_row, null);
			
			txtFirst=(TextView) convertView.findViewById(R.id.clomRowWold);
			txtSecond=(TextView) convertView.findViewById(R.id.clomnRowTransWorld);
			txtThird=(TextView) convertView.findViewById(R.id.clomnRowDate);

			
            if (txtFirst != null) {
                txtFirst.setText(hashMap.get((Constant.FIRST_COLUMN)));
            }

            if (txtSecond != null) {
                txtSecond.setText(hashMap.get(Constant.SECOND_COLUMN));
            }

            if (txtThird != null) {
                txtThird.setText(hashMap.get(Constant.THIRD_COLUMN));
            }

			

		}
		

		
		
		return convertView;
	
	}

	public void add(HashMap<String,String> hashMap) {
		mylist.add(hashMap);
		notifyDataSetChanged();
		
	}

}
