package com.suxsem.geekstore.activities;

import com.suxsem.geekstore.R;
import com.suxsem.geekstore.business.SectionsPagerAdapter;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.design.widget.TabLayout;
import android.os.Bundle;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

public class ContainerActivity extends AppCompatActivity {

	// invoked when the activity is created or recreated
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);

		// check Internet connection by querying the Android connectivity service 
		
		ConnectivityManager cm =
				(ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = cm.getActiveNetworkInfo();
		
		if ( netInfo == null || !netInfo.isConnectedOrConnecting()) {
			
			// Internet connection is not available, show a toast and exit from app
			
			Toast.makeText(this, "Internet non disponibile", Toast.LENGTH_LONG).show();
			finish();
			return;
			
		}

		// load the activity layout
		
		setContentView(R.layout.activity_container);

		// obtain the Toolbar widget and set it as an Actionbar
		
	    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);	    	    
	    setSupportActionBar(toolbar);
	    
        // get the ViewPager so that it can display fragments and attach a new pager adapter to delegate fragment management
	    
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);               
        FragmentPagerAdapter pagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(), this);                
        viewPager.setAdapter(pagerAdapter);

        // bind the v to the TabLayout
        
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager);
        
	}

}
