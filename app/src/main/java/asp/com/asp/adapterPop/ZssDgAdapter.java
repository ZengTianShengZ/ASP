package asp.com.asp.adapterPop;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;

import java.util.List;

import asp.com.asp.R;
import asp.com.asp.activity.AddGdActivity;
import asp.com.asp.activity.EditQiangActivity;
import asp.com.asp.activity.ShowImageActivity;
import asp.com.asp.view.SnackbarUtil;

public class ZssDgAdapter extends CommonAdapter<String>{
	private static final int REQUEST_CODE_ALBUM = 1;
	private AddGdActivity activity;
	private Context mContext;
	public ZssDgAdapter(Context context, List<String> mDatas, int itemLayoutId, AddGdActivity activity) {
		super(context, mDatas, itemLayoutId);
		this.activity = activity;
	}

	@Override
	public void convert(ViewHolder helper, final String item) {
		 final int position = helper.getPosition();
		//    /storage/sdcard0/DCIM/Camera

		final ImageView mImageView = helper.getView(R.id.id_show_image);
		final ImageView delect_btn = helper.getView(R.id.id_show_image_delect_btn);

		if(position == 0){
			delect_btn.setVisibility(View.GONE);
			mImageView.setImageResource(R.mipmap.add_image);

		}else{
			delect_btn.setVisibility(View.VISIBLE);

			helper.setImageByUrl(R.id.id_show_image,item);

		}

		delect_btn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				mDatas.remove(item);
				notifyDataSetChanged();
			}
		});

		mImageView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(position == 0){

					if(mDatas.size()>9){
						SnackbarUtil.GreenSnackbar(mContext,mImageView,"图片不能超过9张！！！");
					}else {
						Intent intent = new Intent(activity, ShowImageActivity.class);
						activity.startActivityForResult(intent, REQUEST_CODE_ALBUM);
					}
				}
			}
		});

	}

}
