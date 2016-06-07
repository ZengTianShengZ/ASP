package asp.com.asp.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

import asp.com.appbase.adapter.BaseRecycleViewAdapter;
import asp.com.appbase.adapter.RecycleViewHolder;
import asp.com.asp.R;
import asp.com.asp.activity.ZhihuStoryActivity;
import asp.com.asp.domain.QiangItem;
import asp.com.asp.domain.ZhihuDailyItem;

/**
 * Created by Administrator on 2016/6/3.
 */
public class ZhihuNewsRecycleAdapter extends BaseRecycleViewAdapter {


    public ZhihuNewsRecycleAdapter(Context context, List<ZhihuDailyItem> datas) {
        super(context, R.layout.item_zhihu_news, datas);
    }

    @Override
    public void convert(RecycleViewHolder holder, Object obj, int holderPosition) {
        final ZhihuDailyItem item = (ZhihuDailyItem)obj;
        Log.i("refresHandle","........convert....66666....");
        SimpleDraweeView thumbnail_image = holder.getView( R.id.iten_zhihu_news_thumbnail_image);
        TextView question_title = holder.getView( R.id.iten_zhihu_news_question_title);
        TextView daily_title = holder.getView( R.id.iten_zhihu_news_daily_title);

        thumbnail_image.setImageURI(Uri.parse(item.getImages()[0] ));

        question_title.setText(item.getTitle()+"");

        daily_title.setText(item.getDate()+"");

        holder.mConvertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("mConvertView","......mConvertView......");
                Intent intent = new Intent(mContext, ZhihuStoryActivity.class);
                intent.putExtra("type", ZhihuStoryActivity.TYPE_ZHIHU);
                intent.putExtra("id", item.getId());
                intent.putExtra("title", item.getTitle());
                mContext.startActivity(intent);
            }
        });

    }

}
