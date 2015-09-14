package com.suxsem.geekstore.test;

import com.suxsem.geekstore.R;
import com.suxsem.geekstore.activities.ContainerActivity;

import android.support.v4.app.Fragment;
import android.test.ActivityInstrumentationTestCase2;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

public class OrdersTest extends ActivityInstrumentationTestCase2<ContainerActivity> {

	private ContainerActivity mActivity;
	private Fragment fragment;

	public OrdersTest(String name) {
		super(ContainerActivity.class);
		setName(name);		
	}

	protected void setUp() throws Exception {
		super.setUp();
		mActivity = getActivity();
		fragment = mActivity.getSupportFragmentManager().findFragmentByTag(
				"android:switcher:"+R.id.viewpager+":1");
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) { 
			e.printStackTrace();
		}		
	}

	protected void tearDown() throws Exception {		
		super.tearDown();
		if(mActivity != null) {
			mActivity.finish();
			mActivity = null;
		}
	}

	public void testLoginObserver() {		
		assertNotNull(fragment);
	}

	public void testOnClickBadCred() {
		final View view = fragment.getView();
		LinearLayout orders = ((LinearLayout) view.findViewById(R.id.orders));
		mActivity.runOnUiThread(new Runnable() {
			public void run() {		
				((Button) view.findViewById(R.id.loginBtn)).performClick();
			}
		});
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) { 
			e.printStackTrace();
		}
		assertTrue(orders.getVisibility() == View.GONE);
	}

	public void testOnClickGoodred() {
		final View view = fragment.getView();
		LinearLayout orders = ((LinearLayout) view.findViewById(R.id.orders));
		mActivity.runOnUiThread(new Runnable() {
			public void run() {
				((EditText) view.findViewById(R.id.user)).setText("user");
				((EditText) view.findViewById(R.id.password)).setText("password");				
				((Button) view.findViewById(R.id.loginBtn)).performClick();
								
			}
		});
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) { 
			e.printStackTrace();
		}
		assertTrue(orders.getVisibility() == View.VISIBLE);
	}

}
