package anjuman.e.husami;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;

import com.google.android.gms.gcm.GcmListenerService;

import java.util.Random;

public class GcmMessageHandler extends GcmListenerService {

//    public static final int MESSAGE_NOTIFICATION_ID = 4353145;

    @Override
    public void onMessageReceived(String from, Bundle data) {

        if (data != null) {

            String message = null;
            if (data.containsKey("message"))
                message = data.getString("message");

            String title = null;
            if (data.containsKey("title"))
                title = data.getString("title");

            createNotification(title, message);

            NotificationsManager notificationsManager = new NotificationsManager(getBaseContext());
            notificationsManager.insertNotificationInDB(message, title);
        }
    }

    // Creates notification based on title and body received
    private void createNotification(String title, String body) {
        try {

            Context context = getBaseContext();

            NotificationManager mNotificationManager = (NotificationManager) context
                    .getSystemService(Context.NOTIFICATION_SERVICE);

            Intent notificationActivity = new Intent(context, NotificationActivity.class);

            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, notificationActivity, 0);

            NotificationCompat.Builder mBuilder;
            String NOTIFICATION_CHANNEL_ID = "my_channel_id_02";

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "My Notifications", NotificationManager.IMPORTANCE_DEFAULT);

                // Configure the notification channel.
                notificationChannel.setDescription("Channel description");
                notificationChannel.enableLights(true);
//                notificationChannel.setLightColor(Color.RED);
                notificationChannel.setVibrationPattern(new long[]{0, 1000, 500, 1000});
                notificationChannel.enableVibration(true);
                if (mNotificationManager != null) {
                    mNotificationManager.createNotificationChannel(notificationChannel);
                }

                mBuilder = new NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID);
            } else {
                mBuilder = new NotificationCompat.Builder(context);
            }


            mBuilder.setSmallIcon(R.drawable.ic_menu_alerts_push)
                    .setColor(ContextCompat.getColor(context, R.color.colorPrimary))
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true);

            if (!TextUtils.isEmpty(body))
                mBuilder.setContentText(body);

            if (!TextUtils.isEmpty(title))
                mBuilder.setContentTitle(title);

            Bitmap bm = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
            mBuilder.setLargeIcon(bm);

            mBuilder.setSound(Settings.System.DEFAULT_NOTIFICATION_URI);

            if (mNotificationManager != null) {
                mNotificationManager.notify(new Random().nextInt(), mBuilder.build());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
