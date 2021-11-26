package work.lpssfxy.www.campuslifeassistantclient.utils.dialog;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;

import com.xuexiang.xui.widget.dialog.materialdialog.MaterialDialog;
import com.xuexiang.xui.widget.dialog.materialdialog.simplelist.MaterialSimpleListAdapter;
import com.xuexiang.xui.widget.dialog.materialdialog.simplelist.MaterialSimpleListItem;
import com.xuexiang.xutil.tip.ToastUtils;

import java.util.ArrayList;
import java.util.List;

import work.lpssfxy.www.campuslifeassistantclient.R;

/**
 * created by on 2021/11/26
 * 描述：Xui对话框工具类
 *
 * @author ZSAndroid
 * @create 2021-11-26-12:16
 */
public class XuiDialogUtils {

    private static final String TAG = "XuiDialogUtils";
    private static XuiDialogUtils xuiDialogUtils;

    public static XuiDialogUtils getInstance() {
        if (xuiDialogUtils != null) {
            xuiDialogUtils = new XuiDialogUtils();
        }
        return xuiDialogUtils;
    }

    public void xuiImgLogoDialog(Context context, List<String> strContentList){


    }
}
