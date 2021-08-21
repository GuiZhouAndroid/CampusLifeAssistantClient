package work.lpssfxy.www.campuslifeassistantclient.utils.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.Window;
import android.widget.Toast;


import java.text.SimpleDateFormat;
import java.util.Date;

import work.lpssfxy.www.campuslifeassistantclient.R;


public class LoadingDialog {

	public static Dialog dialog;
	public static Date data;
	public static SimpleDateFormat format;
	

	public static void showToast(Context context, String msg){
		Toast.makeText(context,msg, 1).show();
	}
	
	public static void showDialog(Context context){
		dialog=new Dialog(context, R.style.dialog);
		dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.login_dialog_item);
		dialog.setCanceledOnTouchOutside(false);
		dialog.show();
	}
	public static void disDialog(){
		dialog.dismiss();
	}
	
	public static String getTime(){
		data=new Date(System.currentTimeMillis());
		format=new SimpleDateFormat("yyyy-MM-dd HH:mm:BaseFragment");
		String string=format.format(data);
		return string;
	}
}
