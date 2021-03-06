package asp.com.appbase.adapter;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import asp.com.appbase.R;

/**
 *  RecyclerView.Adapter  基类
 *
 * @ClassName: BaseRecycleViewAdapter
 * @Description: TODO
 * @author zss
 * @date 2016-4-29 PM
 */

public abstract class BaseRecycleViewAdapter<T> extends RecyclerView.Adapter<RecycleViewHolder> {

	//正常条目
	private static final int TYPE_NORMAL_ITEM = 0;
	//加载条目
	private static final int TYPE_LOADING_ITEM = 1;

	protected Context mContext;
	protected int mLayoutId;
	protected List<T> mDatas;
	protected LayoutInflater mInflater;

	public static final int LAST_POSITION = -1;

	public BaseRecycleViewAdapter(Context context, int layoutId, List<T> datas) {
		mContext = context;
		mInflater = LayoutInflater.from(context);
		mLayoutId = layoutId;
		mDatas = datas;
	}

	@Override
	public int getItemCount() {
		return mDatas.size() == 0 ? 0 : mDatas.size() + 1;
	}
	@Override
	public long getItemId(int position) {
		return position;
	}
	@Override
	public int getItemViewType(int position) {
		if (position + 1 == getItemCount()) {
			return TYPE_LOADING_ITEM;
		} else {
			return TYPE_NORMAL_ITEM;
		}
	}

	@Override
	public void onBindViewHolder(final RecycleViewHolder holder, final int position) {
		holder.updatePosition(position);
		if(position + 1 == getItemCount()){
			TextView lodingTv = holder.getView(R.id.tv_loading);
			lodingTv.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					if(mOnLoadingListener!=null){
						mOnLoadingListener.loading(holder.getView(R.id.progress_loading));
					}
				}
			});
		}else {
			convert(holder, mDatas.get(position), holder.getPosition());
		}
	}

	@Override
	public RecycleViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {

		if(viewType == TYPE_NORMAL_ITEM){
			final RecycleViewHolder viewHolder = RecycleViewHolder.get(mContext, parent, mLayoutId);
			return viewHolder;
		}else{
			final RecycleViewHolder loadingViewHolder = RecycleViewHolder.get(mContext, parent, R.layout.loading_layout);
			return loadingViewHolder;
		}

	}

	public abstract void convert(RecycleViewHolder holder, T t, int holderPosition);

	/**
	 * 添加一条数据
	 * @param t
	 * @param position
     */
	public void insertedItem(T t,int position){
		mDatas.add(position,t);
		notifyItemInserted(position);
	}
	/**
	 * 移除一条数据
	 * @param position
     */
	public void removePosition(int position){
		if(position >=0) {
			mDatas.remove(position);
			notifyItemRemoved(position);

		}
	}
	/**
	 * 移除所有选中 的 item
	 *
	 */
	public void removeAllSelectPosition( Map<Integer,Integer> map) {

		Map<Integer, Integer> orderMap = new TreeMap<Integer, Integer>(
				new Comparator<Integer>() {
					public int compare(Integer obj1, Integer obj2) {
						// 升序 排序
						return obj1.compareTo(obj2);
					}
				});
		orderMap.putAll(map);


		int poFlag = 0;
		int keyMin = 0;
		int keyMax = 0;
		//遍历map中的值
		for (Integer value : orderMap.values()) {

			if(poFlag == 0){
				keyMin = value;
			}
			keyMax = value;
			int position = value;

			// 注意这里的 imageStrList.remove 需要 position-i，因为 remove完 list
			// 里面的参数都会向移一位的
			mDatas.remove(position - poFlag);
			if (position == LAST_POSITION && getItemCount() > 0)
				position = getItemCount() - 1;
			if (position > LAST_POSITION && position < getItemCount()) {
				notifyItemRemoved(position);
			}
			poFlag++;
		}

/*		for (int i = 0; i < positions.size(); i++) {
			int position = positions.get(i);
			// 注意这里的 imageStrList.remove 需要 position-i，因为 remove完 list
			// 里面的参数都会向移一位的
			mDatas.remove(position - i);
			if (position == LAST_POSITION && getItemCount() > 0)
				position = getItemCount() - 1;
			if (position > LAST_POSITION && position < getItemCount()) {
				notifyItemRemoved(position);
			}
		}
		//notifyDataSetChanged();
*/		// 刷新 选中的 第一位 和 最后一位，而不是 整屏刷新

		notifyItemRangeChanged(orderMap.get(keyMin),orderMap.get(keyMax));

	}


	/**
	 * 添加 新的 item
	 * @param t
	 */
	public void addNewPosition(List<T> t) {
		int i = 0;
		for (T item : t) {
			// 图片更新在第二张
			i++;
			mDatas.add(i, (T) t);
			notifyItemInserted(i);
		}

	}


	public void myNotifyData(){
		notifyDataSetChanged();
	}


	private OnLoadingListener mOnLoadingListener;

	/**
	 * 加载更多接口
	 */
	public interface OnLoadingListener {
		void loading(View progress_loading);
	}

	/**
	 * 设置监听接口
	 *
	 * @param onLoadingListener onLoadingListener
	 */
	public void setOnLoadingListener(OnLoadingListener onLoadingListener) {
		mOnLoadingListener = onLoadingListener;
	}

}
