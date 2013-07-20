package org.hosteditor;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import android.util.Log;


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
		for(int i = 0; i < mItemList.size(); i++){
			mItemList.remove(i);
		}
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
        	String mountCmd = "mount -o rw,remount -t " + mFileType + mFileSystemName + " /system\n";
        	Log.d("the mount cmd is => ", mountCmd);
			localDataOutputStream.writeBytes(mountCmd);
	        localDataOutputStream.writeBytes("echo '' > /system/etc/hosts\n");
	        localDataOutputStream.writeBytes("exit\n");
	        localDataOutputStream.flush();
//			for(String item : mItemList){
//		        localDataOutputStream.wrijteBytes("echo " + item + ">> /system/etc/hosts\n");
//			}
//	        localDataOutputStream.writeBytes("exit\n");
//	        localDataOutputStream.flush();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	
	
	/**
	 * get the mount argment
	 */
	private void initArgv(){
		String line = "";
		try {
			BufferedReader buffered = new BufferedReader(new InputStreamReader(Runtime
					.getRuntime().exec("cat /proc/mounts")
					.getInputStream()));
			while(null != (line = buffered.readLine())){
				if(line.contains("system")){
					break;
				}
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (line != null) {
			String[] argvs = line.split(" ");
			if (argvs.length >= 3) {
				mFileSystemName = argvs[0];
				mFileType = argvs[2];
			}
		}
	}
}