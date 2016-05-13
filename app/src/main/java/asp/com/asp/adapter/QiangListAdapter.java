package asp.com.asp.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import asp.com.appbase.adapter.BaseListAdapter;
import asp.com.asp.domain.QiangItem;

/**
 * Created by Administrator on 2016/5/13.
 */
public class QiangListAdapter extends BaseListAdapter<QiangItem> {

    public QiangListAdapter(Context context, List<QiangItem> list) {
        super(context, list);
    }

    @Override
    public View bindView(int position, View convertView, ViewGroup parent) {
        return null;
    }
}
