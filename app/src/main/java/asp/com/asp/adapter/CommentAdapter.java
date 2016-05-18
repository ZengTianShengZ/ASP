package asp.com.asp.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

import asp.com.appbase.adapter.BaseListAdapter;
import asp.com.appbase.adapter.ViewHolder;
import asp.com.asp.R;
import asp.com.asp.domain.Comment;
import asp.com.asp.domain.QiangItem;

/**
 * Created by Administrator on 2016/5/17.
 */
public class CommentAdapter extends BaseListAdapter<Comment> {
    public CommentAdapter(Context context, List<Comment> list, QiangItem item) {
        super(context, list);
    }

    @Override
    public View bindView(int position, View convertView, ViewGroup parent) {

        final Comment comment = list.get(position);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_comment, null);
        }
        SimpleDraweeView comment_logo = ViewHolder.get(convertView, R.id.item_comment_logo);

        String logoUrl = comment.getUser().getAvatar().getFileUrl(mContext);
        comment_logo.setImageURI(Uri.parse(logoUrl));

        TextView nameTv = ViewHolder.get(convertView, R.id.item_comment_nameTv);
        TextView time_Tv = ViewHolder.get(convertView, R.id.item_comment_time_Tv);
        TextView replay_Tv = ViewHolder.get(convertView, R.id.item_comment_replay_Tv);
        TextView content_Tv = ViewHolder.get(convertView, R.id.item_comment_content_Tv);

        nameTv.setText(comment.getUser().getNickname()+"");
        time_Tv.setText(comment.getCreatedAt());
        content_Tv.setText(comment.getCommentContent());

        return convertView;
    }
}
