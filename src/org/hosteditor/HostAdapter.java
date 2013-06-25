package org.hosteditor;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Paint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckedTextView;

public class HostAdapter extends BaseAdapter{

	// ip patter
	private String ipPatter = "\\d{1,2}|1\\d\\d|2[0-4]\\d|25[0-5]";
	private Context mCtx;
	private List<String> mHostList;
	public HostAdapter(Context ctx, List<String> list){
		mCtx = ctx;
		mHostList = list;
	}
	
	@Override
	public int getCount() {
		
		return mHostList.size();
	}

	@Override
	public Object getItem(int position) {
		return mHostList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		CheckedTextView checkView = null;
		if(null == convertView){
			LayoutInflater inflater = LayoutInflater.from(mCtx);
			convertView = inflater.inflate(android.R.layout.simple_list_item_multiple_choice, null);
			checkView = (CheckedTextView)convertView.findViewById(android.R.id.text1);
			checkView.setTextSize(14);
//			((CheckedTextView)convertView)
		}else{
			checkView = (CheckedTextView)convertView.getTag();
		}
		String item = mHostList.get(position);
		if(item.contains("#")){
			checkView.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
		}
		checkView.setText(item);
		convertView.setTag(checkView);
		return convertView;
	}
    	
}
