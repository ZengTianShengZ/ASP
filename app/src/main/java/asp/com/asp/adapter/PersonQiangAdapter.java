package asp.com.asp.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.List;

import asp.com.appbase.adapter.BaseListAdapter;
import asp.com.appbase.adapter.ViewHolder;
import asp.com.asp.R;
import asp.com.asp.domain.QiangItem;
import asp.com.asp.view.innerGridView;

/**
 * Created by Administrator on 2016/5/19.
 */
public class PersonQiangAdapter extends BaseListAdapter<QiangItem> {

    private QiangItemGridViewAdapter mQiangItemGridViewAdapter;

    public PersonQiangAdapter(Context context, List<QiangItem> list) {
        super(context, list);
    }

    @Override
    public View bindView(int position, View convertView, ViewGroup parent) {
        final QiangItem item = list.get(position);
        if (convertView == null) {
            // convertView = createViewByType(position);
            convertView = mInflater.inflate(R.layout.item_qiang, null);
        }

        SimpleDraweeView qiang_logo = ViewHolder.get(convertView, R.id.item_qiang_logo);
        qiang_logo.setVisibility(View.GONE);
        TextView nameTv = ViewHolder.get(convertView, R.id.item_qiang_nameTv);
        TextView time_Tv = ViewHolder.get(convertView, R.id.item_qiang_time_Tv);
        TextView comment_Tv = ViewHolder.get(convertView, R.id.item_qiang_comment_Tv);
        TextView context_Tv = ViewHolder.get(convertView, R.id.item_qiang_context_Tv);

        innerGridView qiang_gridView = ViewHolder.get(convertView, R.id.item_qiang_gridView);

   /* personal 墙 adapter  不需要显示头像
      String Imageurl = null;
        if (item.getAuthor().getAvatar()!=null) {
            Imageurl = item.getAuthor().getAvatar().getFileUrl(mContext);
            qiang_logo.setImageURI(Uri.parse(Imageurl));
        }*/

        if(item.getAuthor().getNickname() != null){
            nameTv.setText(item.getAuthor().getNickname()+"");
        }
        time_Tv.setText(item.getCreatedAt()+"");

        context_Tv.setText(item.getContent()+"");

        if (null == item.getContentfigureurl()) {
            qiang_gridView.setVisibility(View.GONE);
        } else {
            qiang_gridView.setVisibility(View.VISIBLE);
            ArrayList<String> paths = new ArrayList<String>();
            if (item.getContentfigureurl() != null)
                paths.add(item.getContentfigureurl().getFileUrl(mContext));
            if (item.getContentfigureurl1() != null)
                paths.add(item.getContentfigureurl1().getFileUrl(mContext));
            if (item.getContentfigureurl2() != null)
                paths.add(item.getContentfigureurl2().getFileUrl(mContext));
            if (item.getContentfigureurl3() != null)
                paths.add(item.getContentfigureurl3().getFileUrl(mContext));
            if (item.getContentfigureurl4() != null)
                paths.add(item.getContentfigureurl4().getFileUrl(mContext));
            if (item.getContentfigureurl5() != null)
                paths.add(item.getContentfigureurl5().getFileUrl(mContext));
            if (item.getContentfigureurl6() != null)
                paths.add(item.getContentfigureurl6().getFileUrl(mContext));
            if (item.getContentfigureurl7() != null)
                paths.add(item.getContentfigureurl7().getFileUrl(mContext));
            if (item.getContentfigureurl8() != null)
                paths.add(item.getContentfigureurl8().getFileUrl(mContext));
            mQiangItemGridViewAdapter = new QiangItemGridViewAdapter(mContext, paths);
            qiang_gridView.setAdapter(mQiangItemGridViewAdapter);
        }


        return convertView;
    }
}
