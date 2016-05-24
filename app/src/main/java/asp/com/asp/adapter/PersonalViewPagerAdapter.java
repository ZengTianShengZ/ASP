package asp.com.asp.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

import asp.com.asp.activity.PersonaQiangActivity;
import asp.com.asp.fragment.PersonQiangFragmentDg;
import asp.com.asp.fragment.PersonalQiangFragment;

public class PersonalViewPagerAdapter extends FragmentPagerAdapter {

    final int PAGE_COUNT = 2;
    private String tabTitles[] = new String[]{"二手", "代购"};
    private Context context;
    private List<Fragment> mFragmentList = new ArrayList<Fragment>();
    private PersonalQiangFragment mPersonalQiangFragment;
    private PersonQiangFragmentDg mPersonQiangFragmentDg;

    public PersonalViewPagerAdapter(FragmentManager fm, PersonaQiangActivity personaQiangActivity) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                if(mPersonalQiangFragment==null){
                    mPersonalQiangFragment = new PersonalQiangFragment();
                return  mPersonalQiangFragment;
                }else{
                    return mPersonalQiangFragment;
                }
            case 1:
                if(mPersonQiangFragmentDg==null){
                    mPersonQiangFragmentDg = new PersonQiangFragmentDg();
                    return mPersonQiangFragmentDg;
                }else{
                    return mPersonQiangFragmentDg;
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