package work.lpssfxy.www.campuslifeassistantclient.base;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

/**
 * created by on 2021/11/14
 * 描述：
 *
 * @author ZSAndroid
 * @create 2021-11-14-11:16
 */
public class MyPushMessageReceiver extends BroadcastReceiver {
    private NotificationManager manager;
    private static final String TAG = "客户端收到的推荐消息";
    @Override
    public void onReceive(Context context, Intent intent) {

        String msg = intent.getStringExtra("msg");
        if (intent.getAction().equals("cn.bmob.push.action.MESSAGE")) {
            Toast.makeText(context, TAG + msg, Toast.LENGTH_SHORT).show();
            Notification.Builder builder = new Notification.Builder(context);
            builder.setContentText("这是推送内容：" + msg)
                    .setContentTitle("推送标题");
//        }
        }

    }}
