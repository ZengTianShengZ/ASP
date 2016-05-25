package asp.com.asp.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

/**
 * SharedPreferencesUtil ��һЩ�򵥵�����
 * 
 * @ClassName: SharedPreferencesUtil
 * @Description: TODO
 * @author zss
 * @date 2016-4-12 AM
 */

public class SharedPreferencesUtil {

	private static SharedPreferencesUtil s = null;
	private static SharedPreferences mySharedPreferences;
	private static SharedPreferences.Editor editor;

	private SharedPreferencesUtil() {
	}

	public static SharedPreferencesUtil getInstance(Context context, String preferencesName) {
		if (s == null)// ˫�ط�
		{
			synchronized (SharedPreferencesUtil.class) {
				if (s == null)
					initData(context, preferencesName);
				s = new SharedPreferencesUtil();
			}  
		}
		return s;
	}

	private static void initData(Context context, String preferencesName) {
		mySharedPreferences = context.getSharedPreferences(preferencesName, context.MODE_PRIVATE);
		editor = mySharedPreferences.edit();
	}

	/**
	 * д��ֵ
	 * @param key
	 * @param value
	 */
	public void editorData(String key, String value) {
		editor.putString(key, value);
		editor.commit();
		 
	}
	public SharedPreferences.Editor getEditor( ) {
		return editor;

	}
	public SharedPreferences getPreferences(){
		return mySharedPreferences;
	}
	/**
	 * ȡֵ
	 * @param key
	 * @return String
	 */
	public String getData(String key) {
		return mySharedPreferences.getString(key, "");
		 
	}
	/**
	 * ���ֵ
	 * @param key
	 */
	public void removeData(String key) {
		editor.remove(key);
		editor.commit();

	}

	/**
	 * ����  foldername �µ� ��·��
	 *
	 * @return
	 */
	public List<String> getAllFolderDir() {
		// ��ȡ��������
		List<String> strDir = new ArrayList<String>();
	 
		Map<String, ?> allContent = mySharedPreferences.getAll();
		if (allContent != null) {
			for (Map.Entry<String, ?> entry : allContent.entrySet()) {

				String dir =  (String) entry.getValue();
				
				if(dir.contains("/")){
					strDir.add(dir);
				}

			}
			return strDir;
		} else {
			return null;
		}
		 
	}

}
