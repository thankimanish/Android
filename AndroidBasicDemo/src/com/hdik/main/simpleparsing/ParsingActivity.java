package com.hdik.main.simpleparsing;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;

import com.hdik.main.R;
import com.hdik.main.common.utils.AndyConstants;
import com.hdik.main.common.utils.AndyUtils;
import com.hdik.main.common.wrapper.AsyncTaskCompleteListener;
import com.hdik.main.common.wrapper.ParsingController;
import com.hdik.main.simpleparsing.adapter.ContactAdapter;
import com.hdik.main.simpleparsing.adapter.ItemsAdapter;
import com.hdik.main.simpleparsing.beans.BeanContact;
import com.hdik.main.simpleparsing.beans.BeanItems;
import com.hdik.main.simpleparsing.parser.ParseContent;

public class ParsingActivity extends Activity implements
		AsyncTaskCompleteListener, OnClickListener {
	private Button btnJson, btnXml;
	private HashMap<String, String> map;
	private ArrayList<BeanContact> listContact;
	private ArrayList<BeanItems> listItems;
	private ListView listView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		btnJson = (Button) findViewById(R.id.btnJson);
		btnXml = (Button) findViewById(R.id.btnXml);
		listView = (ListView) findViewById(R.id.listViewItems);
		btnJson.setOnClickListener(this);
		btnXml.setOnClickListener(this);
		listContact = new ArrayList<BeanContact>();
		listItems = new ArrayList<BeanItems>();

	}

	@Override
	public void onTaskCompleted(String response, int service_code) {
		// TODO Auto-generated method stub
		AndyUtils.removeSimpleProgressDialog();
		AndyUtils.removeCustomeProgressDialog();
		if (response != null) {
			System.out.println(response);
			switch (service_code) {
			case 1:
				listContact.clear();
				new ParseContent().parseJson(response, listContact);
				listView.setAdapter(new ContactAdapter(ParsingActivity.this,
						listContact));
				break;
			case 2:
				listItems.clear();
				new ParseContent().parseXmlUsingSax(response, listItems);
				listView.setAdapter(new ItemsAdapter(ParsingActivity.this,
						listItems));
				break;
			}
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
//		AndyUtils.showSimpleProgressDialog(this, null, "Loading...", false);
		AndyUtils.showCustomeProgressDialog(this, "Loading...", true);
		switch (v.getId()) {
		case R.id.btnJson:
			map = new HashMap<String, String>();
			map.put(AndyConstants.Param.PARAM_URL, AndyConstants.SERVER_URL);
			// map.put("north", "44.1");
			// map.put("south", "9.9");
			new ParsingController(this, map, 1);
			break;

		case R.id.btnXml:
			map = new HashMap<String, String>();
			map.put(AndyConstants.Param.PARAM_URL, AndyConstants.SERVER_URL_XML);
			// map.put("username", "demo");
			// map.put("lang", "de");
			new ParsingController(this, map, 2);
			break;
		}

	}

}
