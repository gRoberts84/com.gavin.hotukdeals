package com.gavin.hotukdeals;

import java.net.URI;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;
import android.widget.Toast;

public class HotUKDeals extends Activity {
	private DataHelper dh = null;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        dh = new DataHelper(this);
        
        TabHost tabHost = (TabHost)findViewById(R.id.tabHost);
        tabHost.setOnTabChangedListener(new OnTabChangeListener(){
        	public void onTabChanged(String tabId) {
        		ProgressDialog pd = ProgressDialog.show(HotUKDeals.this, "Please wait", "Loading deals");
        		List<Deal> items = dh.getResults("1a18b264b07c3af391b0f34a58e4fe82", "all", "all", true, tabId, 1, false, "");
        		populateData(items, tabId);
        		pd.dismiss();
        	}
        });
        tabHost.setup();
        
    	ListView lv = (ListView)findViewById(R.id.list_hot);
    	lv.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				Deal d = (Deal)arg0.getItemAtPosition(arg2);
				goDeal(d);
			}
    	});
    	lv = (ListView)findViewById(R.id.list_new);
    	lv.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				Deal d = (Deal)arg0.getItemAtPosition(arg2);
				goDeal(d);
			}
    	});
    	lv = (ListView)findViewById(R.id.list_discussed);
    	lv.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				Deal d = (Deal)arg0.getItemAtPosition(arg2);
				goDeal(d);
			}
    	});
    	lv = (ListView)findViewById(R.id.list_custom);
    	lv.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				Deal d = (Deal)arg0.getItemAtPosition(arg2);
				goDeal(d);
			}
    	});
        
        TabSpec spec = null;
        
        spec = tabHost.newTabSpec(this.getString(R.string.tab_hot_key)).
        setIndicator(this.getString(R.string.tab_hot)).
        setContent(R.id.tab_hot);
        tabHost.addTab(spec);

        spec = tabHost.newTabSpec(this.getString(R.string.tab_new_key)).
        setIndicator(this.getString(R.string.tab_new)).
        setContent(R.id.tab_new);
        tabHost.addTab(spec);

        spec = tabHost.newTabSpec(this.getString(R.string.tab_discussed_key)).
        setIndicator(this.getString(R.string.tab_discussed)).
        setContent(R.id.tab_discussed);
        tabHost.addTab(spec);

        spec = tabHost.newTabSpec(this.getString(R.string.tab_custom_key)).
        setIndicator(this.getString(R.string.tab_custom)).
        setContent(R.id.tab_custom);
        tabHost.addTab(spec);
    }
    
    private void goDeal(Deal d)
    {
    	Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(d.MobileDealLink));
    	startActivity(i);
    }
    
    private void populateData(List<Deal> deals, String tabId)
    {
    	ListView lv = null;
    	
    	if(tabId == this.getString(R.string.tab_hot_key))
    		lv = (ListView)findViewById(R.id.list_hot);
    	if(tabId == this.getString(R.string.tab_new_key))
    		lv = (ListView)findViewById(R.id.list_new);
    	if(tabId == this.getString(R.string.tab_discussed_key))
    		lv = (ListView)findViewById(R.id.list_discussed);
    	if(tabId == this.getString(R.string.tab_custom_key))
    		lv = (ListView)findViewById(R.id.list_custom);

    	lv.setAdapter(new ArrayAdapter<Deal>(this, R.layout.deal, deals));
    }
}