package com.hdik.main.listviewmultichoice;

import java.util.ArrayList;
import java.util.Collections;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.hdik.main.R;
import com.hdik.main.listviewmultichoice.adapter.MyAdapter;
import com.hdik.main.listviewmultichoice.bean.DummyData;

public class ListViewMultiChoice extends Activity implements OnClickListener {

	DummyData beanData;
	ArrayList<DummyData> list = new ArrayList<DummyData>();
	MyAdapter adapter;
	ListView listView;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.listview_multichoice);
		Button btn = (Button) findViewById(R.id.btnAdd);
		listView = (ListView) findViewById(R.id.listView);
		Button btnDel = (Button) findViewById(R.id.btnDel);

		btn.setOnClickListener(this);
		btnDel.setOnClickListener(this);
		adapter = new MyAdapter(ListViewMultiChoice.this, list);

		listView.setAdapter(adapter);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnAdd:
			beanData = new DummyData();
			EditText edit = (EditText) findViewById(R.id.txtItem);
			beanData.setTitle(edit.getText().toString());
			list.add(beanData);
			edit.setText("");
			adapter.notifyDataSetChanged();
			break;
		case R.id.btnDel:
			ArrayList<Integer> selectedItems = new ArrayList<Integer>();

			for (int i = 0; i < list.size(); i++) {
				if (list.get(i).isChecked())
					selectedItems.add(i);
			}

			Collections.sort(selectedItems, Collections.reverseOrder());
			for (int i : selectedItems) {

				list.remove(i);

			}
			adapter.notifyDataSetChanged();

			break;
		}

	}
}