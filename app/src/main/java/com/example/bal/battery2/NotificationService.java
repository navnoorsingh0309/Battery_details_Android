package com.example.bal.battery2;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Handler;
import android.support.v4.app.NotificationCompat;
import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;
public class NotificationService extends JobService
{
    private int notis = 0;
    int low = 0, full = 0, enough = 0, chr = 0;
    //Timer tr = new Timer();
    Handler handle = new Handler();
    Runnable notiservice;
    {
        notiservice = new Runnable() {
            @Override
            public void run() {
                IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
                Intent batteryStatus = getBaseContext().registerReceiver(null, ifilter);
                //-----Get Precentage-----
                // Get the battery scale
                int scale = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
                // get the battery level
                int level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
                // Calculate the battery charged percentage
                float percentage = level / (float) scale;
                // Update the progress bar to display current battery charged percentage
                int mProgressStatus = (int) ((percentage) * 100);
                //________________________________________________
                int status = batteryStatus.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
                boolean isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING ||
                        status == BatteryManager.BATTERY_STATUS_FULL;
                //Charging Notification
                if (isCharging) {
                    CancelNotification(6654);
                    low = 0;
                    if (mProgressStatus < 100 && chr == 0) {
                        CancelNotification(6655);
                        ShowNotification(6655, "Bit n Byte Battery Diagnose", "Charger is Plugged In.", R.drawable.crgnimg);
                        chr = 1;
                    }
                    //Enough battery
                    if (mProgressStatus >= 80 && mProgressStatus < 100 && enough == 0) {
                        CancelNotification(6652);
                        ShowNotification(6652, "Bit n Byte Battery Diagnose", "Sufficient Battery, can Unplug it.", R.drawable.enough);
                        enough = 1;
                    }
                    if (mProgressStatus == 100 && full == 0) {
                        CancelNotification(6655);
                        CancelNotification(6653);
                        chr = 0;
                        ShowNotification(6653, "Bit n Byte Battery Diagnose", "Battery Full, Please Unplug Charger", R.drawable.fullb);
                        full = 1;
                    }
                } else {
                    //Battery Low Notification-----------
                    if (mProgressStatus <= 20 && low == 0) {
                        CancelNotification(6654);
                        ShowNotification(6654, "Bit n Byte Battery Diagnose", "Battery is Low, Please Plug in Charger.", R.drawable.batterylow);
                        low = 1;
                    } else {
                        CancelNotification(6654);
                        low = 0;
                    }
                    //Full Battery
                    CancelNotification(6652);
                    CancelNotification(6653);
                    CancelNotification(6655);
                    chr = 0;
                    full = 0;
                    enough = 0;
                }
                handle.postDelayed(notiservice, 1);
            }
        };
    }

    public boolean onStartJob(final JobParameters pars)
    {
        try
        {
            handle.postDelayed(notiservice, 1);
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        return true;
    }
    public boolean onStopJob(JobParameters params) {
        CancelNotification(6652);
        CancelNotification(6653);
        CancelNotification(6654);
        CancelNotification(6655);
        low = 0;
        full = 0;
        enough = 0;
        chr = 0;
        Intent serviceIntent = new Intent(this, NotificationService.class);
        stopService(serviceIntent);
        return false;
    }
    public void ShowNotification(int id, String Title, String Text, int Icon)
    {
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getBaseContext(), "BitnByteCharging")
                .setSmallIcon(Icon)
                .setContentTitle(Title)
                .setContentText(Text)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setOngoing(true);
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        PendingIntent contentIntent = PendingIntent.getActivity(this, id,
                new Intent(this, MainActivity.class), PendingIntent.FLAG_NO_CREATE);
        mBuilder.setContentIntent(contentIntent);
        // notificationID allows you to update the notification later on.
        mNotificationManager.notify(id, mBuilder.build());
    }
    public void CancelNotification(int id)
    {
        NotificationManager manager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        manager.cancel(id);
    }
}