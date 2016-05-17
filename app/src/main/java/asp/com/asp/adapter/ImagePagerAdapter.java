package asp.com.asp.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.net.Uri;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;


public class ImagePagerAdapter extends PagerAdapter {
	private ArrayList<String> urls;
	private Context mContext;
	private SimpleDraweeView mSimpleDraweeView;
	public ImagePagerAdapter(Context mContext,ArrayList<String> urls){
		this.urls= urls;
		this.mContext = mContext;
	}
	@Override
	public int getCount() {
		return urls.size();
	}
	
	@Override
	public boolean isViewFromObject(View view, Object obj) {
		return view==obj;
	}
	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		mSimpleDraweeView = null;
		mSimpleDraweeView=new SimpleDraweeView(mContext);
		mSimpleDraweeView.setScaleType(ScaleType.CENTER_CROP);
		mSimpleDraweeView.setImageURI(Uri.parse(urls.get(position)));
		container.addView(mSimpleDraweeView);
		return mSimpleDraweeView;
	}
	public void destroyItem(View arg0, int position, Object arg2) {
		
                ((ViewPager) arg0).removeView((View)arg2);  
            }
	

}
