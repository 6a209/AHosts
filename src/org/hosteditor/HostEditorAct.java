package org.hosteditor;

import android.content.Intent;
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
	
	private Button mDelAllBtn;
	
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(R.string.add)
			.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
		menu.add(R.string.debug)
			.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
		menu.add(R.string.select_all).setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
		return true;
	}

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //This uses the imported MenuItem from ActionBarSherlock
    	String title = item.getTitle().toString();
    	if(title.equals(getString(R.string.add))){
    		toAddAct();
    	}else if (title.equals(getString(R.string.debug))){

    	}else if(title.equals(getString(R.string.select_all))){
    		item.setTitle(R.string.cancle_select_all);
    		selectAll();
    	}else if(title.equals(getString(R.string.cancle_select_all))){
    		item.setTitle(R.string.select_all);
    		cancelSelectAll();
    	}
        return true;
    }
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	setTheme(R.style.Theme_Sherlock);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        mApi = new HostApi();
        if(!mApi.checkRoot()){
        	
        }
        mListView = (ListView) findViewById(R.id.listview);
        mListView.setItemsCanFocus(false);
        mListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        mToggleBtn = (Button)findViewById(R.id.toggle_btn);
        mDelAllBtn = (Button)findViewById(R.id.del_btn);
        mToggleBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				long [] ids = mListView.getCheckedItemIds();
				toggleItme(ids);
			}
		});
        
        mDelAllBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				long [] ids = mListView.getCheckedItemIds();
				delItem(ids);
			}
		});
        initItems();
    }
    
    @Override
    public void onActivityResult(int reqCode, int resultCode, Intent data){
    	if(RESULT_OK == resultCode){
    		if(Const.REQ_ADD_CODE == reqCode){
    			initItems();
    		}
    	}
    }
    
    
    private void initItems(){
    	mApi.init();
    	mAdapter = new HostAdapter(this, mApi.getItemList());
    	mListView.setAdapter(mAdapter);
    }

    private void toAddAct(){
    	Intent intent = new Intent(this, AddItemAct.class);
    	startActivityForResult(intent, Const.REQ_ADD_CODE);
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
    
    private void selectAll(){
    	for(int i = 0; i < mAdapter.getCount(); i++){
    		mListView.setItemChecked(i, true);
    	}
    }
    
    private void cancelSelectAll(){
    	for(int i = 0; i < mAdapter.getCount(); i++){
    		mListView.setItemChecked(i, false);
    	}
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
