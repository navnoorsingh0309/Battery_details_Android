package com.example.bal.battery2;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Handler;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.webkit.URLUtil;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ImageView;
import android.widget.Toast;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import static android.view.View.VISIBLE;

public class MainActivity extends AppCompatActivity
{
    Handler AdvertisementHandler = new Handler();
    //New Version Ad
    private Runnable NewVerAdvertisement = new Runnable() {
        public void run() {
            //Check Internet
            ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            if ((connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                    connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED)) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            URL url = new URL("https://bitnbytecomputers.co.in/AppAds/BatteryAdv/Version.ver");
                            HttpsURLConnection.setFollowRedirects(false);
                            HttpsURLConnection uc = (HttpsURLConnection) url.openConnection();
                            uc.setRequestMethod("HEAD");
                            if (uc.getResponseCode() == HttpURLConnection.HTTP_OK) {
                                HttpsURLConnection.setFollowRedirects(true);
                                HttpsURLConnection uc1 = (HttpsURLConnection) url.openConnection();
                                BufferedReader br = new BufferedReader(new InputStreamReader(uc1.getInputStream()));
                                final String line = br.readLine();
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (Float.parseFloat(line) > Float.parseFloat(getVersionInfo())) {
                                            RelativeLayout NewVLayout = findViewById(R.id.NewVLayout);
                                            NewVLayout.setVisibility(VISIBLE);
                                        }
                                    }
                                });
                            }
                        } catch (Exception ex) {
                            Toast.makeText(getApplicationContext(), ex.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                }).start();
            }
        }
    };
    //get the current version number and name
    private String getVersionInfo() {
        String versionName = "";
        try {
            PackageInfo packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            versionName = packageInfo.versionName;
        } catch (Exception e) {

        }
        return versionName;
    }
    int lineno = 0;
    int Flineno = 0, Slineno = 1, Tlineno = 2;
    int Adno = 1;
    String FirstLine, SecondLine, ThirdLine;
    Bitmap OAdvImg;
    private Runnable OAdvertisement = new Runnable() {
        public void run() {
            //Check Internet
            ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
            if((connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                    connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED))
            {
                new Thread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        try
                        {
                            URL url = new URL("https://bitnbytecomputers.co.in/AppAds/BatteryAdv/Text.txt");
                            HttpsURLConnection.setFollowRedirects(false);
                            HttpsURLConnection uc = (HttpsURLConnection) url.openConnection();
                            uc.setRequestMethod("HEAD");
                            if (uc.getResponseCode() == HttpURLConnection.HTTP_OK) {
                                HttpsURLConnection.setFollowRedirects(true);
                                HttpsURLConnection uc1 = (HttpsURLConnection) url.openConnection();
                                BufferedReader br = new BufferedReader(new InputStreamReader(uc1.getInputStream()));
                                String line = "";
                                while ((line = br.readLine()) != null) {
                                    if (lineno == Flineno)
                                        FirstLine = line;
                                    else if (lineno == Slineno)
                                        SecondLine = line;
                                    else if (lineno == Tlineno)
                                        ThirdLine = line;
                                    lineno++;
                                }
                                //Onine Image
                                URL urlConnection = new URL("https://bitnbytecomputers.co.in/AppAds/BatteryAdv/Ad" + FirstLine + ".png");
                                HttpURLConnection connection = (HttpURLConnection) urlConnection
                                        .openConnection();
                                connection.setConnectTimeout(6000);
                                connection.setDoInput(true);
                                connection.connect();
                                InputStream input = connection.getInputStream();
                                OAdvImg = BitmapFactory.decodeStream(input);
                                AdSettings(lineno, Flineno, Slineno, Tlineno, Adno);
                                runOnUiThread(new Runnable() {
                                    public void run() {
                                        TextView OAdvHeadView = findViewById(R.id.OHeadAdv);
                                        TextView OAdvTextView = findViewById(R.id.OAdvMainText);
                                        OAdvHeadView.setText(SecondLine);
                                        OAdvTextView.setText(ThirdLine);
                                        ImageView AdView = findViewById(R.id.OAdvImg);
                                        AdView.setImageBitmap(OAdvImg);
                                        RelativeLayout OAdvLayout = findViewById(R.id.OAdvLayout);
                                        OAdvLayout.setVisibility(VISIBLE);
                                    }
                                });
                            }
                        } catch (Exception e)
                        {
                            Toast.makeText(getApplicationContext(), "Unexpected Error", Toast.LENGTH_LONG).show();
                        }
                    }
                }).start();
                AdvertisementHandler.postDelayed(OAdvertisement, 5000);
            }
        }
    };
    private void AdSettings(int NumLine, int FNum, int SNum, int TNum, int Adnum)
    {
        Flineno = FNum;
        Slineno = SNum;
        Tlineno = TNum;
        Adno = Adnum;
        if(Adno<((NumLine)/3)) {
            Flineno += 3;
            Slineno += 3;
            Tlineno += 3;
            Adno++;
        }
        else if(Adno==((NumLine)/3))
        {
            Flineno = 0;
            Slineno = 1;
            Tlineno = 2;
            Adno=1;
        }
        lineno = 0;
    }
    int adno = 1;
    Handler handle = new Handler();
    //Ad Runable
    private Runnable Changeads = new Runnable()
    {
        public void run()
        {
            TextView head = (TextView)findViewById(R.id.AdHead);
            TextView address = (TextView)findViewById(R.id.AdAddress);
            TextView main = (TextView)findViewById(R.id.AdMain);
            TextView addition = (TextView)findViewById(R.id.AdAdd);
            if(adno==1)
            {
                head.setText("Rainbow Designers Boutique");
                address.setText("B/s Satkartarian Gurud.,Batala  Ph:+91-70092-60150");
                main.setText("Specialists in: Party Wear and Casual Wear, Bridal Dresses, Lehnga, Salwar Kameez, Pants, Kurtis, and Anarkali Dresses");
                addition.setText("Online Booking and Shipping also available");
                adno=2;
            }
            else if(adno==2)
            {
                head.setText("Bit 'n' Byte Computers");
                address.setText("Smadh Road, Batala    Ph:+91-98153-41886");
                main.setText("Learn C, C++, Java, HTML, CoralDraw, Photoshop, Tally Accounts, Basics of Computer, AutoCad, Hardware and Sotware Solution");
                addition.setText("Special Coaching:-Going Abroad and B.Tech.Students");
                adno = 1;
            }
            handle.postDelayed(Changeads, 5000);
        }
    };
    //______________________________________________________________________
    Context mContext;
    private TextView mTextViewInfo;
    private TextView mTextViewPercentage, mtvstatus;
    private ProgressBar mProgressBar;
    private ImageView mchargingimg;
    private int mProgressStatus = 0, notishow = 0; //if Notification is Shown
    int yy=0;
    // Initialize a new BroadcastReceiver instance
    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver()
    {
        @Override
        public void onReceive(Context context, Intent intent)
        {
            // Get the widgets reference from XML layout
            mTextViewInfo = (TextView) findViewById(R.id.tv_info);
            mTextViewPercentage = (TextView) findViewById(R.id.tv_percentage);
            mProgressBar = (ProgressBar) findViewById(R.id.pb);
            mtvstatus = (TextView) findViewById(R.id.tv_status);
            mchargingimg = (ImageView)findViewById(R.id.chargingimage);
            ProgressBar progressBar = (ProgressBar)findViewById(R.id.pb);
            // Get the battery scale----
            int scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE,-1);
            // get the battery level----
            int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL,-1);
            // Calculate the battery charged percentage
            float percentage = level/ (float) scale;
            // Update the progress bar to display current battery charged percentage
            mProgressStatus = (int)((percentage)*100);
            // Show the battery charged percentage text inside progress bar
            mTextViewPercentage.setText("" + mProgressStatus + "%");
            //Temperature----
            float temp = intent.getIntExtra(BatteryManager.EXTRA_TEMPERATURE,0);
            //Health
            int deviceHealth = intent.getIntExtra(BatteryManager.EXTRA_HEALTH,0);
            String Health="";
            if(deviceHealth == BatteryManager.BATTERY_HEALTH_COLD){

                Health = "Cold";
            }

            if(deviceHealth == BatteryManager.BATTERY_HEALTH_DEAD){

                Health = "Dead";
            }

            if (deviceHealth == BatteryManager.BATTERY_HEALTH_GOOD){

                Health = "Good";
            }

            if(deviceHealth == BatteryManager.BATTERY_HEALTH_OVERHEAT) {

                Health = "Over Heat";
            }
            if (deviceHealth == BatteryManager.BATTERY_HEALTH_OVER_VOLTAGE){

                Health = "Over Voltage";
            }

            if (deviceHealth == BatteryManager.BATTERY_HEALTH_UNKNOWN){

                Health = "Unknown";
            }
            if (deviceHealth == BatteryManager.BATTERY_HEALTH_UNSPECIFIED_FAILURE){
                Health = "Unspecified Falure";
            }
            //Voltage
            int batteryVol = (int)(intent.getIntExtra(BatteryManager.EXTRA_VOLTAGE,0));
            float fullVoltage = (float) (batteryVol * 0.001);
            //Technology
            String technology = intent.getStringExtra("technology");
            //Plugged
            int plugged = intent.getIntExtra("plugged", -1);
            //Charging
            int status=intent.getIntExtra(BatteryManager.EXTRA_STATUS,-1);
            String pluggeds="";
            if(status==BatteryManager.BATTERY_STATUS_CHARGING)
            {
                pluggeds = getPlugTypeString(plugged);
                if(notishow==0) {
                    mtvstatus.setText("Charging");
                    mtvstatus.setVisibility(VISIBLE);
                    mchargingimg.setVisibility(VISIBLE);
                    progressBar.setProgressTintList(ColorStateList.valueOf(Color.BLUE));
                    notishow = 1;
                }
            }
            else {
                if(getPlugTypeString(plugged)=="Unknown")
                    pluggeds = "Unplugged";
                else
                    pluggeds = getPlugTypeString(plugged);
                if(notishow==1) {
                    mtvstatus.setVisibility(TextView.INVISIBLE);
                    mchargingimg.setVisibility(ImageView.INVISIBLE);
                    notishow = 0;
                }
                //Battery Low & Medium & High
                if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    if(mProgressStatus<=20) {
                        mtvstatus.setText("Low");
                        mtvstatus.setVisibility(VISIBLE);
                        progressBar.setProgressTintList(ColorStateList.valueOf(Color.RED));
                    }
                    else if(mProgressStatus>20 && mProgressStatus<=70){
                        mtvstatus.setVisibility(TextView.INVISIBLE);
                        progressBar.setProgressTintList(ColorStateList.valueOf(Color.YELLOW));
                    }
                    else if(mProgressStatus>=80 && mProgressStatus<100)
                    {
                        mtvstatus.setVisibility(TextView.INVISIBLE);
                        progressBar.setProgressTintList(ColorStateList.valueOf(Color.rgb(0, 255, 0)));
                    }
                }
            }
            if(mProgressStatus==100)
            {
                mtvstatus.setText("Full");
                mtvstatus.setVisibility(VISIBLE);
                progressBar.setProgressTintList(ColorStateList.valueOf(Color.rgb(0, 200, 0)));
            }
            // Display the battery charged percentage in progress bar
            mProgressBar.setProgress(mProgressStatus);
            mTextViewInfo.setText(Html.fromHtml("<font color=WHITE>Scale:</font> <font color=YELLOW>"+scale+"</font><font color=WHITE>&nbsp&nbsp&nbsp&nbsp Level:</font>" +
                    "<font color=YELLOW>"+level+"</font><font color=WHITE>&nbsp&nbsp&nbsp&nbsp Temp:</font><font color=YELLOW>"+temp/10+"°C</font>" +
                    "    <font color=WHITE>&nbsp&nbsp&nbsp&nbsp Health:</font><font color=YELLOW>"+Health+"</font><font color=WHITE>&nbsp&nbsp&nbsp&nbsp Voltage:</font><font color=YELLOW>"+fullVoltage+"W"+
                    "</font><font color=WHITE>&nbsp&nbsp&nbsp&nbsp Technology:</font><font color=YELLOW>"+technology+"</font><font color=WHITE>&nbsp&nbsp&nbsp&nbsp Plugged:"+
                    "</font><font color=YELLOW>"+pluggeds+"</font>"));
        }
    };
    private String getPlugTypeString(int plugged) {
        String plugType = "Unknown";
        switch (plugged) {
            case BatteryManager.BATTERY_PLUGGED_AC:
                plugType = "Wall Scoket";
                break;
            case BatteryManager.BATTERY_PLUGGED_USB:
                plugType = "USB";
                break;
            case BatteryManager.BATTERY_PLUGGED_WIRELESS:
                plugType = "Wireless";
                break;
        }

        return plugType;
    }
    //Schedule Job
    public void scheduleJob(Context context) {
        //creating new firebase job dispatcher
        JobScheduler jobScheduler = (JobScheduler) getSystemService(Context.JOB_SCHEDULER_SERVICE);

        JobInfo jobInfo = new JobInfo.Builder(11, new ComponentName(this, NotificationService.class))
                // only add if network access is required
                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
                .build();

        jobScheduler.schedule(jobInfo);
    }

    public void onDestroy()
    {
        super.onDestroy();
        Intent serviceIntent = new Intent(this, NotificationService.class);
        stopService(serviceIntent);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        //Starting Service
        setContentView(R.layout.activity_main);
        //FirstRun();
        scheduleJob(this);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Battery Diagnose");
        //Start Ad Timer
        handle.postDelayed(Changeads, 5000);
        //Set Progressbar Color
        ProgressBar progressB = (ProgressBar)findViewById(R.id.pb);
        progressB.setProgressTintList(ColorStateList.valueOf(Color.YELLOW));
        // Get the application context
        mContext = getApplicationContext();
        // Initialize a new IntentFilter instance
        IntentFilter iFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        // Register the broadcast receiver
        mContext.registerReceiver(mBroadcastReceiver,iFilter);
        //New Version'
        AdvertisementHandler.postDelayed(NewVerAdvertisement, 0);
        //Ads
        AdvertisementHandler.postDelayed(OAdvertisement, 0);
        //CLose New Btn
        Button CloseNewVBtn = findViewById(R.id.NewVCLose);
        CloseNewVBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RelativeLayout NewVerAdLayout = findViewById(R.id.NewVLayout);
                NewVerAdLayout.setVisibility(View.INVISIBLE);
            }
        });
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
}