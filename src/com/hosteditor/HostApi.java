package com.hosteditor;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;


public class HostApi {
	
	private String mFileType;
	private String mFileSystemName;
	private List<String> mItemList;
	private static final String PATH = "/etc/hosts";
	private Process mProcess;
	
	public HostApi(){
		mItemList = new ArrayList<String>();
	}
	
	public boolean checkRoot(){
		try {
			mProcess = Runtime.getRuntime().exec("su");
		} catch (IOException e) {
			return false;
		}
		return true;
	}
	
	public void init(){
		initArgv();
		readHost();
	}
	
	public List<String> getItemList(){
		return mItemList;
	}
	
	public void append(String item){
		mItemList.add(item);
		writeHost();
	}
	
	public void toogle(int [] ids){
		for(int i = 0; i < ids.length; i++){
			String item = mItemList.get(i);
			if(item.startsWith("#")){
	    		item.replace("#", "");
	    	}else{
	    		item = "#" + item;
	    	}
			mItemList.remove(i);
			mItemList.add(i, item);
		}
		writeHost();
	
	}
	
	public void del(int [] idx){
		mItemList.remove(idx);
		writeHost();
	}
	
	private void readHost(){
        FileReader fr;
        BufferedReader br;
		try {
			fr = new FileReader(PATH);
	        br = new BufferedReader(fr);
	        String line = "";
	        try {
				while((line = br.readLine()) != null){
				   mItemList.add(line);
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    
	}
	
	private void writeHost(){
		DataOutputStream localDataOutputStream = new DataOutputStream(mProcess.getOutputStream());
        try {
			localDataOutputStream.writeBytes("mount -o rw,remount -t " + mFileType + mFileSystemName + " /system\n");
	        localDataOutputStream.writeBytes("echo '' > /system/etc/hosts\n");
			for(String item : mItemList){
		        localDataOutputStream.writeBytes("echo " + item + ">> /system/etc/hosts\n");
			}
	        localDataOutputStream.writeBytes("exit\n");
	        localDataOutputStream.flush();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	
	
	/**
	 * get the mount argment
	 */
	private void initArgv(){
		String str = "";
		try {
			str = new BufferedReader(new InputStreamReader(Runtime
					.getRuntime().exec("cat /proc/mounts | grep /system")
					.getInputStream())).readLine();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (str != null) {
			String[] arrayOfString2 = str.split(" ");
			if (arrayOfString2.length >= 3) {
				mFileSystemName = arrayOfString2[0];
				mFileType = arrayOfString2[2];
			}
		}
	}
}