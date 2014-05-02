package com.hdik.main.listviewfilter;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.hdik.main.R;

public class ListViewFilterActivity extends Activity {

	// The data to show
	List<BeanColors> colorsList = new ArrayList<BeanColors>();
	ColorsAdapter adapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.listview_filter_activity);
		colorsList.add(new BeanColors("Yellow"));
		colorsList.add(new BeanColors("Red"));
		colorsList.add(new BeanColors("Orange"));
		colorsList.add(new BeanColors("Blue"));
		colorsList.add(new BeanColors("White"));
		colorsList.add(new BeanColors("Black"));
		colorsList.add(new BeanColors("Pink"));

		ListView lv = (ListView) findViewById(R.id.listView);

		adapter = new ColorsAdapter(colorsList, this);
		lv.setAdapter(adapter);
		registerForContextMenu(lv);

		// TextFilter
		lv.setTextFilterEnabled(true);
		EditText editTxt = (EditText) findViewById(R.id.editTxt);

		editTxt.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {

				if (count < before) {
					// We're deleting char so we need to reset the adapter data
					adapter.resetData();
				}

				adapter.getFilter().filter(s.toString());

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {
			}
		});
	}

	// We want to create a context Menu when the user long click on an item
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {

		super.onCreateContextMenu(menu, v, menuInfo);
		AdapterContextMenuInfo aInfo = (AdapterContextMenuInfo) menuInfo;

		// We know that each row in the adapter is a Map
		BeanColors clr = adapter.getItem(aInfo.position);
		menu.setHeaderTitle("Select Action for " + clr.getName());
		menu.add(1, 2, 2, "Remove");

	}

	// This method is called when user selects an Item in the Context menu
	@Override
	public boolean onContextItemSelected(MenuItem item) {

		AdapterContextMenuInfo aInfo = (AdapterContextMenuInfo) item
				.getMenuInfo();
		colorsList.remove(aInfo.position);
		adapter.notifyDataSetChanged();
		return true;
	}

	// Handle user click
	public void addColor(View view) {
		final Dialog d = new Dialog(this);
		d.setContentView(R.layout.dialog);
		d.setTitle("Add Color");
		d.setCancelable(true);

		final EditText edit = (EditText) d.findViewById(R.id.editTextPlanet);
		Button b = (Button) d.findViewById(R.id.button1);
		b.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				String planetName = edit.getText().toString();
				ListViewFilterActivity.this.colorsList.add(new BeanColors(
						planetName));
				ListViewFilterActivity.this.adapter.notifyDataSetChanged();
				d.dismiss();
			}
		});

		d.show();
	}

}
