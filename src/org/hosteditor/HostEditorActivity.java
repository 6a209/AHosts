package org.hosteditor;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;

public class HostEditorActivity extends SherlockActivity{
	
	
	private static final String HOSTS_ADDRESS = "/etc/hosts";
	private ArrayList<String> mItemList;
	private ListView mListView;
	private String mHostContent;
	private static final boolean APPEND = true;
	private static final boolean NOT_APPEND = false;
	private Button mToggleBtn;
	private HostAdapter mAdapter;
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(R.string.add).setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
		menu.add(R.string.debug).setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
		menu.add(R.string.del).setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
		return true;
	}

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //This uses the imported MenuItem from ActionBarSherlock
    	String title = item.getTitle().toString();
    	if(title.equals(getString(R.string.add))){
    		
    	}else if (title.equals(getString(R.string.debug))){
    		
    	}else if(title.equals(getString(R.string.del))){
    		long [] ids = mListView.getCheckItemIds();
    		delItem(ids);
    	}
        return true;
    }
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	setTheme( R.style.Theme_Sherlock);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        try {
			Runtime.getRuntime().exec("su");
			Runtime.getRuntime().exec("mount -o rw,remount -t yaffs2 /dev/block/mtdblock3 /system");
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        mItemList = new ArrayList<String>();
        mListView = (ListView) findViewById(R.id.listview);
        mListView.setItemsCanFocus(false);
        mListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        mToggleBtn = (Button)findViewById(R.id.toggle_btn);
        mToggleBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				long [] ids = mListView.getCheckItemIds();
				toggleItme(ids);
			}
		});
        try {
			Log.d("the host is ", readFile(HOSTS_ADDRESS));
			mHostContent = readFile(HOSTS_ADDRESS);
			initItems(mHostContent);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
    }
    
    private void initItems(String content){
    	String [] list = content.split("\n");
    	for(int i = 0; i < list.length; i++){
    		String str = list[i];
    		if(str.trim().length() > 0){
        		mItemList.add(list[i]);
    		}
    		Log.d("item is  ", list[i]);
    	}
    	mAdapter = new HostAdapter(this, mItemList);
    	mListView.setAdapter(mAdapter);
    }
    
    private void delItem(long[] index){
    	for(int i = 0; i < index.length; i++){
    		String item = mItemList.get((int)index[i]);
    		mHostContent = mHostContent.replace(item, "");	
    	}
    	try {
			writeFile(mHostContent, NOT_APPEND);
			mAdapter.notifyDataSetChanged();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			showToast(getString(R.string.write_file_err));
			return;
		}
    	
    }
    
    
    private void createItem(String newItem){
    	try{
        	writeFile(newItem, APPEND);
        	mAdapter.notifyDataSetChanged();
    	}catch (Exception e) {
    		showToast(getString(R.string.write_file_err));
		}
    }
    
    private void toggleItme(long [] ids){
    	for(int i = 0; i < ids.length; i++){
    		String item = mItemList.get((int)ids[i]);
        	if(item.startsWith("#")){
        		item.replace("#", "");
        	}else{
        		item = "#" + item;
        	}
        	mHostContent = mHostContent.replace(mItemList.get((int)ids[i]), item);		
    	}
    	try{
    		writeFile(mHostContent, NOT_APPEND);
    		mAdapter.notifyDataSetChanged();
    	}catch (Exception e) {
    		e.printStackTrace();
    		showToast(getString(R.string.write_file_err));
		}
    }
    
    private void showToast(String text){
    	Toast.makeText(this, text, Toast.LENGTH_LONG).show();
    }
    
    private String readFile(String path) throws IOException{
        File file = new File(path);
        if(!file.exists() || file.isDirectory()){
            throw new FileNotFoundException();
        }
        FileInputStream fis = new FileInputStream(file);
        byte[] buf = new byte[1024];
        StringBuffer sb = new StringBuffer();
        while((fis.read(buf)) != -1){
            sb.append(new String(buf));   
            buf = new byte[1024];
        }
        return sb.toString();
    }
    
    
    private void writeFile(String content, boolean isAppend) throws IOException{
    	
        File file = new File(HOSTS_ADDRESS);
        if(!file.exists()){
            file.createNewFile();
        }
        FileOutputStream out = new FileOutputStream(file, isAppend);      
        out.write(content.getBytes());
        out.close();
    }
}