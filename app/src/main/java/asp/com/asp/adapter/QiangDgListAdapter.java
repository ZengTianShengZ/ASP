package asp.com.asp.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
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
import asp.com.asp.activity.GoodsDetailActivityCs_;
import asp.com.asp.activity.PersonaQiangActivity;
import asp.com.asp.domain.QiangItem;
import asp.com.asp.domain.QiangItemDg;
import asp.com.asp.utils.ConfigConstantUtil;
import asp.com.asp.utils.OperationBmobDataUtil;
import asp.com.asp.view.innerGridView;

/**
 * Created by Administrator on 2016/5/13.
 */
public class QiangDgListAdapter extends BaseListAdapter<QiangItemDg> {

    private final int TYPE_RECOMMEND_SELLSE = 1;
    private final int TYPE_SEND_TXT = 1;

    private int comment_Count;

    private OperationBmobDataUtil mOperationBmobDataUtil;
    private QiangItemGridViewAdapter mQiangItemGridViewAdapter;

    public QiangDgListAdapter(Context context, List<QiangItemDg> list) {
        super(context, list);
        mOperationBmobDataUtil = mOperationBmobDataUtil.getInstance();
        mOperationBmobDataUtil.initData(context);
    }

    @Override
    public View bindView(int position, View convertView, ViewGroup parent) {
        final QiangItemDg item = list.get(position);
        if (convertView == null) {
           // convertView = createViewByType(position);
            convertView = mInflater.inflate(R.layout.item_qiang, null);
        }
       SimpleDraweeView qiang_logo = ViewHolder.get(convertView, R.id.item_qiang_logo);

        TextView nameTv = ViewHolder.get(convertView, R.id.item_qiang_nameTv);
        TextView time_Tv = ViewHolder.get(convertView, R.id.item_qiang_time_Tv);
        TextView comment_Tv = ViewHolder.get(convertView, R.id.item_qiang_comment_Tv);
        TextView context_Tv = ViewHolder.get(convertView, R.id.item_qiang_context_Tv);

        context_Tv.setText(item.getContent()+"");

        comment_Count =  mOperationBmobDataUtil.queryCommentCount(mContext,item.getObjectId(),comment_Tv);

        innerGridView qiang_gridView = ViewHolder.get(convertView, R.id.item_qiang_gridView);

        String Imageurl = null;
        if (item.getAuthor().getAvatar()!=null) {
            Imageurl = item.getAuthor().getAvatar().getFileUrl(mContext);
            qiang_logo.setImageURI(Uri.parse(Imageurl));
        }
        if(item.getAuthor().getNickname() != null){
            nameTv.setText(item.getAuthor().getNickname()+"");
            nameTv.setTextColor(Color.RED);
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

        convertView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent intent = new Intent(mContext,GoodsDetailActivityCs_.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("itemDgQiang", item);
                intent.putExtra("comment_Count",comment_Count);
                mContext.startActivity(intent);

            }
        });
        qiang_logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext,PersonaQiangActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra(ConfigConstantUtil.intentDtat_Author, item.getAuthor());
                mContext.startActivity(intent);
            }
        });

        return  convertView;
    }
    /**
     * 返回发送或是接收 的 布局
     * @param position
     * @return View
     */
    private View createViewByType(int position) {

        return list.get(position).getMessageType() == TYPE_RECOMMEND_SELLSE ?
                mInflater.inflate(R.layout.item_qiang, null)
                :
                mInflater.inflate(R.layout.item_qiang, null);
    }
    @Override
    public int getViewTypeCount() {
        return 2;
    }

}
