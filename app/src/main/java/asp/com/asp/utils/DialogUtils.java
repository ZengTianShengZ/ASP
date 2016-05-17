package asp.com.asp.utils;

import android.app.Activity;
import android.app.Dialog;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import asp.com.asp.R;

/**
 * Builder模式 创建 内容可定制的 dialog
 *
 * @ClassName: DialogUtils
 * @Description: TODO
 * @author zss
 * @date 2016-4-21 PM
 */
public class DialogUtils {
 
	private EditText dlg_edit;
	
	private String title_str;
	private String left_btn_str, right_btn_str;
	private String edit_hint_str;

	private int title_visible, edit_visible;
	private int left_btn_visible, right_btn_visible;

	private static int viewVisible = View.VISIBLE;
 
	private DialogUtils(Builder b) {

		this.title_str = b.title_str;
		this.left_btn_str = b.left_btn_str;
		this.right_btn_str = b.right_btn_str;
		this.edit_hint_str = b.edit_hint_str;

		this.title_visible = b.title_visible;
		this.edit_visible = b.edit_visible;
		this.left_btn_visible = b.left_btn_visible;
		this.right_btn_visible = b.right_btn_visible;

	}

	public static class Builder {
		private String title_str = "新建文件夹";
		private String left_btn_str = "取消", right_btn_str = "确认";
		private String edit_hint_str = "未命名文件夹";
		private int title_visible = viewVisible, edit_visible = viewVisible;
		private int left_btn_visible = viewVisible, right_btn_visible = viewVisible;

		public Builder titleStr(String title_str_) {
			title_str = title_str_;
			return this;
		}

		public Builder leftBtnStr(String left_btn_str_) {
			left_btn_str = left_btn_str_;
			return this;
		}

		public Builder rightBtnStr(String right_btn_str_) {
			right_btn_str = right_btn_str_;
			return this;
		}

		public Builder editHintStr(String edit_hint_str_) {
			edit_hint_str = edit_hint_str_;
			return this;
		}

		public Builder setTitleVisible(int val) {
			title_visible = val;
			return this;
		}

		public Builder setEditVisible(int val) {
			edit_visible = val;
			return this;
		}

		public Builder setLeftBtnVisible(int val) {
			left_btn_visible = val;
			return this;
		}

		public Builder setRightBtnVisible(int val) {
			right_btn_visible = val;
			return this;
		}

		public DialogUtils build() { // ����������һ���¶���
			return new DialogUtils(this);
		}
	}

	public View getDialogView(Activity context) {
 
		
		View builderDialog = LayoutInflater.from(context).inflate(R.layout.dlg_creat_folder, null);

		TextView dlg_text = (TextView) builderDialog.findViewById(R.id.dlg_creat_folder_tv);
		dlg_text.setText(title_str);
		dlg_text.setVisibility(title_visible);
 
	    dlg_edit = (EditText) builderDialog.findViewById(R.id.dlg_creat_folder_et);
		dlg_edit.setHint(edit_hint_str);
		dlg_edit.setVisibility(edit_visible);

		Button left_btn = (Button) builderDialog.findViewById(R.id.dlg_creat_folder_cancel);
		left_btn.setText(left_btn_str);
		left_btn.setVisibility(left_btn_visible);
		
		Button right_btn = (Button) builderDialog.findViewById(R.id.dlg_creat_folder_affirm);
		right_btn.setText(right_btn_str);
		right_btn.setVisibility(right_btn_visible);
		
		
		left_btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(mDialogLeftButtonClick!=null){
					mDialogLeftButtonClick.dialogLeftButtonClickListener();
				}
			}
		});
		
		right_btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(mDialogRightButtonClick!=null){
					mDialogRightButtonClick.dialogRightButtonClickListener(dlg_edit.getText()+"");
				}
			}
		});

		return builderDialog;
	}

	private DialogLeftButtonClick mDialogLeftButtonClick;
	
	public void leftButtonClickListener(DialogLeftButtonClick Click){
		this.mDialogLeftButtonClick = Click;
	}
	 
	public interface DialogLeftButtonClick{
		public void dialogLeftButtonClickListener();
	}	
	
	private DialogRightButtonClick mDialogRightButtonClick;
	
	public void rightButtonClickListener(DialogRightButtonClick Click){
		this.mDialogRightButtonClick = Click;
	}
	
	public interface DialogRightButtonClick{
		public void dialogRightButtonClickListener(String editStr);
	}	
	
	
	private static View view;

	public static View getView() {
		return view;
	}

	// 创建dialog
	public static Dialog CreateDialog(Activity context, int layoutRes, int gravity, boolean matchParentWidth,
			String text) {
		view = context.getLayoutInflater().inflate(layoutRes, null);
		Dialog dlg = new Dialog(context, R.style.CustomDialog);
		dlg.setContentView(view);
		dlg.setCanceledOnTouchOutside(true);
		Window window = dlg.getWindow();
		window.setGravity(gravity);

		if (matchParentWidth) {
			android.view.WindowManager.LayoutParams layoutParams = window.getAttributes();
			// 获取屏幕大小
			DisplayMetrics metric = new DisplayMetrics();
			context.getWindowManager().getDefaultDisplay().getMetrics(metric);
			layoutParams.width = metric.widthPixels / 2;
			window.setAttributes(layoutParams);
		}
		return dlg;
	}

}
