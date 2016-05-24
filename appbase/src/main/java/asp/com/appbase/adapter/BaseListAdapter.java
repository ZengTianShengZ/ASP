package asp.com.appbase.adapter;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Toast;

/**
 *  BaseListAdapter 基类
 *
 */
@SuppressLint("UseSparseArrays")
public abstract class BaseListAdapter<T> extends BaseAdapter {



	public List<T> list ;

	public Context mContext;

	public LayoutInflater mInflater;

	public List<T> getList() {
		return this.list;
	}

	public void setList(List<T> list) {
		this.list = list;
		notifyDataSetChanged();
	}
 

	public void add(T e) {
 
		this.list.add(e);
		System.out.println(list.size());
		 
	 	this.notifyDataSetChanged();
	}

	public void addAll(List<T> list) {
		this.list.addAll(list);
		notifyDataSetChanged();
	}

	public void remove(int position) {
		this.list.remove(position);
		notifyDataSetChanged();
	}

	/**
	 * �Ƴ�ѡ�� �� item
	 *
	 */
	public void removeSelectPosition( Map<Integer,Integer> map) {

		 Map<Integer, Integer> orderMap = new TreeMap<Integer, Integer>(
	                new Comparator<Integer>() {
	                    public int compare(Integer obj1, Integer obj2) {
	                        // ���� ����
	                        return obj1.compareTo(obj2);
	                    }
	                });
		 orderMap.putAll(map);
		 
		 
		  int poFlag = 0;
 
		//����map�е�ֵ  
		 for (Integer value : orderMap.values()) {  
 
			 int position = value;
			 
			// ע������� imageStrList.remove ��Ҫ position-i����Ϊ remove�� list
			// ����Ĳ�����������һλ��
			list.remove(position - poFlag);
	 
		    poFlag++;
		 }  
 
		 
		notifyDataSetChanged();

	}
	
	public BaseListAdapter(Context context, List<T> list) {
		super();
		this.mContext = context;
		this.list = list;
		mInflater = LayoutInflater.from(context);
 
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		System.out.println(position);
		convertView = bindView(position, convertView, parent);
		// ���ڲ��������
		
		addInternalClickListener(convertView, position, list.get(position));
		return convertView;
	}

	public abstract View bindView(int position, View convertView,
			ViewGroup parent);

	// adapter�е��ڲ�����¼�
	public Map<Integer, onInternalClickListener> canClickItem;

	private void addInternalClickListener(final View itemV, final Integer position,final Object valuesMap) {
		if (canClickItem != null) {
			for (Integer key : canClickItem.keySet()) {
				View inView = itemV.findViewById(key);
				final onInternalClickListener inviewListener = canClickItem.get(key);
				if (inView != null && inviewListener != null) {
					inView.setOnClickListener(new OnClickListener() {

						public void onClick(View v) {
							inviewListener.OnClickListener(itemV, v, position,
									valuesMap);
						}
					});
				}
			}
		}
	}

	public void setOnInViewClickListener(Integer key,
			onInternalClickListener onClickListener) {
		if (canClickItem == null)
			canClickItem = new HashMap<Integer, onInternalClickListener>();
		canClickItem.put(key, onClickListener);
	}

	public interface onInternalClickListener {
		public void OnClickListener(View parentV, View v, Integer position,
									Object values);
	}

	Toast mToast;

	public void ShowToast(final String text) {
		if (!TextUtils.isEmpty(text)) {
			((Activity) mContext).runOnUiThread(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					if (mToast == null) {
						mToast = Toast.makeText(mContext, text,
								Toast.LENGTH_SHORT);
					} else {
						mToast.setText(text);
					}
					mToast.show();
				}
			});

		}
	}

 

}
