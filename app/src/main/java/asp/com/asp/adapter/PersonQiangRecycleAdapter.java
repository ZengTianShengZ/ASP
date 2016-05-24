package asp.com.asp.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.List;

import asp.com.appbase.adapter.BaseRecycleViewAdapter;
import asp.com.appbase.adapter.RecycleViewHolder;
import asp.com.appbase.adapter.ViewHolder;
import asp.com.asp.R;
import asp.com.asp.domain.QiangItem;
import asp.com.asp.view.innerGridView;

/**
 * Created by Administrator on 2016/5/20.
 */
public class PersonQiangRecycleAdapter  extends BaseRecycleViewAdapter   {

    private QiangItemGridViewAdapter mQiangItemGridViewAdapter;

    private Context mContext;


    public PersonQiangRecycleAdapter(Context context, List<QiangItem> mListItems) {
        super(context, R.layout.item_personal_qiang, mListItems);
        this.mContext  =context;
    }

    @Override
    public void convert(RecycleViewHolder holder, Object obj, int holderPosition) {
        final QiangItem item = (QiangItem)obj;

        SimpleDraweeView imageView = holder.getView( R.id.personal_qiang_sdView);
        TextView nameTv = holder.getView( R.id.item_personal_qiang_nameTv);
        TextView time_Tv = holder.getView( R.id.item_personal_qiang_time_Tv);
        TextView comment_Tv = holder.getView(  R.id.item_qiang_comment_Tv);
        TextView context_Tv = holder.getView( R.id.item_personal_qiang_content_Tv);


        if(item.getAuthor().getNickname() != null){
            nameTv.setText(item.getAuthor().getNickname()+"");
        }
        time_Tv.setText(item.getCreatedAt()+"");

        context_Tv.setText(item.getContent()+"");

        if (null == item.getContentfigureurl()) {
         //   imageView.setVisibility(View.GONE);
        } else {

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

            imageView.setImageURI(Uri.parse(paths.get(0)));


        }


    }

}
