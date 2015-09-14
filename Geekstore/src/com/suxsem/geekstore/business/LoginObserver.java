package com.suxsem.geekstore.business;


import com.suxsem.geekstore.R;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

public class LoginObserver implements OnClickListener {

	private Fragment fragment;

	// instantiate the object with the fragment that hosts the login button
	
	public LoginObserver(Fragment fragment) {
		this.fragment = fragment;
	}
	
	// invoked when the login button is pressed (thanks to the observer design pattern)

	@Override
	public void onClick(View v) {
		
		// start the JsonParser AsyncTask passing the username and password provided by the user
		
		View view = fragment.getView();
		String user = ((EditText) view.findViewById(R.id.user)).getText().toString().trim();
		String password = ((EditText) view.findViewById(R.id.password)).getText().toString().trim();

		new JsonParser().execute(user, password);

	}

	private class JsonParser extends AsyncTask<String, Void, String> {
		private ProgressDialog pDialog;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			
			// hide the virtual keyboard
			
			AppCompatActivity activity = (AppCompatActivity) fragment.getActivity(); 
			View view = activity.getCurrentFocus();
			if (view != null) {  
				InputMethodManager imm = (InputMethodManager)activity.getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
			}
			
			// show a ProgressDialog to block the user interaction
			
			pDialog = new ProgressDialog(fragment.getActivity());
			pDialog.setMessage("Recupero gli ordini...");
			pDialog.setIndeterminate(true);
			pDialog.setCancelable(false);
			pDialog.show();
		}

		@Override
		protected String doInBackground(String... cred) {
			
			// create a new JsonDownloader object and query the main server for orders

			JsonDownloader jsonDownloader = new JsonDownloader();
			return jsonDownloader.getJson("https://geekstore-suxsem.herokuapp.com/orders.json",
					cred[0], cred[1]);
		}

		@Override
		protected void onPostExecute(String response) {
			
			// create a new OrderPresenter to visually format the response, then dismiss the waiting dialog
			
			new OrderPresenter(fragment).present(response);
			pDialog.dismiss();
			
		}
	}	

}
