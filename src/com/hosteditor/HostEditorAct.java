package com.hosteditor;

import org.hosteditor.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;

public class HostEditorAct extends SherlockFragmentActivity{
	
	
	private ListView mListView;
	private Button mToggleBtn;
	private HostAdapter mAdapter;
	private HostApi mApi;
	private Fragment mAddItemFragment;
	
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
    	setTheme(R.style.Theme_Sherlock);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        mAddItemFragment = (AddItemFragment)getSupportFragmentManager().findFragmentById(R.id.add_item);
        mApi = new HostApi();
        if(!mApi.checkRoot()){
        	
        }
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
    }
    
    private void initItems(){
    	mApi.init();
    	mAdapter = new HostAdapter(this, mApi.getItemList());
    	mListView.setAdapter(mAdapter);
    }
    
    private void delItem(long[] ids){
    	mApi.del(convert2intArray(ids));
    	mAdapter.notifyDataSetChanged();
    }
    
    
    public void createItem(String newItem){
    	try{
    		mApi.append(newItem);
        	mAdapter.notifyDataSetChanged();
    	}catch (Exception e) {
    		showToast(getString(R.string.write_file_err));
		}
    }
    
    private void toggleItme(long [] ids){
    	mApi.toogle(convert2intArray(ids));
    }
    
    private int[] convert2intArray(long [] ids){
    	int [] pos = new int [ids.length];
    	for(int i = 0; i < ids.length; i++){
    		pos[i] = (int)ids[i];
    	}
    	return pos;
    }
    
    private void showToast(String text){
    	Toast.makeText(this, text, Toast.LENGTH_LONG).show();
    }
    
}
