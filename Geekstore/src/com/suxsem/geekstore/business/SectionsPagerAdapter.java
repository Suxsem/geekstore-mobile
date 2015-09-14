package com.suxsem.geekstore.business;

import java.util.Locale;

import com.suxsem.geekstore.fragments.OrdersFragment;
import com.suxsem.geekstore.fragments.StoresFragment;
import com.suxsem.geekstore.R;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

public class SectionsPagerAdapter extends FragmentPagerAdapter {

	private Context context; 
	private OrdersFragment ordersFragment;
	private StoresFragment storesFragment;

	// resources array to hold tab icons
	
	private int[] imageResId = {
	        R.drawable.ic_store_white_18dp,
	        R.drawable.ic_cart_outline_white_18dp	        
	};
		
	public SectionsPagerAdapter(FragmentManager fm, Context c) {
		super(fm);		
		context = c;
	}

	// get the correct fragment according to the selected tab (just only two tabs)
	// if a fragment is already instantiated, don't recreate it
	//	(useful when more then two tabs are present: up to a fragment is cached by the Android framework) 
	
	@Override
	public Fragment getItem(int position) {
		switch (position) {
		case 0:
			return storesFragment == null ? storesFragment = StoresFragment.newInstance() : storesFragment;
		case 1:
			return ordersFragment == null ? ordersFragment = OrdersFragment.newInstance() : ordersFragment;
		default:
			return null;
		}		
	}

	// get the total number of fragments
	
	@Override
	public int getCount() {
		return 2;
	}

	// get fragment title from string resources according to the selected tab
	
	private String getTitle(int position) {
		Locale l = Locale.getDefault();
		switch (position) {
		case 0:
			return context.getString(R.string.title_section1).toUpperCase(l);
		case 1:
			return context.getString(R.string.title_section2).toUpperCase(l);
		default:
			return "";
		}		
	}
	
	// get fragment header (icon + title) according to the selected tab
	
	@Override
	public CharSequence getPageTitle(int position) {

		// we use a SpannableString object that can hold drawable objects
		
		Drawable image = ContextCompat.getDrawable(context, imageResId[position]);
	    image.setBounds(0, 0, image.getIntrinsicWidth(), image.getIntrinsicHeight());
	    SpannableString sb = new SpannableString("   " + getTitle(position));
	    ImageSpan imageSpan = new ImageSpan(image, ImageSpan.ALIGN_BOTTOM);
	    sb.setSpan(imageSpan, 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
	    return sb;
	}
}