package org.hosteditor;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.MenuItem.OnMenuItemClickListener;


/**
 * add a new item activity
 * @author 6a209
 * Jun 25, 2013
 */
public class AddItemAct extends SherlockFragmentActivity{
	
	private AddItemFragment mAddItemFragment;
	
	@Override 
	public void onCreate(Bundle bundle){
		super.onCreate(bundle);
		mAddItemFragment = new AddItemFragment();
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
    	ft.add(android.R.id.content, mAddItemFragment).commit();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(R.string.save)
			.setOnMenuItemClickListener(new OnMenuItemClickListener() {
				@Override
				public boolean onMenuItemClick(MenuItem item) {
					String ip = mAddItemFragment.getIp();
					String host = mAddItemFragment.getHost();
					String aliasName = mAddItemFragment.getAliasIp();
					Intent intent = new Intent();
					intent.putExtra(Const.IP_KEY, ip);
					intent.putExtra(Const.HOST_KEY, host);
					intent.putExtra(Const.ALIAS_KEY, aliasName);
					setResult(RESULT_OK, intent);
					return false;
				}
			})
			.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
		return true;
	}
}