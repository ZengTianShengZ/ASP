package asp.com.asp.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

import asp.com.asp.fragment.PersonQiangFragmentDg;
import asp.com.asp.fragment.PersonalMeDgFragment_;
import asp.com.asp.fragment.PersonalMeFragment_;
import asp.com.asp.fragment.PersonalQiangFragment;

public class PersonalMeViewPagerAdapter extends FragmentPagerAdapter {

    final int PAGE_COUNT = 2;
    private String tabTitles[] = new String[]{"二手", "代购"};
    private Context context;
    private List<Fragment> mFragmentList = new ArrayList<Fragment>();
    private PersonalMeDgFragment_ mPersonalMeDgFragment;
    private PersonalMeFragment_ mPersonalMeFragment;

    public PersonalMeViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                if(mPersonalMeFragment==null){
                    mPersonalMeFragment = new PersonalMeFragment_();
                return  mPersonalMeFragment;
                }else{
                    return mPersonalMeFragment;
                }
            case 1:
                if(mPersonalMeDgFragment==null){
                    mPersonalMeDgFragment = new PersonalMeDgFragment_();
                    return mPersonalMeDgFragment;
                }else{
                    return mPersonalMeDgFragment;
                }

        }
           return null;
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }
}