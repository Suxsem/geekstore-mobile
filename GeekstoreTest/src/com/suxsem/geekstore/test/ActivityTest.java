package com.suxsem.geekstore.test;

import com.suxsem.geekstore.activities.ContainerActivity;
import android.test.ActivityInstrumentationTestCase2;

public class ActivityTest extends ActivityInstrumentationTestCase2<ContainerActivity> {

	private ContainerActivity mActivity;

	public ActivityTest(String name) {
		super(ContainerActivity.class);
		setName(name);
	}

	protected void setUp() throws Exception {
		super.setUp();
		mActivity = getActivity();
	}

	protected void tearDown() throws Exception {		
		super.tearDown();
		if(mActivity != null) {
			mActivity.finish();
			mActivity = null;
		}
	}

	public final void testActivityStarted() {
		assertNotNull(mActivity);
		assertTrue(mActivity instanceof ContainerActivity);
	}

}
