package com.hosteditor;

import org.hosteditor.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;


public class AddItemFragment extends Fragment{
	
	private EditText mIpEt;
	private EditText mHostEt;
	private Button mSaveBtn;
	
	@Override
	public void onCreate(Bundle bundle){
		
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle bundle){
		View view = inflater.inflate(R.layout.add_host, container);
		mIpEt = (EditText)view.findViewById(R.id.ip);
		mHostEt = (EditText)view.findViewById(R.id.host);
		mSaveBtn = (Button)view.findViewById(R.id.save_btn);
		mSaveBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				HostEditorAct act = (HostEditorAct)getActivity();
				String ip = mIpEt.getText().toString();
				String host = mHostEt.getText().toString();
				act.createItem(ip + " " + host);
			}
		});
		return view;
	}
	
	
}