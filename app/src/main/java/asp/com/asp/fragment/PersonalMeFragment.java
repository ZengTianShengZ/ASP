package asp.com.asp.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.androidannotations.annotations.EFragment;

import asp.com.asp.R;

/**
 * Created by Administrator on 2016/5/25.
 */
@EFragment
public class PersonalMeFragment extends Fragment{
    private Context mContext;
    private View mRootview;
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootview = inflater.inflate(R.layout.fragment_personal_qiang, container, false);

        mContext = getActivity();

        return mRootview;
    }


}
