package asp.com.asp.adapterPop;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.Toast;

import asp.com.asp.R;

public class MyAdapter extends CommonAdapter<String> {

	/**
	 * �û�ѡ���ͼƬ���洢ΪͼƬ������·��
	 */
	public static List<String> mSelectedImage = new LinkedList<String>();
	public static ArrayList<String> imgItem = new ArrayList<String>();
	public static ArrayList<String> imgDirPath = new ArrayList<String>();

	public static int flag = 0;
	/**
	 * �ļ���·��
	 */
	private String mDirPath;
	private Context context;

	public MyAdapter(Context context, List<String> mDatas, int itemLayoutId,
			String dirPath) {
		super(context, mDatas, itemLayoutId);
		this.mDirPath = dirPath;
		this.context = context;
	}

	@Override
	public void convert(final ViewHolder helper, final String item) {

		// ����no_pic
		helper.setImageResource(R.id.id_item_image, R.mipmap.zss_pictures_no);
		// ����no_selected
		helper.setImageResource(R.id.id_item_select,
				R.mipmap.zss_picture_unselected);
		// ����ͼƬ
		helper.setImageByUrl(R.id.id_item_image, mDirPath + "/" + item);
		final ImageView mImageView = helper.getView(R.id.id_item_image);
		final ImageView mSelect = helper.getView(R.id.id_item_select);

		// ͼƬ �˾� �� ��ʹͼƬ�䰵Ч��
		mImageView.setColorFilter(null);
		// ����ImageView�ĵ���¼�
		mImageView.setOnClickListener(new OnClickListener() {
			// ѡ����ͼƬ�䰵����֮��֮
			@Override
			public void onClick(View v) {
					// �Ѿ�ѡ�����ͼƬ
					if (mSelectedImage.contains(mDirPath + "/" + item)) {

						mSelectedImage.remove(mDirPath + "/" + item);
						mSelect.setImageResource(R.mipmap.zss_picture_unselected);
						mImageView.setColorFilter(null);

						imgItem.remove(item);
						imgDirPath.remove(mDirPath);
					} else{
					// δѡ���ͼƬ
					// �տ�ʼͼƬ��û������� �� ��ͼƬ���ʱ���� mSelectedImage ��� path
					 
						if(imgItem.size() < 9){
							
						 
						mSelectedImage.add(mDirPath + "/" + item);
						Log.i("iiiiiiiiiyyyyyyy", item);
						mSelect.setImageResource(R.mipmap.zss_pictures_selected);
						mImageView.setColorFilter(Color.parseColor("#77000000"));

						imgItem.add(item);
						imgDirPath.add(mDirPath);

						}else{
							Toast.makeText(context, "ͼƬѡ���ܳ���9��", Toast.LENGTH_SHORT).show();
							return;
						}
					}
			}
		});

		/**
		 * �Ѿ�ѡ�����ͼƬ����ʾ��ѡ�����Ч��
		 */
		if (mSelectedImage.contains(mDirPath + "/" + item)) {

			mSelect.setImageResource(R.mipmap.zss_pictures_selected);
			mImageView.setColorFilter(Color.parseColor("#77000000"));
		}

	}

	public ArrayList<String> returnImgItem() {
		return imgItem;
	}

	public ArrayList<String> returnImgDirPath() {
		return imgDirPath;
	}

}
