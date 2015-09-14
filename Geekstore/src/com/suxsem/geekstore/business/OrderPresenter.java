package com.suxsem.geekstore.business;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.suxsem.geekstore.R;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class OrderPresenter {

	private Fragment fragment;

	// instantiate the object with the fragment that will host the orders

	public OrderPresenter (Fragment fragment) {
		this.fragment = fragment;
	}

	public void present(String source) {
		View view = fragment.getView();
		if (source == null) {

			// if no source is passed, show a short snackbar to the user and return;

			Snackbar.make(view, "AUTENTICAZIONE NON RIUSCITA", Snackbar.LENGTH_SHORT).show();

			return;
		}

		// hide the login form and show the (still blank) orders list

		view.findViewById(R.id.login).setVisibility(View.GONE);
		view.findViewById(R.id.orders).setVisibility(View.VISIBLE);

		try {

			// create a new JSONArray object starting from the source String

			JSONArray orders = new JSONArray(source);

			if (orders.length() == 0) {

				// if no orders is found, show a short snackbar to the user

				Snackbar.make(view, "NESSUN ORDINE TROVATO", Snackbar.LENGTH_SHORT).show();

			} else {

				// get the layout inflater service and find the orders LinearLayout

				LayoutInflater inflater = (LayoutInflater)fragment.getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				LinearLayout orders_view = ((LinearLayout) view.findViewById(R.id.orders));

				// cycle through all orders to parse and show them

				for (int i = 0; i < orders.length(); i++) {
					View order_view = inflater.inflate(R.layout.order, orders_view, false);

					// get the i-order
					JSONObject order = orders.getJSONObject(i);

					// format and show the order date to dd/MM/yyyy
					Date created_at = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(order.getString("created_at").substring(0, 10));
					SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
					((TextView) order_view.findViewById(R.id.created_at)).setText(dateFormat.format(created_at));

					// format and show the product price, adding the discount (if present)
					JSONObject product = order.getJSONObject("product");
					String product_text = product.getString("name") + "\n" + product.getString("price") + "€";
					int discount = product.getInt("discount");
					if (discount > 0)
						product_text = product_text + " (-" + discount + "%)";
					((TextView) order_view.findViewById(R.id.product)).setText(product_text);

					// cycle through all order upgrades						
					JSONArray upgrades = order.getJSONArray("upgrades");
					if (upgrades.length() > 0) {
						String upgrades_text = "";
						for (int j = 0; j < upgrades.length(); j++) {
							JSONObject upgrade = upgrades.getJSONObject(j);
							// format the upgrade name and price
							if (j > 0)
								upgrades_text += "\n";
							upgrades_text = upgrades_text + upgrade.getString("name") + "\n" + upgrade.getString("price") + "€";
						}
						// show the upgrades
						((TextView) order_view.findViewById(R.id.upgrades)).setText(upgrades_text);
					}

					// show the order price
					((TextView) order_view.findViewById(R.id.price)).setText(orders.getJSONObject(i).getString("price") + "€");

					// show the order status
					((TextView) order_view.findViewById(R.id.status)).setText(orders.getJSONObject(i).getString("status"));
					orders_view.addView(order_view);
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}				

	}

}
