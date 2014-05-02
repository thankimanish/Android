/*
 * Copyright (C) 2012 jfrankie (http://www.survivingwithandroid.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.hdik.main.listviewfilter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.hdik.main.R;

public class ColorsAdapter extends ArrayAdapter<BeanColors> implements Filterable {

	private List<BeanColors> planetList;
	private Context context;
	private Filter planetFilter;
	private List<BeanColors> origPlanetList;
	
	public ColorsAdapter(List<BeanColors> planetList, Context ctx) {
		super(ctx, R.layout.img_row_layout, planetList);
		this.planetList = planetList;
		this.context = ctx;
		this.origPlanetList = planetList;
	}
	
	public int getCount() {
		return planetList.size();
	}

	public BeanColors getItem(int position) {
		return planetList.get(position);
	}

	public long getItemId(int position) {
		return planetList.get(position).hashCode();
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		View v = convertView;
		
		PlanetHolder holder = new PlanetHolder();
		
		// First let's verify the convertView is not null
		if (convertView == null) {
			// This a new view we inflate the new layout
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = inflater.inflate(R.layout.img_row_layout, null);
			// Now we can fill the layout with the right values
			TextView tv = (TextView) v.findViewById(R.id.name);
			TextView distView = (TextView) v.findViewById(R.id.dist);

			
			holder.planetNameView = tv;
			
			
			v.setTag(holder);
		}
		else 
			holder = (PlanetHolder) v.getTag();
		
		BeanColors p = planetList.get(position);
		holder.planetNameView.setText(p.getName());
		
		
		return v;
	}

	public void resetData() {
		planetList = origPlanetList;
	}
	
	
	/* *********************************
	 * We use the holder pattern        
	 * It makes the view faster and avoid finding the component
	 * **********************************/
	
	private static class PlanetHolder {
		public TextView planetNameView;
		
	}
	

	
	/*
	 * We create our filter	
	 */
	
	@Override
	public Filter getFilter() {
		if (planetFilter == null)
			planetFilter = new PlanetFilter();
		
		return planetFilter;
	}



	private class PlanetFilter extends Filter {

		
		
		@Override
		protected FilterResults performFiltering(CharSequence constraint) {
			FilterResults results = new FilterResults();
			// We implement here the filter logic
			if (constraint == null || constraint.length() == 0) {
				// No filter implemented we return all the list
				results.values = origPlanetList;
				results.count = origPlanetList.size();
			}
			else {
				// We perform filtering operation
				List<BeanColors> nPlanetList = new ArrayList<BeanColors>();
				
				for (BeanColors p : planetList) {
					if (p.getName().toUpperCase().startsWith(constraint.toString().toUpperCase()))
						nPlanetList.add(p);
					
					}
				
				
				results.values = nPlanetList;
				results.count = nPlanetList.size();

			}
			return results;
		}

		@Override
		protected void publishResults(CharSequence constraint,
				FilterResults results) {
			
			// Now we have to inform the adapter about the new list filtered
//			if (results.count == 0)
//				notifyDataSetInvalidated();
//			else {
				planetList = (List<BeanColors>) results.values;
				notifyDataSetChanged();
//			}
			
		}
		
	}
}
