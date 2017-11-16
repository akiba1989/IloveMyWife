package com.bone7.ilovemywife.notification;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

import com.bone7.ilovemywife.MainScreenActivity;
import com.bone7.ilovemywife.R;

import java.util.Calendar;
import java.util.Random;


/**
 * Created by ptyagi on 4/17/17.
 */

/**
 * AlarmReceiver handles the broadcast message and generates Notification
 */
public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        //Get notification manager to manage/send notifications


        //Intent to invoke app when click on notification.
        //In this sample, we want to start/launch this sample app when user clicks on notification
        Intent intentToRepeat = new Intent(context, MainScreenActivity.class);
        //set flag to restart/relaunch the app
        intentToRepeat.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        //Pending intent to handle launch of Activity in intent above
        PendingIntent pendingIntent =
                PendingIntent.getActivity(context, NotificationHelper.ALARM_TYPE_RTC, intentToRepeat, PendingIntent.FLAG_UPDATE_CURRENT);

        //Build notification
        Notification repeatedNotification = buildLocalNotification(context, pendingIntent).build();

        //Send local notification
        NotificationHelper.getNotificationManager(context).notify(NotificationHelper.ALARM_TYPE_RTC, repeatedNotification);
    }

    public NotificationCompat.Builder buildLocalNotification(Context context, PendingIntent pendingIntent) {
        NotificationManager mNotificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
// The id of the channel.
        String id = "my_channel_01";

        int importance = NotificationManager.IMPORTANCE_DEFAULT;
        NotificationCompat.Builder builder;
        if(android.os.Build.VERSION.SDK_INT > 26)
        {
            NotificationChannel mChannel = new NotificationChannel(id, "Love Calendar Notifications", importance);
// Configure the notification channel.
//        mChannel.setDescription(description);
            mChannel.enableLights(true);
// Sets the notification light color for notifications posted to this channel, if the device supports this feature.
            mChannel.setLightColor(Color.BLUE);
            mChannel.enableVibration(true);
            mChannel.setVibrationPattern(new long[]{100, 100});
            mNotificationManager.createNotificationChannel(mChannel);

        }
        builder = (NotificationCompat.Builder) new NotificationCompat.Builder(context,"my_channel_01")
                        .setContentIntent(pendingIntent)
                        .setSmallIcon(R.mipmap.ic_notification)
                        .setContentTitle(getContentTitle())
                        .setAutoCancel(true);




        return builder;
    }
    public String getContentTitle()
    {
        Calendar calendar = Calendar.getInstance();

        if(MainScreenActivity.appConfig!=null && MainScreenActivity.appConfig.eventList.size() > 0)
        {
            for(int i = 0;i< MainScreenActivity.appConfig.eventList.size();i++)
            if(calendar.get(Calendar.DAY_OF_MONTH) == Integer.valueOf(MainScreenActivity.appConfig.eventList.get(i).eventDate.substring(8,10))
                    && (calendar.get(Calendar.MONTH) == Integer.valueOf(MainScreenActivity.appConfig.eventList.get(i).eventDate.substring(5,7))-1
                    || calendar.get(Calendar.MONTH) == Integer.valueOf(MainScreenActivity.appConfig.eventList.get(i).eventDate.substring(5,7))+11) )
            {
                return "Your anniversary is on this day next month, remember to prepare something!";
            }
        }
        Random random = new Random();
        String list[] = {"Are you creating more pleasurable interactions in your relationship?", "Remember to take time to have some fun together every day!"
                ,"Remember to go out on a date with your lover!", "Do you miss your lover? Let's say \"I love you\".",
                "Did you send your lover a message today yet?", "Remember  to ask your lover daily how they're doing and if they need anything from you.",
                "Taking a shower with your loved one is always a great idea.", "You should spend Time Together On a Regular Basis"};
        return list[random.nextInt(list.length-1)];
    }
}
