package work.lpssfxy.www.campuslifeassistantclient.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.hjq.permissions.OnPermissionCallback;
import com.hjq.permissions.Permission;
import com.hjq.permissions.XXPermissions;
import com.hjq.toast.ToastUtils;

import java.util.List;

/**
 * created by on 2021/11/30
 * 描述：权限执行动作工具类
 *
 * @author ZSAndroid
 * @create 2021-11-30-11:24
 */
public class XXPermissionsAction {

    private static XXPermissionsAction xxPermissionsAction;

    public static XXPermissionsAction getInstance() {
        if (xxPermissionsAction == null) {
            return new XXPermissionsAction();
        }
        return xxPermissionsAction;
    }

    /**
     * 拨打电话（直接拨打电话）
     *
     * @param phoneNum 电话号码
     */
    public void callPhone(Context context, String phoneNum) {
        XXPermissions.with(context)
                // 申请单个权限
                .permission(Permission.CALL_PHONE)
                .request(new OnPermissionCallback() {
                    @Override
                    public void onGranted(List<String> permissions, boolean all) {
                        if (all) {
                            Intent intent = new Intent(Intent.ACTION_CALL);
                            Uri data = Uri.parse("tel:" + phoneNum);
                            intent.setData(data);
                            context.startActivity(intent);
                        } else {
                            ToastUtils.show("获取部分权限成功，但部分权限未正常授予");
                        }
                    }

                    @Override
                    public void onDenied(List<String> permissions, boolean never) {
                        if (never) {
                            ToastUtils.show("被永久拒绝授权，请手动授予拨打电话权限");
                            // 如果是被永久拒绝就跳转到应用权限系统设置页面
                            XXPermissions.startPermissionActivity(context, permissions);
                        } else {
                            ToastUtils.show("获取拨打电话权限失败");
                        }
                    }
                });
    }

    /**
     * 相机
     *
     */
    public void camera(Context context) {
        XXPermissions.with(context)
                .permission(Permission.CAMERA)
                .request(new OnPermissionCallback() {
                    @Override
                    public void onGranted(List<String> permissions, boolean all) {
                        if (all) {
                            ToastUtils.show("获取拍照权限成功");
                        }
                    }
                });
    }
}
