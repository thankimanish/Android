package com.hdik.main.tabfragment.thirdtab;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

import com.hdik.main.R;
import com.hdik.main.tabfragment.main.BaseFragment;
import com.hdik.main.tabfragment.utils.Const;




public class Tab3FirstScreen extends BaseFragment implements OnClickListener {
	private Button btnNext;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.tab3_firstscreen,
				container, false);

		btnNext = (Button) view.findViewById(R.id.btnNext);
		btnNext.setOnClickListener(this);
		System.out.println("replace");
		return view;
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		fragmentTabActivity.addFragments(Const.TAB_THIRD,
				new Tab3SecondScreen(), true, true);
	}

}
