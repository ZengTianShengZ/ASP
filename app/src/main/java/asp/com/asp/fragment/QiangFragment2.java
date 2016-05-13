package asp.com.asp.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.androidannotations.annotations.EFragment;

import asp.com.asp.R;

/**
 * Created by Administrator on 2016/5/12.
 */

@EFragment
public class QiangFragment2  extends Fragment {
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_me, container, false);
        return view;
    }
    public static QiangFragment2 newInstance(int position) {
        // TODO 自动生成的方法存根
        QiangFragment2 fragment = new QiangFragment2();
        Bundle bundle = new Bundle();
        bundle.putInt("page", position);
        fragment.setArguments(bundle);
        return fragment;
    }
}