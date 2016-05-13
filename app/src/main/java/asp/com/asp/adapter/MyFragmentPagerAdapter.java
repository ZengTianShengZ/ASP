package asp.com.asp.adapter;

import java.util.ArrayList;
import java.util.List;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;


public class MyFragmentPagerAdapter extends FragmentPagerAdapter {

	private List<Fragment> mFragmentList = new ArrayList<Fragment>();
	
	public MyFragmentPagerAdapter(FragmentManager fm,List<Fragment> mFragmentList) {
		super(fm);
		this.mFragmentList = mFragmentList;
	}

	@Override
	public Fragment getItem(int position) {
		// TODO Auto-generated method stub
		return mFragmentList.get(position);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mFragmentList.size();
	}
 

}
