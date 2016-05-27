package asp.com.asp.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

import asp.com.appbase.adapter.BaseRecycleViewAdapter;
import asp.com.appbase.adapter.RecycleViewHolder;
import asp.com.appbase.adapter.ViewHolder;
import asp.com.asp.R;
import asp.com.asp.domain.Comment;
import asp.com.asp.domain.QiangItem;

/**
 * Created by Administrator on 2016/5/26.
 */
public class CommentRecycleviewAdapter  extends BaseRecycleViewAdapter {


    private Context mContext;
    public CommentRecycleviewAdapter(Context context, List<Comment> list) {
        super(context, R.layout.item_comment, list);
        this.mContext  =context;

    }

    @Override
    public void convert(RecycleViewHolder holder, Object obj, int holderPosition) {
        final Comment comment = (Comment)obj;

        SimpleDraweeView comment_logo = holder.getView( R.id.item_comment_logo);

        if(comment.getUser().getAvatar()!= null) {
            String logoUrl = comment.getUser().getAvatar().getFileUrl(mContext);
            comment_logo.setImageURI(Uri.parse(logoUrl));
        }

        TextView nameTv =  holder.getView( R.id.item_comment_nameTv);
        TextView time_Tv = holder.getView( R.id.item_comment_time_Tv);
        TextView replay_Tv = holder.getView(  R.id.item_comment_replay_Tv);
        TextView content_Tv = holder.getView( R.id.item_comment_content_Tv);

        nameTv.setText(comment.getUser().getNickname()+"");
        time_Tv.setText(comment.getCreatedAt());
        content_Tv.setText(comment.getCommentContent());

        replay_Tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mReplayTvOnClickListener!=null){
                    if(comment.getUser()!=null){
                        mReplayTvOnClickListener.replayTvClick(comment.getUser().getNickname());
                    }

                }
            }
        });

    }

    public interface ReplayTvOnClickListener{
        public void replayTvClick(String nickname);
    }
    private ReplayTvOnClickListener mReplayTvOnClickListener;
    public  void setReplayTvOnClickListener(ReplayTvOnClickListener mReplayTvOnClickListener){
        this. mReplayTvOnClickListener = mReplayTvOnClickListener;
    }
}
