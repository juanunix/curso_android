package com.paola.mybatterystatus;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.BatteryManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;

public class MyBatteryReceiver extends BroadcastReceiver {

    private static final String TAG = MyBatteryReceiver.class.getName();
    private static final String EXTRA_LEVEL = "EXTRA_LEVEL";

    public MyBatteryReceiver() {

    }

    @Override
    public void onReceive(Context context, Intent intent) {
        int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
        Log.i("MyBatteryTAG", String.valueOf(level));

        if(level< 20) {
            NotificationCompat.Builder notifBuilder = new NotificationCompat.Builder(context);
            Intent intentActivity = new Intent(context, ActivityShowBatteryLevel.class);

            intentActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK );
            intentActivity.putExtra(EXTRA_LEVEL, level);

      /*     PendingIntent pendingIntent = PendingIntent.getActivity(context, 200, intentActivity, PendingIntent.FLAG_UPDATE_CURRENT);

            notifBuilder.setContentTitle(context.getString(R.string.battery_notification));
            notifBuilder.setContentIntent(pendingIntent);
            notifBuilder.setAutoCancel(true);
            notifBuilder.setContentText(context.getText(R.string.battery_not_desc));
            notifBuilder.setSmallIcon(R.drawable.ic_battery_std_white_24dp);


            NotificationManager mNotificationManager =
                    (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

            mNotificationManager.notify(1, notifBuilder.build());
        }*/

          // The stack builder object will contain an artificial back stack for the
// started Activity.
// This ensures that navigating backward from the Activity leads out of
// your application to the Home screen.
            TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
// Adds the back stack for the Intent (but not the Intent itself)
            stackBuilder.addParentStack(ActivityShowBatteryLevel.class);
// Adds the Intent that starts the Activity to the top of the stack
            stackBuilder.addNextIntent(intentActivity);
            PendingIntent resultPendingIntent =
                    stackBuilder.getPendingIntent(
                            0,
                            PendingIntent.FLAG_UPDATE_CURRENT
                    );
            notifBuilder.setContentIntent(resultPendingIntent);

            notifBuilder.setContentTitle(context.getString(R.string.battery_notification));
            //  notifBuilder.setContentIntent(pendingIntent);
            notifBuilder.setAutoCancel(true);
            notifBuilder.setContentText(context.getText(R.string.battery_not_desc));
            notifBuilder.setSmallIcon(R.drawable.ic_battery_std_white_24dp);


            NotificationManager mNotificationManager =
                    (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

            mNotificationManager.notify(1, notifBuilder.build());
        }

    }
}

