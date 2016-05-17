package asp.com.asp.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.List;

import asp.com.appbase.adapter.BaseListAdapter;
import asp.com.appbase.adapter.ViewHolder;
import asp.com.asp.R;
import asp.com.asp.domain.QiangItem;

/**
 * Created by Administrator on 2016/5/16.
 */
public class QiangItemGridViewAdapter  extends BaseListAdapter<String> {



    public QiangItemGridViewAdapter(Context mContext, ArrayList<String> paths) {
        super(mContext,paths);
    }


    @Override
    public View bindView(int position, View convertView, ViewGroup parent) {
        final String item = list.get(position);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_grid_view, null);
        }
        SimpleDraweeView item_grid = ViewHolder.get(convertView, R.id.item_grid_view);

        if(item != null){
            item_grid.setImageURI(Uri.parse(item));
        }

        return convertView;
    }
}
