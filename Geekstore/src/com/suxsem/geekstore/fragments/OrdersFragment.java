package com.suxsem.geekstore.fragments;

import com.suxsem.geekstore.R;
import com.suxsem.geekstore.business.LoginObserver;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class OrdersFragment extends Fragment {

	// returns a new instance of this fragment
	
	public static OrdersFragment newInstance() {
		OrdersFragment fragment = new OrdersFragment();
		return fragment;
	}

	public OrdersFragment() {
	}

	// invoked when the fragment is firstly created
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_orders, container, false);
		
		// attach a LoginObserver listener to the login button to implement the observer pattern
		
		rootView.findViewById(R.id.loginBtn).setOnClickListener(new LoginObserver(this));
		
		return rootView;
	}
}