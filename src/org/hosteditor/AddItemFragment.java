package org.hosteditor;

import org.hosteditor.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

/**
 * add item
 * @author 6a209
 * Jun 25, 2013
 */
public class AddItemFragment extends Fragment{
	
	private EditText mIpEt;
	private EditText mHostEt;
	private EditText mAliasIpEt;
	
	@Override
	public void onCreate(Bundle bundle){
		super.onCreate(bundle);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle bundle){
		View view = inflater.inflate(R.layout.add_host, container, false);
		mIpEt = (EditText)view.findViewById(R.id.ip);
		mHostEt = (EditText)view.findViewById(R.id.host);
		mAliasIpEt = (EditText)view.findViewById(R.id.alias_ip);
		return view;
	}
	
	public String getIp(){
		return mIpEt.getText().toString();
	}
	
	public String getHost(){
		return mHostEt.getText().toString();
	}
	
	public String getAliasIp(){
		return mAliasIpEt.getText().toString();
	}
	
	
}